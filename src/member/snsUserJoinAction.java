package member;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class snsUserJoinAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// īī������ ���� ���� ó��
		request.setCharacterEncoding("utf-8");
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
		String userNickname = request.getParameter("userNickname");	
		String userPhoto = request.getParameter("userPhoto");
		
		//�׼� ������ ��ü ����
		ActionForward forward = new ActionForward();
		
		
		// ���� dto�� ����
		MemberDTO mdto=new MemberDTO();
		
		mdto.setUserId(userId);
		mdto.setUserPwd(userPwd);
		mdto.setUserNickname(userNickname);
		mdto.setUserPhoto(userPhoto);
		
		// request�� ����� ���� �ð������� DTO�� setter ��ü �޼ҵ带 ���� �߰� �����Ѵ�		
		mdto.setJoinDate(new Timestamp(System.currentTimeMillis()));
		
		// �α��� DBó��
		MemberDAO mdao = new MemberDAO();
		int logResult = mdao.snsUserJoin(mdto);
		
		if(logResult==1){
			HttpSession session = request.getSession();
			session.setAttribute("userId",userId);
			session.setAttribute("userNickname",userNickname);
			session.setAttribute("userPhoto",userPhoto);
		}
		
		forward.setRedirect(false);				
		forward.setPath("./vanco.me");			
		return forward;
	}

}
