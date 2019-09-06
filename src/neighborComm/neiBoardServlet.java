package neighborComm;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.editMyPhotoAction;


public class neiBoardServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {doProcess(request,response);}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {doProcess(request,response);
	}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		// ��û�� �����ּҰ� ���(���ؽ�Ʈ �н������ּ�-���ؽ�Ʈ���� = ���� ���)
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		int contextPathLength= contextPath.length();
		String command = RequestURI.substring(contextPathLength);// ��ü�ּҿ��� ��û�ּҸ� �߶� ���
		
		System.out.println("command��������:"+command);
		
		//doProcess �������� ����
		ActionForward forward = null;
		Action action=null;		
		
		// ��û�ּ�(command)�� ���� �۾� ó�� �����ϱ�
		
// �� ���� ����(/neiBoardContent.ne)////////////////////////////////////////////////////////////////////
	if(command.equals("/neiBoardContent.ne")){		
		String num = request.getParameter("num");
		String pageNum = request.getParameter("pageNum");

		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("/neighborComm/neiBoardContent.jsp?num="+num+"&pageNum="+pageNum);  
	
	}else if(command.equals("/neighborComm.ne")){	
	// �츮���� ����
		forward=new ActionForward();
		forward.setRedirect(false);			
		forward.setPath("/neighborComm/neighborComm.jsp"); 
	
	}else if(command.equals("/neighborCommAction.ne")){	
		// �츮���� ���� �׼�
		action=new neighborCommAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
			
			
	
		
		//Prev �ؽ�Ʈ,���������� ��ư	
	}else if(command.equals("/neighborCommMove.ne")){
		String pageNum=request.getParameter("pageNum");
		forward=new ActionForward();
		forward.setRedirect(false);			
		forward.setPath("/neighborComm/neighborComm.jsp?pageNum="+pageNum);

// �۾��� ��ư
	}else if(command.equals("/write.ne")){
		forward=new ActionForward();
		forward.setRedirect(false);			
		forward.setPath("./neighborComm/neiBoardWrite.jsp");
	
//�۾��� �׼� neiBoardWriteAction.
	}else if(command.equals("/neiBoardWriteAction.ne")){
		action=new neiBoardWriteAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}

//�ۻ��� �׼� neiBoardDelete.ne
	}else if(command.equals("/neiBoardDelete.ne")){
		action=new neiBoardDeleteAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
//�� ���������� �̵� neiBoardModify.ne
	}else if(command.equals("/neiBoardModify.ne")){
		String num = request.getParameter("num");
		String pageNum = request.getParameter("pageNum");

		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/neiBoardModify.jsp?num="+num+"&pageNum="+pageNum); 
	//�� ���� �׼�������
	}else if(command.equals("/neiBoardModifyAction.ne")){
		action=new neiBoardModifyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}		
	
//���û��� �׼� neiReplyDelete.ne
	}else if(command.equals("/neiReplyDelete.ne")){
		action=new neiReplyDeleteAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
	
//��۴ޱ� �׼� neiReplyAction.ne
	}else if(command.equals("/neiReplyAction.ne")){
		action=new neiReplyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
//���۴ޱ� �׼� neiReReplyAction.ne
	}else if(command.equals("/neiReReplyAction.ne")){
		action=new neiReReplyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
	
//////////////////////���⼭ ���ʹ� �Ҹ��� ���� ����////////////////////////		
/////////////////// �Ҹ��� ������ ������ �̵� 		
	}else if(command.equals("/moimContent.ne")){
		String moimNum = request.getParameter("moimNum");
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimContent.jsp?moimNum="+moimNum); 
		
	// �Ҹ��� �Խ��������� �̵�
	}else if(command.equals("/moimBoard.ne")){
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimContent.jsp?pageCall=moimBoard.jsp"); 
	
	// �Ҹ��� info������ �̵� 	
	}else if(command.equals("/moimInfo.ne")){
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimContent.jsp?pageCall=moimInfo.jsp"); 

	}else if(command.equals("/moimMemberInfo.ne")){
	// ���� ȸ������ ������
		String moimNum = request.getParameter("moimNum");
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("/neighborComm/moimContent.jsp?pageCall=moimMemberInfo.jsp");	
		
	}else if(command.equals("/moimMaking.ne")){
	// ���� ����� ������ �̵�			
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimMaking.jsp"); 

		
	}else if(command.equals("/moimMakingAction.ne")){
	// ���Ӹ���� �׼� ������
		action=new moimMakingAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}	
		
	}else if(command.equals("/insertThunderAction.ne")){
	// ���ӳ� ���� ����� �׼�
		action=new insertThunderAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}	
		
	}else if(command.equals("/getThunderListAction.ne")){
	// ���� ����Ʈ �������� �׼�
		action=new getThunderListAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/insertThunderUserAction.ne")){
	// ���ӳ� ���� ���� ��û �׼�(���)
		action=new insertThunderUserAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/deleteThunderUserAction.ne")){
	// ���ӳ� ���� ���� ���� �׼�(���)
		action=new deleteThunderUserAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/getThunderUserAction.ne")){
	// ���� ����/������ �����ʻ��� ����ϴ� �׼�
		action=new getThunderUserAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/moimModify.ne")){
	// ���� ���� ���� ������
		String moimNum = request.getParameter("moimNum");
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimModify.jsp?moimNum="+moimNum); 
		System.out.println(moimNum);
		
	}else if(command.equals("/moimModifyAction.ne")){
	// ���� ����/������ �����ʻ��� ����ϴ� �׼�
		action=new moimModifyAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/thunderDelAction.ne")){
	// ���� �����ϱ�
		action=new thunderDelAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/moimJoinAction.ne")){
	// ���� �����ϱ�
		action=new moimJoinAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/updateLevelAction.ne")){
	// ���� �����ϱ�
		action=new updateLevelAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/joinWaitDelAction.ne")){
	// ���� ���� ��� ���� �׼�
		action=new joinWaitDelAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/joinConfirmAction.ne")){
	// ���� ���� ���� �׼�
		action=new joinConfirmAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/updateCautionAction.ne")){
	// ����� �׼�
		action=new updateCautionAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/forcedMoimMemberDelAction.ne")){
	// �������� �׼�
		action=new forcedMoimMemberDelAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/moimJoinCancelAction.ne")){
	// �������� �׼�
		action=new moimJoinCancelAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
	}else if(command.equals("/moimBoardContent.ne")){
	// ���ӰԽ��� �� �б� �̵�
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimContent.jsp?pageCall=moimBoardContent.jsp"); 
	
	}else if(command.equals("/moimBoardWrite.ne")){
	// ���ӰԽ��� �� ���� �̵�
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./neighborComm/moimContent.jsp?pageCall=moimBoardWrite.jsp"); 
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