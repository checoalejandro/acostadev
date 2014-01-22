package helper.json;

public class ChatMessage {

	String message;
	String time;
	int userid;
	int bizid;
	String name;
	int type;
	
	public ChatMessage(String message){
		this.message = message;
		this.time = "tiempo";
		this.type = 1;
		this.name = "Negocio";
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getTime(){
		return time;
	}
	
	public int getUserId(){
		return userid;
	}
	
	public int getBizId(){
		return bizid;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isUser(){
		return (type == 0);
	}
}
