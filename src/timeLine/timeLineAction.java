package timeLine;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class timeLineAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		
		// 글쓴이 정보(vancomember)와 타임라인 기본정보(timeline) 조인 해서 가져오기
		timeLineDAO tdao = new timeLineDAO();		
		ArrayList<timeLineDTO> timeLinelist = tdao.getTimeLine();		
		request.setAttribute("timeLinelist", timeLinelist);
				
		// 업로드한 사진 가져오기
		timeLineDAO tdao1 = new timeLineDAO();		
		ArrayList<timeLineDTO> photoList = tdao1.getTimePhoto();		
		request.setAttribute("photoList", photoList);
		
		//댓글 가져오기 (ajax로 대체해서 가져왔으므로, 삭제함)
		//timeLineDAO tdao2 = new timeLineDAO();		
		//ArrayList<timeLineDTO> replyList = tdao2.getTimeReply();		
		//request.setAttribute("replyList", replyList);
	
		//사진갯수 가져오기
		timeLineDAO tdao3 = new timeLineDAO();		
		ArrayList<timeLineDTO> photoCount = tdao3.getTimePhotoCount();		
		request.setAttribute("photoCount", photoCount);
		
		ActionForward forward=new ActionForward();		
		forward.setPath("./timeLine/timeLine.jsp");		
		return forward;
	}

}
