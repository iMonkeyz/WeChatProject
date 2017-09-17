package com.imonkeyz.demo.controller;

import com.imonkeyz.demo.entity.GroupInfoData;
import com.imonkeyz.demo.entity.UserInfoData;
import com.imonkeyz.demo.service.WeChatService;
import com.imonkeyz.demo.utils.WxUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
		return "group-info-setting";
	}

	@RequestMapping(value = "/img/save", method = RequestMethod.POST)
	public void saveImage(@RequestParam String base64, ModelMap model) {
		System.out.println(base64);
	}

	@RequestMapping(value = "/group/save", method = RequestMethod.POST)
	public @ResponseBody Long save(@RequestBody GroupInfoData groupInfoData) {
		Long id = weChatService.save(groupInfoData);
		LOG.info("GroupInfoData has been saved with Unique ID: " + id);
		return id;
	}

	@RequestMapping(value = "/group/share/{id}", method = RequestMethod.GET)
	public String share(@PathVariable Long id, ModelMap map) {
		GroupInfoData groupInfoData = weChatService.find(id);
		map.put("groupInfo", groupInfoData);
		return "group-info-page";
	}
}
