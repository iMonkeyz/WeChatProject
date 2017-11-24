package com.imonkeyz.demo.service.impl;

import com.imonkeyz.demo.dao.WXGroupDAO;
import com.imonkeyz.demo.entity.AuthTokenData;
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

	public WeChatServiceImpl() {
		LOG.info("===============================================");
	}

	public Long saveGroupInfo(GroupInfoData groupInfoData) {
		Long id = null;
		try {
			id = wxGroupDAO.saveGroupInfo(groupInfoData);
			LOG.info("GroupInfo has been saved with Unique ID: " + id);
		} catch (SQLException e) {
			LOG.error("Errors while saving GroupInfo", e);
		}
		return id;
	}

	public GroupInfoData findGroupInfo(Long id) {
		GroupInfoData groupInfoData = null;
		try {
			groupInfoData = wxGroupDAO.findGroupInfoByID(id, false);
		} catch (SQLException e) {
			LOG.error("Errors while accessing GroupInfo by ID: " + id, e);
		}
		return groupInfoData;
	}

	public GroupInfoData findGroupInfoForEdit(Long id) {
		GroupInfoData groupInfoData = null;
		try {
			groupInfoData = wxGroupDAO.findGroupInfoByID(id, true);
		} catch (SQLException e) {
			LOG.error("Errors while accessing GroupInfo by ID: " + id, e);
		}
		return groupInfoData;
	}

	public boolean removeGroupInfo(Long id) {
		try {
			return wxGroupDAO.removeGroupInfoByID(id);
		} catch (SQLException e) {
			LOG.error("Errors while removing GroupInfo by ID: " + id, e);
		}
		return false;
	}

	public List<GroupInfoData> findAllLightGroupInfoByOpenId(String openID) {
		List<GroupInfoData> list = new ArrayList<GroupInfoData>();
		try {
			list = wxGroupDAO.findAllGroupInfoByOpenId(openID, false);
		} catch (Exception e) {
			LOG.error("Errors while accessing Light GroupInfo with OPENID: " + openID, e);
		}
		return list;
	}

	public List<GroupInfoData> findAllFullGroupInfoByOpenId(String openID) {
		List<GroupInfoData> list = new ArrayList<GroupInfoData>();
		try {
			list = wxGroupDAO.findAllGroupInfoByOpenId(openID, true);
		} catch (Exception e) {
			LOG.error("Errors while accessing Full GroupInfo with OPENID: " + openID, e);
		}
		return list;
	}

	public String qr2OpendId(String infoID, String openID) {
		String qrData = null;
		try {
			qrData = wxGroupDAO.assignQr2OpenId(infoID, openID);
		} catch (SQLException e) {
			LOG.error("Errors while assigning QR 2 OPENID", e);
		}
		return qrData;
	}

	public void saveAuthToken(AuthTokenData token) {
		try {
			wxGroupDAO.saveAuthToken(token);
		} catch (SQLException e) {
			LOG.error("Errors while saving AuthTokenData", e);
		}
	}

	public boolean validateAuthToken(String uuid) {
		try {
			long expiredMs = 60 * 2 * 1000L;  //2 minutes
			AuthTokenData token = wxGroupDAO.findAuthTokenByUUID(uuid);
			if ( token != null ) {
				long now = System.currentTimeMillis();
				return now - token.getTsm() <= expiredMs;
			}
		} catch (SQLException e) {
			LOG.error("No AuthToken can be found with uuid: " + uuid);
		}
		return false;
	}

	public void removeAuthToken(String uuid) {
		try {
			wxGroupDAO.removeAuthTokenByUUID(uuid);
		} catch (SQLException e) {}
	}
}
