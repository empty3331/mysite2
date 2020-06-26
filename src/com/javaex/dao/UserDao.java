package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	private void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	// 회원추가
	public int insert(UserVo vo) {
		int count = 0;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += "INSERT INTO users ";
			query += " VALUES (seq_users_no.nextval,?,?,?,?)";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getGender());

			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();

		return count;
	}

	// 로그인한 사용자 가져오기
	public UserVo getUser(String id, String password) {
		UserVo vo = null;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " SELECT no, name";
			query += " FROM users";
			query += " WHERE id =?";
			query += " AND password = ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, id);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");

				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return vo;
	}
	// 사용자 정보 가져오기

	public UserVo getUser(int no) {
		UserVo vo = null;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " SELECT no, id, password, name, gender";
			query += " FROM users";
			query += " WHERE no = ?";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int rNo = rs.getInt("no");
				String id = rs.getString("id");
				String password = rs.getString("password");
				String name = rs.getString("name");
				String gender = rs.getString("gender");

				vo = new UserVo(rNo, id, password, name,gender);
			
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return vo;
	}
	
	public int update(UserVo vo) {
		int count = 0;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " update users";
			query += " set password = ?,";
			query += "     name = ?,";
			query += "     gender = ?";
			query += " where no = ?";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, vo.getPassword());
			pstmt.setString(2, vo.getName());
			pstmt.setString(3, vo.getGender());
			pstmt.setInt(4, vo.getNo());
			
			count = pstmt.executeUpdate();
			
		}
		catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}

}
