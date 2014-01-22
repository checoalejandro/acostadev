package helper.json;

public class AnswerPromos {
	PromoList data;
	int status;
	String msg;
	int points;
	
	public PromoList getPromos(){
		return this.data;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public int getPoints(){
		return this.points;
	}
}
