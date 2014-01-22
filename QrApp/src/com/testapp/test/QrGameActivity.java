package com.testapp.test;

import com.google.gson.Gson;

import helper.database.DBAdapter;
import helper.json.Game;
import helper.json.QuestionQrGame;
import helper.threads.GetActivity;
import helper.threads.GetBizInfo;
import helper.threads.GetPrize;
import helper.threads.SendLog;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

public class QrGameActivity extends Activity {

	Game game;
	Button send;
	QuestionQrGame question;
	int bizid;
	volatile public ProgressDialog pd;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	public QrGameActivity(){
		this.game = GetActivity.act.getData().getGame();
		for(int i = 0; i < GetActivity.act.getData().getGame().getTotalQrs(); i++){
			if(this.game.getQuestions().get(i).qrid == MainActivity.answer.getQrId()){
				question = this.game.getQuestions().get(i);
			}
		}
		this.bizid = GetActivity.act.getBizId();
	}
	
	public boolean isAllAnswered (){
		for(int i = 0; i < GetActivity.act.getData().getGame().getTotalQrs(); i++){
			if(!this.game.getQuestions().get(i).isDiscovered()){
				return false;
			}
		}
		return true;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_game);
        TextView tv;
        setElements();
        tv = (TextView) findViewById(R.id.lbl_qrgame_ques);
	    tv.setText(question.getQuestion());
	    send = (Button) findViewById(R.id.qrgame_btn_next);
	    send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendAnswer();
			}
		});
	    
		tv = (TextView) findViewById(R.id.qrgame_bizinfo);
		tv.setText(GetBizInfo.act.getBizneInfo().getName());
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openBizne2();
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qr_game, menu);
        return true;
    }
    
    public boolean checkGame(){
    	int total = this.game.getQuestions().size();
    	int current = 0;
    	for(int i = 0; i < total; i++){
    		if(this.game.getQuestions().get(i).isDiscovered()){
    			current++;
    		}
    	}
    	
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
    	if(current == total){
    		
    		String data = "&userid=" + cursor.getString(1).toString() + "&idgame=" + this.game.getIdGame();
    		
    		GetPrize getprize = new GetPrize(this,pd);
    		getprize.execute(new String[] {data,"loadScore"});
    		return true;
    	}else{
    		
    		return false;
    	}
    	
    }
    
    public void continueCheck(){
    	
    }
    
    public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
    
    public void openBizne2(){
    		DBAdapter database = new DBAdapter(this);
    		Cursor cursor = null;
    		
    		database.open();
            cursor = database.getAllUsers();
            database.close();
            
    		ProgressDialog pd;
			pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
			
			String data = "&bizid=" + GetActivity.act.getBizId() +
					"&userid=" + cursor.getString(1).toString();
			
			GetBizInfo getinfo = new GetBizInfo(this, pd);
			getinfo.execute(new String[] {data,"getBizneInfo"});
    }
	
	public void setElements(){
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
    	if(isAllAnswered()){
    		Toast.makeText(this, "Ya haz contestado este juego" , Toast.LENGTH_SHORT).show();
			this.finish();
    	}
    	if(question.isDiscovered() && !isAllAnswered()){
    		Toast.makeText(this, "Ya haz contestado esta pregunta" , Toast.LENGTH_SHORT).show();
			this.finish();
		}
    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = inflater.inflate(R.layout.activity_qr_game, null);

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
    
    public void sendAnswer(){
    	QrResultsActivity.results = "";
    	String results = "Respuesta(s) Correcta(s):\n\n";
    	
    	for(int i = 0; i < question.answers.size(); i++){
    		if(question.answers.get(i).isCorrect()){
    			results = results + question.answers.get(i).getAnswerText() + "\n";
    		}
    	}
//    	switch(question.type){
//		case 1:
//			RadioGroup rg = (RadioGroup)findViewById(R.id.radio_group);
//			for(int i = 0; i < rg.getChildCount(); i++){
//				RadioButton rb;
//				rb = (RadioButton) rg.getChildAt(i);
//				
//				if(question.answers.get(i).isCorrect()){
//					Results.total++;
//					if(rb.isChecked()){
//						results = results + question.answers.get(i).getAnswerText() + " - Correcta\n";
//					}else{
//						results = results + question.answers.get(i).getAnswerText() + "\n";
//					}
//				}
//				// For stats...
//				if(rb.isChecked()){
//					if(!question.answers.get(i).isCorrect()){
//						results = results + question.answers.get(i).getAnswerText() + " - Incorrecta\n";
//					}
//				}else{
//					
//				}
//			}
//			break;
//				
//		case 0:
//			LinearLayout cbg = (LinearLayout) findViewById(R.id.check_group);
//			for(int i = 0; i < cbg.getChildCount(); i++){
//				CheckBox cb;
//				cb = (CheckBox) cbg.getChildAt(i);
//				
//				if(question.answers.get(i).isCorrect()){
//					Results.total++;
//					if(cb.isChecked()){
//						results = results + "\n" + question.answers.get(i).getAnswerText() + " - Correcta";
//					}else{
//						results = results + "\n" + question.answers.get(i).getAnswerText() + " - Incorrecta";
//					}
//				}
//				if(cb.isChecked()){
//					if(!question.answers.get(i).isCorrect()){
//						results = results + "\n" + question.answers.get(i).getAnswerText() + " - Incorrecta";
//					}
//				}else{
//					
//				}
//			}
//			break;
//		}
    	QrResultsActivity.prize = "Aún no haz terminado el juego";
    	QrResultsActivity.results = results;
    	results = "";
    	for(int i = 0; i < GetActivity.act.getData().getGame().getTotalQrs(); i++){
    		if(this.game.getQuestions().get(i).qrid == MainActivity.answer.getQrId()){
				this.game.getQuestions().get(i).discovered = 1;
			}
			if(this.game.getQuestions().get(i).discovered == 0){
				results = results + "QR " + (i+1) + ": " + this.game.getQuestions().get(i).getLocation() + "\n";
			}else{
				results = results + "QR " + (i+1) + ": Descubierto\n";
			}
    		this.game.getQuestions().get(i).question = "";
    		this.game.getQuestions().get(i).location = "";
    		this.game.getQuestions().get(i).answers.clear();
			
		}
    	int total = this.game.getQuestions().size();
    	int current = 0;
    	for(int j = 0; j < total; j++){
    		if(this.game.getQuestions().get(j).isDiscovered()){
    			current++;
    		}
    	}
    	final String results2 = results; 
    	if(current == total){
			final Dialog dialog = new Dialog(this);
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.dialog_ok);

			// set the custom dialog components - text, image and button
			TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
			tv.setText(QrResultsActivity.results + "\n¡Felicidades! Haz completado este juego.");

			Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButtonOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					continueMethod(results2);
				}
			});
			
			dialog.show();
    		
    	}else{
    		final Dialog dialog = new Dialog(this);
    		dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.dialog_ok);
			dialog.setTitle("Resultados");

			// set the custom dialog components - text, image and button
			TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
			tv.setText(QrResultsActivity.results);

			Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
			// if button is clicked, close the custom dialog
			dialogButtonOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					continueMethod(results2);
				}
			});
			
			dialog.show();
    	}
    	
    	
    	
    }
    
    public void continueMethod(String results){
    	this.checkGame();
    	
    	QrResultsActivity.locations = results;
    	database.open();
        cursor = database.getAllUsers();
        database.close();
        
        
    	Gson gson = new Gson();
    	
    	String json = gson.toJson(this.game.getQuestions());
    	
    	String data = "&game=" + json + "&userid=" + cursor.getString(1).toString() + "&idgame=" + this.game.getIdGame();
    	
    	SendLog sendlog = new SendLog(this,null,this.bizid);
    	sendlog.execute(new String[] {data,"changeLog"});
    }
    
	public void openBizne(){
		
		DBAdapter database = new DBAdapter(this);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		final int bizid = this.bizid;
		ProgressDialog pd;
 				pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
 				
 				String data = "&bizid=" + bizid +
 						"&userid=" + cursor.getString(1).toString();
 				
 				GetBizInfo getinfo = new GetBizInfo(this, pd);
				getinfo.execute(new String[] {data,"getBizneInfo"});
	}
    
}
