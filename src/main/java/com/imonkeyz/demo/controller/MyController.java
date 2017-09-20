package com.imonkeyz.demo.controller;

import com.imonkeyz.demo.entity.GroupInfoData;
import com.imonkeyz.demo.entity.UserInfoData;
import com.imonkeyz.demo.service.WeChatService;
import com.imonkeyz.demo.utils.QRUtil;
import com.imonkeyz.demo.utils.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class MyController {

	private final static Logger LOG = Logger.getLogger(MyController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private WeChatService weChatService;

	@RequestMapping("/qr")
	public String qr(@RequestParam String code, @RequestParam String state, ModelMap map) {
		UserInfoData userInfo = WxUtil.getUserInfo(code);
		map.put("code", code);
		map.put("state", state);
		map.put("userInfo", userInfo);
		return "index";
	}

	@RequestMapping(value = "/join/{openid}", method = RequestMethod.GET)
	public String join(@PathVariable String openid, ModelMap map) {
		ServletContext servletContext = request.getSession().getServletContext();

		long joinCount = 0;
		if (servletContext.getAttribute("joinCount") != null) {
			joinCount = Long.parseLong(servletContext.getAttribute("joinCount").toString());
		}
		joinCount += 1;
		servletContext.setAttribute("joinCount", joinCount);
		return "join";
	}

	@RequestMapping("/group")
	public String groupInfoPage() {
		return "group-info-page";
	}
	@RequestMapping("/group-setting")
	public String groupInfoSettingPage() {
		return "group-info-edit";
	}

	@RequestMapping(value = "/img/save", method = RequestMethod.POST)
	public void saveImage(@RequestParam String base64, ModelMap model) {
		System.out.println(base64);
	}

	@RequestMapping(value = "/group/save", method = RequestMethod.POST)
	public @ResponseBody Long save(@RequestBody GroupInfoData groupInfoData) {
		return weChatService.save(groupInfoData);
	}

	@RequestMapping(value = "/group/share/{id}", method = RequestMethod.GET)
	public String share(@RequestParam(required = false) String code, @PathVariable Long id, ModelMap map) {
		//http://localhost:8090/wx/group/share/1505794165282
		if ( code == null ) {
			StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
			sb.append("appid=wxac058681c6aff3a8");
			sb.append("&redirect_uri=http%3A%2F%2Fimonkeyz.ngrok.cc%2Fwx%2Fgroup%2Fshare%2").append(id);
			sb.append("&response_type=code");
			sb.append("&scope=snsapi_userinfo");
			sb.append("&state=").append(id);
			sb.append("#wechat_redirect");
			return "redirect:" + sb.toString();//授权跳转
		}
		UserInfoData userInfoData = WxUtil.getUserInfo(code);
		GroupInfoData groupInfoData = weChatService.find(id);
		map.put("userInfo", userInfoData);
		map.put("groupInfo", groupInfoData);
		return "group-info-page";
	}

	@RequestMapping(value = "/group/admin/login")
	public String adminLoginPage(ModelMap map) {
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		map.put("uuid", uuid);
		return "group-admin-login";
	}

	/**
	 * 用户管理授权登录二维码
	 * @param uuid
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/group/qr/auth/{uuid}")
	public void genQr(@PathVariable String uuid, HttpServletResponse response) throws IOException {
		BufferedImage img = QRUtil.qRCodeBufImg("http://imonkeyz.ngrok.cc/wx/group/qr/scan/" + uuid);
		response.setContentType("image/png");
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(img, "png", out);
	}

	/**
	 * 群信息分享页面二维码
	 * @param groupId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/group/qr/share/{groupId}")
	public void groupSahreQr(@PathVariable String groupId, HttpServletResponse response) throws IOException {
		StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
		sb.append("appid=wxac058681c6aff3a8");
		sb.append("&redirect_uri=http%3A%2F%2Fimonkeyz.ngrok.cc%2Fwx%2Fgroup%2Fshare%2F").append(groupId);
		sb.append("&response_type=code");
		sb.append("&scope=snsapi_userinfo");
		sb.append("&state=").append(groupId);
		sb.append("#wechat_redirect");
		BufferedImage img = QRUtil.qRCodeCommon(sb.toString(), 12);
		response.setContentType("image/png");
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(img, "png", out);
	}

	/**
	 * 扫描二维码跳转到授权页面
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value = "/group/qr/scan/{uuid}")
	public String scanQr(@PathVariable String uuid) {
		StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
		sb.append("appid=wxac058681c6aff3a8");
		sb.append("&redirect_uri=http%3A%2F%2Fimonkeyz.ngrok.cc%2Fwx%2Fgroup%2Fadmin%2Fauthorization");
		sb.append("&response_type=code");
		sb.append("&scope=snsapi_userinfo");
		sb.append("&state=").append(uuid);
		sb.append("#wechat_redirect");
		return  "redirect:" + sb.toString();
	}

	@RequestMapping(value = "/group/admin/authorization", method = RequestMethod.GET)
	public String authorization(@RequestParam String code, @RequestParam("state") String uuid) {
		LOG.info("接收到授权请求 CODE: " + code + ", STATE(UUID): " + uuid);
		UserInfoData userInfo = WxUtil.getUserInfo(code);
		LOG.info("检测授权用户信息: " + userInfo);
		if ( userInfo.getErrCode() == null ) {
			request.getSession().getServletContext().setAttribute(uuid, new Date().getTime());
			return "authorized";
		} else {
			return "unauthorized";
		}
	}

	@RequestMapping(value = "/group/admin/validation/{uuid}")
	public @ResponseBody boolean validation(@PathVariable String uuid) {
		/*LOG.info("请求验证UUID: " + uuid);*/
		long validMs = 60 * 2 * 1000L;  //2 minutes
		Object o = request.getSession().getServletContext().getAttribute(uuid);
		if ( o != null ) {
			request.getSession().getServletContext().removeAttribute(uuid);
			long ms = Long.valueOf(o.toString());
			LOG.info("已找到对应UUID授权时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(new Date(ms)));
			long now = System.currentTimeMillis();
			LOG.info("校验结果: "+ (now - ms< validMs));
			return now - ms < validMs;
		}
		return false;
	}
}
