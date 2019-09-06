package neighborComm;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class moimJoinAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		String moimNum = request.getParameter("moimNum");		
		String userId = request.getParameter("userId");		
		int maxQuota = Integer.parseInt(request.getParameter("maxQuota"));
		
		ActionForward forward = new ActionForward();
		
		moimDAO mdao = new moimDAO();
		int result = mdao.insertMoimMember(moimNum, userId, maxQuota);
		
		// result -1 : ����ȸ�� ���ԺҰ�  //    0:�����ʰ�  // 1:�������� 
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		
		if(result==-1){
			out.print("<script>");
			out.print("alert('��������� ȸ���� �簡���� �Ұ��� �մϴ�.');");
			out.print("location.href='neighborComm.ne';");
			out.print("</script>");
			forward=null;
		}else if(result==0){
			out.print("<script>");
			out.print("alert('���������� �ʰ��Ǿ����ϴ�.');");
			out.print("location.href='moimContent.ne?moimNum="+moimNum+"';");
			out.print("</script>");		
			forward=null;
		}else{						
			forward.setRedirect(true); // false : �����̷�Ʈ ������� ������
			forward.setPath("./moimContent.ne?moimNum="+moimNum);
				
		}
		return forward;
	}

}
