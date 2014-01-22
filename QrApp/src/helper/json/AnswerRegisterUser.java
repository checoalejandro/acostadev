package helper.json;

public class AnswerRegisterUser {
	int status;
	String msg;
	User data;
	
	public String getMsg(){
        return msg;
    }
	
	public User getData(){
        return data;
    }
	
	public int getStatus(){
		return status;
	}
}
