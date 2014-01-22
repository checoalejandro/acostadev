package helper.json;


public class QuestionQrGame {
	public int type;
	public String question;
	public String location;
	public int idq;
	public int qrid;
	public int discovered;
	public AnswersList answers;
	
	public QuestionQrGame(){

	}
	
	public void setAnswer(Answer a){
		this.answers.add(a);
	}
	
	public int getAnsNumber(){
		return this.answers.size();
	}
	
	public void removeAnswer(Answer a){
		this.answers.remove(a);
	}
	
	public helper.json.Answer getAnswerByIndex(int index){
		return this.answers.get(index);
	}
	
	public int getIdQuestion(){
		return this.idq;
	}
	
	public boolean isDiscovered(){
		return (this.discovered == 1) ? true : false;
	}
	
	public int getQrId(){
		return this.qrid;
	}
	
	public int getType(){
		return this.type;
	}
	
	public String getQuestion(){
		return this.question;
	}
	
	public String getLocation(){
		return this.location;
	}
	
	public AnswersList getObjectAnswers(){
		return this.answers;
	}
}
