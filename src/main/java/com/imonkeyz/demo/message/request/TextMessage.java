package com.imonkeyz.demo.message.request;

/**
 * 文本消息
 * Created by Shinelon on 2017/4/9.
 */
public class TextMessage extends BaseMessage {
	// 消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
