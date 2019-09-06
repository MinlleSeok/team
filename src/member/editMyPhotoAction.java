package member;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class editMyPhotoAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("utf-8");
		
		//���� ������ ���� ��ü ����	
		HttpSession session = request.getSession();

		/*
		 * 
		 // ��1������ �̷��� ��μ��� ������, ��2�� �ణ�޶���
		 String uploadPath=request.getRealPath("imageProfile");		
		
		ServletContext ctx = getServletContext();*/
		/*String uploadPath=ctx.getRealPath("imageProfile");*/
		
		//1. ���� �������� ��������Ʈ ���� ���ؽ�Ʈ ��ü ������, 
		ServletContext ctx = request.getServletContext();
		
		
		//2. ���ؽ�Ʈ ��ü�� ���� ���ε�� ������ ��� �־ �����ϱ� 
		String realPath	= ctx.getRealPath("imageProfile");
		
		//3. ���ε� ���� �ִ� ũ�� ����(10mb)
		int max = 1024*1024*10;
		
		// ���� ���ε尴ü ȣ��		
		MultipartRequest multi=new MultipartRequest(request,realPath,max,"utf-8",new DefaultFileRenamePolicy());
		
		
		//request�� ��ƿ� ���� ������ �����´�. 	
		String userId=multi.getParameter("userId");
			
		String existDogPic= multi.getParameter("existDogPic");	// ���� ������Ʈ ������ ��� ���� ���ϸ� �ֱ� ���� �۾�
		String existUserPic= multi.getParameter("existUserPic");
			
		// ���� ��ǲ�±��� name�Ӽ����� ������ ���ε�� ���ϸ��� �޾ƿ� �����θ� �������ش�.
		//�̶� ������ ����������, ���ο� ���ϸ��� ��������, ������ ������ ������ ���ϸ��� �����Ѵ�.	
		String upDogPhoto = (multi.getFilesystemName("upDogPhoto")!=null)? "/vc/imageProfile/"+multi.getFilesystemName("upDogPhoto") :existDogPic ;
		String upUserPhoto = (multi.getFilesystemName("upUserPhoto")!=null)? "/vc/imageProfile/"+multi.getFilesystemName("upUserPhoto") :existUserPic ;

		// �������� DB�� ����
			MemberDAO mdao = new MemberDAO();	
			
		int result = mdao.editMyPic(upDogPhoto, upUserPhoto, userId); 
			
		// ���ǰ��� ���ο� user���� ����
		session.setAttribute("userPhoto", upUserPhoto);
			
			
		
		ActionForward forward = new ActionForward();
		forward.setRedirect(true); // true : �����̷�Ʈ ������� ������
		forward.setPath("./mypage.me");		
		
		return forward;
	}

}
