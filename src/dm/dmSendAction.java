package dm;

import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class dmSendAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		ActionForward forward=null;

		// ���� ������ �Բ� hidden���� ���� ������ ���� ����
		request.setCharacterEncoding("utf-8");
		String dmContent=request.getParameter("dmContent");
		String sendUserId=request.getParameter("sendUserId");
		String sendNickname=request.getParameter("sendNickname");
		String receiveUserId = request.getParameter("receiveUserId");
		String receiveNickname = request.getParameter("receiveNickname");

		//�����̷�Ʈ�� ���� url ���� ����
			// yourPage�� ���� ������ url�� ������ ����(null)
			// �����Կ��� ���� ������ url�� ����
		String url = request.getParameter("url");
		// null�̿� �ε������� -1�� ��ȯ�Ǿ� if������ ���� ���������� ���� url ���� ��������
		int urlChk = url.indexOf("h");
		System.out.println("�ε���"+urlChk);		
		
		dmDTO ddto = new dmDTO();
		ddto.setDmContent(dmContent);
		ddto.setReceiveUserId(receiveUserId);
		ddto.setReceiveNickname(receiveNickname);
		ddto.setSendUserId(sendUserId);
		ddto.setSendNickname(sendNickname);
		
		// dto�� ������� �ʴ� �ý��� ������ �߰� ���� 
		ddto.setWriteTime(new Timestamp(System.currentTimeMillis()));
		ddto.setIp(request.getRemoteAddr());
		
		dmDAO ddao= new dmDAO();
		int sendCheck=ddao.sendDm(ddto); 
			
			if(urlChk==0){	
				url="yourPage.me?yourId="+receiveUserId;
			}else{url="dmbox.dm";}
			
			if(sendCheck==1){
				out.println("<script>");
				out.println("alert('������ ���½��ϴ�.');");
				out.println("location.href='"+url+"';");
				out.println("</script>");
				
				/*forward = new ActionForward();
				forward.setRedirect(false);//�����̷�Ʈ ������� ������				
				forward.setPath(url);*/
				
			}else{
				out.println("<script>");
				out.println("alert('���� �����⿡ �����Ͽ����ϴ�. �ٽ��ѹ� �õ��� �ּ���.');");
				out.println("location.href='"+url+"';");				
				out.println("</script>");
			}
		return null;
	}

}
