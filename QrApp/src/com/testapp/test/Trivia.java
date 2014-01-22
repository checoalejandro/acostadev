package com.testapp.test;

import java.util.ArrayList;

import com.google.gson.Gson;

import helper.database.DBAdapter;
import helper.json.Act;
import helper.json.Profile;
import helper.json.QuestionActivities;
import helper.json.QuestionsActivitiesList;
import helper.json.UserAnswer;
import helper.threads.GetActivity;
import helper.threads.GetBizInfo;
import helper.threads.SendAnswers;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Trivia extends Activity {

	Act act;
	QuestionsActivitiesList trivia;
	private int question_index;
	private QuestionActivities question;
	@SuppressWarnings("unused")
	private ArrayList<UserAnswer> useranswers = new ArrayList<UserAnswer>();
	@SuppressWarnings("unused")
	private int answer_index = 0;
	Button next;
	
	volatile public ProgressDialog pd;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	
	public Trivia(){
		Results.clearResults();
		this.act = GetActivity.act.getActivity();
		this.trivia = GetActivity.act.getData().getQuestions();
		this.question_index = 0;
		this.question = this.trivia.get(this.question_index);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trivia);
		setElements();
		next = (Button)findViewById(R.id.trivia_btn);
	    next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextQuestion();
			}
		});
	    TextView tv = (TextView)findViewById(R.id.trivia_question);
	    tv.setText(question.getQuestion());
	    
	    tv = (TextView)findViewById(R.id.trivia_title);
	    tv.setText(act.getName());
	    
	    tv = (TextView)findViewById(R.id.lbl_trivia_bizname);
	    tv.setText("Información del negocio");
	    tv.setVisibility(View.GONE);
	    tv = (TextView)findViewById(R.id.lbl_name_section);
	    tv.setOnClickListener(new OnClickListener() {
	 	      @Override
	 	      public void onClick(View v) {
	 	    	  	openBizne();
	 	      }
	 	    });
	    
	    tv = (TextView)findViewById(R.id.lbl_name_section);
	    tv.setText(MainActivity.lastbiz.getName());
	    
 	    
 	    
 	   ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	   	btn_home = (ImageButton) findViewById(R.id.btn_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gohome();
				}
			});
	}
	
	public void openBizne(){
		
		DBAdapter database = new DBAdapter(this);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		final int bizid = MainActivity.answer.getBizId();
		ProgressDialog pd;
 				pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
 				
 				String data = "&bizid=" + bizid +
 						"&userid=" + cursor.getString(1).toString();
 				
 				GetBizInfo getinfo = new GetBizInfo(this, pd);
				getinfo.execute(new String[] {data,"getBizneInfo"});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trivia, menu);
		
		return true;
	}
	
	private void nextQuestion(){
		if(!validateAnswers()){
			Toast.makeText(this, "Por favor selecciona por lo menos una respuesta" , Toast.LENGTH_SHORT).show();
			return;
		}
		// Get the answers...
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
		
		switch(question.type){
		case 1:
			RadioGroup rg = (RadioGroup)findViewById(R.id.radio_group);
			for(int i = 0; i < rg.getChildCount(); i++){
				RadioButton rb;
				rb = (RadioButton) rg.getChildAt(i);
				
				if(question.getAnswers().get(i).isCorrect()){
					Results.total++;
					if(rb.isChecked()){
						Results.corrects++;
					}else{
						Results.incorrects++;
					}
				}
				// For stats...
				if(rb.isChecked()){
					Results.useranswers.add(new UserAnswer(question.getIdQuestion(),question.answers.get(i).getIdAnswer(),Integer.parseInt(cursor.getString(1).toString())));
					if(!question.getAnswers().get(i).isCorrect()){
						Results.incorrects++;
					}
				}else{
					
				}
			}
			break;
				
		case 0:
			LinearLayout cbg = (LinearLayout) findViewById(R.id.check_group);
			for(int i = 0; i < cbg.getChildCount(); i++){
				CheckBox cb;
				cb = (CheckBox) cbg.getChildAt(i);
				
				if(question.getAnswers().get(i).isCorrect()){
					Results.total++;
					if(cb.isChecked()){
						Results.corrects++;
					}else{
						Results.incorrects++;
					}
				}
				if(cb.isChecked()){
					Results.useranswers.add(new UserAnswer(question.getIdQuestion(),question.answers.get(i).getIdAnswer(),Integer.parseInt(cursor.getString(1).toString())));
					if(!question.getAnswers().get(i).isCorrect()){
						Results.incorrects++;
					}
				}else{
					
				}
			}
			break;
		}
		
		// Change the question
		question_index++;
		if(question_index < trivia.size()){
			LinearLayout ll = new LinearLayout(this);
			ll.removeAllViews();
			question = trivia.get(question_index);
			setElements();
			next = (Button)findViewById(R.id.trivia_btn);
		    next.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					nextQuestion();
				}
			});
		    TextView tv = (TextView)findViewById(R.id.trivia_question);
		    tv.setText(question.getQuestion());
		    
		    tv = (TextView)findViewById(R.id.trivia_title);
		    tv.setText(act.getName());
		    
		    tv = (TextView)findViewById(R.id.lbl_trivia_bizname);
		    tv.setText(MainActivity.lastbiz.getName());
		    
	 	    tv.setOnClickListener(new OnClickListener() {
	 	 	      @Override
	 	 	      public void onClick(View v) {
	 	 	    	  	openBizne();
	 	 	      }
	 	 	    });
	 	   ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
		   	btn_home.setVisibility(ImageButton.VISIBLE);
		   	btn_home.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						gohome();
					}
				});
		}else{
			
			String result = "";
			if(GetActivity.act.getActivity().getType() == 1)
				result = "Obtuviste " + Results.corrects + " respuestas correctas de " + Results.total;
			else
				result = "Gracias por contestar la encuesta.";

			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_ok);
			dialog.setTitle("Resultados");

			// set the custom dialog components - text, image and button
			TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
			tv.setText(result);

			Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButtonOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					finishActivity();
				}
			});
			
			dialog.show();
			
		}
	}
	
	public void finishActivity(){
		
//		this.finish();
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
	
	public boolean validateAnswers(){
		
		switch(question.getType()){
		case 1: RadioGroup rg = (RadioGroup) findViewById(R.id.radio_group);
				for(int i = 0; i < rg.getChildCount(); i++){
					RadioButton rb;
					rb = (RadioButton) rg.getChildAt(i);
					if(rb.isChecked()){
						return true;
					}
				}
				break;
		case 0: 
				LinearLayout cbg = (LinearLayout) findViewById(R.id.check_group);
				for(int i = 0; i < cbg.getChildCount(); i++){
					CheckBox cb;
					cb = (CheckBox) cbg.getChildAt(i);
					if(cb.isChecked()){
						return true;
					}
				}
				break;
		}
			
		return false;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	public void setElements(){
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = inflater.inflate(R.layout.activity_trivia, null);

	    // Find the ScrollView 
	    ScrollView sv = (ScrollView) v.findViewById(R.id.biz_scroll);

	    // Create a LinearLayout element
	    LinearLayout ll = new LinearLayout(this);
	    ll.setId(R.id.main_layout);
	    ll.setOrientation(LinearLayout.VERTICAL);
	    
	    switch(question.getType()){
	    case 1: 
	    	RadioGroup rbs = new RadioGroup(this);
	    	rbs.setId(R.id.radio_group);
	    	RadioButton rb;
	    	 for(int i = 0; i < question.answers.size(); i++){
	 	    	rb = new RadioButton(this);
	 	    	rb.setText(question.answers.get(i).getAnswerText());
	 	    	try {
	 				rb.setId(R.id.class.getField("trivia_answer" + i).getInt(0));
	 			} catch (IllegalArgumentException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (SecurityException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (IllegalAccessException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (NoSuchFieldException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 	    	rbs.addView(rb);
	 	    }
	    	 ll.addView(rbs);
	    	 break;
	    case 0: 
	    	CheckBox ckb;
	    	LinearLayout chks = new LinearLayout(this);
	    	chks.setId(R.id.check_group);
	    	chks.setOrientation(LinearLayout.VERTICAL);
	    	for(int i = 0; i < question.answers.size(); i++){
	 	    	ckb = new CheckBox(this);
	 	    	ckb.setText(question.answers.get(i).getAnswerText());
	 	    	try {
	 				ckb.setId(R.id.class.getField("trivia_answer" + i).getInt(0));
	 			} catch (IllegalArgumentException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (SecurityException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (IllegalAccessException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (NoSuchFieldException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 	    	chks.addView(ckb);
	 	    }
	    	ll.addView(chks);
	    	 break;
	    }

	    // Add the LinearLayout element to the ScrollView
	    sv.addView(ll);

	    // Display the view
	    setContentView(v);
	}
	
}
