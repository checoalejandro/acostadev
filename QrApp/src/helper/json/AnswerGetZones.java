package helper.json;

import java.util.List;

public class AnswerGetZones {

	List<Zone> data;
	int status;
	String msg;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public List<Zone> getData(){
		return this.data;
	}
}
