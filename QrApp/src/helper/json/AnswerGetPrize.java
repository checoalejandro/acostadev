package helper.json;

public class AnswerGetPrize {
	int status;
	String msg;
	Prize prize;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public Prize getPrize(){
		return this.prize;
	}
}
