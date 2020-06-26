package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestBookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestBookVo;

@WebServlet("/gbc")
public class GuestBookController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/gbc --> doGet");
		
		String action = request.getParameter("action");
		
		if("addlist".equals(action)) {
			System.out.println("addlist");
			
			GuestBookDao gbd = new GuestBookDao();
		    List<GuestBookVo> gbList = gbd.getGuestBookList();
		    
		    //데이터 리퀘스트에 추가
		    request.setAttribute("guestBookList", gbList);
		    
		    //포워드
		    WebUtil.forword(request, response,"/WEB-INF/views/guestbook/addList.jsp");			
		} else if ("add".equals(action)) {
			System.out.println("방명록저장");
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");

			GuestBookVo gbv = new GuestBookVo(name,password,content);
			GuestBookDao gbd= new GuestBookDao();

			gbd.guestBookInsert(gbv);

			WebUtil.redirect(request, response, "/mysite2/gbc?action=addlist");
		} else if("deleteform".equals(action)) {
			System.out.println("삭제");
			
			int no = Integer.parseInt(request.getParameter("no")); 
			
			request.setAttribute("no", no);
			
			WebUtil.forword(request, response,"/WEB-INF/views/guestbook/deleteForm.jsp");
			
		} else if("delete".equals(action)) {
			System.out.println("삭제합시다");
			
			int id = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");
			GuestBookDao gbd= new GuestBookDao();
			gbd.guestBookDelete(id,password);
			
			WebUtil.redirect(request, response, "/mysite2/gbc?action=addlist");
			
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
