package member;

import java.sql.*;
import java.util.ArrayList;

import javax.naming.*;
import javax.sql.DataSource;

public class MemberDAO {
	
	//*********** getConn()���� : Ŀ�ؼ�Ǯ�� ���� Ŀ�ؼ� ��ücon�� ����� ���� �޼ҵ�. 	
	private Connection getConn() throws Exception{
	
		Connection con = null;
		Context init = new InitialContext();

		// Ŀ�ؼ� Ǯ ���(context.xml������ <Resource> �±��� name������ ������)
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/vanco");
		con = ds.getConnection();
		return con;
	}// getConn() ����

	
	//*********** userLogin() ���� : ȸ�� �α��� �޼ҵ�
		// ���ϰ� : �α��μ���(1), ���Ʋ��(0), ID����(-1)
	public int userLogin(String userId, String userPwd) {
		int logResult=-1;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		
		try {
			con = getConn();
			sql="select * from vancomember where userId=?";
			pstmt=con.prepareStatement(sql);			
			
			pstmt.setString(1,userId);
			rs=pstmt.executeQuery();	
			
			if(rs.next()){//id���� ������(true) => ��� ���ϴ� �α��� �˻� ����
				if(userPwd.equals(rs.getString("userPwd"))){
					logResult=1;
				}else{
					logResult=0;
				}
			}else{
				logResult=-1;
			}			
		} catch (Exception e) {
			System.out.println("userLogin()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}		
		return  logResult;
	} //userLogin() ����
	
	
	//*********** idUserJoin() ���� : ȸ�� ���� �޼ҵ�	
	public String idUserJoin(MemberDTO mdto){
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="";
		String userPhoto="";
		
		
		try {
			con=getConn();
			sql="insert into vancoMember(userId, userPwd, userNickname,joinDate,userGender,userPhoto) values(?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			
			// ?�� ����
			pstmt.setString(1,mdto.getUserId());
			pstmt.setString(2,mdto.getUserPwd());
			pstmt.setString(3,mdto.getUserNickname());
			pstmt.setTimestamp(4,mdto.getJoinDate());
			pstmt.setString(5,mdto.getUserGender());
			
			System.out.println(mdto.getUserId());
			
			if(mdto.getUserGender().equals("��")){
			pstmt.setString(6,"/vanco/imageProfile/defaultMan.jpg");
			userPhoto="/vanco/imageProfile/defaultMan.jpg";
			}else{
			pstmt.setString(6,"/vanco/imageProfile/defaultWoman.jpg");
			userPhoto="/vanco/imageProfile/defaultWoman.jpg";
			}
			
			// ���� ����
			pstmt.executeUpdate();	
			pstmt.close();
			
			//ȸ������ ���� �� �ٷ� �α��� ��Ű��			
			sql = "select userPwd from vancoMember where userId=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,mdto.getUserId());
			rs=pstmt.executeQuery();			
			
			if(rs.next()){//id���� ������(true) => ��� ���ϴ� �α��� �˻� ����
				if(mdto.getUserPwd().equals(rs.getString("userPwd"))){
					
				}else{
					System.out.println("��� Ʋ��");
				}
			}else{
				System.out.println("���̵� ����");
			}					
		} catch (Exception e) {
			System.out.println("idUserJoin()����"+e);
			e.printStackTrace();
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}
		return userPhoto;
	} // idUserJoin() ����
	
	

	
//*********** getUserInfo() ���� :(����� id��) myPage���� ��� user���� ������., �׿� session�����θ� ������ ���� ���� �ʿ��Ҷ� ��ܾ�
			// �α��νÿ��� id���� ���ǿ� ������, ����/�г���/���� �������� ���⼭ ������.
		
		public MemberDTO getUserInfo(String userId){
			
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			MemberDTO mdto=new MemberDTO();
						
			try {
				con = getConn();
				sql="select * from vancomember where userId=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1,userId);
				rs=pstmt.executeQuery();
				
				while(rs.next()){
					mdto=new MemberDTO();
					
					mdto.setUserNum(rs.getInt("userNum"));
					mdto.setUserId(rs.getString("userId"));
					mdto.setUserNickname(rs.getString("userNickname"));
					mdto.setUserPwd(rs.getString("userPwd"));
					mdto.setUserPhoto(rs.getString("userPhoto"));
					mdto.setDogPhoto(rs.getString("dogPhoto"));
					mdto.setUserCity(rs.getString("userCity"));
					mdto.setUserDistrict(rs.getString("userDistrict"));
					mdto.setUserBirth(rs.getInt("userBirth"));
					mdto.setUserGender(rs.getString("userGender"));
					mdto.setUserPosition(rs.getString("userPosition"));
					mdto.setUserComment(rs.getString("userComment"));	
					mdto.setJoinDate(rs.getTimestamp("joinDate"));
				}			
			} catch (Exception e) {
				System.out.println("getUserInfo()����"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}		
			return mdto;
		} //getUserInfo() ����
		


//*********** editUserInfo() ���� : �������������� ���� �����ϴ� �޼ҵ�, �����ϸ� 1�� ����
	
	public int editUserInfo(MemberDTO mdto){
		
		Connection con = null;
		PreparedStatement pstmt=null;
		String sql = "";
		int result=0;
								
		try {
			con = getConn();
			sql="update vancomember set userNickname=?, userPwd=?, userBirth=?,userCity=?, userDistrict=?, userPosition=?, userComment=? where userid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,mdto.getUserNickname());
			pstmt.setString(2,mdto.getUserPwd());
			pstmt.setInt(3,mdto.getUserBirth());
			pstmt.setString(4,mdto.getUserCity());
			pstmt.setString(5,mdto.getUserDistrict());
			pstmt.setString(6,mdto.getUserPosition());
			pstmt.setString(7,mdto.getUserComment());
			pstmt.setString(8,mdto.getUserId());
			pstmt.executeUpdate();
			
			result=1;
										
		} catch (Exception e) {
			System.out.println("editUserInfo()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}		
	
		return result;
	} //editUserInfo() ����

		
	
	
//*********** getSessionInfo() ���� : �ε��� ȭ�鿡�� session���� �ʿ��� ������ ��� �߰��صδ� �޼ҵ�
		// �α��νÿ��� id���� ���ǿ� ������, ����/�г���/���� �� �߰��Է� ������ index���� db�����Ͽ� �߰� ���� �ϸ� ��???
	
	public ArrayList<MemberDTO> getSessionInfo(String userId){
		
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		ArrayList<MemberDTO> sessionList = new ArrayList<MemberDTO>();
		
		try {
			con = getConn();
			sql="select * from vancoMember where userId=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,userId);
			rs=pstmt.executeQuery();
			
			if(rs.next()){
				MemberDTO mdto=new MemberDTO();
				
				mdto.setUserNum(rs.getInt("userNum"));
				mdto.setUserId(rs.getString("userId"));
				mdto.setUserNickname(rs.getString("userNickname"));
				mdto.setUserPwd(rs.getString("userPwd"));
				mdto.setUserPhoto(rs.getString("userPhoto"));
				mdto.setDogPhoto(rs.getString("dogPhoto"));
				mdto.setUserCity(rs.getString("userCity"));
				mdto.setUserDistrict(rs.getString("userDistrict"));
				mdto.setUserBirth(rs.getInt("userBirth"));
				mdto.setUserGender(rs.getString("userGender"));
				mdto.setUserPosition(rs.getString("userPosition"));
				mdto.setUserComment(rs.getString("userComment"));	
				mdto.setJoinDate(rs.getTimestamp("joinDate"));
				
				sessionList.add(mdto);
			}			
		} catch (Exception e) {
			System.out.println("getSessionInfo()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return sessionList;
	} //getSessionInfo() ����
	
	
	
	//*********** icCheck() ���� : ȸ������ �� id �ߺ� Ȯ�� �޼ҵ� (�ߺ��� 0����, ���԰���1����)
	
	public int idCheck(String userId){
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="";
		int check = 1;
		
		try {
			con = getConn();
			sql="select * from vancoMember where userId=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,userId);
			rs=pstmt.executeQuery();
			
			if(rs.next()){// ���� ������ (�ߺ�), 	
				check=0;	
			}else{ //������ ���� ����
				check=1;
			}			
		} catch (Exception e) {
			System.out.println("icCheck()����"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		
		return check;
	}  // icCheck()��
	
	
//*********** editMyPic() ���� : ���������� ���� ����	
	public int editMyPic(String dogPhoto, String userPhoto, String userId){
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		int result=0;
		
		try {
			con=getConn();
			sql="update vancoMember set dogPhoto=?, userPhoto=? where userId=?";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1,dogPhoto);
			pstmt.setString(2,userPhoto);
			pstmt.setString(3,userId);
			
			// ���� ����
			pstmt.executeUpdate();	
			
			result=1;
					
		} catch (Exception e) {
			System.out.println("editMyPic()����"+e);
			e.printStackTrace();
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
		}
		
		return result;
	} // editMyPic() ����	
	

//*********** snsLoginIdChk() ���� : ȸ�� �α��� �޼ҵ�
	// ���ϰ� : true(���̵����), false(���̵�����)
public boolean snsLoginIdChk(String userId) {
		
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = "";
	boolean idChk=false;
	
	try {
		con = getConn();
		sql="select userId from vancomember where userId=?";
		pstmt=con.prepareStatement(sql);			
		
		pstmt.setString(1,userId);
		rs=pstmt.executeQuery();	
		
		if(rs.next()){//id���� ������(true) => idChk=false
			idChk=true;
		}			
	} catch (Exception e) {
		System.out.println("snsLoginIdChk����"+e);
	}finally{
		if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
		if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
	}		
	return  idChk;
} //snsLoginIdChk() ����
	
//*********** snsUserLogin() ���� : SNSȸ�� �α��� �޼ҵ�
	// ���ϰ� : �α��μ���(1), ���Ʋ��(0), ID����(-1)
	public int snsUserLogin(String userId) {
	int logResult=-1;
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String sql = "";
	
	try {
		con = getConn();
		sql="select * from vancomember where userId=?";
		pstmt=con.prepareStatement(sql);			
		
		pstmt.setString(1,userId);
		rs=pstmt.executeQuery();	
		
		if(rs.next()){
			
			logResult=1;
		}			
	} catch (Exception e) {
		System.out.println("snsUserLogin()����"+e);
	}finally{
		if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
		if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
	}		
	return  logResult;
	} //snsUserLogin() ����


	
//*********** snsUserJoin() ���� : ȸ�� ���� �޼ҵ�	
	public int snsUserJoin(MemberDTO mdto){
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql="";
		int logResult=-1;
		
		try {
			con=getConn();
			sql="insert into vancoMember(userId, userPwd, userNickname,joinDate,userPhoto) values(?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			
			// ?�� ����
			pstmt.setString(1,mdto.getUserId());
			pstmt.setString(2,mdto.getUserPwd());
			pstmt.setString(3,mdto.getUserNickname());
			pstmt.setTimestamp(4,mdto.getJoinDate());
			pstmt.setString(5,mdto.getUserPhoto());
			
			System.out.println(mdto.getUserId());
			
			/*if(mdto.getUserGender().equals("��")){
			pstmt.setString(6,"/vanco/imageProfile/defaultMan.jpg");
			userPhoto="/vanco/imageProfile/defaultMan.jpg";
			}else{
			pstmt.setString(6,"/vanco/imageProfile/defaultWoman.jpg");
			userPhoto="/vanco/imageProfile/defaultWoman.jpg";
			}*/
			
			// ���� ����
			pstmt.executeUpdate();	
			pstmt.close();
			
			//ȸ������ ���� �� �ٷ� �α��� ��Ű��			
			sql = "select userPwd from vancoMember where userId=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,mdto.getUserId());
			rs=pstmt.executeQuery();			
			
			if(rs.next()){//id���� ������(true) => ��� ���ϴ� �α��� �˻� ����
				if(mdto.getUserPwd().equals(rs.getString("userPwd"))){
					logResult=1;
				}else{
					logResult=0;
				}
			}else{
				logResult=-1;
			}					
		} catch (Exception e) {
			System.out.println("snsUserJoin()����"+e);
			e.printStackTrace();
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}
		return logResult;
	} // snsUserJoin() ����


	

	
	
	
	
} //DAO Ŭ���� ����
