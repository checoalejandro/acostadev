package helper.json;

public class BizneInfo {
	String name;
	String address1;
	String email;
	String cat_original;
	String reg_original;
	String telephone;
	int catid;
	int bizid;
	int likes;
	double lng;
	double lat;
	String time;
	
	public String getTime(){
		return time;
	}
	
	public BizneInfo(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getAddress(){
		return this.address1;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getCategory(){
		return this.cat_original;
	}
	
	public String getRegion(){
		return this.reg_original;
	}
	
	public String getTelephone(){
		return this.telephone;
	}
	
	public int getBizId(){
		return this.bizid;
	}
	
	public double getLongitude(){
		return this.lng;
	}
	
	public double getLatitude(){
		return this.lat;
	}
	
	public int getCatId(){
		return this.catid;
	}
	
	public int getLikes(){
		return likes;
	}
}

