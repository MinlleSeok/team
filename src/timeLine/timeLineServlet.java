package timeLine;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import neighborComm.neiBoardDeleteAction;


public class timeLineServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {doProcess(request,response);}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {doProcess(request,response);}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
		// ��û�� �����ּҰ� ���(���ؽ�Ʈ �н������ּ�-���ؽ�Ʈ���� = ���� ���)
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		int contextPathLength= contextPath.length();
		String command = RequestURI.substring(contextPathLength);// ��ü�ּҿ��� ��û�ּҸ� �߶� ���		
		
		System.out.println(command + "  �����ּ� ��û��");
		
		//doProcess �������� ����
		ActionForward forward = null;
		Action action=null;		
		
		// ��û�ּ�(command)�� ���� �۾� ó�� �����ϱ�
		
// Ÿ�Ӷ��� ����(/timeLine.ti)////////////////////////////////////////////////////////////////////
	if(command.equals("/timeLine.ti")){
		//������ �̵���� ����(true=�����̷�Ʈ, false=����ġ(��� ��������))
		forward=new ActionForward();
		forward.setRedirect(false);	
		forward.setPath("/timeLineAction.ti");  //�̵��� ������ ��� �ּҰ� ����(ȸ������ �Է� ������)
	
	}else if(command.equals("/timeLineAction.ti")){
	// Ÿ�Ӷ��� ��û�� �ٷ� ���� �������� �׼�
		action=new timeLineAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/addLikeAction.ti")){
		// ���ƿ� ���ϱ� �׼�
			action=new addLikeAction();		
			try {
				forward=action.execute(request, response);			
			} catch (Exception e) {
				e.printStackTrace();	
			}
			
	}else if(command.equals("/delLikeAction.ti")){
		// ���ƿ� ���� �׼�
		action=new delLikeAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/getLikeAction.ti")){
		// ���ƿ� ī����
		action=new getLikeAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/likeClickCheckAction.ti")){
		// ���ƿ� Ŭ�� ���� üũ
		action=new likeClickCheckAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}if(command.equals("/timeLineWrite.ti")){		
		// Ÿ�Ӷ��� ���� �ø��� ������ �̵�
		forward=new ActionForward();
		forward.setRedirect(false);	
		forward.setPath("./timeLine/timeLineWrite.jsp"); 
	
	}else if(command.equals("/timeLineWriteAction.ti")){
		// Ÿ�Ӷ��� �۾���(���� �ø���)
		action=new timeLineWriteAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/insertTimeReplyAction.ti")){
		// Ÿ�Ӷ��� ��� ����
		action=new insertTimeReplyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/insertTimeReReplyAction.ti")){
		// Ÿ�Ӷ��� ���� ����
		action=new insertTimeReReplyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/getReplyAction.ti")){
		// Ÿ�Ӷ��� ��� ����Ʈ �ޱ�
		action=new getReplyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/getReplyCountAction.ti")){
		// Ÿ�Ӷ��� ��ۼ� ����
		action=new getReplyCountAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}
	
	
	
	
	
	
	// ������� ó�� (������)////////////////////////////////////////////////////////////////////
	if(forward!=null){
		if(forward.isRedirect()){
			response.sendRedirect(forward.getPath());
		}else{//����ġ ������� ������ ��� ���� ���� ������
			RequestDispatcher dispatcher=request.getRequestDispatcher(forward.getPath());
			dispatcher.forward(request, response);}
		
	}// ������� if ����		
} // doProcess() ����	
} // ���� ����