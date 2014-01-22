package helper.json;

import java.util.List;

public class ActivityGroup {

	int idg;
	String name;
	List<Acti> activities;
	int scheduled;
	int bizid;
	String bizname;
	
	public boolean isScheduled(){
		return (scheduled == 1 ? true : false);
	}
	
	public int getId(){
		return idg;
	}
	
	public String getName(){
		return name;
	}
	
	public List<Acti> getActivities(){
		return activities;
	}
	
	public int getBizId(){
		return bizid;
	}
	
	public String getBizName(){
		return bizname;
	}
}
