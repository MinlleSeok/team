package member;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class MemberServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {doProcess(request,response);System.out.println("�ΰ�");}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {doProcess(request,response);System.out.println("������Ʈ");}
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("���� �۵�");
		
		// ��û�� �����ּҰ� ���(���ؽ�Ʈ �н������ּ�-���ؽ�Ʈ���� = ���� ���)
		String RequestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		int contextPathLength= contextPath.length();
		String command = RequestURI.substring(contextPathLength);// ��ü�ּҿ��� ��û�ּҸ� �߶� ���
		System.out.println("1:"+RequestURI);
		System.out.println("2:"+contextPath);
		System.out.println("3:"+contextPathLength);
		System.out.println("command��������:"+command);
		
		//doProcess �������� ����
		ActionForward forward = null;
		Action action=null;		
		
		// ��û�ּ�(command)�� ���� �۾� ó�� �����ϱ�
		
// ȸ������ ���� ����(userJoin.me)////////////////////////////////////////////////////////////////////
	if(command.equals("/userJoin.me")){
		//������ �̵���� ����(true=�����̷�Ʈ, false=����ġ(��� ��������))
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./member/userJoin.jsp");  //�̵��� ������ ��� �ּҰ� ����(ȸ������ �Է� ������)
	
// ���̵� ȸ������ ����////////////////////////////////////////////////////////////////////	
	}else if(command.equals("/idUserJoin.me")){
		forward=new ActionForward();
		forward.setRedirect(false);			
		forward.setPath("./member/idUserJoin.jsp");  //�̵��� ������ ��� �ּҰ� ����(idȸ������ �Է� ������)
		
	}else if(command.equals("/userJoinAction.me")){
		// DBȸ������ ó���� ����  Action���� ��ü ����(����Ͻ�����) : DB�۾��� userJoinAction���� ����
		action=new userJoinAction();
		
		try {
			forward=action.execute(request, response);
		} catch (Exception e) {
			System.out.println("userJoinAction.me���� ����"+e);
		}	
		
// ���������� ����(vacno.me)////////////////////////////////////////////////////////////////////	
	}else if(command.equals("/vanco.me")){
		//������ �̵���� ����(true=�����̷�Ʈ, false=����ġ(��� �������))
		forward=new ActionForward();
		forward.setRedirect(false);
		
		//�̵��� ������ ��� �ּҰ� ����
		forward.setPath("/index.jsp");	
		

// �α��������� ����(userLogin.me)////////////	
	}else if(command.equals("/userLogin.me")){
		String url = request.getParameter("url");
			
		forward=new ActionForward();
		forward.setRedirect(false);
		forward.setPath("./member/login.jsp?url="+url);
		
		// �α��� �۾� ������ ����()////////////		
	}else if(command.equals("/userLoginAction.me")){
		// DB�� �α��� ó���� ���� Action���� ��ü ����(����Ͻ� ����) : DB�۾��� MemberLoginActionŬ������ ȣ���Ͽ� ����
		action=new userLoginAction();
		
		try {
			forward=action.execute(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	// �α��� �Ϸ� �� url ���� ////////////		
		}else if(command.equals("/loginOK.me")){
			
			
			/*String url = request.getParameter("url");			
			contextPath = request.getContextPath();
			contextPathLength= contextPath.length();
			String finalUrl = url.substring(contextPathLength);// ��ü�ּҿ��� ��û�ּҸ� �߶� ���
			System.out.println("url1:"+url);
			System.out.println("url2:"+contextPath);
			System.out.println("url3:"+contextPathLength);
			System.out.println("url��������:"+finalUrl);	*/	
			
			forward=new ActionForward();
			forward.setRedirect(false);
			forward.setPath("vanco.me");

// ���������� ����(./mypage.me)////////////	
	}else if(command.equals("/mypage.me")){
		//������ �̵���� ����(true=�����̷�Ʈ, false=����ġ(��� ��������))
		forward=new ActionForward();
		forward.setRedirect(false);
		
		//�̵��� ������ ��� �ּҰ� ����(�α��� ������)
		forward.setPath("./mypage/mypage.jsp");


// �α׾ƿ� ����(./mypage.me)////////////	
	}else if(command.equals("/logout.me")){
		action=new logoutAction();
		
		try {
			forward=action.execute(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

// �ű԰����� ������� ����(./mandatoryInfo.me)////////////	
	}else if(command.equals("/mandatoryInfo.me")){
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./mypage/mandatoryInfo.jsp");
	
	// �ű԰����� ������� �׼�(./mandatoryInfoAction.me)////////////	
	}else if(command.equals("/mandatoryInfoAction.me")){
		action=new mandatoryInfoAction();		
		try {
			forward=action.execute(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

// ������ �������� �׼�(./mypageEdit.me)////////////			
	}else if(command.equals("/mypageEdit.me")){
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./mypage/mypageEdit.jsp");
		
	// ������ �������� �׼�(./editMypageAction.me)////////////	
	}else if(command.equals("/editMypageAction.me")){
		action=new editMypageAction();		
		try {
			forward=action.execute(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
// ���������� ���� �������� �׼�(./editMyPhotoAction.me)////////////					
	}else if(command.equals("/editMyPhotoAction.me")){
		action=new editMyPhotoAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();
		}

// ����� ������ Ȯ�� ������ �̵�(
	
	}else if(command.equals("/yourPage.me")){
		String yourId = request.getParameter("yourId");
		System.out.println(yourId);
		System.out.println(request.getContextPath());
		
		forward=new ActionForward();
		forward.setRedirect(false);		
		forward.setPath("./mypage/yourPage.jsp?yourId="+yourId);
		
// īī���� ȸ������ ���̵� ���� ���� Ȯ�� �� id���� �� �α��� ó��. idChk		
	}else if(command.equals("/snsIdChk.me")){
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html, charset=utf-8");
						
		System.out.println("sns���̵�üũ ȣ�� ��.");	
		
		// ���۵� id�� DB�� ID���� ���� Ȯ�� �� ajax����� �̿��Ͽ� ������� ���� �Ѵ�.
		String userId = request.getParameter("userId");	
		System.out.println("�������� �Ѿ�� Id�� :"+userId);
		
		
		action=new snsIdChkAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	//īī���� ȸ������ �׼� 	
	}else if(command.equals("/snsUserJoinAction.me")){
		System.out.println("sns�α��� ȣ���");
		/*
		String userId = request.getParameter("userId");			
		System.out.println("snsUserJoin ȣ�� id :"+userId);*/
		
		action=new snsUserJoinAction();		
		try {
			forward=action.execute(request, response);			
		} catch (Exception e) {
			System.out.println("snsUserJoinAction����"+e);
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
