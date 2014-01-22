package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.Profile;
import helper.json.QuestionActivities;
import helper.json.UserAnswer;
import helper.threads.GetActivity;
import helper.threads.GetBizInfo;
import helper.threads.SendAnswers;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ResultsActivity extends Activity {
	
	int corrects = 0;
	int incorrects = 0;
	int total;
	ArrayList<UserAnswer> answers;
	ArrayList<QuestionActivities> questions;
	ArrayList<UserAnswer> ans = new ArrayList<UserAnswer>();
	TextView tv;
	
	volatile public ProgressDialog pd;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	public ResultsActivity(){
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		
		Gson gson = new Gson();
		UserAnswer[] usrans = new UserAnswer[Results.useranswers.size()];
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
		
		for(int i = 0; i < Results.useranswers.size(); i++){
			usrans[i] = Results.useranswers.get(i);
		}
		
		Profile profile = new Profile( Integer.parseInt(cursor.getString(1).toString()),GetActivity.act.getActivity().getId());
		sendAnswers(gson.toJson(usrans), gson.toJson(profile));
		
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void endActivity(){
		this.finish();
	}
	
	public void sendAnswers(String answers,String profile){
		pd = ProgressDialog.show( this, "Enviando respuestas...", "Espere, por favor", true, false);
		try{
			String data = "&answers=" + answers +
					"&profile=" + profile;
			
			SendAnswers sendanswers = new SendAnswers(this, pd );
			sendanswers.execute(new String[] {data,"saveAndroidAns"});
			
//			openBizne();
			
		}catch(Exception e){
			Log.d("SocialGo", "ResultsActivity-sendAnswers: Error: "+e.toString());
			pd.cancel();
  			e.printStackTrace();
  			//finish();
		}
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
		return true;
	}
	
//	public void checkAnswers(){
//		this.answers = Results.useranswers;
//		this.questions = GCMConnection.act.getData().getQuestions();
//		total = questions.size();
//		
//		for(int i = 0; i < total; i++){
//			ans = Results.getAnswersByQuestion(questions.get(i).idquestion);
//			for(int j = 0; j < questions.get(i).getAnswers().size(); j ++){
//				if(questions.get(i).getAnswers().get(j).isCorrect()){
//					if(ans.get(j).value){
//						corrects++;
//					}else{
//						incorrects++;
//					}
//				}else{
//					if(ans.get(j).value){
//						incorrects++;
//					}
//				}
//			}
//		}
//	}

}
