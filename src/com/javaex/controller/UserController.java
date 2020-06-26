package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");

		if ("joinForm".equals(action)) {// 회원가입 폼
			System.out.println("joinForm");
			WebUtil.forword(request, response, "/WEB-INF/views/user/joinForm.jsp");

		} else if ("join".equals(action)) {// 회원가입
			System.out.println("join");
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");

			UserVo vo = new UserVo(id, password, name, gender);
			System.out.println(vo.toString());

			UserDao userdao = new UserDao();
			userdao.insert(vo);

			WebUtil.forword(request, response, "/WEB-INF/views/user/joinOk.jsp");
		} else if ("loginForm".equals(action)) {// 로그인폼
			WebUtil.forword(request, response, "/WEB-INF/views/user/loginForm.jsp");

		} else if ("login".equals(action)) {
			System.out.println("login");
			String id = request.getParameter("id");
			String password = request.getParameter("password");

			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, password);

			if (authVo == null) {// 로그인실패
				System.out.println("로그인실패");
				WebUtil.redirect(request, response, "/mysite2/user?action=loginForm&result=fail");

			} else {
				// 로그인성공
				// 세션영역에 값을 추가
				HttpSession session = request.getSession();
				session.setAttribute("authUser", authVo);

				WebUtil.redirect(request, response, "/mysite2/main");

			}

		} else if("logout".equals(action)) {//로그아웃 일때
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite2/main");
		} else if("modifyForm".equals(action)) {
			System.out.println("modifyForm");
			HttpSession session = request.getSession();
			//UserVo vo = (UserVo)session.getAttribute("authUser");
			//vo.getNo();
			int no = ((UserVo)session.getAttribute("authUser")).getNo();
			
			UserDao userDao = new UserDao();
			UserVo vo =userDao.getUser(no);
			System.out.println(vo.toString());
			
	
			request.setAttribute("userVo", vo);
			
			WebUtil.forword(request, response, "/WEB-INF/views/user/modifyForm.jsp");
			
		} else if("modify".equals(action)) {
			HttpSession session = request.getSession();
			int no = ((UserVo)session.getAttribute("authUser")).getNo();
			System.out.println(no);
			
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo vo = new UserVo(no,"",password,name,gender);//생성자 새로 안 넣고 값 넣어도 됨
			System.out.println(vo.toString());
			
			UserDao userDao = new UserDao();
			userDao.update(vo);
			
			//세션값 업데이트
			//필요없는값도 같이 세션에 올라감
			//session.setAttribute("authUser", vo);
					
			//세션에 이름만 수정하기
			UserVo sVo = (UserVo)session.getAttribute("authUser");
			sVo.setName(name);
			
			WebUtil.redirect(request, response, "/mysite2/main");
			
		} 

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
