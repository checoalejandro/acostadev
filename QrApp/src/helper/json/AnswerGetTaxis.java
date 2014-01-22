package helper.json;

public class AnswerGetTaxis {

	int status;
	String msg;
	TaxiRequest data;
	
	public int getStatus(){
		return status;
	}
	
	public String getMsg(){
		return msg;
	}
	
	public TaxiRequest getData(){
		return data;
	}
}
