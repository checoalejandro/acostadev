package helper.json;

import java.util.List;

public class AnswerGetChat {

	int status;
	String msg;
	List<ChatMessage> data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public List<ChatMessage> getMessages(){
		return data;
	}
}
