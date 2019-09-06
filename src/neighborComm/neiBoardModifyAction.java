package neighborComm;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class neiBoardModifyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//neiBoardWrite.jsp���� ���޵� ����������+���ε� ���� ������ request�� ����Ǿ� �����Ƿ� ��� ���ް��� �����Ѵ�.
		// �׷���, �ش� �������� MultipartRequestŸ���̱� ������ request.getParameter�δ� ���� �� ����.

		request.setCharacterEncoding("utf-8");

		//1. ���� �������� ��������Ʈ ���� ���ؽ�Ʈ ��ü ������, 
		ServletContext ctx = request.getServletContext();
		
		
		//2. ���ؽ�Ʈ ��ü�� ���� ���ε�� ������ ��� �־ �����ϱ� 
		String realPath	= ctx.getRealPath("imageUpload");
		
		//3. ���ε� ���� �ִ� ũ�� ����(10mb)
		int max = 1024*1024*10;
		
		// ���� ���ε尴ü ȣ��		
		MultipartRequest multi=new MultipartRequest(request,realPath,max,"utf-8",new DefaultFileRenamePolicy());
		
		
		//request�� ��ƿ� ���� ������ �����´�. 	
		String subject=multi.getParameter("subject");
		String content=multi.getParameter("content");
		String contentNum= multi.getParameter("num");	
		String pageNum= multi.getParameter("pageNum");
		
		String existPic1= multi.getParameter("existPic1");	// ���� ������Ʈ ������ ��� ���� ���ϸ� �ֱ� ���� �۾�
		String existPic2= multi.getParameter("existPic2");
		String existPic3= multi.getParameter("existPic3");
		String existPic4= multi.getParameter("existPic4");
		
		int num = Integer.parseInt(contentNum);
			
		// ���� ��ǲ�±��� name�Ӽ����� ������ ���ε�� ���ϸ��� �޾ƿ� �����θ� �������ش�.
		//�̶� ������ ����������, ���ο� ���ϸ��� ��������, ������ ������ ������ ���ϸ��� �����Ѵ�.	
		String upPhoto1 = (multi.getFilesystemName("upPhoto1")!=null)? "/vc/imageUpload/"+multi.getFilesystemName("upPhoto1") :existPic1 ;
		String upPhoto2 = (multi.getFilesystemName("upPhoto2")!=null)? "/vc/imageUpload/"+multi.getFilesystemName("upPhoto2") :existPic2 ;
		String upPhoto3 = (multi.getFilesystemName("upPhoto3")!=null)? "/vc/imageUpload/"+multi.getFilesystemName("upPhoto3") :existPic3 ;
		String upPhoto4 = (multi.getFilesystemName("upPhoto4")!=null)? "/vc/imageUpload/"+multi.getFilesystemName("upPhoto4") :existPic4 ;

		// �������� DTO�� ����
			neiBoardDTO ndto = new neiBoardDTO();
			ndto.setSubject(subject);
			ndto.setContent(content);
			ndto.setUpPhoto1(upPhoto1);
			ndto.setUpPhoto2(upPhoto2);	
			ndto.setUpPhoto3(upPhoto3);	
			ndto.setUpPhoto4(upPhoto4);	
			
		// �Է¹��� �� DB�� ����
			neiBoardDAO ndao = new neiBoardDAO();
			ndao.modifyContent(ndto, num); 	

			ActionForward forward = new ActionForward();
			forward.setRedirect(true); // true : �����̷�Ʈ ������� ������
			forward.setPath("./neighborComm.ne");		
		

		return forward;
	}

}
