package helper.json;

public class AnswerGame{

	int status;
	String msg;
	Game data;
	
	public AnswerGame(){
		
	}
	
	public Game returnData(){
		return this.data;
	}
	
	public void setGame(Game g){
		this.data = g;
	}
}
