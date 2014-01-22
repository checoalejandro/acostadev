package helper.json;

public class UserPrize {

	int id;
	String name;
	String prize;
	public int redeemed;
	String date;
	String expiration;
	int head;
	boolean hidden = true;
	String description;
	
	public String getDescription(){
		return description;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPrize(){
		return prize;
	}
	
	public String getDate(){
		return expiration;
	}
	
	public int getRedemed(){
		return redeemed;
	}
	
	public boolean isRedeemed(){
		return (redeemed == 1 ? true : false);
	}
	
	public boolean isHead(){
		return (head == 1 ? true : false); 
	}
	
	public void hide(boolean h){
		hidden = h;
	}
	
	public boolean hidden(){
		return hidden;
	}
}
