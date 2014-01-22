package helper.json;

public class AnswerGetInfo {
	int status;
	String msg;
	BizneInfo bizneinfo;
	PromotionList biznepromos;
	PromoList bizneprizes;
	int userpoints;
	int like;
	int prizescount;
	
	public PromotionList getPromotions(){
		return this.biznepromos;
	}
	
	public PromoList getPrizes(){
		return this.bizneprizes;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public BizneInfo getBizneInfo(){
		return this.bizneinfo;
	}
	
	public int getUserPoints(){
		return this.userpoints;
	}
	
	public boolean IsLiked(){
		return (like == 1);
	}
	
	public int getPrizesCount(){
		return prizescount;
	}
}
