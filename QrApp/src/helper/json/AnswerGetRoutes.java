package helper.json;

import java.util.List;

public class AnswerGetRoutes {

	List<Route> data;
	int status;
	String msg;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public List<Route> getData(){
		return this.data;
	}
}
