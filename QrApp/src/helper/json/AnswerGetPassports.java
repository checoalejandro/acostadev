package helper.json;

import java.util.List;

public class AnswerGetPassports {

	int status;
	String msg;
	List<Passport> data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public List<Passport> getPassports(){
		return data;
	}
}
