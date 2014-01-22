package helper.tools;

public class MyZone{
	double latitude;
	double longitude;
	
	public MyZone(double latitude, double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public boolean isSet(){
		if(latitude != 0 && longitude != 0){
			return true;
		}
		return false;
	}
}