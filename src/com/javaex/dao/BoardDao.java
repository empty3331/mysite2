package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {
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
	
	//게시판 글쓰기
	public int boardInsert(BoardVo boardVo) {
		int count = 0;
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " INSERT INTO board ";
			query += " VALUES (seq_board_no.nextval,?,?,0,SYSDATE,?)";
			
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUserNo());
			
			count = pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	
	//게시판 리스트(노검색)
	public List<BoardVo> boardList(){
		return boardList("");
	}
	
	//게시판  리스트(검색)
	public List<BoardVo> boardList(String keyword){
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		getConnection();
		try {
			String query = "";
			query += " SELECT b.no,";
			query += "        b.title,";
			query += "        u.name,";
			query += "        b.hit,";
			query += "        to_char(b.reg_date, 'yyyy-mm-dd') reg_date,";
			query += "       b.user_no";
			query += " FROM board b, users u";
			query += " WHERE b.user_no = u.no";
			
			if (keyword != "" || keyword == null) {
				query += " and b.title like ? ";
				pstmt = conn.prepareStatement(query);
				
				pstmt.setString(1, '%' + keyword + '%');
			} else {
				pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			}
			
			rs = pstmt.executeQuery();
			
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				
				BoardVo boardVo = new BoardVo(no,title,name,hit,regDate,userNo);
				boardList.add(boardVo);
			}
			
		}
		catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		
		return boardList;
	}
	
	//게시물 읽기
	
	public BoardVo getBoard(int no) {
		BoardVo bVo = null;
		getConnection();
		try {
			String query = "";
			query += "SELECT b.no,";
			query += "       u.name,";
			query += "       b.hit,";
			query += "       to_char(b.reg_date, 'yyyy-mm-dd') reg_date,";
			query += "       b.title,";
			query += "       b.content,";
			query += "       b.user_no";
			query += " FROM board b left outer join users u";
			query += " ON b.user_no = u.no";
			query += " where b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
		    
		    // 4.결과처리
		    while(rs.next()) {
		    	String name = rs.getString("name");
		    	int hit = rs.getInt("hit");
		    	String regDate = rs.getString("reg_date");
		    	String title = rs.getString("title");
		    	String content = rs.getString("content");
		    	int userNo = rs.getInt("user_no");
		    	
		    	bVo = new BoardVo(no, name, hit, regDate, title, content, userNo);
		    }
		}
		catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return bVo;
	}
	
	//게시물 삭제
	public void boardDelete(int no) {
		getConnection();
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = ""; // 쿼리문 문자열만들기, ? 주의
			query += " delete from board ";
			query += " where no = ? ";
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			
			pstmt.setInt(1, no);// ?(물음표) 중 1번째, 순서중요
			
			pstmt.executeUpdate(); 
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
	}
	
	//게시물 수정
	public void boardUpdate(int no, String title, String content) {
		getConnection();
		try {
			String query = "";
			query += " update board ";
			query += " set title = ?,";
			query += "     content   = ? ";
			query += " where no = ? ";
			
			pstmt = conn.prepareStatement(query); 
			
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, no);
			
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		
	}
	

}
