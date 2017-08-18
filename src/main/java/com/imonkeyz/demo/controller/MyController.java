package com.imonkeyz.demo.controller;

import com.imonkeyz.demo.entity.UserInfoData;
import com.imonkeyz.demo.utils.WxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Controller
public class MyController {

	@Autowired
	private HttpServletRequest request;

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
}
