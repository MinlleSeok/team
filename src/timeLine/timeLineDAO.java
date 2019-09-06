package timeLine;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


import member.MemberDAO;
import member.MemberDTO;
import neighborComm.moimDTO;

public class timeLineDAO {

//*********** getConn()���� : Ŀ�ؼ�Ǯ�� ���� Ŀ�ؼ� ��ücon�� ����� ���� �޼ҵ�. 	
	private Connection getConn() throws Exception{
	
		Connection con = null;
		Context init = new InitialContext();

		// Ŀ�ؼ� Ǯ ���(context.xml������ <Resource> �±��� name������ ������)
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/vanco");
		con = ds.getConnection();
		return con;
	} // getConn() ����
	
	

//*********** getTimeLine() ���� : Ÿ�Ӷ��� ���� �� Ÿ�Ӷ��� ���� �������� �޼ҵ�
	public ArrayList<timeLineDTO> getTimeLine() {
	
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";		
		
		ArrayList<timeLineDTO> list=new ArrayList<timeLineDTO>();
		
		try {				
		// ������ �����ϱ�
			con=getConn();				
			
			sql ="SELECT A.*, B.userNickname, B.userPhoto, B.userCity, B.userDistrict "				
				+ "FROM timeLine A, vancomember B "
				+ "WHERE A.userId = B.userId "
				+ "GROUP BY A.timeLineNum "
				+ "ORDER BY A.writeTime DESC;";
			
			pstmt=con.prepareStatement(sql);			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				timeLineDTO tdto=new timeLineDTO();
				
				// ������ ���� ó�� �ϱ�(JSTL ���� �ڹٴܿ��� ó�� �ϴ� ���� ����)
				String content = "";
				if(rs.getString("content")!=null){
					content = rs.getString("content").replace("\r\n","<br>").replace("\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
				}
				
				// �ð� ���� �ٲٱ�				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm"); 
							
				String writeTime=sdf.format(rs.getTimestamp("writeTime"));				
				
				Date now = new Date();				
				Date wTime = sdf.parse(writeTime);
				long writetime = wTime.getTime();
				long nowTime=now.getTime();
				long timeGap = (nowTime-writetime)/1000/60;  // �и������带 ������ ȯ��
				
				if(timeGap<2){ writeTime = "����";}
				else if(timeGap>=2 && timeGap<60){writeTime = timeGap+"����";}
				else if(timeGap>=60 && timeGap<1440){writeTime = timeGap/60+"�ð���";}
				else if(timeGap>=1440 && timeGap<43200){writeTime = timeGap/60/24+"����";}
				else if(timeGap>=43200){writeTime = timeGap/60/24/30+"������";}			
				
				
				tdto.setTimeLineNum(rs.getInt("timeLineNum"));
				tdto.setTitle(rs.getString("title"));
				tdto.setContent(content);
				tdto.setWriteTimeStr(writeTime);
				tdto.setUserId(rs.getString("userId"));	
				tdto.setUserPhoto(rs.getString("userPhoto"));	
				tdto.setUserCity(rs.getString("userCity"));	
				tdto.setUserDistrict(rs.getString("userDistrict"));	
				tdto.setUserNickname(rs.getString("userNickname"));	
				
				list.add(tdto);
			}
		} catch (Exception e) {
			System.out.println("getTimeLine()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
		return list;
	} //getTimeLine()����


//*********** getTimePhoto() ���� : Ÿ�Ӷ��� ���� �� Ÿ�Ӷ��� ���� �������� �޼ҵ�
	public ArrayList<timeLineDTO> getTimePhoto() {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
			
		ArrayList<timeLineDTO> photoList=new ArrayList<timeLineDTO>();
		
		try {				
		// ������ �����ϱ�
			con=getConn();				
			
			sql ="SELECT A.photoUrl, A.timeLineNum, A.userId "
				+ "FROM timeLinePhoto A, timeLine B "
				+ "WHERE A.userId = B.userId "
				+ "AND A.timeLineNum = B.timeLineNum "
				+ "ORDER BY A.photoNum;";
			
			pstmt=con.prepareStatement(sql);			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				timeLineDTO tdto=new timeLineDTO();
				
				tdto.setTimeLineNum(rs.getInt("timeLineNum"));
				tdto.setPhotoUrl(rs.getString("photoUrl"));	
				tdto.setUserId(rs.getString("userId"));
								
				photoList.add(tdto);
			}
		} catch (Exception e) {
			System.out.println("getTimePhoto()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
		return photoList;
	}//getTimePhoto ����
	

//*********** getTimePhotoCount() ���� : Ÿ�Ӷ��� ���� �� Ÿ�Ӷ��� ���� �������� �޼ҵ�
	public ArrayList<timeLineDTO> getTimePhotoCount() {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
			
		ArrayList<timeLineDTO> photoList=new ArrayList<timeLineDTO>();
		
		try {				
		// ������ �����ϱ�
			con=getConn();				
			
			sql ="SELECT timeLineNum, COUNT(timeLineNum) AS photoCount FROM timelinephoto group by timeLineNum";
							
			pstmt=con.prepareStatement(sql);			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				timeLineDTO tdto=new timeLineDTO();
				
				tdto.setTimeLineNum(rs.getInt("timeLineNum"));
				tdto.setPhotoCount(rs.getInt("photoCount"));	
												
				photoList.add(tdto);
			}
		} catch (Exception e) {
			System.out.println("getTimePhotoCount()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
		return photoList;
	}//getTimePhotoCount ����
			
	
	


//*********** getTimeReply() ���� : Ÿ�Ӷ��� ���� �� Ÿ�Ӷ��� ��� �������� �޼ҵ�
	public ArrayList<timeLineDTO> getTimeReply() {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		ArrayList<timeLineDTO> replyList=new ArrayList<timeLineDTO>();
		
		try {				
			con=getConn();				
			
			sql ="SELECT A.*, C.userNickname, C.userPhoto "
				+ "FROM timelinereply A, timeline B, vancomember C  "
				+ "WHERE A.timeLineNum = B.timeLineNum "
				+ "AND A.userId = C.userId "
				+ "GROUP BY A.timeLineReplyNum "
				+ "ORDER BY re_ref DESC, re_lev, writeTime";
			
			pstmt=con.prepareStatement(sql);			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				timeLineDTO tdto=new timeLineDTO();
				
				// �ð� ���� �ٲٱ�				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd"); 
							
				String writeTime=sdf.format(rs.getTimestamp("writeTime"));
				String writeTime1=sdf1.format(rs.getTimestamp("writeTime"));
				
				Date now = new Date();				
				Date wTime = sdf.parse(writeTime);
				long writetime = wTime.getTime();
				long nowTime=now.getTime();
				long timeGap = (nowTime-writetime)/1000/60;  // �и������带 ������ ȯ��
				
				if(timeGap<2){ writeTime = "����";}
				else if(timeGap>=2 && timeGap<60){writeTime = timeGap+"����";}
				else if(timeGap>=60 && timeGap<1440){writeTime = timeGap/60+"�ð���";}
				else{writeTime=writeTime1;}		
								
				//reply  ����
				tdto.setTimeLineReplyNum(rs.getInt("timeLineReplyNum"));
				tdto.setTimeLineNum(rs.getInt("timeLineNum"));
				tdto.setUserId(rs.getString("userId"));
				tdto.setContent(rs.getString("content"));
				tdto.setWriteTimeStr(writeTime);
				tdto.setRe_ref(rs.getInt("re_ref"));
				tdto.setRe_lev(rs.getInt("re_lev"));
				tdto.setRe_seq(rs.getInt("re_seq"));				
				tdto.setReOwnerNick(rs.getString("reOwnerNick"));
				
				// ȸ���������� ������ ��۴� ��� ����				
				tdto.setUserPhoto(rs.getString("userPhoto"));	
				tdto.setUserNickname(rs.getString("userNickname"));	
				
				replyList.add(tdto);
			}
		} catch (Exception e) {
			System.out.println("getTimeReply()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}			
		return replyList;
	
	}//getTimeReply()����


//*********** addLikeCount() ���� : ���ƿ� ī��Ʈ �߰�
	public void addLikeCount(String timeLineNum, String userId, String likeId) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		try {				
		// ������ �����ϱ�
			con=getConn();				
			
			sql = "insert into timeLineLikes(timeLineNum, userId, likeId ) values(?,?,?)";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, timeLineNum);
			pstmt.setString(2, userId);
			pstmt.setString(3, likeId);
			pstmt.executeUpdate();			
		
		} catch (Exception e) {
			System.out.println("addLikeCount()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
	} //addLikeCount()����



//*********** delLikeCount() ���� : ���ƿ� ī��Ʈ ����
	public void delLikeCount(String timeLineNum, String likeId) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		
		try {				
		// ������ �����ϱ�
			con=getConn();				
			
			sql = "delete from timeLineLikes where timeLineNum = ? and likeId = ?";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, timeLineNum);
			pstmt.setString(2, likeId);	
			pstmt.executeUpdate();
						
			
		} catch (Exception e) {
			System.out.println("delLikeCount()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}	
		
	} //delLikeCount()����

		
//*********** getLikeCount() ���� : ���ƿ� ī��Ʈ ����
	public int getLikeCount(String timeLineNum) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int likesCount=0;
		
		try {				
			con=getConn();				
		
			// ���ƿ� ���� ����
			sql = "SELECT COUNT(*) FROM timelinelikes where timeLineNum ="+timeLineNum;

			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				likesCount = rs.getInt(1);				
			}
			
		} catch (Exception e) {
			System.out.println("getLikeCount()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
		return likesCount;
		
	} //getLikeCount()����
	
	
	
//*********** getReplyCount() ���� : ���ƿ� ī��Ʈ ����
	public int getReplyCount(String timeLineNum) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int replyCount=0;
		
		try {				
			con=getConn();				
		
			// ���ƿ� ���� ����
			sql = "SELECT COUNT(*) FROM timelinereply where timeLineNum ="+timeLineNum;

			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				replyCount = rs.getInt(1);				
			}
			
		} catch (Exception e) {
			System.out.println("getReplyCount()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
		return replyCount;
		
	} //getReplyCount()����
	


//*********** getLikeClickId() ���� : ���ƿ� �������� ����Ȯ��
	public int getLikeClickId(String timeLineNum, String likeId) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int clickCheck=0;
		
		try {				
			con=getConn();				
		
			// ���ƿ� ���� ����
			sql = "SELECT likeId FROM timelinelikes where timeLineNum =? and likeId=?";
			
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,timeLineNum);
			pstmt.setString(2,likeId);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				clickCheck = 1;				
			}
			
		} catch (Exception e) {
			System.out.println("getLikeClickId()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
		return clickCheck;
		
	} //getLikeClickId()����



	public void insertTimeLine(timeLineDTO tdto) {
		
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int timeLineNum = 0;
				
		try {
		// �ֱ� �۹�ȣ ��������.
			con=getConn();
			sql="select max(timeLineNum)from timeLine"; //���� �ֱ��� �� ��ȣ �˻�
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				timeLineNum=rs.getInt(1)+1;   // �˻��� �ֽű۹�ȣ +1 �� ������ �۹�ȣ�� ����
			}else{
				timeLineNum=1; //(���� ������ 1������ ����)
			}			
			
		// ������ �����ϱ�1 (������ Ÿ�Ӷ��� ���̺� ����)			
			String userId = tdto.getUserId();
			String content = tdto.getContent();				
			
			sql = "insert into timeLine(timeLineNum, content, userId) values(?,?,?)";
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, timeLineNum);
			pstmt.setString(2, content);
			pstmt.setString(3, userId);
			pstmt.executeUpdate();	

		// ������ �����ϱ�2 (�������� Ÿ�Ӷ��� ���� ���̺� ����)			
			String photoUrl1 = tdto.getPhotoUrl();
			String photoUrl2 = tdto.getIp();
			String photoUrl3 = tdto.getTitle();
			String photoUrl4 = tdto.getReOwnerNick();

			
			if(!(photoUrl1.equals("/vc/imageUpload/null"))){
				sql = "insert into timeLinePhoto(timeLineNum, userId, photoUrl) values (?,?,?)";
				
				pstmt.close();
				pstmt=con.prepareStatement(sql);
				
				pstmt.setInt(1, timeLineNum);
				pstmt.setString(2, userId);
				pstmt.setString(3, photoUrl1);
				pstmt.executeUpdate();
			}
			
			if(!(photoUrl2.equals("/vc/imageUpload/null"))){
				sql = "insert into timeLinePhoto(timeLineNum, userId, photoUrl) values (?,?,?)";
				
				pstmt.close();
				pstmt=con.prepareStatement(sql);
				
				pstmt.setInt(1, timeLineNum);
				pstmt.setString(2, userId);
				pstmt.setString(3, photoUrl2);
				pstmt.executeUpdate();		
			}
			
			if(!(photoUrl3.equals("/vc/imageUpload/null"))){
				sql = "insert into timeLinePhoto(timeLineNum, userId, photoUrl) values (?,?,?)";
				
				pstmt.close();
				pstmt=con.prepareStatement(sql);
				
				pstmt.setInt(1, timeLineNum);
				pstmt.setString(2, userId);
				pstmt.setString(3, photoUrl3);
				pstmt.executeUpdate();		
			}
			
			if(!(photoUrl4.equals("/vc/imageUpload/null"))){
				sql = "insert into timeLinePhoto(timeLineNum, userId, photoUrl) values (?,?,?)";
				
				pstmt.close();
				pstmt=con.prepareStatement(sql);
				
				pstmt.setInt(1, timeLineNum);
				pstmt.setString(2, userId);
				pstmt.setString(3, photoUrl4);
				pstmt.executeUpdate();		
			}			
			
			
		
		} catch (Exception e) {
			System.out.println("insertTimeLine()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}			
		
	}//insertTimeLine()����
		

//*********** insertTimeReply() ���� : ��۴ޱ�
	public void insertTimeReply(String timeLineNum, String content, String userId) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int timeLineReplyNum = 0;
		
		try {
		// �ֱ� �۹�ȣ ��������.
			con=getConn();
			sql="select max(timeLineReplyNum)from timeLineReply"; //���� �ֱ��� �� ��ȣ �˻�
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				timeLineReplyNum=rs.getInt(1)+1;   // �˻��� �ֽű۹�ȣ +1 �� ������ �۹�ȣ�� ����
			}else{
				timeLineReplyNum=1; //(���� ������ 1������ ����)
		}				
			
			
		// ������ �����ϱ�						
			sql = "insert into timeLineReply(timeLineReplyNum, timeLineNum, content, userId, re_ref) values(?,?,?,?,?)";
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, timeLineReplyNum);
			pstmt.setString(2, timeLineNum);
			pstmt.setString(3, content);
			pstmt.setString(4, userId);
			pstmt.setInt(5, timeLineReplyNum);
			
			pstmt.executeUpdate();			
		
		} catch (Exception e) {
			System.out.println("insertTimeReply()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
	} //insertTimeReply()����
	
	

//*********** insertTimeReReply() ���� : ���۴ޱ�
	public void insertTimeReReply(String timeLineNum, String re_ref, String content, String userId, String reOwnerNick) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		try {				
		// ������ �����ϱ�
			con=getConn();	
			
			System.out.println(userId);
			
			sql = "insert into timeLineReply(timeLineNum, re_ref, content, userId, reOwnerNick, re_lev) values(?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, timeLineNum);
			pstmt.setString(2, re_ref);
			pstmt.setString(3, content);
			pstmt.setString(4, userId);
			pstmt.setString(5, reOwnerNick);
			pstmt.setInt(6, 1);
			pstmt.executeUpdate();			
		
		} catch (Exception e) {
			System.out.println("insertTimeReReply()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
	} //insertTimeReReply()����	
	
	
} // DAOŬ���� ����
