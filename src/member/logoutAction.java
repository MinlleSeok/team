package member;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class logoutAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	// �α׾ƿ� ��û�� �������� ���ǿ����� ���� �ʱ�ȭ �� �� �α׾ƿ� �޼���â ����ְ� CarMain.jsp �������� �̵���Ŵ
			HttpSession session=request.getSession(true);
			session.invalidate();
			
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('�α׾ƿ� �Ǿ����ϴ�.');");
			out.println("location.href='./vanco.me'");
			out.println("</script>");
					
			return null;
	}

}
