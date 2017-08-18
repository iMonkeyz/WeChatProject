package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenData extends BaseData {
	private String accessToken;
	private long expiresIn;
	private String refreshToken;
	private String openId;
	private String scope;

	public AccessTokenData(String errCode, String errMsg) {
		super(errCode, errMsg);
	}

	public AccessTokenData(String accessToken, long expiresIn) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
	}
}
