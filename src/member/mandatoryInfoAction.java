package member;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class mandatoryInfoAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		
	// �ڹٽ�ũ��Ʈ ������� ����Ʈ������ ��ü ���� (�׻� ĳ���� Ÿ�� ������ ��ü���� ���� ���;� ��!!!)
	response.setContentType("text/html;charset=UTF-8");
	PrintWriter out = response.getWriter();	


	request.setCharacterEncoding("utf-8");
	String userId = request.getParameter("userId");
	String userNickname = request.getParameter("userNickname");
	String userPwd = request.getParameter("userPwd");
 	String userPosition = request.getParameter("userPosition");
	int userBirth = Integer.parseInt(request.getParameter("userBirth"));
	String userCity = request.getParameter("userCity");
	String userDistrict = request.getParameter("userDistrict");
	String userComment = request.getParameter("userComment"); 

	MemberDTO mdto = new MemberDTO();
	
	mdto.setUserId(userId);
	mdto.setUserNickname(userNickname);
	mdto.setUserPwd(userPwd);
	mdto.setUserPosition(userPosition);
	mdto.setUserBirth(userBirth);
	mdto.setUserCity(userCity);
	mdto.setUserDistrict(userDistrict);
	mdto.setUserComment(userComment);
	
	MemberDAO mdao = new MemberDAO();		
	int result = mdao.editUserInfo(mdto); 

	if(result==1){
		out.println("<script>");
		out.println("alert('ȸ�������� ��� �Ǿ����ϴ�');");
		out.println("location.href='./mypage.me'");
		out.println("</script>");	
		
		return null;
	}else{
		out.println("<script>");
		out.println("alert('ȸ������ ����� �����Ͽ����ϴ�.');");
		out.println("location.href='./mypage.me'");
		out.println("</script>");	
		
		return null;
	}
	
			
}

}
