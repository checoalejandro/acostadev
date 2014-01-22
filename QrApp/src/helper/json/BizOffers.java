package helper.json;

import java.util.List;

public class BizOffers {

	List<ActivityGroup> groups;
	List<Acti> offers;
	String name;
	
	public String getName(){
		return name;
	}
	
	public List<ActivityGroup> getGroups(){
		return groups;
	}
	
	public List<Acti> getOffers(){
		return offers;
	}
}
