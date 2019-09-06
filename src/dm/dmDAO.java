package dm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import neighborComm.neiBoardDTO;

public class dmDAO {

//*********** getConn()���� : Ŀ�ؼ�Ǯ�� ���� Ŀ�ؼ� ��ücon�� ����� ���� �޼ҵ�. 	
		private Connection getConn() throws Exception{
		
			Connection con = null;
			Context init = new InitialContext();

			// Ŀ�ؼ� Ǯ ���(context.xml������ <Resource> �±��� name������ ������)
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/vanco");
			con = ds.getConnection();
			return con;
		}// getConn() ����
		

//*********** sendDm()���� : �޼����� ������ �޼ҵ� 	
		public int sendDm(dmDTO ddto){
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			int sendCheck=0;
			
			try {
				con=getConn();				
				
			// ������ �����ϱ�
				sql="insert into dmbox(dmContent, sendUserId, sendNickname, receiveUserId, receiveNickname, writeTime, ip)"
						+ "values(?,?,?,?,?,?,?)";	
				pstmt=con.prepareStatement(sql);
				
				pstmt.setString(1, ddto.getDmContent());
				pstmt.setString(2, ddto.getSendUserId());
				pstmt.setString(3, ddto.getSendNickname());
				pstmt.setString(4, ddto.getReceiveUserId());
				pstmt.setString(5, ddto.getReceiveNickname());		
				pstmt.setTimestamp(6, ddto.getWriteTime());
				pstmt.setString(7,ddto.getIp());					
				
				pstmt.executeUpdate();
				sendCheck=1;
				
			} catch (Exception e) {
				System.out.println("sendDm()����"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
			}
			return sendCheck;
		}	// sendDm() ��

		
		
//*********** getDmList()���� : ���� DM�� ��� �ѷ��ִ� �޼ҵ�	
		public ArrayList<dmDTO> getDmList(int startRow, int pageSize, String receiveUserId){			
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			
			// ArrayList ��ü ����
			ArrayList<dmDTO> dmList=new ArrayList<dmDTO>();
			
			try {
				con=getConn();
								
				sql="select * from dmbox where receiveUserId=? order by writeTime DESC limit ?,?";
				pstmt=con.prepareStatement(sql);			
				pstmt.setString(1, receiveUserId);
				pstmt.setInt(2, startRow);
				pstmt.setInt(3, pageSize);
				rs=pstmt.executeQuery();				
				
				while(rs.next()){
					dmDTO ddto=new dmDTO();
					
					ddto.setSendUserId(rs.getString("sendUserId"));
					ddto.setReceiveUserId(rs.getString("receiveUserId"));
					ddto.setSendNickname(rs.getString("sendNickname"));
					ddto.setReceiveNickname(rs.getString("receiveNickname"));
					ddto.setDmContent(rs.getString("dmContent"));
					ddto.setWriteTime(rs.getTimestamp("writeTime"));
					ddto.setReadCheck(rs.getInt("readCheck"));
					dmList.add(ddto);
				}				
			} catch (Exception e) {
				System.out.println("getDmList()����"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}			
			return dmList;			
		}  //getDmList()��		

		
//*********** updateReadCheck()���� : �� ������ ��ȸ�� 1 ���� ������	
	public void updateReadCheck(String receiveUserId){
		Connection con = null;
		PreparedStatement pstmt=null;
		String sql = "";		
		try {
			con=getConn();				
			sql = "update dmbox set readCheck=readCheck+1 where receiveUserId =?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,receiveUserId);
			pstmt.executeUpdate();				
		} catch (Exception e) {
			System.out.println("updateReadCheck����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}			
	}//updateReadCheck()��
	
	

//*********** countNewDm()���� : ��� ���� �����ܿ��� ���ο� DM�ִ��� ���� üũ
		public int countNewDm(String receiveUserId){
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int dmReadChk = 0;
			String sql = "";		
			try {
				con=getConn();				
				
				sql="select count(*) from dmbox where receiveUserId=? and readCheck=0";	// ��� ������ ���� �Լ�
				
				pstmt=con.prepareStatement(sql);			
				pstmt.setString(1, receiveUserId);
				rs=pstmt.executeQuery();
				
				if(rs.next()){
					dmReadChk=rs.getInt(1);
				}				
				System.out.println(dmReadChk);					
				
			} catch (Exception e) {
				System.out.println("countNewDm����"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}	
			
		return dmReadChk;
	}//updateReadCount()��
	
	
	
//*********** getDmCount()���� : ���� ���������� �����ϴ� ��� ������ ���� �Լ�
	public int getDmCount(String receiveUserId){
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		int count = 0;
		
		try {
			con=getConn();
			sql="select count(*) from dmbox where receiveUserId=? ";	// ���̺��� �� ���� ���� �Լ�
			
			pstmt=con.prepareStatement(sql);	
			pstmt.setString(1, receiveUserId);
			rs=pstmt.executeQuery();			
			
			if(rs.next()){
				count=rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("getDmCount()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return count;
	}//getDmCount()��	
	
	
	
	
	
	
}//DAO Ŭ���� ��
