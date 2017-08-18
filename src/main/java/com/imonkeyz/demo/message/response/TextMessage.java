package com.imonkeyz.demo.message.response;

/**
 * 文本消息
 * Created by Shinelon on 2017/4/9.
 */
public class TextMessage extends BaseMessage {
	// 回复的消息内容
	private String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
