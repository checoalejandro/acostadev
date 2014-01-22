package helper.json;

import java.util.List;

public class Taxi {

	int taxiid;
	String name;
	String user_address;
	String neighborhood;
	int registered;
	double lng;
	double lat;
	int sdistance;
	List<Phone> telephones;
	
	public int getId(){
		return taxiid;
	}
	
	public String getName(){
		return name;
	}
	
	public String getAddress(){
		return user_address;
	}
	
	public String getNeihgborhood(){
		return neighborhood;
	}
	
	public boolean isRegistered(){
		return (registered == 1) ? true : false;
	}
	
	public double getLongitude(){
		return lng;
	}
	
	public double getLatitude(){
		return lat;
	}
	
	public int getDistance(){
		return sdistance;
	}
	
	public List<Phone> getPhones(){
		return telephones;
	}
}
