package helper.json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Promo {
	int idpromo;
	String description;
	String daytostart;
	String daytofinish;
	int points;
	int bizid;
	
	public int getId(){
		return this.idpromo;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public Date getDaytostart() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(this.daytostart);
		date = format.parse(this.daytostart);
		return date;
	}
	
	public Date getDaytofinish() throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(this.daytofinish);
		date = format.parse(this.daytofinish);
		return date;
	}
	
	public int getPoints(){
		return this.points;
	}
	
	public int getBizId(){
		return this.bizid;
	}
}
