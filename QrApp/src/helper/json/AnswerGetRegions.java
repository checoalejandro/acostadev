package helper.json;

import java.util.List;

public class AnswerGetRegions {
	
	int status;
	String msg;
	List<Region> data;

	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public List<Region> getData(){
		return this.data;
	}
	
}
