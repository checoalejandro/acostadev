package helper.json;

public class Zone {

	double lat;
	double lng;
	int id;
	String name;
	String type;
	
	public Zone(double lat,double lng, int id, String name, String type){
		this.lat = lat;
		this.lng = lng;
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public double getLatitude(){
		return lat;
	}
	
	public double getLongitude(){
		return lng;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean isDefault(){
		return (type.equals("default")) ? true : false;
	}
	
	public boolean isUser(){
		return (type.equals("user")) ? true : false;
	}
	
}
