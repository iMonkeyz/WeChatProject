package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccessTokenData extends BaseData {
	private String accessToken;
	private long expiresIn;
	private String refreshToken;
	private String openId;
	private String scope;

	private long createdTs = System.currentTimeMillis();

	public AccessTokenData(String errCode, String errMsg) {
		super(errCode, errMsg);
	}

	public AccessTokenData(String accessToken, long expiresIn) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
	}

	public AccessTokenData(String accessToken, long expiresIn, String refreshToken, String openid, String scope) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.refreshToken = refreshToken;
		this.openId = openid;
		this.scope = scope;
	}

	public boolean isExpired () {
		return this.createdTs + expiresIn * 1000L < System.currentTimeMillis();
	}
}
