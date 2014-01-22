package helper.json;

import java.util.List;

public class AndroidView {

	List<Acti> acts;
	List<ActivityGroup> actgps;
	List<OfferGroup> offers;
	List<Promo> prizes;
	int checkins;
	List<Attention> alerts;
	String bizname;
	int subbizid;
	
	public String getBizneName(){
		return bizname;
	}
	
	public List<Acti> getActivities(){
		return acts;
	}
	
	public List<ActivityGroup> getActivityGroups(){
		return actgps;
	}
	
	public List<OfferGroup> getOffers(){
		return offers;
	}
	
	public List<Promo> getPrizes(){
		return prizes;
	}
	
	public boolean hasCheckins(){
		return (checkins == 1) ? true : false;
	}
	
	public boolean hasAlerts(){
		return (alerts.size() != 0) ? true : false;
	}
	
	public int getSubBizId(){
		return subbizid;
	}
	
	public List<Attention> getAttentions(){
		return alerts;
	}
}
