package neighborComm;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.MemberDAO;
import member.MemberDTO;

public class neighborCommAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Head���� neiboard Ŭ�� �� neiBoard���� ���� ���� ��������, �̷α��� �Ǵ� ���� �̵�Ͻ� ���Ӱ��� ���� ������
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		
		request.setCharacterEncoding("utf-8");		
	 	String userId=request.getParameter("userId");
	 		 	
	 	MemberDAO mdao = new MemberDAO();	 	
	 	MemberDTO mdto = mdao.getUserInfo(userId);
	 	
	 	String userCity = mdto.getUserCity();		 		 	
	 	
	 	if(userCity.equals("�̵��")){
	 		out.println("<script>");
			out.println("alert('�������� �� �ʼ����� ��� �� ��� �����մϴ�.');");
			out.println("location.href='mypage.me';");
			out.println("</script>");
			
			return null;			
	 	}
		 	
	 	
	 	ActionForward forward = new ActionForward();
		forward.setRedirect(true);//����ġ ������� ������
		forward.setPath("./neighborComm.ne");
		
		return forward;
	 	
	}

}
