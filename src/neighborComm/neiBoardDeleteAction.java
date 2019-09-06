package neighborComm;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class neiBoardDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("pageNum");
		
		// �ڹٽ�ũ��Ʈ ������� ����Ʈ������ ��ü ���� (�׻� ĳ���� Ÿ�� ������ ��ü���� ���� ���;� ��!!!)
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		ActionForward forward=null;
		
		neiBoardDAO ndao = new neiBoardDAO();
		
		// check ���Ϲޱ� : 1(����), 0(����)
		int delCheck=ndao.delContent(num);
		   
		if(delCheck==1){
			
		out.println("<script>");
		out.println("alert('�����Ǿ����ϴ�.');");
		out.println("</script>");
		
		forward = new ActionForward();
		forward.setRedirect(false);
		forward.setPath("./neighborComm/neighborComm.jsp?pageNum="+pageNum);
				
	}else{
		out.println("<script>");
		out.println("alert('������ �����Ͽ����ϴ�.');");
		out.println("history.back();");
		out.println("</script>");	
	}	
		return forward;
	}

}
