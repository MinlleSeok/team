package neighborComm;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class thunderDelAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
	 	int thunderNum=Integer.parseInt(request.getParameter("thunderNum"));
	 	int moimNum=Integer.parseInt(request.getParameter("moimNum"));
	 	
	 	neiBoardDAO ndao = new neiBoardDAO();
	 	int result = ndao.delThunder(thunderNum);
	 	
	 	
	 	// �ڹٽ�ũ��Ʈ ������� ����Ʈ������ ��ü ���� (�׻� ĳ���� Ÿ�� ������ ��ü���� ���� ���;� ��!!!)
	 	response.setContentType("text/html;charset=UTF-8");
	 	PrintWriter out = response.getWriter();
	 		
	 	ActionForward forward=null;	
	 			
 		out.println("<script>");
 		out.println("alert('�����Ǿ����ϴ�.');");
 		out.println("</script>");
 		
 		forward = new ActionForward();
 		forward.setRedirect(false);
 		forward.setPath("moimContent.ne?moimNum="+moimNum);
	 	
		return forward;
	}

}
