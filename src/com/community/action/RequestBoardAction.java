package com.community.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.community.db.Community;
import com.community.db.CommunityDAO;
import com.controller.action.CommandAction;

/*
 * community requestBoard Action
 */

public class RequestBoardAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		
		request.setCharacterEncoding("UTF-8");
		
		String name = request.getParameter("name");
		String category = request.getParameter("category");
		
		System.out.println(name);
		System.out.println(category);
		
		int count = CommunityDAO.getInstance().getCommCount();
		count += 10000 + 1;
		
		Community com = new Community();
		com.setNum(count);
		com.setCategory(category);
		com.setName(name);
		
		CommunityDAO.getInstance().insertCommunity(com);
		
		request.setAttribute("url", "list.bo");
		return "redirect2.jsp";
	}

	
}
