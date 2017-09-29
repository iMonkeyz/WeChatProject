package com.imonkeyz.demo.service;

import com.imonkeyz.demo.entity.AuthTokenData;
import com.imonkeyz.demo.entity.GroupInfoData;

import java.sql.SQLException;
import java.util.List;

public interface WeChatService {
	public Long saveGroupInfo(GroupInfoData groupInfoData);

	/**
	 * 根据ID查找GrouInfo
	 * @param id
	 * @return
	 */
	public GroupInfoData findGroupInfo(Long id);
	public GroupInfoData findGroupInfoForEdit(Long id);
	public boolean removeGroupInfo(Long id);

	/**
	 * 根据OPENID查找所有GroupInfo, 返回轻量数据, 只包含info的 id, name, datetime 字段
	 * @param openID
	 * @return
	 */
	public List<GroupInfoData> findAllLightGroupInfoByOpenId(String openID);

	/**
	 * 根据OPENID查找所有GroupInfo, 返回全部数据, 包含所有字段内容
	 * @param openID
	 * @return
	 */
	public List<GroupInfoData> findAllFullGroupInfoByOpenId(String openID);

	public String qr2OpendId(String infoID, String openID);

	public void saveAuthToken(AuthTokenData token);
	public boolean validateAuthToken(String uuid);

	public void removeAuthToken(String s);
}
