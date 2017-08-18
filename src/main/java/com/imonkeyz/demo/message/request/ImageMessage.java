package com.imonkeyz.demo.message.request;

/**
 * 图片消息
 * Created by Shinelon on 2017/4/9.
 */
public class ImageMessage extends BaseMessage {
	// 图片链接
	private String PicUrl;

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
}
