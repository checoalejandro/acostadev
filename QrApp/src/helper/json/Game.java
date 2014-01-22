package helper.json;


public class Game {
	QuestionsList questions;
	int IDGAME;
	int CURRENTQRS;
	int TOTALQRS;
	int QRID;
	
	public Game(){
		
	}
	
	public QuestionsList getQuestions(){
		return this.questions;
	}
	
	public int getIdGame(){
		return this.IDGAME;
	}
	
	public int getCurrentQrs(){
		return this.CURRENTQRS;
	}
	
	public int getTotalQrs(){
		return this.TOTALQRS;
	}
	
}
