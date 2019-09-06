package timeLine;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class getLikeAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("utf-8");
		// ajax리턴을 위한 데이터 타입 설정 및 변수 준비
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		String timeLineNum = request.getParameter("timeLineNum");
				
		timeLineDAO tdao = new timeLineDAO();
		int likesCount = tdao.getLikeCount(timeLineNum); 
		
		// ajax로 데이터 보내기
		out.print(likesCount);
		out.flush();		
		
		return null;
	}

}
