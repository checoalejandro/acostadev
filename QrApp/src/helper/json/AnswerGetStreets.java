package helper.json;

import java.util.List;

public class AnswerGetStreets {
	
	int status;
	String msg;
	List<Street> data;

	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public List<Street> getData(){
		return this.data;
	}
	
}
