package com.testapp.test;

import helper.json.Parking;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ParkingActivity extends Activity {

	Parking park;
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking);
		if(ParkingsMapActivity.selected_park != null){
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

	public ParkingActivity(){
		this.park = ParkingsMapActivity.selected_park;
	}
	
	private void setInfo(){
		
		// Name
		tv = (TextView)findViewById(R.id.lbl_name);
		if(!park.getName().equals("")){
			tv.setText(park.getName());
		}else{
			tv.setVisibility(View.GONE);
		}
		// address
		tv = (TextView)findViewById(R.id.lbl_address);
		if(!park.getAddress().equals("")){
			tv.setText(park.getAddress());
		}else{
			tv.setVisibility(View.GONE);
		}
		// neighborhood
		tv = (TextView)findViewById(R.id.lbl_neighborhood);
		if(!park.getNeighborhood().equals("")){
			tv.setText(park.getNeighborhood());
		}else{
			tv.setVisibility(View.GONE);
		}
		// rate
		tv = (TextView)findViewById(R.id.lbl_rate);
		if(!park.getRate().equals("")){
			tv.setText(park.getRate());
		}else{
			tv.setVisibility(View.GONE);
		}
		// distance
		tv = (TextView)findViewById(R.id.lbl_sdistance);
		if(!(park.getSDistance() == 0)){
			tv.setText("Distancia: " + park.getSDistance());
		}else{
			tv.setVisibility(View.GONE);
		}
		// status
		tv = (TextView)findViewById(R.id.lbl_status);
		if(!park.getDetailsStatus().getStatus().equals("")){
			tv.setText(park.getDetailsStatus().getStatus());
		}else{
			tv.setVisibility(View.GONE);
		}
		// comment
		tv = (TextView)findViewById(R.id.lbl_comment);
		if(!park.getDetailsStatus().getComment().equals("")){
			tv.setText(park.getDetailsStatus().getComment());
		}else{
			tv.setVisibility(View.GONE);
		}
		// phones
		tv = (TextView)findViewById(R.id.lbl_phones);
		if(!(park.getPhones().size() == 0)){
			tv.setText("Teléfono(s): ");
			for(int i = 0; i < park.getPhones().size(); i++){
				if(park.getPhones().size() == 1){
					tv.setText(tv.getText() + park.getPhones().get(0).getNumber());
				}else{
					tv.setText(tv.getText() + park.getPhones().get(i).getNumber() + " ");
				}
				
			}
			
		}else{
			tv.setVisibility(View.GONE);
		}
	}
}
