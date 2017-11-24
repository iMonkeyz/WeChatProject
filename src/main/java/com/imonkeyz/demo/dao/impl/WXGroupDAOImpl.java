package com.imonkeyz.demo.dao.impl;

import com.imonkeyz.demo.dao.WXGroupDAO;
import com.imonkeyz.demo.entity.AuthTokenData;
import com.imonkeyz.demo.entity.GroupInfoData;
import com.imonkeyz.demo.entity.PanelInfoData;
import com.imonkeyz.demo.entity.QRCodeData;
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
import java.util.UUID;

@Repository
public class WXGroupDAOImpl implements WXGroupDAO {

	private final static Logger LOG = Logger.getLogger(WXGroupDAOImpl.class);

	@Autowired
	private JdbcConnectionPool h2dbcp;

	public Long saveGroupInfo(GroupInfoData groupInfoData) throws SQLException {
		String sql = " MERGE INTO WXGROUP.INFO (ID, NAME, OPENID, DATETIME, INTRO, BANNER, AVATAR) KEY(ID) VALUES (?, ?, ?, ?, ?, ?, ?) ";
		Long id = groupInfoData.getId() == null ? new Date().getTime() : groupInfoData.getId();
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		int i = 1;
		pst.setLong(i++, id);
		pst.setString(i++, groupInfoData.getName());
		pst.setString(i++, groupInfoData.getOpenId());
		pst.setString(i++, groupInfoData.getDatetime());
		pst.setString(i++, groupInfoData.getIntro());
		pst.setString(i++, groupInfoData.getBanner());
		pst.setString(i++, groupInfoData.getAvatar());
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
		String sql = "INSERT INTO WXGROUP.QR (ID, INFOID, DATA) VALUES(?, ?, ?)";
		PreparedStatement pst = conn.prepareStatement(sql);
		for (QRCodeData qr : groupInfoData.getQrs()) {
			pst.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
			pst.setLong(2, id);
			pst.setString(3, qr.getData());
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

	public GroupInfoData findGroupInfoByID(Long id, boolean editMode) throws SQLException {
		GroupInfoData groupInfoData = null;
		String sql = " SELECT * FROM WXGROUP.INFO WHERE ID=? AND STATE='000' ";
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
			List<QRCodeData> qrs = null;
			if ( editMode ) {
				qrs = getQrs(conn, id);
			}
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

	private List<QRCodeData> getQrs(Connection conn, Long infoId) throws SQLException {
		List<QRCodeData> qrs = new ArrayList<QRCodeData>();
		String sql =" SELECT ID, 0 as COUNTER, DATA FROM WXGROUP.QR A WHERE A.INFOID=? AND NOT EXISTS( SELECT 1 FROM WXGROUP.QR2OPENID WHERE A.ID=QRID ) " +
					" UNION " +
					" SELECT qr.ID, COUNT(1) as COUNTER, qr.DATA FROM WXGROUP.QR qr JOIN WXGROUP.QR2OPENID qo ON qr.ID = qo.QRID WHERE qr.INFOID=? GROUP BY qr.ID " +
					"";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, infoId);
		pst.setLong(2, infoId);
		ResultSet rs = pst.executeQuery();
		while ( rs.next() ) {
			String uuid = rs.getString("ID");
			String data = rs.getString("DATA");
			int counter = rs.getInt("COUNTER");
			qrs.add(new QRCodeData(uuid, data, counter));
		}
		return qrs;
	}

	public boolean removeGroupInfoByID(Long id) throws SQLException {
		String sql = " UPDATE WXGROUP.INFO SET STATE='100' WHERE ID=? ";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setLong(1, id);
		int i = pst.executeUpdate();
		conn.close();
		return i > 0;
	}

	public List<GroupInfoData> findAllGroupInfoByOpenId(String openID, boolean isFull) throws Exception {
		List<GroupInfoData> list = new ArrayList<GroupInfoData>();
		String sql = " SELECT * FROM WXGROUP.INFO WHERE OPENID=? AND STATE='000' ";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, openID);
		ResultSet rs = pst.executeQuery();
		while( rs.next() ) {
			long id = rs.getLong("ID");
			String name = rs.getString("NAME");
			String datetime = rs.getString("DATETIME");
			String banner = rs.getString("BANNER");
			List<PanelInfoData> infos = null;
			if ( isFull ) {
				infos = getPanels(conn, id);
			}
			list.add(new GroupInfoData(id, name, datetime, banner, infos));
		}
		conn.close();
		return list;
	}

	public String assignQr2OpenId(String infoID, String openID) throws SQLException {
		String qrID = null;
		String qrData = null;

		String sql = " SELECT a.ID, a.DATA FROM WXGROUP.QR a JOIN WXGROUP.QR2OPENID b ON a.ID=b.QRID WHERE a.INFOID=? AND b.OPENID=? ";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, infoID);
		pst.setString(2, openID);
		ResultSet rs = pst.executeQuery();
		if ( rs.next() ) {
			//1. 已有, 直接读取
			qrID = rs.getString("ID");
			qrData= rs.getString("DATA");
			LOG.info("用户已分配QR[" + qrID + "]");
		} else {
			//2. 没有, 新分配一个.
			//2.1 先读取可分配的qr
			sql = " SELECT a.ID, a.DATA, COUNT(1) AS EXPOSED FROM WXGROUP.QR a LEFT JOIN WXGROUP.QR2OPENID b ON a.ID=b.QRID WHERE a.INFOID=? GROUP BY a.ID HAVING COUNT(1) < 100 ORDER BY EXPOSED DESC LIMIT 1 ";
			pst = conn.prepareStatement(sql);
			pst.setString(1, infoID);
			rs = pst.executeQuery();
			if ( rs.next() ) {
				qrID = rs.getString("ID");
				qrData = rs.getString("DATA");
				int exposed = rs.getInt("EXPOSED");
				LOG.info("用户[" + openID + "] 新分配QR[" + qrID + "], QR曝光次数: " + exposed);

				//2.2 将ID分配到OPENID
				sql = " INSERT INTO WXGROUP.QR2OPENID (QRID, OPENID) VALUES (?, ?) ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, qrID);
				pst.setString(2, openID);
				pst.executeUpdate();
			}
		}
		conn.close();
		return qrData;
	}

	public void saveAuthToken(AuthTokenData token) throws SQLException {
		String sql = " MERGE INTO WXGROUP.AUTH (UUID, TSM) KEY(UUID) VALUES(?, ?) ";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, token.getUuid());
		pst.setLong(2, token.getTsm());
		pst.executeUpdate();
		conn.close();
	}

	public AuthTokenData findAuthTokenByUUID(String uuid) throws SQLException {
		AuthTokenData token = null;
		String sql = " SELECT * FROM WXGROUP.AUTH WHERE UUID=? ";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, uuid);
		ResultSet rs = pst.executeQuery();
		if ( rs.next() ) {
			token = new AuthTokenData(uuid, rs.getLong("tsm"));
		}
		conn.close();
		return token;
	}

	public void removeAuthTokenByUUID(String uuid) throws SQLException {
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(" DELETE FROM WXGROUP.AUTH WHERE UUID=? ");
		pst.setString(1, uuid);
		pst.executeUpdate();
		conn.close();
	}
}
