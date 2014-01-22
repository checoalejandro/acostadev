package helper.json;

public class AnswerFavorites {
	int status;
	String msg;
	FavoritesList data;
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public FavoritesList getData(){
		return this.data;
	}
}
