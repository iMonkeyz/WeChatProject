package com.imonkeyz.demo.service;

import com.imonkeyz.demo.entity.GroupInfoData;

import java.sql.SQLException;

public interface WeChatService {
	public Long save(GroupInfoData groupInfoData);
	public GroupInfoData find(Long id);
	public boolean remove(Long id);
}
