package com.imonkeyz.demo.controller;

import com.imonkeyz.demo.annotation.WeChatAdminRequired;
import com.imonkeyz.demo.annotation.WeChatGuestRequired;
import com.imonkeyz.demo.constants.WeChatConstants;
import com.imonkeyz.demo.controller.base.GroupBaseController;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Jesse on 2017/9/25.
 */
@Controller
@RequestMapping("/group")
public class GroupGuestController extends GroupBaseController {
	private final static Logger LOG = Logger.getLogger(GroupGuestController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private WeChatService weChatService;

	/**
	 * 扫描群信息二维码, 请求授权并跳转至信息页
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/qr/scan/{id}", method = RequestMethod.GET)
	public String scanShareQr(@PathVariable long id) {
		String uri = DOMAIN + "/group/share" .replaceAll(":", "%3A").replaceAll("/", "%2F");
		StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
		sb.append("appid=wxac058681c6aff3a8");
		sb.append("&redirect_uri=").append(uri);
		sb.append("&response_type=code");
		sb.append("&scope=snsapi_userinfo");
		sb.append("&state=").append(id);
		sb.append("#wechat_redirect");
		return "redirect:" + sb.toString();//授权跳转
	}

	/**
	 * 访问群分享信息页, 已授权
	 * @param code
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public String share(@RequestParam(required = false) String code, @RequestParam(value = "state") Long id, ModelMap map) {
		UserInfoData userInfoData = WxUtil.getUserInfo(code);
		GroupInfoData groupInfoData = weChatService.findGroupInfo(id);

		//保存用户信息到session
		request.getSession().setAttribute(WeChatConstants.SESSION_GUEST, userInfoData.getOpenId());

		map.put("userInfo", userInfoData);
		map.put("groupInfo", groupInfoData);
		return "group-info-page";
	}

	/**
	 * 以当前用户加入群.
	 * @param id
	 * @param map
	 * @return
	 */
	@WeChatGuestRequired
	@RequestMapping(value = "/share/{id}/join", method = RequestMethod.GET)
	public String groupJoin(@PathVariable String id, ModelMap map) {
		String openId = (String) request.getSession().getAttribute(WeChatConstants.SESSION_GUEST);
		String qrData = weChatService.qr2OpendId(id, openId);
		map.addAttribute("qrData", qrData);
		return "group-join";
	}
}
