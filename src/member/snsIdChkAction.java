package member;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class snsIdChkAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		ActionForward forward = new ActionForward();
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		
		String userId = request.getParameter("userId");
		// System.out.println("�׼��������� �Ѿ�� id�� :"+userId);
		
		//1. DB�� ���̵� ���� ���� Ȯ�� �� ����� ����
		MemberDAO mdao = new MemberDAO();
		boolean idChk = mdao.snsLoginIdChk(userId);
		// System.out.println("�׼������� üũ�� :"+idChk);
		
		
		if(idChk==false){
			//3. id���� ������ false���� ajax data�� ���Ͻ�Ŵ			
			response.setContentType("text/html;charset=UTF-8");			
			out.print("noId");			
			return null;
			
			
		}else{
			MemberDTO mdto = mdao.getUserInfo(userId);
			String userNickname=mdto.getUserNickname();
			String userPhoto=mdto.getUserPhoto();
			/*String url = request.getParameter("url");*/
			//System.out.println("���̵������� �������� ���� �۵���!");
			
			HttpSession session = request.getSession();
			session.setAttribute("userId",userId);
			session.setAttribute("userNickname",userNickname);
			session.setAttribute("userPhoto",userPhoto);		
		}
		forward = new ActionForward();
		forward.setRedirect(false);//�����̷�Ʈ ������� ������
		forward.setPath("vanco.me");			
		return forward;
		
	} //execute �޼ҵ� ����
}// Ŭ���� ����
