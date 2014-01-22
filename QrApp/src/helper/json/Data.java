package helper.json;

public class Data {
	QuestionsActivitiesList questions;
	String link;
	String message;
	String information;
	Game GAME;
	
	public Game getGame(){
		return this.GAME;
	}
	
	public String getLink(){
		return this.link;
	}
	
	public String getMessage(){
		return this.message;
	}
	
	public String getInformation(){
		return this.information;
	}
	
	public QuestionsActivitiesList getQuestions(){
		return this.questions;
	}
	
	public void addQuestion(QuestionActivities q){
		this.questions.add(q);
	}
	
	public String toString(int type){
		String questions = "";
		switch (type){
		case 0: 
			for(int i = 0; i < this.questions.size(); i++){
				questions = questions + "\n" + this.questions.get(i).question;
			}
			return questions; 
		case 1: 
			for(int i = 0; i < this.questions.size(); i++){
				questions = questions + "\n" + this.questions.get(i).question;
			}
			return questions; 
		case 2: return this.information; 
		case 3: return this.message; 
		case 4: return this.link; 
		case 5: return this.link;
		case 6: for(int i = 0; i < this.GAME.getQuestions().size(); i++){
			questions = questions + "\n" + this.GAME.getQuestions().get(i).getIdQuestion() + ". " + this.GAME.getQuestions().get(i).getQuestion() + " Loc: " + this.GAME.getQuestions().get(i).getLocation();
		}
		return questions; 
		default: return "error";
		}
	}
}
