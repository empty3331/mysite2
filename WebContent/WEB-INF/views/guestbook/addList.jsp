<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.javaex.vo.GuestBookVo" %>
<%@ page import="com.javaex.dao.GuestBookDao" %>
<%@ page import="java.util.List" %>

<%
	GuestBookDao gbd = new GuestBookDao();
	List<GuestBookVo> gbList = gbd.getGuestBookList();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/mysite2/assets/css/mysite.css" rel="stylesheet"
	type="text/css">
<link href="/mysite2/assets/css/guestbook.css" rel="stylesheet"
	type="text/css">
</head>
<body>

	<div id="wrap">
		<jsp:include page="/WEB-INF/views/include/header.jsp"></jsp:include>
		<!-- //header -->

		<div id="nav">
			<ul>
				<li><a href="">방명록</a></li>
				<li><a href="">갤러리</a></li>
				<li><a href="">게시판</a></li>
				<li><a href="">입사지원서</a></li>
			</ul>
			<div class="clear"></div>
		</div>
		<!-- //nav -->
		<div id="aside">
			<h2>방명록</h2>
			<ul>
				<li>일반방명록</li>
				<li>ajax방명록</li>
			</ul>
		</div>
		<!-- //aside -->

		<div id="content">

			<div id="content-head">
				<h3>일반방명록</h3>
				<div id="location">
					<ul>
						<li>홈</li>
						<li>방명록</li>
						<li class="last">일반방명록</li>
					</ul>
				</div>
				<div class="clear"></div>
			</div>
			<!-- //content-head -->

			<div id="guestbook">
				<form action="/mysite2/gbc" method="get">
					<table id="guestAdd">
						<colgroup>
							<col style="width: 70px;">
							<col>
							<col style="width: 70px;">
							<col>
						</colgroup>
						<tbody>
							<tr>
								<td><label class="form-text" for="input-uname">이름</label></td>
								<td><input id="input-uname" type="text" name="name"></td>
								<td><label class="form-text" for="input-pass">패스워드</label></td>
								<td><input id="input-pass" type="password" name="password"></td>
							</tr>
							<tr>
								<td colspan="4"><textarea name="content" cols="72" rows="5"></textarea></td>
							</tr>
							<tr class="button-area">
								<td colspan="4"><button type="submit">등록</button></td>
							</tr>
						</tbody>

					</table>

					<!-- //guestWrite -->
					<input type="hidden" name="action" value="add">
				</form>
				

				<%
					for (GuestBookVo guest : gbList) {
				%>
				<table class="guestRead">
					<colgroup>
						<col style="width: 10%;">
						<col style="width: 40%;">
						<col style="width: 40%;">
						<col style="width: 10%;">
					</colgroup>

					<tr>
						<td><%=guest.getNo()%></td>
						<td><%=guest.getName()%></td>
						<td><%=guest.getDate()%></td>
						<td><a
							href="/mysite2/gbc?no=<%=guest.getNo()%>&action=deleteform">삭제</a>
						</td>
					</tr>
					<tr>
						<td colspan="4"><%= guest.getContent() %></td>
					</tr>
				</table>
				<br>
				<%} %>


			</div>
			<!-- //guestbook -->
		</div>
		<!-- //content  -->


		<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
		<!-- //footer -->
	
</div>
	<!-- //wrap -->
</body>
</html>