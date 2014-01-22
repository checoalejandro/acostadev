package com.testapp.test;

import helper.database.DBAdapter;
import helper.threads.GetProfile;
import helper.threads.GetRegions;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProfileActivity extends Activity {
	
	public ProgressDialog dialog;
	public LinearLayout ll;
	DBAdapter database = new DBAdapter(this);
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// Set Title
		TextView title = (TextView)findViewById(R.id.lbl_name_section);
		title.setText("Tu Perfil");
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gohome();
				}
			});
	   	btn_home = (ImageButton) findViewById(R.id.btn_home);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
    	
    	Button btn_changecity = (Button) findViewById(R.id.btn_business_change_city);
		btn_changecity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeRegion();
			}
			
		});
		database.open();
		cursor = database.getCity();
		database.close();
		btn_changecity.setText("Cambiar ciudad: " + cursor.getString(1));
		
		ll = (LinearLayout)findViewById(R.id.llprofile);
		ll.setVisibility(View.GONE);
		
		dialog = ProgressDialog.show(this, "Cargando...","Cargando perfil", true);
		
		String data = "&userid=" + MainActivity.userid;
		
		GetProfile profile = new GetProfile(this, dialog);
		profile.execute(new String[] {data,"getProfile"});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	
	private void changeRegion(){
//		ProgressDialog pd;
		DBAdapter database = new DBAdapter(this);
		database.open();
        Cursor cursor = database.getAllUsers();
        database.close();
		
		String data = "&userid=" + cursor.getString(1).toString();
		GetRegions getregions = new GetRegions(this, null,true);
    	getregions.execute(new String[] {data,"getRegions"});
	}
    
}
