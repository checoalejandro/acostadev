package helper.json;

public class Act {
	int ID;
	int TYPE;
	String NAME;
	int POINTS;
	int ALERT;
	int HASPRIZE;
	
	public int getId(){
		return this.ID;
	}
	
	public int getType(){
		return this.TYPE;
	}
	
	public String getName(){
		return this.NAME;
	}
	
	public int getPoints(){
		return POINTS;
	}
	
	public String toString(){
		String t = "";
		switch (this.TYPE){
		case 0: t = "Survey"; break;
		case 1: t = "Trivia"; break;
		case 2: t = "Comment"; break;
		case 3: t = "Message"; break;
		case 4: t = "Link"; break;
		case 5: t = "Video"; break;
		}
		return "\nName: " + this.NAME + "\nType: " + t;
	}
	
	public boolean hasPrizes(){
		return (HASPRIZE == 1);
	}
}
