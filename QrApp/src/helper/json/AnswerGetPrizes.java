package helper.json;

import java.util.List;

public class AnswerGetPrizes {

	int status;
	String msg;
	List<UserPrize> data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public List<UserPrize> getData(){
		return data;
	}
}
