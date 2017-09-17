package com.imonkeyz.demo.startup;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.*;

@Component
public class H2ServerStartup {

	private final static Logger LOG = Logger.getLogger(H2ServerStartup.class);

	private final static String TABLE_SCHEMA = "WXGROUP";
	private final static String SQL_SCHEMA = "CREATE SCHEMA WXGROUP";

	private final static String TABLE_INFO = "INFO";
	private final static String SQL_WXGROUPINFO = "create table WXGROUP.INFO (ID bigint primary key, NAME varchar(255) not null, DATETIME varchar(16), INTRO text, BANNER longtext, AVATAR longtext)";

	private final static String TABLE_PANEL = "PANEL";
	private final static String SQL_WXGROUPPANEL = "create table WXGROUP.PANEL (ID int auto_increment primary key, INFOID bigint not null, TITLE varchar(255), CONTENT text)";

	private Server tcpServer = null;
	private Server webServer = null;

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
			initTable(TABLE_INFO, SQL_WXGROUPINFO);
			initTable(TABLE_PANEL, SQL_WXGROUPPANEL);
			LOG.info("Database is OK !");
			LOG.info("Database Status: MaxConnections = " + h2dbcp.getMaxConnections() + ", ActiveConnections = " + h2dbcp.getActiveConnections());
		} catch (SQLException e) {
			LOG.error("Errors while initializing database!", e);
		}
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
		try {tcpServer.stop();} catch (Exception e) {}
		try {webServer.stop();} catch (Exception e) {}
	}
}
