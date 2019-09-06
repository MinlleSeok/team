package neighborComm;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class neiReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
	 	int contentNum=Integer.parseInt(request.getParameter("contentNum"));
		int contentPageNum=Integer.parseInt(request.getParameter("contentPageNum"));
	 	
	 	String content=request.getParameter("content");
	 	String userId=request.getParameter("userId");
	 	String userNickname=request.getParameter("userNickname");
	 	String userPhoto=request.getParameter("userPhoto");
	 	int re_ref=Integer.parseInt(request.getParameter("re_ref"));
	 	int re_lev=Integer.parseInt(request.getParameter("re_lev"));
	 	int re_seq=Integer.parseInt(request.getParameter("re_seq"));


	 	neiBoardDTO ndto = new neiBoardDTO();
	 	ndto.setContentNum(contentNum);
	 	ndto.setContent(content);
	 	ndto.setUserId(userId);
	 	ndto.setUserNickname(userNickname);
	 	ndto.setUserPhoto(userPhoto);
	 	ndto.setRe_ref(re_ref);
	 	ndto.setRe_lev(re_lev);
	 	ndto.setRe_seq(re_seq);	
		
		// dto�� ������� �ʴ� �ý��� ������ �߰� ���� 
		ndto.setWriteTime(new Timestamp(System.currentTimeMillis()));
		ndto.setIp(request.getRemoteAddr()); 	//request ��ü �������� ip, ����� ������ �������� �� ����ִ�
		
		// �Է¹��� �� DB�� ����
		neiBoardDAO ndao = new neiBoardDAO();
		ndao.insertNeiReply(ndto,contentNum);	
		
		//��� �� ������Ʈ
		int num=contentNum;  //(����� contentNum��, ������ num�� ��ġ��.)
		ndao.updateReplyCount(num);
	
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);//�����̷�Ʈ ������� ������
		forward.setPath("./neighborComm/neiBoardContent.jsp?num="+contentNum+"&pageNum="+contentPageNum);
		
		return forward;
	}

}
