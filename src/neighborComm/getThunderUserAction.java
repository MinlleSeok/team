package neighborComm;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class getThunderUserAction implements Action {

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		request.setCharacterEncoding("UTF-8");
		String moimNum = request.getParameter("moimNum");
		String thunderNum = request.getParameter("thunderNum");
		
		
		moimDAO mdao = new moimDAO();
		ArrayList<thunderUserDTO> list = mdao.getThunderUser(moimNum, thunderNum);
		
		// list�� �޾ƿ� ���� ������ ajax ȣ���� js���Ϸ� �����ϱ� ���� jaon�迭��ü�� �����ϱ�
		
		//1. ���� js���Ͽ� ������ json������Ʈ ����(��� ���� ������ �Ѱ��� ��� �������� out.print�� ���Ͻ����ֱ� ���� ��ü)
		JSONObject finalObj = new JSONObject();
		
		//2. json�����͸� ������ ���� �迭 ���� 
		JSONArray jArray = new JSONArray();
		
		//3. for������ arraylist���� ������ ������ json�迭�� �ֱ�
		for(int i=0;i<list.size();i++){
			
			JSONObject tempObj = new JSONObject(); // �迭���� �����͸� ������ �� json������Ʈ
			tempObj.put("userId", list.get(i).getUserId());
			tempObj.put("userPhoto", list.get(i).getUserPhoto());
			tempObj.put("userNickname", list.get(i).getUserNickname());		
			tempObj.put("thunderNum", list.get(i).getThunderNum());	
			
			// tempObj�� ����� 1���� ���� ����Ʈ ��� ������ json�迭�� (jArray)�� ����
			jArray.add(tempObj);
						
		} // for�� ����
		
		// for���� ���� �迭�� ����� ��� ������ json��ü�� ��Ƽ� string���� ��ȯ �� �ڹٽ�ũ��Ʈ�� ajax�� ȣ�⿡ data���� �����Ѵ�.	
		// ajax�� ������ ���ؼ��� stringŸ������ ��ȯ �� �����ؾ� ������ �ս��� ���� 
		finalObj.put("allUser",jArray);				
		
		// ajax������ ���� ������ Ÿ�� ���� �� ���� �غ�
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.print(finalObj.toString());
		out.flush();
		
		// �������� forward�� �Բ� ���� ���� ����!(ajax�� ���ϵ�)
		return null;
	}

}
