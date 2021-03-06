package com.imonkeyz.demo.startup;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class H2ServerStartup {

	private final static Logger LOG = Logger.getLogger(H2ServerStartup.class);

	private final static String TABLE_SCHEMA = "WXGROUP";
	private final static String SQL_SCHEMA = "CREATE SCHEMA WXGROUP";

	private final static String TABLE_INFO = "INFO";
	private final static String SQL_WXGROUP_INFO = "create table WXGROUP.INFO (ID bigint primary key, OPENID varchar(32) not null, NAME varchar(255) not null, DATETIME varchar(16), INTRO text, BANNER text, AVATAR longtext, STATE varchar(3) not null default '000')";

	private final static String TABLE_PANEL = "PANEL";
	private final static String SQL_WXGROUP_PANEL = "create table WXGROUP.PANEL (ID int auto_increment primary key, INFOID bigint not null, TITLE varchar(255), CONTENT text)";

	private final static String TABLE_QR = "QR";
	private final static String SQL_WXGROUP_QR = "create table WXGROUP.QR (ID varchar(32) primary key, INFOID bigint not null, DATA text not null)";

	private final static String TABLE_QR2OPENID = "QR2OPENID";
	private final static String SQL_WXGROUP_QR2OPENID = "create table WXGROUP.QR2OPENID(ID int auto_increment, QRID varchar(32) not null, OPENID varchar(32) not null, primary key (QRID, OPENID))";

	private final static String TABLE_AUTH = "AUTH";
	private final static String SQL_WXGROUP_AUTH = "create table WXGROUP.AUTH(UUID varchar(32) primary key, TSM bigint not null)";

	private Server tcpServer = null;
	private Server webServer = null;

	private Timer tokenCleaner;

	@Autowired
	private JdbcConnectionPool h2dbcp;

	@PostConstruct
	public void init() throws SQLException {
		startServer();
		initDataBase();
	}

	private void startServer() throws SQLException {
		tcpServer = Server.createTcpServer(new String[] {}).start();
		LOG.info("DATABASE TCP STATUS: " + tcpServer.getStatus());

		webServer = Server.createWebServer(new String[] {}).start();
		LOG.info("DATABASE WEB STATUS: " + webServer.getStatus());
	}

	private void initDataBase() {
		LOG.info("Database Initializing ...");
		//h2dbcp.setMaxConnections(10);
		try {
			initSchema();
			initTable(TABLE_INFO, SQL_WXGROUP_INFO);
			initTable(TABLE_PANEL, SQL_WXGROUP_PANEL);
			initTable(TABLE_QR, SQL_WXGROUP_QR);
			initTable(TABLE_QR2OPENID, SQL_WXGROUP_QR2OPENID);
			initTable(TABLE_AUTH, SQL_WXGROUP_AUTH);
			LOG.info("Database is OK !");
			LOG.info("Database Status: MaxConnections = " + h2dbcp.getMaxConnections() + ", ActiveConnections = " + h2dbcp.getActiveConnections());

			initTokenCleaner();
		} catch (SQLException e) {
			LOG.error("Errors while initializing database!", e);
		}
	}

	private void initTokenCleaner() {
		LOG.info("AuthToken Cleaner is working ...");
		tokenCleaner = new Timer();
		tokenCleaner.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					long expiredMs = 60 * 5 * 1000L;
					long tsm = System.currentTimeMillis() - expiredMs;
					String tableName = TABLE_SCHEMA + "." + TABLE_AUTH;
					Connection conn = h2dbcp.getConnection();
					PreparedStatement pst = conn.prepareStatement(" DELETE FROM " + tableName + " WHERE TSM < ? ");
					pst.setLong(1, tsm);
					int i = pst.executeUpdate();
					if ( i > 0 ) {
						LOG.info(i + " expired token has been cleaned.");
					}
					conn.close();
				} catch (SQLException e) {}
			}
		}, 0, 60000);
	}

	/**
	 * init schema WXGROUP
	 * @throws SQLException
	 */
	private void initSchema() throws SQLException {
		String sql = "SELECT COUNT(1) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=?";
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, TABLE_SCHEMA);
		ResultSet rs = pst.executeQuery();
		if ( rs.next() ) {
			if ( rs.getInt(1) < 1 ) {
				conn.createStatement().execute(SQL_SCHEMA);
				LOG.info("SCHEMA [" + TABLE_SCHEMA + "] is OK!");
			}
		}
		conn.close();
	}

	/**
	 * create table
	 * @param initSql
	 * @return
	 * @throws SQLException
	 */
	private void initTable(String tableName, String initSql) throws SQLException {
		Connection conn = h2dbcp.getConnection();
		if ( !checkTable(conn, tableName) ) {
			Statement st = conn.createStatement();
			st.execute(initSql);
			LOG.info("Table [" + TABLE_SCHEMA + "." + tableName + "] is OK!");
		}
		conn.close();
	}

	/**
	 * returning table is existing TRUE / FALSE
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	private boolean checkTable(Connection conn, String tableName) throws SQLException {
		String sql = " SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=? AND TABLE_NAME=? ";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, TABLE_SCHEMA);
		pst.setString(2, tableName);
		ResultSet rs = pst.executeQuery();
		return rs.next() ? rs.getInt(1) > 0 : false;
	}

	@PreDestroy
	public void destroy() {
		try {tokenCleaner.cancel();} catch (Exception e) {}
		try {tcpServer.stop();} catch (Exception e) {}
		try {webServer.stop();} catch (Exception e) {}
	}
}
