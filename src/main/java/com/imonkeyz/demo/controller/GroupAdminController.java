package com.imonkeyz.demo.controller;

import com.imonkeyz.demo.annotation.WeChatAdminRequired;
import com.imonkeyz.demo.constants.WeChatConstants;
import com.imonkeyz.demo.controller.base.GroupBaseController;
import com.imonkeyz.demo.entity.AuthTokenData;
import com.imonkeyz.demo.entity.GroupInfoData;
import com.imonkeyz.demo.entity.QRCodeData;
import com.imonkeyz.demo.entity.UserInfoData;
import com.imonkeyz.demo.service.WeChatService;
import com.imonkeyz.demo.utils.QRUtil;
import com.imonkeyz.demo.utils.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jesse on 2017/9/25.
 */
@Controller
@RequestMapping("/group/admin")
public class GroupAdminController extends GroupBaseController{
	private final static Logger LOG = Logger.getLogger(GroupAdminController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private WeChatService weChatService;

	/**
	 * 后台管理入口, 需授权. 已授权: 显示内容 ; 无授权: 扫码授权.
	 * @param map
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String admin(ModelMap map) {
		HttpSession session = request.getSession();
		Object o = session.getAttribute("ADMIN");
		if ( o != null ) {
			UserInfoData admin = (UserInfoData) o;
			List<GroupInfoData> groupInfos = weChatService.findAllFullGroupInfoByOpenId(admin.getOpenId());
			map.addAttribute("groupInfos", groupInfos);
		}
		return "group-admin";
	}

	/**
	 *
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/exit", method = RequestMethod.GET)
	public String logOff(ModelMap map) {
		request.getSession().invalidate();
		return "redirect:/group/admin";
	}

	@RequestMapping("/test")
	public String test() {
		return  "group-info-edit";
	}

	/**
	 * 生成管理二维码
	 * @throws IOException
	 */
	@RequestMapping(value = "/qr/auth/ajax", method = RequestMethod.GET)
	public @ResponseBody QRCodeData makeAdminQrAjax() throws IOException {
		String uuid = randomUUID();
		long tsm = System.currentTimeMillis();
		BufferedImage img = QRUtil.qRCodeBufImg(DOMAIN + "/group/admin/qr/scan/" + uuid);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", bos);
		byte[] imageBytes = bos.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		String imgData = encoder.encode(imageBytes);
		bos.close();

		request.getSession().setAttribute("AUTH_UUID", uuid);
		request.getSession().setAttribute("AUTH_UUID_TSM", tsm);

		return new QRCodeData(tsm, imgData);
	}

	/**
	 * 生成管理二维码
	 * @throws IOException
	 */
	@RequestMapping(value = "/qr/auth", method = RequestMethod.GET)
	public void makeAdminQr(HttpServletResponse response) throws IOException {
		//1. 清理上次的UUID
		HttpSession session = request.getSession();
		Object lastUUID = session.getAttribute("AUTH_UUID");
		if ( lastUUID != null ) {
			weChatService.removeAuthToken( lastUUID.toString() );
		}

		//2. 生成新的UUID
		String uuid = randomUUID();
		long tsm = System.currentTimeMillis();

		session.setAttribute("AUTH_UUID", uuid);
		weChatService.saveAuthToken(new AuthTokenData(uuid, tsm));

		BufferedImage img = QRUtil.qRCodeBufImg(DOMAIN + "/group/admin/qr/scan/" + uuid);
		response.setContentType("image/png");
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(img, "png", out);
	}

	/**
	 * 扫描管理二维码, 重定向到鉴权页并请求授权
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = "/qr/scan/{uuid}", method = RequestMethod.GET)
	public String scanAdminQr(@PathVariable String uuid) {
		//1. 先验证二维码是否过期
		boolean isPass = weChatService.validateAuthToken(uuid);
		if ( isPass ) {
			String uri = DOMAIN + "/group/admin/authorization".replaceAll(":", "%3A").replaceAll("/", "%2F");
			StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
			sb.append("appid=wxac058681c6aff3a8");
			sb.append("&redirect_uri=").append(uri);
			sb.append("&response_type=code");
			sb.append("&scope=snsapi_userinfo");
			sb.append("&state=").append(uuid);
			sb.append("#wechat_redirect");
			return "redirect:" + sb.toString();
		}
		return "authorization";
	}

	/**
	 * 对授权结果进行确认, 授权成功则保存用户信息
	 * @param code
	 * @param state
	 * @return
	 */
	@RequestMapping(value = "/authorization", method = RequestMethod.GET)
	public String authorization(@RequestParam String code, @RequestParam String state, ModelMap map) {
		ServletContext application = request.getSession().getServletContext();

		LOG.info("接收到授权批准 CODE: " + code + ", STATE: " + state);

		UserInfoData userInfo = WxUtil.getUserInfo(code);
		if ( userInfo.getErrCode() == null ) {
			userInfo.setAuthMs(new Date().getTime());
			application.setAttribute(state, userInfo);
			map.addAttribute("isPass", true);
		}
		return "authorization";
	}

	/**
	 * Ajax验证state是否已经授权
	 * @return
	 */
	@RequestMapping(value = "/validation", method = RequestMethod.GET)
	public @ResponseBody String validation() {
		HttpSession session = request.getSession();
		ServletContext application = session.getServletContext();

		//1. 用于处理多窗口重复检查
		if ( session.getAttribute(WeChatConstants.SESSION_ADMIN) != null ) {
			return WeChatConstants.AUTH_SUCCESS;
		}

		//2. 正常检查
		try {
			String uuid = (String) session.getAttribute(WeChatConstants.SESSION_AUTH_UUID);
			UserInfoData admin = (UserInfoData) application.getAttribute(uuid);

			application.removeAttribute(uuid); //清理uuid

			if ( admin != null ) {
				session.setAttribute(WeChatConstants.SESSION_ADMIN, admin);
				return WeChatConstants.AUTH_SUCCESS;
			}
		} catch (Exception e) {}

		return WeChatConstants.AUTH_FAILED;
	}

	@WeChatAdminRequired
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editGroupInfo(@PathVariable Long id, ModelMap map) {
		if ( id != 0 ) {
			GroupInfoData groupInfo = weChatService.findGroupInfoForEdit(id);
			map.put("groupInfo", groupInfo);
		}
		return "group-info-edit";
	}

	@WeChatAdminRequired
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listGroupInfo() {
		return "group-admin-list";
	}

	@WeChatAdminRequired
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Long saveGroupInfo(@RequestBody GroupInfoData groupInfoData) {
		UserInfoData admin = (UserInfoData) request.getSession().getAttribute(WeChatConstants.SESSION_ADMIN);
		groupInfoData.setOpenId(admin.getOpenId());

		return weChatService.saveGroupInfo(groupInfoData);
	}

	@WeChatAdminRequired
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String viewGroupInfo(@PathVariable Long id, ModelMap map) {
		UserInfoData admin = (UserInfoData) request.getSession().getAttribute(WeChatConstants.SESSION_ADMIN);
		GroupInfoData groupInfo = weChatService.findGroupInfo(id);

		map.put("userInfo", admin);
		map.put("groupInfo", groupInfo);
		map.put("adminMode", true);

		return "group-info-page";
	}

	/**
	 * Ajax方式生成群信息分享二维码
	 * @param id
	 * @return QRCodeData
	 * @throws IOException
	 */
	@WeChatAdminRequired
	@RequestMapping(value = "/qr/make/{id}", method = RequestMethod.GET)
	public @ResponseBody QRCodeData makeSahreQr(@PathVariable long id) throws IOException {
		BufferedImage img = QRUtil.qRCodeBufImg(DOMAIN + "/group/share/" + id);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(img, "png", bos);
		byte[] imageBytes = bos.toByteArray();
		BASE64Encoder encoder = new BASE64Encoder();
		String imgData = encoder.encode(imageBytes);
		bos.close();

		return new QRCodeData(id, imgData);
	}

	/**
	 * Stream方式生成群信息分享二维码
	 * @param id
	 * @param response
	 * @throws IOException
	 */
	@WeChatAdminRequired
	@RequestMapping(value = "/qr/stream/{id}", method = RequestMethod.GET)
	public void makeSahreQrStream(@PathVariable long id, HttpServletResponse response) throws IOException {
		BufferedImage img = QRUtil.qRCodeBufImg(DOMAIN + "/group/qr/scan/" + id);
		response.setContentType("image/png");
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(img, "png", out);
	}
}
