package neighborComm;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class neiReplyDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		int num = Integer.parseInt(request.getParameter("num"));
		int pageNum = Integer.parseInt(request.getParameter("pageNum"));
		int replyNum = Integer.parseInt(request.getParameter("replyNum"));
		int re_ref = Integer.parseInt(request.getParameter("re_ref"));
		int re_lev = Integer.parseInt(request.getParameter("re_lev"));
		
		// �ڹٽ�ũ��Ʈ ������� ����Ʈ������ ��ü ���� (�׻� ĳ���� Ÿ�� ������ ��ü���� ���� ���;� ��!!!)
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		ActionForward forward=null;
		
		// ����� 1�� �̻� �޸��� ���� �ȵǵ��� ó���ϱ�(re_ref ������ 1�ʰ��̰�, re_lev=0�̸� ���� �Ұ�)
		neiBoardDAO ndao = new neiBoardDAO();
		int re_refChk=ndao.getRe_refCount(re_ref);
		
		if(re_refChk>1 && re_lev==0){
			out.println("<script>");
			out.println("alert('����� �޸� ���� ������ �� �����ϴ�.');");
			out.println("history.back();");
			out.println("</script>");				
		}else{			
				// ��� 1���� ������, 			
				// check ���Ϲޱ� : 1(����), 0(����)
				int delCheck=ndao.delReply(replyNum);
				
				if(delCheck==1){							
				// DB����� ��� ���� �����ϱ�
				ndao.updateReplyCount(num);		
				
				forward = new ActionForward();
				forward.setRedirect(false);//�����̷�Ʈ ������� ������
				forward.setPath("./neighborComm/neiBoardContent.jsp?num="+num+"&pageNum="+pageNum);
				
			}else{
				out.println("<script>");
				out.println("alert('������ �����Ͽ����ϴ�.');");
				out.println("history.back();");
				out.println("</script>");	
			}
	}
		
		return forward;
	}

}
