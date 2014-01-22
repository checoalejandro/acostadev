package helper.json;

public class AnswerGetCategories {
	int status;
	String msg;
	ParentCategList data;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public ParentCategList getData(){
		return this.data;
	}
}
