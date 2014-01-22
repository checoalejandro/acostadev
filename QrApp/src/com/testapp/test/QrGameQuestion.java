package com.testapp.test;

import helper.json.QuestionQrGame;
import helper.tools.CheckBoxList;
import helper.tools.RadioList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QrGameQuestion extends Activity{
	
	public static QuestionQrGame questionQrGame;
	private TextView lbl_question;
	private CheckBoxList answers_check = new CheckBoxList();
	private RadioList answers_radio = new RadioList();
	@SuppressWarnings("unused")
	private int noAns;
	private Button btn_next;
	
	public QrGameQuestion(){
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_question);
       
    }
    
    public void switchActivity(){
    	Intent launchGame = new Intent(this, PostQuestion.class);
    	// Sends 2 cuz it will only show Locations and not if it's right or wrong
    	launchGame.putExtra("result",2);
        startActivity(launchGame);
    }
    
    @SuppressWarnings("static-access")
	public void nextQuestion(){
    	int flag = 0;
    	switch (this.questionQrGame.getType()){
    	case 0: 
    		if(!this.answers_radio.isAnswered()){
    			Context context = getApplicationContext();
    	    	CharSequence text = "Selecciona una respuesta";
    	    	int duration = Toast.LENGTH_SHORT;
    	
    	    	Toast toast = Toast.makeText(context, text, duration);
    	    	toast.show();
    		}else{
    			for(int i = 0; i<this.questionQrGame.getAnsNumber(); i++){
		    		if(answers_radio.get(i).isSelected()){
		    			if(questionQrGame.getAnswerByIndex(i).isCorrect()){
		    				flag = 1;
		    			}else{
		    				flag = 0;
		    				break;
		    			} 
		    		}
    			}
    			this.questionQrGame.discovered = 1;
	    		Intent launchGame = new Intent(this, PostQuestion.class);
	    		launchGame.putExtra("result",flag);
	            startActivity(launchGame);
	    		break;
    		}
	    	
    	break;
    	case 1:
    		if(!this.answers_check.isAnswered()){
    			Context context = getApplicationContext();
    	    	CharSequence text = "Selecciona una respuesta";
    	    	int duration = Toast.LENGTH_SHORT;
    	
    	    	Toast toast = Toast.makeText(context, text, duration);
    	    	toast.show();
    		}else{
    			for(int i = 0; i<this.questionQrGame.getAnsNumber(); i++){
		    		if(answers_check.get(i).isChecked()){
		    			if(questionQrGame.getAnswerByIndex(i).isCorrect()){
		    				flag = 1;
		    			}else{
		    				flag = 0;
		    				break;
		    			} 
		    		}if(!answers_check.get(i).isChecked()){
		    			if(questionQrGame.getAnswerByIndex(i).isCorrect()){
		    				flag = 0;
		    			}
		    		}
		    	}
    			this.questionQrGame.discovered = 1;
	    		Intent launchGame = new Intent(this, PostQuestion.class);
	    		launchGame.putExtra("result",flag);
	            startActivity(launchGame);
	    		break;
    		}
    		
    	}
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @SuppressWarnings("static-access")
	public void setQuestion(){
    	
    	lbl_question = (TextView)findViewById(R.id.question_text);
        lbl_question.setText(questionQrGame.getQuestion());
        
        btn_next = (Button)findViewById(R.id.id_btn_test);
        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                nextQuestion();
            }
        });
    	
    	switch(this.questionQrGame.getType()){
    	case 0:for(int i = 0; i < this.questionQrGame.getAnsNumber(); i++){
	    		int id = 0;
				try {
					id = R.id.class.getField("radio" + i).getInt(0);
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
	    		RadioButton radio = (RadioButton)findViewById(id);
	    		radio.setText(this.questionQrGame.getAnswerByIndex(i).getAnswerText());
	    		radio.setVisibility(RadioButton.VISIBLE);
	    		LinearLayout layout= (LinearLayout) findViewById(R.id.answers_check_layout);
	    		layout.setVisibility(LinearLayout.VISIBLE);
	    		this.answers_radio.add(radio);
	    		
	    	}
    		break;
	    case 1:for(int i = 0; i < this.questionQrGame.getAnsNumber(); i++){
	    		int id = 0;
				try {
					id = R.id.class.getField("checkBox" + i).getInt(0);
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
	    		CheckBox chkbox = (CheckBox)findViewById(id);
	    		chkbox.setText(this.questionQrGame.getAnswerByIndex(i).getAnswerText());
	    		chkbox.setVisibility(CheckBox.VISIBLE);
	    		LinearLayout layout= (LinearLayout) findViewById(R.id.answers_option_layout);
	    		layout.setVisibility(LinearLayout.VISIBLE);
	    		this.answers_check.add(chkbox);
	    	}
	    	break;
    	}
    	
    }
    
    public void reset_all(){
    	int i;
    	for(i=0;i<this.answers_check.size();i++){
    		this.answers_check.get(i).setVisibility(CheckBox.GONE);
    	}
    	for(i=0;i<this.answers_radio.size();i++){
    		this.answers_radio.get(i).setVisibility(CheckBox.GONE);
    	}
    	
    	this.answers_check.clear();
    	this.answers_radio.clear();
    	
    }

}
