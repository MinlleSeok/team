package member;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class userJoinAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		
		
		// 1. ȸ������ ���������� �Է��� ���� ó��
		request.setCharacterEncoding("utf-8");
		String userId = request.getParameter("userId");
		String userNickname = request.getParameter("userNickname");	
		
		
		//�׼� ������ ��ü ����
		ActionForward forward = new ActionForward();
		
		
		MemberDAO mdao =  new MemberDAO();
		int chk=mdao.idCheck(userId);
		
		System.out.println("üũ�� : "+chk);
		
		if(chk==0){
			// HTML �±� ����� ���� printwriter��ü ����
			response.setContentType("text/html;charset=utf-8"); 
			PrintWriter out = response.getWriter();
			
						
			out.println("<script type='text/javascript'>");
			out.println("alert('�̹� ���Ե� ���̵��Դϴ�. ���ο� ���̵� �̿��� �ּ���');");
			out.println("history.back();");
			out.println("</script>");
			return null;
			
		}else{
		//2. request�� ��ƿ� ���� DTO����
			MemberDTO mdto=new MemberDTO();
			mdto.setUserId(userId);
			mdto.setUserPwd(request.getParameter("userPwd"));
			mdto.setUserNickname(userNickname);
			mdto.setUserGender(request.getParameter("userGender"));			

		// 3. request�� ����� ���� �ð������� DTO�� setter ��ü �޼ҵ带 ���� �߰� �����Ѵ�		
			mdto.setJoinDate(new Timestamp(System.currentTimeMillis()));		
		
		// 6.idUserJoin() �޼ҵ� ȣ��(�Ű������� mdto ���� ����) �� �α��� ����� ���� �ޱ�	(������η� ���Ϲ���)	
			String userPhoto = mdao.idUserJoin(mdto);		
		 
		// 7. ���Ϲ��� �α��� ��������� ���� ó��
			HttpSession session = request.getSession();
			session.setAttribute("userId",userId);
			session.setAttribute("userNickname",userNickname);
			session.setAttribute("userPhoto",userPhoto);
		} // id�ߺ�üũ if-else�� ����%>
		
		// 8. ȸ������ ���� �� ������ �̵���Ŀ���(T/F),�̵��������ּҰ��� �������� �����̷�Ʈ ������� ��ȯ��Ų��.		
			//������ �̵���� ����(true=�����̷�Ʈ, false=����ġ(��� ��������))
	
		forward.setRedirect(false);			
		forward.setPath("./vanco.me");	
		return forward;
		
	} //execute �޼ҵ� ����
}// Ŭ���� ����
