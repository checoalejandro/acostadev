package helper.json;

import java.util.List;

public class Parking {
	int parkingid;
	String name;
	String address;
	String neighborhood;
	String rate;
	double lng;
	double lat;
	int sdistance;
	Detailsstatus detailsstatus;
	List<Phone> phones;
	
	public int getParkingId(){
		return this.parkingid;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getAddress(){
		return this.address;
	}
	
	public String getNeighborhood(){
		return this.neighborhood;
	}
	
	public String getRate(){
		return this.rate;
	}
	
	public double getLongitude(){
		return this.lng;
	}
	
	public double getLatitude(){
		return this.lat;
	}
	
	public int getSDistance(){
		return sdistance;
	}
	
	public Detailsstatus getDetailsStatus(){
		return this.detailsstatus;
	}
	
	public List<Phone> getPhones(){
		return phones;
	}

}
