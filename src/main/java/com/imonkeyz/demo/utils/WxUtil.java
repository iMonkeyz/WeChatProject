package com.imonkeyz.demo.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.imonkeyz.demo.entity.AccessTokenData;
import com.imonkeyz.demo.entity.UserInfoData;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WxUtil {
	private final static String APPID = "wxac058681c6aff3a8";
	private final static String APPSECRET = "dfc57f2678815c07f9f9237a6f95a2e0";
	private final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	private final static String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	private final static Map<String, AccessTokenData> accessTokenMap = new HashMap<String, AccessTokenData>();

	private static RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(15000)
			.setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000)
			.build();


	public static AccessTokenData getToken() {
		String url = TOKEN_URL.replace("APPID", APPID).replace("SECRET", APPSECRET);
		JsonObject json = getJSON(url);
		AccessTokenData accessTokenData = null;
		if (json != null && json.get("errcode") == null) {
			String accessToken = json.get("access_token").getAsString();
			long expiresIn = Long.parseLong(json.get("expires_in").getAsString());
			accessTokenData = new AccessTokenData(accessToken, expiresIn);
		}
		System.out.println("获取到AccessTokenData=" + accessTokenData);
		return accessTokenData;
	}

	public static AccessTokenData getAccessToken(String code) {
		AccessTokenData accessTokenData = accessTokenMap.get(code);
		if (accessTokenData == null) {
			String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("SECRET", APPSECRET).replace("CODE", code);
			JsonObject json = getJSON(url);

			if (json != null) {
				if (json.get("errcode") == null) {
					String accessToken = json.get("access_token").getAsString();
					long expiresIn = Long.parseLong(json.get("expires_in").getAsString());
					String refreshToken = json.get("refresh_token").getAsString();
					String openid = json.get("openid").getAsString();
					String scope = json.get("scope").getAsString();
					accessTokenData = new AccessTokenData(accessToken, expiresIn, refreshToken, openid, scope);
				} else {
					String errcode = json.get("errcode").getAsString();
					String errmsg = json.get("errmsg").getAsString();
					accessTokenData = new AccessTokenData(errcode, errmsg);
				}
			}
			accessTokenMap.put(code, accessTokenData);
		}
		System.out.println("获取到AccessTokenData=" + accessTokenData);
		return accessTokenData;
	}

	public static UserInfoData getUserInfo(String code) {
		UserInfoData userInfoData = null;
		AccessTokenData accessToken = getAccessToken(code);
		if (accessToken.getErrCode() == null) {
			String url = USER_INFO_URL.replace("ACCESS_TOKEN", accessToken.getAccessToken()).replace("OPENID", accessToken.getOpenId());
			JsonObject json = getJSON(url);

			if (json != null) {
				if (json.get("errcode") == null) {
					String openid = json.get("openid").getAsString();
					String nickname = json.get("nickname").getAsString();
					String sex = json.get("sex").getAsString();
					String province = json.get("province").getAsString();
					String city = json.get("city").getAsString();
					String country = json.get("country").getAsString();
					String headimgurl = json.get("headimgurl").getAsString();
					String privilege = json.get("privilege").toString();
					String unionid = null;
					if (json.get("unionid") != null) {
						unionid = json.get("unionid").getAsString();
					}
					userInfoData = new UserInfoData(openid, nickname, sex, province, city, country, headimgurl, privilege, unionid);
				} else {
					String errcode = json.get("errcode").getAsString();
					String errmsg = json.get("errmsg").getAsString();
					userInfoData = new UserInfoData(errcode, errmsg);
				}
			}
		} else {
			userInfoData = new UserInfoData(accessToken.getErrCode(), accessToken.getErrMsg());
		}
		System.out.println("获取到UserInfoData=" + userInfoData);
		return userInfoData;
	}

	private static JsonObject getJSON(final String url) {
		System.out.println("GET " + url);
		HttpGet get = new HttpGet(url);
		CloseableHttpClient client = HttpClients.createDefault();
		get.setConfig(requestConfig);

		JsonObject json = null;
		try {
			CloseableHttpResponse response = client.execute(get);
			String content = EntityUtils.toString(response.getEntity(), "UTF-8");
			json = new JsonParser().parse(content).getAsJsonObject();

			response.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void main(String[] args) {
		AccessTokenData token = getToken();
		System.out.println(token);
	}
}
