package helper.json;

import helper.threads.GetActivity;

public class UserAnswer {
	int idquestion;
	int idanswer;
	int idactivity;
	int iduser;
	
	public UserAnswer(int idq, int ida, int iduser){
		this.idanswer = ida;
		this.idquestion = idq;
		this.idactivity = GetActivity.act.getActivity().getId();
		this.iduser = iduser;
	}
	
	public int getIdQuestion(){
		return this.idquestion;
	}
	
	public int getIdAnswer(){
		return this.idanswer;
	}
	
	public String toString(){
		return "IDQ: " + this.idquestion + " IDA: " + this.idanswer;
	}
}
