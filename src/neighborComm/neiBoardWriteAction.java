package neighborComm;

import java.sql.Timestamp;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class neiBoardWriteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//neiBoardWrite.jsp���� ���޵� ����������+���ε� ���� ������ request�� ����Ǿ� �����Ƿ� ��� ���ް��� �����Ѵ�.
		// �׷���, �ش� �������� MultipartRequestŸ���̱� ������ request.getParameter�δ� ���� �� ����.

		request.setCharacterEncoding("utf-8");

		// request.getRealPath("�����")�� ���� ������ ������ ���� ��θ� ���ؿ´�;
		/*String uploadPath=request.getRealPath("imageUpload");*/
		
		//1. ���� �������� ��������Ʈ ���� ���ؽ�Ʈ ��ü ������, 
		ServletContext ctx = request.getServletContext();
		
		//2. ���ؽ�Ʈ ��ü�� ���� ���ε�� ������ ��� �־ �����ϱ� 
		String realPath	= ctx.getRealPath("imageUpload");
		
		//3. ���ε� ���� �ִ� ũ�� ����(10mb)
		int max = 1024*1024*10;
		
		// ���� ���ε尴ü ȣ��
			// ���尴ü ���ް�
				//1. ���±׿��� ���޹��� request��ü�� �����ڷ� ����
				//2. ���� ���ε� �� ��� ��ġ ����
				//3. max size
				//4. �����̸��� �ѱ��� ��� ó���� �� �ֵ��� ���ڵ� Ÿ�� ����
				//5. �Ȱ��� �̸��� ������ ���ε� �� �� �ڵ� ���ϸ� ��ȯ��ü ���� 
		MultipartRequest multi=new MultipartRequest(request,realPath,max,"utf-8",new DefaultFileRenamePolicy());
		
		
		//request�� ��ƿ� �Է������� ���������� ��� �����´�.
		String userId=multi.getParameter("userId");
		String userNickname=multi.getParameter("userNickname");
		String userPhoto=multi.getParameter("userPhoto");
		String subject=multi.getParameter("subject");
		String content=multi.getParameter("content");		
		
		// ���� ��ǲ�±��� name�Ӽ����� ������ ���ε�� ���ϸ��� �޾ƿ� �����θ� �������ش�.
		String upPhoto1 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto1");
		String upPhoto2 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto2");
		String upPhoto3 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto3");
		String upPhoto4 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto4");

		
		// �������� DTO�� ����
			neiBoardDTO ndto = new neiBoardDTO();
			ndto.setUserId(userId);
			ndto.setUserNickname(userNickname);
			ndto.setUserPhoto(userPhoto);
			ndto.setSubject(subject);
			ndto.setContent(content);
			ndto.setUpPhoto1(upPhoto1);
			ndto.setUpPhoto2(upPhoto2);	
			ndto.setUpPhoto3(upPhoto3);	
			ndto.setUpPhoto4(upPhoto4);	
		
		// dto�� ������� �ʴ� �ý��� ������ �߰� ���� 			
			ndto.setWriteTime(new Timestamp(System.currentTimeMillis()));
			ndto.setIp(request.getRemoteAddr());  	//request ��ü �������� ip, ����� ������ �������� �� ����ִ�
				
			
		// �Է¹��� �� DB�� ����
			neiBoardDAO ndao = new neiBoardDAO();
			ndao.insertNeiBoard(ndto); 	

		// �̵���� ����
			ActionForward forward = new ActionForward();
			forward.setRedirect(true); // true : �����̷�Ʈ ������� ������
			forward.setPath("./neighborComm.ne");		
		
		
		return forward;
	}

}
