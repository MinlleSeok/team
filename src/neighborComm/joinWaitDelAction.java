package neighborComm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class joinWaitDelAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");		
		String userId = request.getParameter("userId");
		int moimNum = Integer.parseInt(request.getParameter("moimNum"));
		
		
		ActionForward forward = new ActionForward();
		
		moimDAO mdao = new moimDAO();
		mdao.deleteJoinWait(userId, moimNum);
		
		forward.setRedirect(true); // false : 리다이렉트 방식으로 포워딩
		forward.setPath("./moimMemberInfo.ne?moimNum="+moimNum);
		
		return forward;
	}

}
