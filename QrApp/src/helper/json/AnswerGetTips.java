package helper.json;

import java.util.List;

public class AnswerGetTips {

	List<Tip> data;
	int status;
	String msg;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public List<Tip> getData(){
		return this.data;
	}
}
