package member;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class userLoginAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String userId = request.getParameter("userId");
		String userPwd = request.getParameter("userPwd");
				
		//���� ������ ���� ��ü ����
		HttpSession session = request.getSession();
		//������ ��ü ����
		ActionForward forward = null;
		// �ڹٽ�ũ��Ʈ ������� ����Ʈ������ ��ü ���� (�׻� ĳ���� Ÿ�� ������ ��ü���� ���� ���;� ��!!!)
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// DAO �ҷ�����(��ü ����)
		MemberDAO mdao = new MemberDAO();
		int logResult = mdao.userLogin(userId, userPwd);

		// rsó��
		if(logResult==1){
			session.setAttribute("userId",userId);
			
			// ���� ����ϱ� ���� Session���� ���� ����� ���� �����ϱ� (session�� id���� �α��� �� �޾ƿ�)	
			ArrayList sessionList=null;	
			sessionList=mdao.getSessionInfo(userId);
			for(int i=0; i<sessionList.size();i++){
				MemberDTO mdto = (MemberDTO)sessionList.get(i);
				
				int userNum=mdto.getUserNum();
				
				String userNickname=mdto.getUserNickname();
				String userPhoto=mdto.getUserPhoto();
				
				
				session.setAttribute("userNickname",userNickname);
				session.setAttribute("userPhoto",userPhoto);
			}
			
			
			// �α��� ���� ó��
			
			forward = new ActionForward();
			forward.setRedirect(false);//�����̷�Ʈ ������� ������
			forward.setPath("loginOK.me");

				
		}else if(logResult==0){
							
			out.println("<script>");
			out.println("alert('���̵� �Ǵ� ��й�ȣ�� Ȯ���� �ּ���');");
			out.println("history.back();");
			out.println("</script>");			
			return null;			
				
		}else{		
			out.println("<script>");
			out.println("alert('���̵� �Ǵ� ��й�ȣ�� Ȯ���� �ּ���');");
			out.println("history.back();");
			out.println("</script>");			
			return null;		
		}

		
		return forward;
	}

}
