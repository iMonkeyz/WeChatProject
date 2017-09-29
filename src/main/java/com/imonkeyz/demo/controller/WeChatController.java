package com.imonkeyz.demo.controller;

import com.imonkeyz.demo.message.request.TextMessage;
import com.imonkeyz.demo.utils.MessageUtil;
import com.imonkeyz.demo.utils.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * Created by Shinelon on 2017/4/9.
 */
@Controller
public class WeChatController {

	@RequestMapping(value = "/")
	public String index() {
		return "redirect:/group/admin";
	}

	@RequestMapping(value = "/connect", method = RequestMethod.GET)
	@ResponseBody
	public void connect(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		System.out.println("Request=" + request.getRequestURL().toString());

		PrintWriter out = response.getWriter();
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}
		out.close();
	}

	@RequestMapping(value = "/connect", method = RequestMethod.POST)
	public void wechatPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> map = MessageUtil.parseXml(request);

		String toUserName = map.get("ToUserName");
		String fromUserName = map.get("FromUserName");
		String createTime = map.get("CreateTime");
		String msgType = map.get("MsgType");
		String eventType = map.get("Event");
		String eventKey = map.get("EventKey");
		String content = map.get("Content");
		String msgId = map.get("MsgId");

		System.out.println("ToUserName = " + toUserName);
		System.out.println("FromUserName = " + fromUserName);
		System.out.println("CreateTime = " + createTime);
		System.out.println("MsgType = " + msgType);
		System.out.println("Event=" + eventType);
		System.out.println("EventKey=" + eventKey);
		System.out.println("Content = " + content);
		System.out.println("MsgId = " + msgId);

		PrintWriter out = response.getWriter();
		TextMessage textMessage = new TextMessage();
		textMessage.setFromUserName(toUserName);
		textMessage.setToUserName(fromUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setMsgType("text");
		//关注
		if ("subscribe".equals(eventType)) {
			textMessage.setContent("欢迎关注xxx公众号!");
		} else if ("CLICK".equals(eventType) && "CheckMyID".equals(eventKey)) {
			textMessage.setContent("您的ID是:\n" + fromUserName);
		} else if ("text".equals(msgType)) {
			textMessage.setContent("您刚才输入的消息是:\n" + content);
		} else {
			textMessage.setContent("输入不在识别范围内.");
		}

		String xml = MessageUtil.textMessageToXml(textMessage);
		System.out.println("Response xml: " + xml);
		System.out.println("===============================================================");
		out.write(xml);
		out.close();
	}
}
