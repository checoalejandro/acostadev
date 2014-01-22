package helper.json;

import java.util.List;

public class AnswerGetOffers {

	int status;
	String msg;
	List<UserOffer> data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public List<UserOffer> getData(){
		return data;
	}
}
