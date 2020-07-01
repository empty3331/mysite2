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
		int no;
		
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
			
			no = Integer.parseInt(request.getParameter("no"));
			BoardVo bVo = bDao.getBoard(no);
			
			//리퀘스트에 게시글 정보 넣기
			request.setAttribute("readVo", bVo);
			
            
			WebUtil.forword(request, response, "/WEB-INF/views/board/read.jsp");
		} else if("writeForm".equals(action)) {
			System.out.println("글쓰기");
			WebUtil.forword(request, response,"/WEB-INF/views/board/writeForm.jsp");
			
		} else if("write".equals(action)) {
			System.out.println("글 등록");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = Integer.parseInt(request.getParameter("user_no"));
			
			BoardVo bVo = new BoardVo(title,content,userNo);
			bDao.boardInsert(bVo);
			
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		} else if("delete".equals(action)) {
			System.out.println("삭제");
			
			no = Integer.parseInt(request.getParameter("no"));
			bDao.boardDelete(no);
			
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
		} else if("modifyForm".equals(action)) {
			System.out.println("수정폼");
			
			no = Integer.parseInt(request.getParameter("no"));
			BoardVo bVo = bDao.getBoard(no);
			
			//리퀘스트에 게시글 정보 넣기
			request.setAttribute("modifyVo", bVo);
            
			WebUtil.forword(request, response,"/WEB-INF/views/board/modifyForm.jsp");
		} else if("modify".equals(action)) {
			
			int listNo = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			bDao.boardUpdate(listNo, title, content);
			
			WebUtil.redirect(request, response, "/mysite2/board?action=list");
			
		} else if("search".equals(action)) {
			System.out.println("검색");
			String keyword = request.getParameter("keyword");
			List<BoardVo> bList = bDao.boardList(keyword);
			
			request.setAttribute("bList", bList);

			// forword 하는 방법
			WebUtil.forword(request, response, "/WEB-INF/views/board/list.jsp");
			
			System.out.println(bList.toString());

		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
