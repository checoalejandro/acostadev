package helper.json;

public class Route {

	int id;
	String name;
	String type;
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean isUser(){
		return (type.equals("user")) ? true : false;
	}
	
	public boolean isDefault(){
		return (type.equals("default")) ? true : false;
	}
}
