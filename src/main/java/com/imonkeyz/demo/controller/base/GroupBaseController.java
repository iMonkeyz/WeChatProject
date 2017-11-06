package com.imonkeyz.demo.controller.base;

import java.util.UUID;

/**
 * Created by Jesse on 2017/9/25.
 */
public class GroupBaseController {
	public final static String DOMAIN = "http://cerd6v.natappfree.cc/wx";
	public final static String APPID = "wxac058681c6aff3a8";
	public final static String SCOPE = "snsapi_userinfo";

	public String makeAuthURL(String uri, String state) {

		StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
		sb.append("appid=").append(APPID);
		sb.append("&redirect_uri=").append( makeURL(uri) );
		sb.append("&response_type=code");
		sb.append("&scope=").append(SCOPE);
		sb.append("&state=").append(state);
		sb.append("#wechat_redirect");
		return sb.toString();
	}

	public String randomUUID() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}

	private String makeURL(String uri) {
		StringBuilder sb = new StringBuilder(DOMAIN);
		sb.append(uri);
		return sb.toString().replaceAll(":", "%3A").replaceAll("/", "%2F");
	}

}
