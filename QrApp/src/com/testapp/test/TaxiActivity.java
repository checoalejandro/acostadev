package com.testapp.test;

import helper.json.Taxi;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class TaxiActivity extends Activity {

	Taxi taxi;
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_taxi);
		if(TaxisMapActivity.selected_taxi != null){
			setInfo();
		}else{
//			Toast.makeText(this, "", duration)
		}
		ImageButton back = (ImageButton) findViewById(R.id.btn_back);
		back.setVisibility(ImageButton.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				setGPSLocation();
				finish();
			}
		});
		
		ImageButton home = (ImageButton) findViewById(R.id.btn_go_home);
		home.setVisibility(ImageButton.GONE);
		home = (ImageButton) findViewById(R.id.btn_home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parking, menu);
		return true;
	}

	public TaxiActivity(){
		this.taxi = TaxisMapActivity.selected_taxi;
	}
	
	private void setInfo(){
		
		// Name
		tv = (TextView)findViewById(R.id.lbl_name);
		if(!taxi.getName().equals("")){
			tv.setText(taxi.getName());
		}else{
			tv.setVisibility(View.GONE);
		}
		// address
		tv = (TextView)findViewById(R.id.lbl_address);
		if(!taxi.getAddress().equals("")){
			tv.setText(taxi.getAddress());
		}else{
			tv.setVisibility(View.GONE);
		}
		// neighborhood
		tv = (TextView)findViewById(R.id.lbl_neighborhood);
		if(!taxi.getNeihgborhood().equals("")){
			tv.setText(taxi.getNeihgborhood());
		}else{
			tv.setVisibility(View.GONE);
		}
		// distance
		tv = (TextView)findViewById(R.id.lbl_sdistance);
		if(!(taxi.getDistance() == 0)){
			tv.setText("Distancia: " + taxi.getDistance());
		}else{
			tv.setVisibility(View.GONE);
		}
		// phones
		tv = (TextView)findViewById(R.id.lbl_phones);
		if(!(taxi.getPhones().size() == 0)){
			tv.setText("Teléfono(s): ");
			for(int i = 0; i < taxi.getPhones().size(); i++){
				if(taxi.getPhones().size() == 1){
					tv.setText(tv.getText() + taxi.getPhones().get(0).getNumber());
				}else{
					tv.setText(tv.getText() + taxi.getPhones().get(i).getNumber() + " ");
				}
			}
			
		}else{
			tv.setVisibility(View.GONE);
		}
	}
}
