package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.Act;
import helper.threads.GetActivity;
import helper.threads.GetBizInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Message extends Activity {

	Act act;
	String message;
	Button close;
	volatile public ProgressDialog pd;
	
	public Message(){
		this.act = GetActivity.act.getActivity();
		this.message = GetActivity.act.getData().getMessage();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		setElements();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		
		return true;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void setElements(){
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
    	
		TextView tv = (TextView) findViewById(R.id.message_activity);
		tv.setText(act.getName());
		tv = (TextView) findViewById(R.id.message_message);
		tv.setText(message);
		
		close = (Button) findViewById(R.id.btn_close);
		close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openBizne(true);
			}
		});
		
		tv = (TextView)findViewById(R.id.message_bizne);
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


	
	public void closeActivity(){
		this.finish();
		
	}

}
