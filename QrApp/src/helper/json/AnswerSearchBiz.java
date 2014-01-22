package helper.json;

public class AnswerSearchBiz {
	int status;
	String msg;
	BizneInfoList data;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public BizneInfoList getData(){
		return this.data;
	}
}