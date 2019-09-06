package neighborComm;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


import member.MemberDAO;
import member.MemberDTO;

public class moimDAO {

//*********** getConn()생성 : 커넥션풀로 부터 커넥션 객체con을 만들기 위한 메소드. 	
	private Connection getConn() throws Exception{
	
		Connection con = null;
		Context init = new InitialContext();

		// 커넥션 풀 얻기(context.xml파일의 <Resource> 태그의 name정보로 가져옴)
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/vanco");
		con = ds.getConnection();
		return con;
	} // getConn() 종료
	
	

//*********** insertThunderUser() 생성 : 사용자가 번개 참여하는 함수
	public void insertThunderUser(String userId, String moimNum, String thunderNum) {
	
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		try {				
		// 데이터 삽입하기
			con = getConn();
			
			// 사용자 추가정보 가져오기
			MemberDAO mdao = new MemberDAO();
			MemberDTO mdto = new MemberDTO();
					
			mdto = mdao.getUserInfo(userId);			
			
			sql="insert into thunderUser(thunderNum, moimNum, userId, userNickname, userPhoto) values(?,?,?,?,?)";			
			
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, thunderNum);
			pstmt.setString(2, moimNum);
			pstmt.setString(3, userId);
			pstmt.setString(4, mdto.getUserNickname());			
			pstmt.setString(5, mdto.getUserPhoto());
						
			int result = pstmt.executeUpdate();
			
			System.out.println("인서트 결과"+1);
			
		} catch (Exception e) {
			System.out.println("insertThunderUser()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}			
	} //insertThunderUser()종료


//*********** deleteThunderUser() 생성 : 사용자가 번개 참여를 삭제 함수
	public void deleteThunderUser(String userId, String moimNum, String thunderNum) {
		
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		try {				
			con = getConn();
						
			MemberDAO mdao = new MemberDAO();
			sql="delete from thunderUser where thunderNum=? and moimNum=? and userId=? ";			
			
			pstmt=con.prepareStatement(sql);
			
			System.out.println(userId+"디에이오");
			
			pstmt.setString(1, thunderNum);
			pstmt.setString(2, moimNum);
			pstmt.setString(3, userId);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("deleteThunderUser()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}			
	} //deleteThunderUser()종료


//*********** getThunderUser() 생성 : 번개에 참여한 사용자 정보를 가져오는 함수
	public ArrayList<thunderUserDTO> getThunderUser(String moimNum, String thunderNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";		
		ArrayList<thunderUserDTO> list = new ArrayList<thunderUserDTO>();
				
		try {
			con = getConn();
			sql="select * from thunderUser where moimNum=? and thunderNum=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			pstmt.setString(2,thunderNum);
			
			rs=pstmt.executeQuery();	
			
			while(rs.next()){				
				
				thunderUserDTO tdto=new thunderUserDTO();
				tdto.setUserId(rs.getString("userId"));
				tdto.setUserNickname(rs.getString("userNickname"));
				tdto.setUserPhoto(rs.getString("userPhoto"));
				tdto.setThunderNum(rs.getInt("thunderNum"));
				
				list.add(tdto);			
			}			
		} catch (Exception e) {
			System.out.println("getThunderUser()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return list;
		
	} //getThunderUser()종료


//*********** insertMoimMember() 생성 : 회원이 모임에 가입하는 함수
	public int insertMoimMember(String moimNum, String userId, int maxQuota) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		moimMemberDTO mdto = new moimMemberDTO();	
		int result=0;
		int caution=0;
		int memberCount=0;
		
						
		try {
			con = getConn();
			sql="select userId from moimblacklist where moimNum=? and userId=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			pstmt.setString(2,userId);		
			
			rs=pstmt.executeQuery();	
						
			// 해당 아이디가 모임에서 강퇴된 기록이 있으면 가입 불가
			if(rs.next()){			
				result = -1;
			}else{
				// 정원초과 여부 확인 후 정원 미달시 가입 받음				
				pstmt.close();
				rs.close();
				
				sql="select count(*) from moimmember where moimNum=?";	// 테이블의 행 수를 세는 함수
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1,moimNum);
				rs = pstmt.executeQuery();				
				while(rs.next()){
					memberCount=rs.getInt(1);
				}
				
				System.out.println(maxQuota);
				System.out.println(memberCount);
				if(memberCount>=maxQuota){
					result=0;
				}else{
					pstmt.close();
					sql="insert into moimMember(moimNum, userId, userLevel) values(?,?,?)";
					pstmt=con.prepareStatement(sql);
					pstmt.setString(1,moimNum);
					pstmt.setString(2,userId);	
					pstmt.setInt(3,-1);	// 가입 후 미승인 상태인 회원은 level -1으로 설정
					pstmt.executeUpdate();
					result=1;
				}
			}
						
						
		} catch (Exception e) {
			System.out.println("insertMoimMember()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return result;	
		
	} //insertMoimMember 종료


//*********** getMoimMember() 생성 : 모임멤버 정보를 가져오는 함수
	public ArrayList<moimMemberDTO> getMoimMember(String moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";		
		ArrayList<moimMemberDTO> list = new ArrayList<moimMemberDTO>();
				
		try {
			con = getConn();
			sql="select * from moimmember NATURAL JOIN vancomember where moimNum=? and userLevel>0 order by userLevel DESC";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			
			rs=pstmt.executeQuery();				
			while(rs.next()){							
				moimMemberDTO mdto=new moimMemberDTO();
				//모임멤버 테이블 부분
				mdto.setMoimMemberNum(rs.getInt("moimMemberNum"));
				mdto.setMoimNum(rs.getInt("moimNum"));
				mdto.setUserId(rs.getString("userId"));
				mdto.setUserLevel(rs.getInt("userLevel"));
				mdto.setCaution(rs.getInt("caution"));
				
				//반코멤버 테이블 부분
				mdto.setUserNickname(rs.getString("userNickname"));
				mdto.setUserPhoto(rs.getString("userPhoto"));
				mdto.setDogPhoto(rs.getString("dogPhoto"));
				mdto.setUserCity(rs.getString("userCity"));
				mdto.setUserDistrict(rs.getString("userDistrict"));
				mdto.setUserBirth(rs.getInt("userBirth"));
				mdto.setUserGender(rs.getString("userGender"));
				mdto.setUserPosition(rs.getString("userPosition"));
				mdto.setUserComment(rs.getString("userComment"));
				
				list.add(mdto);			
			}			
		} catch (Exception e) {
			System.out.println("getMoimMember()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return list;		
	} //getMoimMember()종료

	
	
	
//*********** getWaitingMember() 생성 : 승인대기 멤버 정보를 가져오는 함수
	public ArrayList<moimMemberDTO> getWaitingMember(String moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";		
		ArrayList<moimMemberDTO> list = new ArrayList<moimMemberDTO>();
				
		try {
			con = getConn();
			sql="select * from moimmember NATURAL JOIN vancomember where moimNum=? and userLevel=-1";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			
			rs=pstmt.executeQuery();				
			while(rs.next()){							
				moimMemberDTO mdto=new moimMemberDTO();
				//모임멤버 테이블 부분
				mdto.setMoimMemberNum(rs.getInt("moimMemberNum"));
				mdto.setMoimNum(rs.getInt("moimNum"));
				mdto.setUserId(rs.getString("userId"));
				mdto.setUserLevel(rs.getInt("userLevel"));
				mdto.setCaution(rs.getInt("caution"));
				
				//반코멤버 테이블 부분
				mdto.setUserNickname(rs.getString("userNickname"));
				mdto.setUserPhoto(rs.getString("userPhoto"));
				mdto.setDogPhoto(rs.getString("dogPhoto"));
				mdto.setUserCity(rs.getString("userCity"));
				mdto.setUserDistrict(rs.getString("userDistrict"));
				mdto.setUserBirth(rs.getInt("userBirth"));
				mdto.setUserGender(rs.getString("userGender"));
				mdto.setUserPosition(rs.getString("userPosition"));
				mdto.setUserComment(rs.getString("userComment"));
				
				list.add(mdto);			
			}			
		} catch (Exception e) {
			System.out.println("getWaitingMember()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return list;		
	} //getWaitingMember()종료
	
	
//*********** getMoimMemberCount() 생성 : 모임멤버 몇수 세는 함수
	public int getMoimMemberCount(String moimNum) {
	
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int memberCount=0;
		
		try {				
			con = getConn();
					
			
			sql="select count(*) from moimmember where moimNum=? and userLevel>0";	// 테이블의 행 수를 세는 함수
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			rs = pstmt.executeQuery();				
			while(rs.next()){
				memberCount=rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("getMoimMemberCount()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}	
		
		return memberCount;
	} //getMoimMemberCount()종료


//*********** updateMemberLevel() 생성 : 운영진 레벨 업 하는 함수
	public void updateMemberLevel(String userId, int level, int moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;		
		String sql = "";		
		try {
			con=getConn();				
			
			if(level==1){			
				sql="update moimMember set userLevel=? where moimNum=? and userId=?";
				pstmt=con.prepareStatement(sql);			
				pstmt.setInt(1, 2);
				pstmt.setInt(2, moimNum);
				pstmt.setString(3, userId);
				pstmt.executeUpdate();
			}else{
				sql="update moimMember set userLevel=? where moimNum=? and userId=?";
				pstmt=con.prepareStatement(sql);			
				pstmt.setInt(1, 1);
				pstmt.setInt(2, moimNum);
				pstmt.setString(3, userId);
				pstmt.executeUpdate();	
			}
			
		} catch (Exception e) {
			System.out.println("updateMemberLevel()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}			
		
	} //updateMemberLevel()종료
	


//*********** getMyLevel() 생성 : 승인대기 멤버 정보를 가져오는 함수
	public int getMyLevel(String userId, String moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";	
		int myLevel=0;
				
		try {
			con = getConn();
			sql="select userLevel from moimmember where moimNum=? and userId=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			pstmt.setString(2,userId);
			
			
			rs=pstmt.executeQuery();				
			while(rs.next()){
				myLevel = rs.getInt("userLevel");
			}			
		} catch (Exception e) {
			System.out.println("getMyLevel()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return myLevel;		
	} //getMyLevel()종료


//*********** deleteJoinWait() 생성 : 승인대기 멤버 삭제하는 함수
	public void deleteJoinWait(String userId, int moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		try {				
			con = getConn();						
			MemberDAO mdao = new MemberDAO();
			sql="delete from moimMember where moimNum=? and userId=? ";			
			
			pstmt=con.prepareStatement(sql);
						
			pstmt.setInt(1, moimNum);
			pstmt.setString(2, userId);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("deleteJoinWait()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}			
	}//deleteJoinWait()종료


	
//*********** updateMemberLevel2() 생성 : 승인대기 멤버 가입승인 하는 함수
	public void updateMemberLevel2(String userId, int moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;		
		String sql = "";		
		try {
			con=getConn();				
					
				sql="update moimMember set userLevel=1 where moimNum=? and userId=?";
				pstmt=con.prepareStatement(sql);			
				
				pstmt.setInt(1, moimNum);
				pstmt.setString(2, userId);
				pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("updateMemberLevel2()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}			
	}//updateMemberLevel2()종료
	
	
//*********** deleteForcedMoimMember() 생성 : 강제 퇴장 및 강퇴 후 블랙리스트 올리는 함수
	public void deleteForcedMoimMember(String userId, int moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		
		try {				
			con = getConn();						
			
			// 멤버 삭제하기
			sql="delete from moimMember where moimNum=? and userId=? ";		
			pstmt=con.prepareStatement(sql);						
			pstmt.setInt(1, moimNum);
			pstmt.setString(2, userId);			
			pstmt.executeUpdate();
			
			//멤버 블랙리스트 올리기
			sql="insert into moimblacklist(moimNum, userId) values(?,?)";			
			pstmt.close();			
			pstmt=con.prepareStatement(sql);			
			pstmt.setInt(1, moimNum);
			pstmt.setString(2, userId);			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("deleteForcedMoimMember()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}			
	}//deleteForcedMoimMember()종료
	

//*********** updateCaution() 생성 : 경고 날리는 함수
	public void updateCaution(String userId, int moimNum, int caution) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;		
		String sql = "";		
		try {
			con=getConn();				
					
				if(caution<2){
			
				sql="update moimMember set caution=? where moimNum=? and userId=?";
				pstmt=con.prepareStatement(sql);				
				pstmt.setInt(1, caution+1);
				pstmt.setInt(2, moimNum);
				pstmt.setString(3, userId);
				pstmt.executeUpdate();
				}else{
					moimDAO mdao = new moimDAO();
					mdao.deleteForcedMoimMember(userId, moimNum);					
				}
		} catch (Exception e) {
			System.out.println("updateCaution()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}			
	}//updateCaution()종료	
	
	
	
	
	
	
	
	
	
} // DAO클래스 종료
