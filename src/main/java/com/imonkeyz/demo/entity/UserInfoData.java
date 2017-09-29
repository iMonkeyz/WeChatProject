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

	private long authMs;

	public UserInfoData(String openId, String nickname, String sex, String province, String city, String country, String headimgurl, String privilege, String unionid) {
		this.openId = openId;
		this.nickname = nickname;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.country = country;
		this.headimgurl = headimgurl;
		this.privilege = privilege;
		this.unionid = unionid;
	}

	public UserInfoData(String errCode, String errMsg) {
		super(errCode, errMsg);
	}
}
