package com.imonkeyz.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoData extends BaseData {
	private String openId;
	private String nickname;
	private String sex;
	private String province;
	private String city;
	private String country;
	private String headimgurl;
	private String privilege;
	private String unionid;

	public UserInfoData(String errCode, String errMsg) {
		super(errCode, errMsg);
	}
}
