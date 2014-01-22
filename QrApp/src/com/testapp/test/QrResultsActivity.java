package com.testapp.test;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class QrResultsActivity extends Activity {
	public static String results;
	public static String locations;
	public static String prize;
	TextView lbl_results;
	TextView lbl_locations;
	TextView lbl_prize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_results);
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
    	
    	ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
    	btn_back.setVisibility(ImageButton.VISIBLE);
    	btn_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				endActivity();
			}
		});

		lbl_locations = (TextView) findViewById(R.id.qr_locations);
		lbl_locations.setText(QrResultsActivity.locations);
		
		lbl_prize = (TextView) findViewById(R.id.qr_prize);
		lbl_prize.setText(QrResultsActivity.prize);
		
		Button btn_again = (Button) findViewById(R.id.btn_again);
		btn_again.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				endActivity();
			}
		});
	}
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
		

	public void endActivity(){
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qr_results, menu);
		return true;
	}


}
