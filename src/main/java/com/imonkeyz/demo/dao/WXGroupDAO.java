package com.imonkeyz.demo.dao;

import com.imonkeyz.demo.entity.AuthTokenData;
import com.imonkeyz.demo.entity.GroupInfoData;

import java.sql.SQLException;
import java.util.List;

public interface WXGroupDAO {
	public Long saveGroupInfo(GroupInfoData groupInfoData) throws SQLException;
	public GroupInfoData findGroupInfoByID(Long id, boolean editMode) throws SQLException;
	public boolean removeGroupInfoByID(Long id);

	/**
	 * 根据OPENID查找所有GroupInfo
	 * @param openID
	 * @param isFull TRUE - 全部数据 / False - 轻量数据
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoData> findAllGroupInfoByOpenId(String openID, boolean isFull) throws Exception;

	public String assignQr2OpenId(String infoID, String openId) throws SQLException;

	public void saveAuthToken(AuthTokenData token) throws SQLException;
	public AuthTokenData findAuthTokenByUUID(String uuid) throws SQLException;
	public void removeAuthTokenByUUID(String uuid) throws SQLException;
}
