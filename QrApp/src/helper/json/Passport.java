package helper.json;

import java.util.List;

public class Passport {

	List<Promo> prizes;
	String name;
	int passid;
	
	public List<Promo> getPrizes(){
		return prizes;
	}
	
	public String getName(){
		return name;
	}
	
	public int getPassportId(){
		return passid;
	}
}
