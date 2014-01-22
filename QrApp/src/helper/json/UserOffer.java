package helper.json;

public class UserOffer {

	int id;
	String offer;
	String name;
	int redeemed;
	String expiration;
	
	public int getId(){
		return id;
	}
	
	public String getOffer(){
		return offer;
	}
	
	public String getName(){
		return name;
	}
	
	public int getRedeemed(){
		return redeemed;
	}
	
	public boolean isRedeemed(){
		return (redeemed == 1 ? true : false);
	}
	
	public String getExpiration(){
		return expiration;
	}
}
