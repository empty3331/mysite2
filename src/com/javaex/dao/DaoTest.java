package com.javaex.dao;

import com.javaex.vo.UserVo;

public class DaoTest {

	public static void main(String[] args) {
		
		UserDao dao = new UserDao();
		
		UserVo vo = new UserVo("hi","3456","김룬룬","male");
		dao.insert(vo);
		
		
	}

}
