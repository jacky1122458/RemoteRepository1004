package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import model.CustomerBean;
import model.CustomerDAO;

public class CustomerDAOJdbc implements CustomerDAO {
	private DataSource dataSource;
	public CustomerDAOJdbc(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	private static final String SELECT_BY_CUSTID = "select * from customer where custid = ?";
	@Override
	public CustomerBean select(String custid) {
		CustomerBean result = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
//			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SELECT_BY_CUSTID);
			stmt.setString(1, custid);
			rset = stmt.executeQuery();
			if(rset.next()) {
				result = new CustomerBean();
				result.setCustid(rset.getString("custid"));
				result.setPassword(rset.getBytes("password"));
				result.setEmail(rset.getString("email"));
				result.setBirth(rset.getDate("birth"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rset!=null) {
				try {
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			if (stmt!=null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
		}
		return result;
	}
	
	private static final String UPDATE = "update customer set password=?, email=?, birth=? where custid=?";
	@Override
	public boolean update(byte[] password, String email, java.util.Date birth, String custid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(UPDATE);
			stmt.setBytes(1, password);
			stmt.setString(2, email);
			if(birth!=null) {
				long time = birth.getTime();
				java.sql.Date date = new java.sql.Date(time);
				stmt.setDate(3, date);
			} else {
				stmt.setDate(3, null);
			}
			stmt.setString(4, custid);
			int i = stmt.executeUpdate();
			if(i==1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt!=null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
			if (conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
		}
		return false;
	}
}
