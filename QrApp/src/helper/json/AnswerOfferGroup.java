package helper.json;

public class AnswerOfferGroup {
	
	int status;
	String msg;
	OfferGroup data;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public OfferGroup getOfferGroup(){
		return this.data;
	}

}
