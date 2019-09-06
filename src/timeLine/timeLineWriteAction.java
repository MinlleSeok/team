package timeLine;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class timeLineWriteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("utf-8");
		
		ServletContext ctx = request.getServletContext();
		
		//2. ���ؽ�Ʈ ��ü�� ���� ���ε�� ������ ��� �־ �����ϱ� 
		String realPath	= ctx.getRealPath("imageUpload");
		System.out.println(realPath);
		//3. ���ε� ���� �ִ� ũ�� ����(10mb)
		int max = 1024*1024*10;		
		
		MultipartRequest multi=new MultipartRequest(request,realPath,max,"utf-8",new DefaultFileRenamePolicy());
		
		//request�� ��ƿ� �Է������� ���������� ��� �����´�.
		String userId=multi.getParameter("userId");	
		String content=multi.getParameter("content");		
		
		// ���� ��ǲ�±��� name�Ӽ����� ������ ���ε�� ���ϸ��� �޾ƿ� �����θ� �������ش�.
		String upPhoto1 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto1");
		String upPhoto2 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto2");
		String upPhoto3 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto3");
		String upPhoto4 = "/vc/imageUpload/"+multi.getFilesystemName("upPhoto4");
		
		// �������� DTO�� ���� (����1~4������ ���� DTO�� �����Ƿ� �ӽ÷� �ٸ� String DTO�� ������Ѽ� ������)
		timeLineDTO tdto = new timeLineDTO();
		tdto.setUserId(userId);
		tdto.setContent(content);
		tdto.setPhotoUrl(upPhoto1);			
		tdto.setIp(upPhoto2);	// PhotoUrl2 ���
		tdto.setTitle(upPhoto3);	// PhotoUrl3 ���
		tdto.setReOwnerNick(upPhoto4);	// PhotoUrl4 ���
		
		// �Է¹��� �� DB�� ����
		timeLineDAO tdao = new timeLineDAO();
		tdao.insertTimeLine(tdto); 
		
		ActionForward forward=new ActionForward();	
		forward.setRedirect(true); // true : �����̷�Ʈ ������� ������
		forward.setPath("timeLine.ti");		
		return forward;
		
	}

}
