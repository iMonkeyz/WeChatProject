package com.imonkeyz.demo.dao.impl;

import com.imonkeyz.demo.dao.WXGroupDAO;
import com.imonkeyz.demo.entity.GroupInfoData;
import com.imonkeyz.demo.entity.PanelInfoData;
import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class WXGroupDAOImpl implements WXGroupDAO {

	private final static Logger LOG = Logger.getLogger(WXGroupDAOImpl.class);

	@Autowired
	private JdbcConnectionPool h2dbcp;

	public Long saveGroupInfo(GroupInfoData groupInfoData) throws SQLException {
		String sql = " MERGE INTO WXGROUP.INFO (ID, NAME, DATETIME, INTRO, BANNER, AVATAR) KEY(ID) VALUES (?, ?, ?, ?, ?, ?) ";
		Long id = new Date().getTime();
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, id);
		pst.setString(2, groupInfoData.getName());
		pst.setString(3, groupInfoData.getDatetime());
		pst.setString(4, groupInfoData.getIntro());
		pst.setString(5, groupInfoData.getBanner());
		pst.setString(6, groupInfoData.getAvatar());
		pst.executeUpdate();

		//save panels
		savePanelInfo(conn, id, groupInfoData);

		//save qrs
		saveQR(conn, id, groupInfoData);

		conn.close();
		return id;
	}

	private void savePanelInfo(Connection conn, Long id, GroupInfoData groupInfoData) throws SQLException {
		clearPanelInfo(conn, id);
		String sql = " INSERT INTO WXGROUP.PANEL (INFOID, TITLE, CONTENT) VALUES (?, ?, ?) ";
		PreparedStatement pst = conn.prepareStatement(sql);
		for (PanelInfoData panel : groupInfoData.getInfos()) {
			pst.setLong(1, id);
			pst.setString(2, panel.getTitle());
			pst.setString(3, panel.getContent());
			pst.addBatch();
		}
		pst.executeBatch();
	}

	private void saveQR(Connection conn, Long id, GroupInfoData groupInfoData) throws SQLException {
		clearQR(conn, id);
		String sql = "INSERT INTO WXGROUP.QR (INFOID, DATA) VALUES(?, ?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		for (String qr : groupInfoData.getQrs()) {
			pst.setLong(1, id);
			pst.setString(2, qr);
			pst.addBatch();
		}
		pst.executeBatch();
	}

	private void clearPanelInfo(Connection conn, Long id) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(" DELETE FROM WXGROUP.PANEL WHERE INFOID=? ");
		pst.setLong(1, id);
		pst.executeUpdate();
	}

	private void clearQR(Connection conn, Long id) throws SQLException {
		PreparedStatement pst = conn.prepareStatement(" DELETE FROM WXGROUP.QR WHERE INFOID=? ");
		pst.setLong(1, id);
		pst.executeUpdate();
	}

	public GroupInfoData findGroupInfoByID(Long id) throws SQLException {
		GroupInfoData groupInfoData = null;
		String sql = " SELECT * FROM WXGROUP.INFO WHERE ID=? ";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, id);
		ResultSet rs = pst.executeQuery();
		if ( rs.next() ) {
			String name = rs.getString("NAME");
			String datetime = rs.getString("DATETIME");
			String intro = rs.getString("INTRO");
			String banner = rs.getString("BANNER");
			String avatar = rs.getString("AVATAR");
			List<PanelInfoData> infos = getPanels(conn, id);
			List<String> qrs = getQrs(conn, id);
			groupInfoData = new GroupInfoData(id, name, datetime, intro, banner, avatar, infos, qrs);
		}
		conn.close();
		return groupInfoData;
	}

	private List<PanelInfoData> getPanels(Connection conn, Long id) throws SQLException {
		List<PanelInfoData> infos = new ArrayList<PanelInfoData>();
		String sql = " SELECT * FROM WXGROUP.PANEL WHERE INFOID=? ";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, id);
		ResultSet rs = pst.executeQuery();
		while ( rs.next() ) {
			String title = rs.getString("TITLE");
			String content = rs.getString("CONTENT");
			infos.add(new PanelInfoData(title, content));
		}
		return infos;
	}

	private List<String> getQrs(Connection conn, Long id) throws SQLException {
		List<String> qrs = new ArrayList<String>();
		String sql = " SELECT * FROM WXGROUP.QR WHERE INFOID=? ";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, id);
		ResultSet rs = pst.executeQuery();
		while ( rs.next() ) {
			String data = rs.getString("DATA");
			qrs.add(data);
		}
		return qrs;
	}

	public boolean removeGroupInfoByID(Long id) {
		return false;
	}
}
