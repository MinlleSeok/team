package neighborComm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import member.MemberDAO;
import member.MemberDTO;

public class neiBoardDAO {
	
	//*********** getConn()생성 : 커넥션풀로 부터 커넥션 객체con을 만들기 위한 메소드. 	
	private Connection getConn() throws Exception{
	
		Connection con = null;
		Context init = new InitialContext();

		// 커넥션 풀 얻기(context.xml파일의 <Resource> 태그의 name정보로 가져옴)
		DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/vanco");
		con = ds.getConnection();
		return con;
	} // getConn() 종료
	
	
	
	//*********** insertNeiBoard()생성 : neiBoardWrite에서 입력받은 글 DB에 삽입하는 메소드
	public void insertNeiBoard(neiBoardDTO ndto){
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		//추가할 새 글에 대한 글 번호를 저장할 변수
		int num = 0;
		
		try {
			
		// 최근 글번호 가져오기.
			con=getConn();
			sql="select max(num)from neiboard"; //가장 최근의 글 번호 검색
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				num=rs.getInt(1)+1;   // 검색한 최신글번호 +1 을 새글의 글번호로 지정
			}else{
				num=1; //(글이 없으면 1번부터 시작)
			}
				
		// 데이터 삽입하기
			sql="insert into neiBoard(num, userId, userNickname, userPhoto, upPhoto1,  upPhoto2,  upPhoto3,  upPhoto4, subject, content, re_ref, re_lev, re_seq, "
					+ "readcount, replycount, writetime, ip) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";			
			
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, ndto.getUserId());
			pstmt.setString(3, ndto.getUserNickname());
			pstmt.setString(4, ndto.getUserPhoto());
			pstmt.setString(5, ndto.getUpPhoto1());
			pstmt.setString(6, ndto.getUpPhoto2());
			pstmt.setString(7, ndto.getUpPhoto3());
			pstmt.setString(8, ndto.getUpPhoto4());
			pstmt.setString(9, ndto.getSubject());
			pstmt.setString(10, ndto.getContent());
			pstmt.setInt(11, num);
			pstmt.setInt(12, ndto.getRe_lev());
			pstmt.setInt(13, ndto.getRe_seq());
			pstmt.setInt(14, ndto.getReadCount());
			pstmt.setInt(15, ndto.getReplyCount());
			pstmt.setTimestamp(16, ndto.getWriteTime());
			pstmt.setString(17, ndto.getIp());
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("insertNeiBoard()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}		
	}	// insertBoard() 끝
	
	
	
	//*********** getBoardCount()생성 : 게시판에 등록된 글 갯수 세는 함수	
	public int getBoardCount(){
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		int count = 0;
		
		try {
			con=getConn();
			sql="select count(*) from neiboard";	// 테이블의 행 수를 세는 함수
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				count=rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("getBoardCount()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return count;
	} //getBoardCount() 종료
	
	
	
	//*********** getBoardList()생성 : 보드 전체를 검색하여 페이지에 일정 갯수만 보여주는 역할	
		public ArrayList<neiBoardDTO> getBoardList(int startRow, int pageSize){			
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			
			
			// ArrayList 객체 생성
			ArrayList<neiBoardDTO> boardList=new ArrayList<neiBoardDTO>();
			
			try {
				con=getConn();
				
				// DB검색 결과 정렬 : 답글을 포함하여 re_ref는 내림차순, 글 전체 번호는 오름차순정렬
					// & limit로 각 페이지마다 첫글 번호와 한페이지당 보여질 글 갯수를 ?로 설정한다.
				
				sql="select * from neiboard order by re_ref desc, re_seq asc limit ?,?";
				pstmt=con.prepareStatement(sql);			
				pstmt.setInt(1, startRow);
				pstmt.setInt(2, pageSize);			
				rs=pstmt.executeQuery();
				
				while(rs.next()){
					neiBoardDTO ndto=new neiBoardDTO();
					
					ndto.setNum(rs.getInt("num"));
					ndto.setUserId(rs.getString("userId"));
					ndto.setUserNickname(rs.getString("userNickname"));
					ndto.setUserPhoto(rs.getString("userPhoto"));
					ndto.setUpPhoto1(rs.getString("upPhoto1"));
					ndto.setUpPhoto2(rs.getString("upPhoto2"));
					ndto.setUpPhoto3(rs.getString("upPhoto3"));
					ndto.setUpPhoto4(rs.getString("upPhoto4"));
					ndto.setSubject(rs.getString("subject"));
					ndto.setContent(rs.getString("content"));
					ndto.setRe_ref(rs.getInt("re_ref"));
					ndto.setRe_lev(rs.getInt("re_lev"));
					ndto.setRe_seq(rs.getInt("re_seq"));
					ndto.setReadCount(rs.getInt("readCount"));
					ndto.setReplyCount(rs.getInt("replyCount"));
					ndto.setWriteTime(rs.getTimestamp("writeTime"));
					ndto.setIp(rs.getString("ip"));
					

					boardList.add(ndto);
				}				
			} catch (Exception e) {
				System.out.println("getBoardList()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}			
			return boardList;			
		}  //getBoardList()끝
		
		
		
	//*********** updateReplyCount()생성 : 댓글쓸때 / 삭제 할때 댓글수 1 증가/감소 시켜줌
		
		public void updateReplyCount(int num){
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			int replyCount = 0;
			String sql = "";		
			try {
				con=getConn();				
				
				sql="select count(*) from neireply where contentNum=?";	// 댓글 갯수를 세는 함수
				
				pstmt=con.prepareStatement(sql);			
				pstmt.setInt(1, num);
				rs=pstmt.executeQuery();
				
				if(rs.next()){
					replyCount=rs.getInt(1);
				}				
				System.out.println(replyCount);
				
				// 댓글 갯수 삽입
				pstmt.close();
				sql="update neiBoard set replycount=? where num=?";
				pstmt=con.prepareStatement(sql);			
				pstmt.setInt(1, replyCount);
				pstmt.setInt(2, num);
				pstmt.executeUpdate();				
				
			} catch (Exception e) {
				System.out.println("updateReplyCount()()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}			
	}//updateReadCount()끝
		
		
	
	//*********** updateReadCount()생성 : 글 읽을때 조회수 1 증가 시켜줌	
		public void updateReadCount(int num){
			Connection con = null;
			PreparedStatement pstmt=null;
			String sql = "";		
			try {
				con=getConn();				
				sql = "update neiBoard set readcount=readcount+1 where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,num);
				pstmt.executeUpdate();				
			} catch (Exception e) {
				System.out.println("updateReadCount오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			}			
		}//updateReadCount()끝
	
	//*********** getContent()생성 : 글 내용을 DB에서 가져옴	
		public neiBoardDTO getContent(int num){
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql = "";	
			neiBoardDTO ndto=null;
			try {
				con=getConn();				
				sql = "select * from neiBoard where num=?";
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1,num);
				rs=pstmt.executeQuery();	
				
				if(rs.next()){
					ndto = new neiBoardDTO();
					
					ndto.setNum(rs.getInt("num"));
					ndto.setUserId(rs.getString("userId"));
					ndto.setUserNickname(rs.getString("userNickname"));
					ndto.setUserPhoto(rs.getString("userPhoto"));
					ndto.setUpPhoto1(rs.getString("upPhoto1"));
					ndto.setUpPhoto2(rs.getString("upPhoto2"));
					ndto.setUpPhoto3(rs.getString("upPhoto3"));
					ndto.setUpPhoto4(rs.getString("upPhoto4"));
					ndto.setSubject(rs.getString("subject"));
					ndto.setContent(rs.getString("content"));
					ndto.setRe_ref(rs.getInt("re_ref"));
					ndto.setRe_lev(rs.getInt("re_lev"));
					ndto.setRe_seq(rs.getInt("re_seq"));
					ndto.setReadCount(rs.getInt("readCount"));
					ndto.setReplyCount(rs.getInt("replyCount"));
					ndto.setWriteTime(rs.getTimestamp("writeTime"));
					ndto.setIp(rs.getString("ip"));				
				}
			} catch (Exception e) {
				System.out.println("getContent()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}	
			
			return ndto;
		}//getContent()끝
		
		
	//*********** delContent()생성 : 글삭제 메소드	
		public int delContent(int num){
			int delCheck=0;
			Connection con=null;
			PreparedStatement pstmt=null;
			String sql="";
			
			try {
				con=getConn();
				sql="delete from neiboard where num=?";
				pstmt=con.prepareStatement(sql);				
				pstmt.setInt(1,num);
				pstmt.executeUpdate();	
				delCheck=1;
						
			} catch (Exception e) {
				System.out.println("delBoard()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			}	
			return delCheck;
		}


		
		
		//*********** getNeiBestList()생성 : 동네게시판 투데이 베스트글 게시용		
		public ArrayList<neiBoardDTO> getNeiBestList(){			
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			
			
			// ArrayList 객체 생성
			ArrayList<neiBoardDTO> list=new ArrayList<neiBoardDTO>();
			
			try {
				con=getConn();				
				// DB검색 결과 정렬 : DD일자가 오늘인 글 중 (readCount+replyCount*3)이 높은 순으로 5개			
				
				sql="select * from neiboard where writeTime > CURRENT_DATE( ) order by (readcount+replycount*3) desc limit 5;";
				pstmt=con.prepareStatement(sql);			
				rs=pstmt.executeQuery();
				
				while(rs.next()){
					neiBoardDTO ndto=new neiBoardDTO();
					
					ndto.setNum(rs.getInt("num"));
					ndto.setUserId(rs.getString("userId"));
					ndto.setUserNickname(rs.getString("userNickname"));
					ndto.setUserPhoto(rs.getString("userPhoto"));
					ndto.setUpPhoto1(rs.getString("upPhoto1"));
					ndto.setUpPhoto2(rs.getString("upPhoto2"));
					ndto.setUpPhoto3(rs.getString("upPhoto3"));
					ndto.setUpPhoto4(rs.getString("upPhoto4"));
					ndto.setSubject(rs.getString("subject"));
					ndto.setContent(rs.getString("content"));
					ndto.setRe_ref(rs.getInt("re_ref"));
					ndto.setRe_lev(rs.getInt("re_lev"));
					ndto.setRe_seq(rs.getInt("re_seq"));
					ndto.setReadCount(rs.getInt("readCount"));
					ndto.setReplyCount(rs.getInt("replyCount"));
					ndto.setWriteTime(rs.getTimestamp("writeTime"));
					ndto.setIp(rs.getString("ip"));
					
					list.add(ndto);
				}				
			} catch (Exception e) {
				System.out.println("getNeiBestList()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}			
			return list;			
		}  //getBoardList()끝
		
		

		//*********** modifyContent()생성 : 댓글쓸때 댓글수 1 증가 시켜줌
		
		public void modifyContent(neiBoardDTO ndto, int num){
			Connection con = null;
			PreparedStatement pstmt=null;
			String sql = "";		
			try {
				con=getConn();				
				
				sql="update neiBoard set subject=?, content=?, upPhoto1=?,  upPhoto2=?,  upPhoto3=?,  upPhoto4=? where num=?";
				pstmt=con.prepareStatement(sql);			
				pstmt.setString(1, ndto.getSubject());
				pstmt.setString(2, ndto.getContent());
				pstmt.setString(3, ndto.getUpPhoto1());
				pstmt.setString(4, ndto.getUpPhoto2());
				pstmt.setString(5, ndto.getUpPhoto3());
				pstmt.setString(6, ndto.getUpPhoto4());
				pstmt.setInt(7, num);
				pstmt.executeUpdate();				
				
			} catch (Exception e) {
				System.out.println("updateReplyCount()()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}	
			}			
	}//updateReadCount()끝
		
		
		
		
		
		
//////////////////////////////////댓글 부분 메소드////////////////////////////////////////////////		
	
		
		
		//*********** getReplyCount()생성 : 게시판에 등록된 글 갯수 세는 함수	
		public int getReplyCount(int contentNum){
			Connection con = null;
			PreparedStatement pstmt=null;
			ResultSet rs = null;
			String sql = "";
			
			int count = 0;
			
			try {
				con=getConn();
				sql="select count(*) from neireply where contentNum=?";	// 테이블의 행 수를 세는 함수
				pstmt=con.prepareStatement(sql);
				pstmt.setInt(1, contentNum);
				/*pstmt.setInt(2, contentPageNum);*/
				rs = pstmt.executeQuery();
				
				if(rs.next()){
					count=rs.getInt(1);
				}
				
			} catch (Exception e) {
				System.out.println("getReplyCount()오류"+e);
			}finally{
				if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
				if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
				if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
			}		
			return count;
		} //getReplyCount() 종료

		
	//*********** insertNeiReply()생성 : 댓글창에서 입력받은 글 DB에 삽입하는 메소드
	
		
	public void insertNeiReply(neiBoardDTO ndto, int contentNum){
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		//추가할 새 글에 대한 글 번호를 저장할 변수
		int num = 0;
		
		try {
			
		// 최근 글번호 가져오기.
			con=getConn();
			sql="select max(num)from neireply"; 
			pstmt=con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				num=rs.getInt(1)+1;   // 검색한 최신글번호 +1 을 새글의 글번호로 지정
			}else{
				num=1; //(글이 없으면 1번부터 시작)
			}
			
		// 데이터 삽입하기
			sql="insert into neireply(num, userId, userNickname, userPhoto, subject, content, re_ref, re_lev, re_seq, "
					+ "writetime, ip, contentNum) values(?,?,?,?,?,?,?,?,?,?,?,?)";	
			
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, ndto.getUserId());
			pstmt.setString(3, ndto.getUserNickname());
			pstmt.setString(4, ndto.getUserPhoto());
			pstmt.setString(5, ndto.getSubject());
			pstmt.setString(6, ndto.getContent());
			pstmt.setInt(7, num);	// 1차 댓글 re_ref는 1차댓글의 num번호 따라감
			pstmt.setInt(8, 0);	// 1차 댓글에서 들여쓰기는 우선 0
			pstmt.setInt(9, 0);	// 1차 댓글에서 답글순서는 우선 0
			pstmt.setTimestamp(10, ndto.getWriteTime());
			pstmt.setString(11, ndto.getIp());
			pstmt.setInt(12,ndto.getContentNum());
			/*pstmt.setInt(13,ndto.getContentPageNum());*/
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("insertNeiReply()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}		
	}	// insertNeiReply() 끝
		
	
	
	
//*********** getNeiReply()생성 : 입력된 댓글을 검색하여 뿌려주는 메소드	
	public ArrayList<neiBoardDTO> getNeiReply(int contentNum){			
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		// ArrayList 객체 생성
		ArrayList<neiBoardDTO> boardList=new ArrayList<neiBoardDTO>();
		
		try {
			con=getConn();
			
			// DB검색 결과 정렬 : 답글을 포함하여 re_ref는 내림차순, 글 전체 번호는 오름차순정렬
				// & limit로 각 페이지마다 첫글 번호와 한페이지당 보여질 글 갯수를 ?로 설정한다.
			
			sql="select * from neireply where contentNum=? order by re_ref asc, re_lev asc, re_seq desc";   /*and contentPageNum=? */
			pstmt=con.prepareStatement(sql);			
			pstmt.setInt(1, contentNum);
			/*pstmt.setInt(2, contentPageNum);*/			
			rs=pstmt.executeQuery();
			
			
			while(rs.next()){
				neiBoardDTO ndto=new neiBoardDTO();
				
				ndto.setNum(rs.getInt("num"));
				ndto.setUserId(rs.getString("userId"));
				ndto.setUserNickname(rs.getString("userNickname"));
				ndto.setUserPhoto(rs.getString("userPhoto"));
				ndto.setSubject(rs.getString("subject"));
				ndto.setContent(rs.getString("content"));
				ndto.setRe_ref(rs.getInt("re_ref"));
				ndto.setRe_lev(rs.getInt("re_lev"));
				ndto.setRe_seq(rs.getInt("re_seq"));
				ndto.setWriteTime(rs.getTimestamp("writeTime"));
				ndto.setIp(rs.getString("ip"));
				ndto.setContentNum(rs.getInt("contentNum"));
				ndto.setReOwnerNick(rs.getString("reOwnerNick"));
				/*ndto.setContentPageNum(rs.getInt("contentPageNum"));*/


				boardList.add(ndto);
			}				
		} catch (Exception e) {
			System.out.println("getBoardList()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}			
		return boardList;			
	}  //getBoardList()끝
		
		
		
	//*********** insertNeiReply2()생성 : 댓글창에서 입력받은 대댓글 DB에 삽입하는 메소드
		//답변글 달기 규칙 (전달받은 DTO에는 답변글 규칙을 위한 변수와 답변글 내용이 저장되어 있다.)
			// re_ref: 댓글 주인과 대댓글을 함께 묶어주기 위한 필드(같은 값을 가짐)
			// re_seq: re_ref 내에서의 원댓글과 대댓글들 간의 순서 지정
			// re_lev: re_ref 내에서 원댓에 대해 대댓글의 들여쓰기 주기 위한 값
			
			//1) 대댓글의 re_ref는 원댓글의 그룹번호(re_ref)값을 사용한다.
			//2) 대댓글의 re_seq값은 원댓글의 re_seq+1 증가한 값을 사용한다.
				// 단, 부모글을 제외한 같은 그룹내 먼저 입력된 글은 seq값+1 증가 시킨다.
			//3) re_rev값은 부모글의 re_lev값에서 +1증가한 값을 사용한다.
		
	public void insertNeiReply2(neiBoardDTO ndto, int contentNum){
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		//추가할 새 글에 대한 글 번호를 저장할 변수
		int num = 0;
		
		try {
			
		// 최근 글번호 가져오기.
			con=getConn();
			sql="select max(num)from neireply"; //가장 최근의 댓글 번호 검색
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				num=rs.getInt(1)+1;   // 검색한 최신글번호 +1 을 새 대댓글의 글번호로 지정
			}else{
				num=1; //(글이 없으면 1번부터 시작)
			}
			
		//답글 순서 재배치 : 원댓글의 그룹과 같은 번호 중 부모들보다 seq값이 큰 글들은 seq+1로 증가시킨다.
			sql="update neireply set re_seq=re_seq+1 where contentNum=? and re_ref=? and re_seq>?";   /*and contentPageNum=? */
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, contentNum);
			/*pstmt.setInt(2, contentPageNum);*/
			pstmt.setInt(2, ndto.getRe_ref());
			pstmt.setInt(3, ndto.getRe_seq());
			pstmt.executeUpdate();
			
			
			
		// 데이터 삽입하기
			sql="insert into neireply(num, userId, userNickname, userPhoto, subject, content, re_ref, re_lev, re_seq, "
					+ "writetime, ip, contentNum, reOwnerNick) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";	 /* contentPageNum*/
			
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, ndto.getUserId());
			pstmt.setString(3, ndto.getUserNickname());
			pstmt.setString(4, ndto.getUserPhoto());
			pstmt.setString(5, ndto.getSubject());
			pstmt.setString(6, ndto.getContent());
			pstmt.setInt(7, ndto.getRe_ref());
			pstmt.setInt(8, ndto.getRe_lev()+1);	// 답글순서는 부모글 +1
			pstmt.setInt(9, ndto.getRe_seq()+1);	// 들여쓰기는 부모글 +1	
			pstmt.setTimestamp(10, ndto.getWriteTime());
			pstmt.setString(11, ndto.getIp());
			pstmt.setInt(12, ndto.getContentNum());
			pstmt.setString(13, ndto.getReOwnerNick());			
			/*pstmt.setInt(13, ndto.getContentPageNum());*/
			
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("insertNeiReply2()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}		
	}	// insertNeiReply2() 끝
	
	
	//*********** delReply()생성 : 댓글삭제 메소드	
	public int delReply(int num){
		int delCheck=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		
		try {
			con=getConn();
			sql="delete from neireply where num=?";
			pstmt=con.prepareStatement(sql);				
			pstmt.setInt(1,num);
			pstmt.executeUpdate();	
			delCheck=1;
					
		} catch (Exception e) {
			System.out.println("delReply()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}	
		return delCheck;
	}		

	
	
	//*********** getRe_refCount()생성 : 댓글이 달렸을 경우 원댓글 삭제 안되도록 댓글 달린 갯수 세는 함수
	public int getRe_refCount(int re_ref){
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		int count = 0;
		
		try {
			con=getConn();
			sql="select count(*) from neireply where re_ref=?";	// 테이블의 행 수를 세는 함수
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				count=rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("getReplyCount()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return count;
	} //getRe_refCount() 종료

	
	
/////////////////////*********** insertMoim()생성 : 모임을 생성하는 함수
	public void insertMoim(moimDTO mdto) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		//추가할 새 글에 대한 글 번호를 저장할 변수
		int moimNum = 0;
		
		try {
			
		// 최근 글번호 가져오기.
			con=getConn();
			sql="select max(moimNum)from moim"; //가장 최근의 글 번호 검색
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				moimNum=rs.getInt(1)+1;   // 검색한 최신글번호 +1 을 새글의 글번호로 지정
			}else{
				moimNum=1; //(글이 없으면 1번부터 시작)
			}
				
		// 데이터 삽입하기
			sql="insert into moim(moimNum, userId, moimCategory, moimTitle, moimIntro,  moimPhoto,  makingTime, userCity, userDistrict) values(?,?,?,?,?,?,?,?,?)";			
			
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, moimNum);
			pstmt.setString(2, mdto.getUserId());
			pstmt.setString(3, mdto.getMoimCategory());
			pstmt.setString(4, mdto.getMoimTitle());
			pstmt.setString(5, mdto.getMoimIntro());
			pstmt.setString(6, mdto.getMoimPhoto());			
			pstmt.setTimestamp(7, mdto.getMakingTime());
			pstmt.setString(8, mdto.getUserCity());
			pstmt.setString(9, mdto.getUserDistrict());			
			pstmt.executeUpdate();
			
			
			//모임멤버에 모임장 정보 입력하기
			sql="insert into moimMember(moimNum, userId, userLevel) values(?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1,moimNum);
			pstmt.setString(2, mdto.getUserId());	
			pstmt.setInt(3, 3);
			pstmt.executeUpdate();
			
			
			
			
		} catch (Exception e) {
			System.out.println("insertMoim()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
		
	} //insertMoim() 종료

/////////////////////*********** modifyMoim() : 모임을 수정하는 함수
	public void modifyMoim(moimDTO mdto) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";	
	
		try {			
			con=getConn();			
			sql="update neiBoard set subject=?, content=?, upPhoto1=?,  upPhoto2=?,  upPhoto3=?,  upPhoto4=? where num=?";
						
			// 수정 데이터 삽입하기
			sql="update moim set moimCategory=?, moimTitle=?, moimIntro=?,  moimPhoto=? where moimNum=?";			
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, mdto.getMoimCategory());
			pstmt.setString(2, mdto.getMoimTitle());
			pstmt.setString(3, mdto.getMoimIntro());
			pstmt.setString(4, mdto.getMoimPhoto());			
			pstmt.setInt(5, mdto.getMoimNum());
			
			pstmt.executeUpdate();
		
		} catch (Exception e) {
			System.out.println("modifyMoim()오류"+e);
			}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}	
		}	
	
	} //modifyMoim() 종료

	
	
	
/////////////*********** getMoimListByDisgtrict()생성 : 우리동네 접속 시 지역으로 모임 현황 가져오는 함수
	public ArrayList<moimDTO> getMoimListByDisgtrict(String userId, String userCity, String userDistrict) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		
		
		// ArrayList 객체 생성
		ArrayList<moimDTO> list=new ArrayList<moimDTO>();
		
		try {
			con=getConn();				
						
			sql="SELECT *,(SELECT COUNT(*) FROM moimmember B where B.moimNum=A.moimNum) as memberCount "
			  + "from moim A "
			  + "WHERE userCity=? AND userDistrict=? ORDER BY moimNum DESC;";
			pstmt=con.prepareStatement(sql);	
			pstmt.setString(1, userCity);
			pstmt.setString(2, userDistrict);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				moimDTO mdto=new moimDTO();
				
				mdto.setMoimNum(rs.getInt("moimNum"));
				mdto.setUserId(rs.getString("userId"));
				mdto.setMoimCategory(rs.getString("moimCategory"));
				mdto.setMoimTitle(rs.getString("moimTitle"));
				mdto.setMoimIntro(rs.getString("moimIntro"));
				mdto.setMoimPhoto(rs.getString("moimPhoto"));
				mdto.setMakingTime(rs.getTimestamp("makingTime"));
				mdto.setUserCity(rs.getString("userCity"));
				mdto.setUserDistrict(rs.getString("userDistrict"));
				mdto.setMaxQuota(rs.getInt("maxQuota"));
				mdto.setMemberCount(rs.getInt("memberCount"));
				
				list.add(mdto);
			}				
		} catch (Exception e) {
			System.out.println("getMoimListByDisgtrict()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}			
		return list;			
		
	} //getMoimListByDisgtrict() 종료

		
	
//*********** getMoimInfo() 생성 : 우리동네에서 모임 제목 클릭 시 모임num으로  해당모임 기본정보 가져옴., 
	
	public moimDTO getMoimInfo(String moimNum){
		
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";
		moimDTO mdto=new moimDTO();
		
					
		try {
			con = getConn();
			sql="select * from moim where moimNum=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				
				mdto.setMoimNum(rs.getInt("moimNum"));
				mdto.setUserId(rs.getString("userId"));
				mdto.setMoimCategory(rs.getString("moimCategory"));
				mdto.setMoimTitle(rs.getString("moimTitle"));
				mdto.setMoimIntro(rs.getString("moimIntro"));
				mdto.setMoimPhoto(rs.getString("moimPhoto"));
				mdto.setUserCity(rs.getString("userCity"));
				mdto.setUserDistrict(rs.getString("userDistrict"));
				mdto.setMakingTime(rs.getTimestamp("makingTime"));
				mdto.setMaxQuota(rs.getInt("maxQuota"));
			}			
		} catch (Exception e) {
			System.out.println("getMoimInfo()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return mdto;
	} //getMoimInfo() 종료


//*********** insertThunder() 생성 : 모임에서 번개 생성하는 함수
	public void insertThunder(thunderDTO tdto) {
	
		Connection con = null;
		PreparedStatement pstmt=null;		
		String sql = "";
		ResultSet rs = null;
		int thunderNum=1;
		
		try {			
			con = getConn();
			
		// 최신 번개 번호 가져오기
			sql ="select max(thunderNum) from thunder";
			pstmt=con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				thunderNum=rs.getInt(1)+1;   // 검색한 번개번호 +1 을 새글의 글번호로 지정
			}else{
				thunderNum=1; //(번개가  없으면 1번부터 시작)
			}
			
		// 번개 정보 삽입하기
			
			sql="insert into thunder(thunderName, thunderPlace, thunderDate,  thunderPerson,  makingTime, userId, moimNum, userPhoto, thunderNum) values(?,?,?,?,?,?,?,?,?)";			
			
			pstmt.close();
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, tdto.getThunderName());
			pstmt.setString(2, tdto.getThunderPlace());
			pstmt.setTimestamp(3, tdto.getThunderDate());
			pstmt.setString(4, tdto.getThunderPerson());				
			pstmt.setTimestamp(5, tdto.getMakingTime());
			pstmt.setString(6, tdto.getUserId());
			pstmt.setString(7, tdto.getMoimNum());
			pstmt.setString(8, tdto.getUserPhoto());
			pstmt.setInt(9, thunderNum);
			pstmt.executeUpdate();
			
			
		// 번개 개설자 참석자 명단 삽입하기				
			// 번개 개설자 추가정보 가져오기
			MemberDAO mdao = new MemberDAO();
			MemberDTO mdto = new MemberDTO();
					
			mdto = mdao.getUserInfo(tdto.getUserId());			
			
			sql="insert into thunderUser(thunderNum, moimNum, userId, userNickname, userPhoto) values(?,?,?,?,?)";			
			
			pstmt.close();
			pstmt=con.prepareStatement(sql);			
			
			pstmt.setInt(1, thunderNum);
			pstmt.setString(2, tdto.getMoimNum());
			pstmt.setString(3, tdto.getUserId());
			pstmt.setString(4, mdto.getUserNickname());			
			pstmt.setString(5, mdto.getUserPhoto());
						
			pstmt.executeUpdate();	
			
			
		} catch (Exception e) {
			System.out.println("insertThunder()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}	
		
	} //insertThunder()종료


//*********** getThunderList() 생성 : 모임에서 번개 리스트 불러오는 함수
	public ArrayList<thunderDTO> getThunderList(String moimNum) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";		
		ArrayList<thunderDTO> list = new ArrayList<thunderDTO>();
					
		try {
			con = getConn();
			sql="select * from thunder where moimNum=? order by thunderDate DESC";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1,moimNum);
			rs=pstmt.executeQuery();
			
			while(rs.next()){	
				thunderDTO tdto=new thunderDTO();
				tdto.setThunderNum(rs.getString("thunderNum"));
				tdto.setThunderName(rs.getString("thunderName"));
				tdto.setThunderPerson(rs.getString("thunderPerson"));
				tdto.setThunderPlace(rs.getString("thunderPlace"));
				tdto.setUserId(rs.getString("userId"));
				tdto.setUserPhoto(rs.getString("userPhoto"));
				tdto.setMakingTime(rs.getTimestamp("makingTime"));
				tdto.setThunderDate(rs.getTimestamp("thunderDate"));				
								
				list.add(tdto);			
			}			
		} catch (Exception e) {
			System.out.println("getThunderList()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}		
		return list;
		
	} //getThunderList() 종료
	

//*********** delThunder()생성 : 번개모임삭제 메소드	
	public int delThunder(int thunderNum){
		int delCheck=0;
		Connection con=null;
		PreparedStatement pstmt=null;
		String sql="";
		
		try {
			con=getConn();
			sql="delete from thunder where thunderNum=?";
			pstmt=con.prepareStatement(sql);				
			pstmt.setInt(1,thunderNum);
			pstmt.executeUpdate();	
			delCheck=1;
					
		} catch (Exception e) {
			System.out.println("delThunder()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
		}	
		return delCheck;
	} //delThunder()끝
	

	
/////////////*********** getMyMoimList()생성 : 우리동네 접속 시 내가 가입한 모임 현황 가져오는 함수
	
	public ArrayList<moimDTO> getMyMoimList(String userId) {
		Connection con = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "";		
	
		// ArrayList 객체 생성
		ArrayList<moimDTO> myList=new ArrayList<moimDTO>();
		
		try {
			con=getConn();				
			// DB검색 결과 정렬 :		
						
			sql = "SELECT A.moimNum, A.userId, B.moimTitle, MAX(C.thunderDate) AS thunderDate "
					+ " FROM moimMember A, moim B, thunder C"
					+ " WHERE A.userId = ? "
					+ " AND A.moimNum = B.moimNum "
					+ " AND B.moimNum = C.moimNum "
					+ " GROUP BY A.moimNum "
					+ " ORDER BY C.thunderDate DESC";
			
			pstmt=con.prepareStatement(sql);	
			pstmt.setString(1, userId);			
			rs=pstmt.executeQuery();
			
			while(rs.next()){
				moimDTO mdto=new moimDTO();				
				mdto.setMoimNum(rs.getInt("moimNum"));
				mdto.setUserId(rs.getString("userId"));
				mdto.setMoimTitle(rs.getString("moimTitle"));				
				mdto.setMakingTime(rs.getTimestamp("thunderDate"));			
				myList.add(mdto);
			}				
		} catch (Exception e) {
			System.out.println("getMyMoimList()오류"+e);
		}finally{
			if(pstmt!=null){try {pstmt.close();} catch (Exception err) {err.printStackTrace();}}
			if(con!=null){try {con.close();} catch (Exception err) {err.printStackTrace();}}			
			if(rs!=null){try {rs.close();} catch (Exception err) {err.printStackTrace();}}
		}			
		return myList;				
	} //getMyMoimList() 종료	
	
	
	
	
	
	
}  //DAO 클래스 끝
