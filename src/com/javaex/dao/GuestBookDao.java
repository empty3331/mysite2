package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestBookVo;

public class GuestBookDao {
	// 0. import java.sql.*;
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

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

	public void close() {
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
	
	// 방명록추가
	public int guestBookInsert(GuestBookVo guestBookVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " INSERT INTO guestbook ";
			query += " VALUES (seq_guestbook_id.nextval,?,?,?,SYSDATE) ";
			// System.out.println(query);

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, guestBookVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, guestBookVo.getPassword()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, guestBookVo.getContent()); // ?(물음표) 중 3번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println("[" + count + "건 추가되었습니다.]");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	// 방명록 리스트(검색안할때)
	public List<GuestBookVo> getGuestBookList() {
		return getGuestBookList("");
	}

	// 방명록 리스트(검색할때)
	public List<GuestBookVo> getGuestBookList(String keword) {
		List<GuestBookVo> guestBookList = new ArrayList<GuestBookVo>();

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행 --> 완성된 sql문을 가져와서 작성할것
			String query = "";
			query += " SELECT no, ";
			query += "        name, ";
			query += "        password, ";
			query += "        content, ";
			query += "        req_date ";
			query += " FROM guestbook";

			if (keword != "" || keword == null) {
				query += " where name like ? ";
				query += " or content like  ? ";
				query += " or no like ? ";
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기

				pstmt.setString(1, '%' + keword + '%'); // ?(물음표) 중 1번째, 순서중요
				pstmt.setString(2, '%' + keword + '%'); // ?(물음표) 중 2번째, 순서중요
				pstmt.setString(3, '%' + keword + '%'); // ?(물음표) 중 3번째, 순서중요
			} else {
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			}

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String date = rs.getString("req_date");

				GuestBookVo guestBookVo = new GuestBookVo(no, name, password, content,date);
				guestBookList.add(guestBookVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return guestBookList;

	}
	
	// 방명록 수정
	public int guestBookUpdate(GuestBookVo guestBookVo) {
		int count = 0;
		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " UPDATE guestbook ";
			query += " set name = ?, ";
			query += "     password = ?, ";
			query += "     content =? ";
			query += " WHERE no =? ";

			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setString(1, guestBookVo.getName()); // ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, guestBookVo.getPassword()); // ?(물음표) 중 2번째, 순서중요
			pstmt.setString(3, guestBookVo.getContent()); // ?(물음표) 중 3번째, 순서중요
			pstmt.setInt(4, guestBookVo.getNo()); // ?(물음표) 중 4번째, 순서중요

			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println(count + "건 수정되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	//방명록 삭제
	public int guestBookDelete(int id,String password) {
		int count = 0;
		getConnection();

		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete from guestbook ";
			query += " where no = ? ";
			query += " and  password = ?";
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			pstmt.setInt(1, id);// ?(물음표) 중 1번째, 순서중요
			pstmt.setString(2, password);
			
			count = pstmt.executeUpdate(); // 쿼리문 실행

			// 4.결과처리
			// System.out.println(count + "건 삭제되었습니다.");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

}
