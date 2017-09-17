package com.imonkeyz.demo.service.impl;

import com.imonkeyz.demo.dao.WXGroupDAO;
import com.imonkeyz.demo.entity.GroupInfoData;
import com.imonkeyz.demo.entity.PanelInfoData;
import com.imonkeyz.demo.service.WeChatService;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WeChatServiceImpl implements WeChatService {

	private final static Logger LOG = Logger.getLogger(WeChatServiceImpl.class);

	@Autowired
	private WXGroupDAO wxGroupDAO;

	public Long save(GroupInfoData groupInfoData) {
		Long id = null;
		try {
			id = wxGroupDAO.saveGroupInfo(groupInfoData);
		} catch (SQLException e) {
			LOG.error("Errors while saving GroupInfo", e);
		}
		return id;
	}

	public GroupInfoData find(Long id) {
		GroupInfoData groupInfoData = null;
		try {
			groupInfoData = wxGroupDAO.findGroupInfoByID(id);
		} catch (SQLException e) {
			LOG.error("Errors while accessing GroupInfo by id: " + id, e);
		}
		return groupInfoData;
	}

	public boolean remove(Long id) {
		return false;
	}
}
