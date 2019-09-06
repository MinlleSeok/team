package com.comments.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.common.db.IbatisDAO;

/*
 * comments table DAO
 */

public class CommentsDAO extends IbatisDAO {

	// ?���??�� ?��?��
	public static CommentsDAO getInstance() {
		
		CommentsDAO _instance = new CommentsDAO();
		
		_instance.SetDB();
		
		return _instance;
		
	}
	
	/*
	 * 기능 메서?��
	 */
	
	@SuppressWarnings("unchecked")
	public ArrayList<Comments> getCommentsList(HashMap<String, Object> map, int page) throws SQLException {
		
		ArrayList<Comments> commentsList = null;
		commentsList = (ArrayList<Comments>)GetDB().queryForList("getCommentsList", map, page, 10);
		
		return commentsList;
	}

	public void insertComment(Comments comments) throws SQLException {
		// TODO Auto-generated method stub
		GetDB().insert("insertComment", comments);
		int max = (Integer)GetDB().queryForObject("maxComment", comments);
		comments.setNum(max);
		GetDB().update("updateComment", comments);
	}

	public void reInsertComment(Comments comments) throws SQLException {
		GetDB().insert("insertReComment", comments);
		int reNum = comments.getReNum();
		int moimNum = comments.getMoimNum();
		int idx = comments.getIdx();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		map.put("moimNum", moimNum);
		map.put("reNum", reNum);
		int max = (Integer)GetDB().queryForObject("maxComment", comments);
		// int sum = (int) GetDB().queryForObject("selectReDepOdr",map);
		map.put("sum", 0);
		map.put("max", max);
		GetDB().update("updateReComment", map);
		
	}

	public void modifyComment(Comments comments) throws SQLException {
		GetDB().update("modifyComment", comments);
	}
	
	public Comments bringComment(Map<String, Integer> map) throws SQLException {

		Comments comments = null;
		comments = (Comments) GetDB().queryForObject("bringComment", map);
		return comments;
	}


	public void deleteComment(Map<String, Integer> map) throws SQLException {
		GetDB().delete("deleteComment", map);
	}

	public String getCommentUser(HashMap<String, Object> map) throws SQLException {
		String userName = "";
		userName = (String) GetDB().queryForObject("getCommentUser", map);
		return userName;
	}

	public int getCommentsCount(HashMap<String, Object> map) throws SQLException {
		int count = 0;
		count = (Integer) GetDB().queryForObject("getCommentsCount", map);
		return count;
	}

	public void reReInsertComment(Comments comments) throws SQLException {
		int reReNum = (Integer) GetDB().queryForObject("reReGetReNum", comments.getReNum());
		comments.setReNum(reReNum);
		GetDB().insert("insertReComment", comments);
		int reNum = comments.getReNum();
		int moimNum = comments.getMoimNum();
		int idx = comments.getIdx();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		map.put("moimNum", moimNum);
		map.put("reNum", reNum);
		int sum = (int) GetDB().queryForObject("selectReDepOdr",map);
		map.put("sum", sum);
		GetDB().update("updateReComment", map);
	}

}
