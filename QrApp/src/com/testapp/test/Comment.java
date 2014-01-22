package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.Act;
import helper.threads.GetActivity;
import helper.threads.GetBizInfo;
import helper.threads.SendComment;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Comment extends Activity {

	Act act;
	String comment;
	Button send;
	volatile public ProgressDialog pd;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	public Comment(){
		this.act = GetActivity.act.getActivity();
		this.comment = GetActivity.act.getData().getInformation();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		setElements();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		
		return true;
	}
	
	private void send(){
		// Get the answer...
		EditText edittext = (EditText) findViewById(R.id.txt_comment);
		String text_comment = edittext.getText().toString();
		if(text_comment.length() == 0){
			Toast.makeText(this, "Por favor llena el campo de comentarios" , Toast.LENGTH_SHORT).show();
			return;
		}else{
			if(text_comment.length() > 255){
				Toast.makeText(this, "Límite de campo, por favor escribe un mensaje más corto" , Toast.LENGTH_SHORT).show();
				return;
			}
		}
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
        
		String data = "&comment=" + text_comment + "&idactivity=" + act.getId() + "&userid=" + cursor.getString(1).toString();
		pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		try{
			
			SendComment sendcomment = new SendComment(this,pd,act.getId());
			sendcomment.execute(new String[] {data,"saveAndroidComment"});
			
			
		}catch(Exception e){
			Log.d("SocialGo", "Comment-sendComment: Error: "+e.toString());
			pd.cancel();
  			e.printStackTrace();
		}
		
		
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void finishActivity(){
		this.finish();
	}
	
	public void setElements(){
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishActivity();
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
    	
		TextView activity = (TextView) findViewById(R.id.commment_activity);
		TextView instructions = (TextView) findViewById(R.id.comment_instructions);
		activity.setText(act.getName());
		instructions.setText(comment);
	
		send = (Button) findViewById(R.id.btn_send);
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				send();
			}
		});
		TextView tv = (TextView)findViewById(R.id.comment_bizne);
	    tv.setText("Información del negocio");
	    tv.setVisibility(View.GONE);
	    tv = (TextView)findViewById(R.id.lbl_name_section);
	    tv.setOnClickListener(new OnClickListener() {
	 	      @Override
	 	      public void onClick(View v) {
	 	    	  	openBizne(false);
	 	      }
	 	});
	    
	    tv = (TextView) findViewById(R.id.lbl_name_section);
	    tv.setText(MainActivity.lastbiz.getName());
	}
		
	public void openBizne(boolean close){
		
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
 				
 				GetBizInfo getinfo = new GetBizInfo(this, pd,close,false);
				getinfo.execute(new String[] {data,"getBizneInfo"});
	}
	
}
