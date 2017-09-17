package com.imonkeyz.demo.dao;

import com.imonkeyz.demo.entity.GroupInfoData;

import java.sql.SQLException;

public interface WXGroupDAO {
	public Long saveGroupInfo(GroupInfoData groupInfoData) throws SQLException;
	public GroupInfoData findGroupInfoByID(Long id) throws SQLException;
	public boolean removeGroupInfoByID(Long id);
}
