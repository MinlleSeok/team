package neighborComm;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class getThunderListAction implements Action {

	@SuppressWarnings("unchecked")
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		String moimNum = request.getParameter("moimNum");
		
		// ajax������ ���� ������ Ÿ�� ���� �� ���� �غ�
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
				
		
		neiBoardDAO ndao = new neiBoardDAO();
		ArrayList<thunderDTO> list = ndao.getThunderList(moimNum);
		
		// list�� �޾ƿ� ���� ������ ajax ȣ���� js���Ϸ� �����ϱ� ���� jaon�迭��ü�� �����ϱ�
		
		//1. ���� js���Ͽ� ������ json������Ʈ ����(��� ���� ������ �Ѱ��� ��� �������� out.print�� ���Ͻ����ֱ� ���� ��ü)
		JSONObject thunderFinalObj = new JSONObject();
		
		//2. json�����͸� ������ ���� �迭 ���� 
		JSONArray jArray = new JSONArray();
		
		//3. for������ arraylist���� ������ ������
		for(int i=0;i<list.size();i++){
			JSONObject tempObj = new JSONObject(); // �迭���� �����͸� ������ �� json������Ʈ
			tempObj.put("thunderNum", list.get(i).getThunderNum());
			tempObj.put("thunderName", list.get(i).getThunderName());
			tempObj.put("thunderPlace", list.get(i).getThunderPlace());
			tempObj.put("thunderPerson", list.get(i).getThunderPerson());
			tempObj.put("userId", list.get(i).getUserId());
			tempObj.put("userPhoto", list.get(i).getUserPhoto());				
			
			// ajax�� json �����͸� �����Ҷ� �����Ͱ� TimestampŸ���̸� ������ ���Ƿ� �ݵ�� toString()���ٰ�!!!
			tempObj.put("makingTime", list.get(i).getMakingTime().toString());	
			tempObj.put("thunderDate", list.get(i).getThunderDate().toString());
			
			// ��¥ ������ ������ �Ľ��Ͽ� �������� �ʿ��� ������ ��ȯ
			Timestamp tempDate = list.get(i).getThunderDate();
			int parsingDate=tempDate.getDate(); // �Ľ��Ͽ� ���� ����
			String parsingTime=""; // �Ľ��Ͽ� �ð� ����
			String parsingDay="";  // �Ľ��Ͽ� ���� ����
			
			// �ð� �ɰ���, �ð��� 1�ڸ� �����̸� �տ� ������ 0 ����
			if(tempDate.getHours()<10){
				parsingTime = "0"+tempDate.getHours()+":";
			}else{
				parsingTime = tempDate.getHours()+":";
			}
			
			// �� �ɰ���, ���� 1�ڸ� �����̸� �տ� ������ 0 ����
			if(tempDate.getMinutes()<10){
				parsingTime += "0"+tempDate.getMinutes();
			}else{
				parsingTime += tempDate.getMinutes();
			}
			
			// ���ڸ� ���Ϸ� �ٲٱ�
			switch (tempDate.getDay()) {
			case 0:
				parsingDay = "�Ͽ���";
				break;
			case 1:
				parsingDay = "������";
				break;
			case 2:
				parsingDay = "ȭ����";
				break;
			case 3:
				parsingDay = "������";
				break;
			case 4:
				parsingDay = "�����";
				break;
			case 5:
				parsingDay = "�ݿ���";
				break;
			case 6:
				parsingDay = "�����";
				break;
			default:
				break;
			} // ����ġ�� ����
			
			// �Ľ��� ���������� tempObj�� �߰� ����			
			tempObj.put("parsingDate", parsingDate);
			tempObj.put("parsingTime", parsingTime);
			tempObj.put("parsingDay", parsingDay);					
			
			// tempObj�� ����� 1���� ���� ����Ʈ ��� ������ json�迭�� (jArray)�� ����
			jArray.add(tempObj);
			
		} // for�� ����
		
		// for���� ���� �迭�� ����� ��� ������ json��ü�� ��Ƽ� string���� ��ȯ �� �ڹٽ�ũ��Ʈ�� ajax�� ȣ�⿡ data���� �����Ѵ�.	
		// ajax�� ������ ���ؼ��� stringŸ������ ��ȯ �� �����ؾ� ������ �ս��� ���� 
		thunderFinalObj.put("allThunderList",jArray);				
		
		
		out.print(thunderFinalObj.toString());
		out.flush();
		
		// �������� forward�� �Բ� ���� ���� ����!(ajax�� ���ϵ�)
		return null;
	}

}
