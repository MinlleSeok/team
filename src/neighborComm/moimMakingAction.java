package neighborComm;

import java.sql.Timestamp;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class moimMakingAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("utf-8");
		
		//1. ���� �������� ��������Ʈ ���� ���ؽ�Ʈ ��ü ������, 
		ServletContext ctx = request.getServletContext();
		
		//2. ���ؽ�Ʈ ��ü�� ���� ���ε�� ������ ��� �־ �����ϱ� 
		String realPath	= ctx.getRealPath("images/moim/");
		System.out.println(realPath);
		
		//3. ���ε� ���� �ִ� ũ�� ����(10mb)
		int max = 1024*1024*10;
		
		MultipartRequest multi=new MultipartRequest(request,realPath,max,"utf-8",new DefaultFileRenamePolicy());
		
		
		//request�� ��ƿ� �Է������� ���������� ��� �����´�.
		String userId=multi.getParameter("userId");
		String moimCategory=multi.getParameter("moimCategory");
		String moimTitle=multi.getParameter("moimTitle");
		String moimIntro=multi.getParameter("moimIntro");
		String userCity=multi.getParameter("userCity");
		String userDistrict=multi.getParameter("userDistrict");
		
		
		// ���� ��ǲ�±��� name�Ӽ����� ������ ���ε�� ���ϸ��� �޾ƿ� �����θ� �������ش�.
		String moimPhoto = "/vc/images/moim/"+multi.getFilesystemName("moimPhoto");
		

		
		// �������� DTO�� ����
			moimDTO mdto = new moimDTO();
			mdto.setUserId(userId);
			mdto.setMoimCategory(moimCategory);
			mdto.setMoimTitle(moimTitle);
			mdto.setMoimIntro(moimIntro);
			mdto.setMoimPhoto(moimPhoto);
			mdto.setUserCity(userCity);
			mdto.setUserDistrict(userDistrict);
		
		// dto�� ������� �ʴ� �ý��� ������ �߰� ���� 			
			mdto.setMakingTime(new Timestamp(System.currentTimeMillis()));
			
		// DB�� ����
			neiBoardDAO ndao = new neiBoardDAO();
			ndao.insertMoim(mdto); 	

		// �̵���� ����
			ActionForward forward = new ActionForward();
			forward.setRedirect(true); // true : �����̷�Ʈ ������� ������
			forward.setPath("./neighborComm.ne");		
		
		
		return forward;
	}

}
