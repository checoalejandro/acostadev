package helper.json;

import java.util.List;

public class OfferGroup {

	List<Offer> offers;
	String name;
	int bizid;
	int groupid;
	
	public int getBizId(){
		return this.bizid;
	}
	
	public int getGroupId(){
		return this.groupid;
	}
	
	public String getName(){
		return this.name;
	}
	
	public List<Offer> getOffers(){
		return this.offers;
	}
	
}
