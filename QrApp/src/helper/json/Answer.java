package helper.json;

public class Answer {
	String answer;
	int iscorrect;
	int idanswer;
	
	public Answer(){
		this.answer = "Answer";
		this.iscorrect = 1;
		this.idanswer = 12;
	}
	
	public String getAnswerText(){
		return this.answer;
	}
	
	public boolean isCorrect(){
		return (this.iscorrect == 1) ? true : false;
	}
	
	public void setIsCorrect(int i){
		this.iscorrect = i;
	}
	
	public int getIdAnswer(){
		return this.idanswer;
	}
}
