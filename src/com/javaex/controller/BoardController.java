package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("board");
		
		BoardDao bDao = new BoardDao();
		
		
		String action = request.getParameter("action");
		
		if("list".equals(action)) {
			System.out.println("list");
			List<BoardVo> bList = bDao.boardList();
			
			//데이터 리퀘스트에 추가
			request.setAttribute("bList", bList);
			
			System.out.println(bList.toString());
			//포워드
			WebUtil.forword(request, response, "/WEB-INF/views/board/list.jsp");
			
		} else if("read".equals(action)) {
			System.out.println("읽어오기");
			
			int no = Integer.parseInt(request.getParameter("no"));
			BoardVo bVo = bDao.getBoard(no);
			
			//리퀘스트에 게시글 정보 넣기
			request.setAttribute("readVo", bVo);
			
            
			WebUtil.forword(request, response, "/WEB-INF/views/board/read.jsp");
			
		}
		
	}
	
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}