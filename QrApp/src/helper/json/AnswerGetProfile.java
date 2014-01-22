package helper.json;

public class AnswerGetProfile {

	int status;
	String msg;
	UserProfile data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public UserProfile getProfile(){
		return data;
	}
}
