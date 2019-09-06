package timeLine;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class timeLineAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		
		// �۾��� ����(vancomember)�� Ÿ�Ӷ��� �⺻����(timeline) ���� �ؼ� ��������
		timeLineDAO tdao = new timeLineDAO();		
		ArrayList<timeLineDTO> timeLinelist = tdao.getTimeLine();		
		request.setAttribute("timeLinelist", timeLinelist);
				
		// ���ε��� ���� ��������
		timeLineDAO tdao1 = new timeLineDAO();		
		ArrayList<timeLineDTO> photoList = tdao1.getTimePhoto();		
		request.setAttribute("photoList", photoList);
		
		//��� �������� (ajax�� ��ü�ؼ� ���������Ƿ�, ������)
		//timeLineDAO tdao2 = new timeLineDAO();		
		//ArrayList<timeLineDTO> replyList = tdao2.getTimeReply();		
		//request.setAttribute("replyList", replyList);
	
		//�������� ��������
		timeLineDAO tdao3 = new timeLineDAO();		
		ArrayList<timeLineDTO> photoCount = tdao3.getTimePhotoCount();		
		request.setAttribute("photoCount", photoCount);
		
		ActionForward forward=new ActionForward();		
		forward.setPath("./timeLine/timeLine.jsp");		
		return forward;
	}

}
