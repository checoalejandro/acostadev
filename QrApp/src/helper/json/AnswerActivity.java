package helper.json;

public class AnswerActivity {
	int status;
	String msg;
	Data data;
	Act activity;
	int bizid;
	
	public AnswerActivity(){
		
	}
	
	public Data getData(){
		return this.data;
	}
	
	public String toString(){
		return "Status: " + this.status + "\nMsg: "+ this.msg + "\nName: " + this.activity.NAME + "\nID: " + this.activity.ID + "\nType: " + this.activity.TYPE + "\nData: " + this.data.toString(activity.TYPE);
	}
	
	public Act getActivity(){
		return this.activity;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public String getMsg(){
		return this.msg;
	}
	
	public int getBizId(){
		return this.bizid;
	}
	
}
