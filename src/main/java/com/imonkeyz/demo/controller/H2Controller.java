package com.imonkeyz.demo.controller;

import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jesse on 2017/9/15.
 */
@Controller
public class H2Controller {
	@Autowired
	private JdbcConnectionPool h2dbcp;

	@RequestMapping("/h2/test")
	public String testdb() throws SQLException {
		Connection conn = h2dbcp.getConnection();
		PreparedStatement pst = conn.prepareStatement("select TABLE_NAME from information_schema.tables;");
		ResultSet rs = pst.executeQuery();
		while( rs.next() ) {
			String table_name = rs.getString("TABLE_NAME");
			System.out.println(table_name);
		}
		return "index";
	}
}
