package helper.json;

public class Acti {

	int idactivity;
	String name;
	int type;
	int points;
	int isoffer;
	int hasprize;
	
	public int getId(){
		return idactivity;
	}
	
	public String getName(){
		return name;
	}
	
	public int getType(){
		return type;
	}
	
	public int getPoint(){
		return points;
	}
	
	public boolean isOffer(){
		return (isoffer == 1);
	}
	
	public boolean hasPrize(){
		return (hasprize == 1);
	}
	
}
