package helper.json;

public class QuestionActivities {
	public int type;
	public String question;
	public String instructions;
	public int idquestion;
	public AnswersList answers;
	
	public int getType(){
		return this.type;
	}
	
	public String getQuestion(){
		return this.question;
	}
	
	public String getInstructions(){
		return this.instructions;
	}
	
	public int getIdQuestion(){
		return this.idquestion;
	}
	
	public AnswersList getAnswers(){
		return this.answers;
	}
}
