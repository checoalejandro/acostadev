package com.testapp.test;

import helper.json.UserPrize;
import helper.threads.GetPrizes;
import helper.threads.RedeemPrize;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class UserPrizesActivity extends Activity {

	List<UserPrize> prizes;
	ArrayList<String> biznes = new ArrayList<String>();
	ArrayList<Object> allprizes = new ArrayList<Object>();
	ArrayList<UserPrize> bizneprizes = new ArrayList<UserPrize>();
	private boolean flag = true;
	
	public UserPrizesActivity(){
		prizes = GetPrizes.ans.getData();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_prizes);
		TextView title = (TextView)findViewById(R.id.lbl_name_section);
		title.setText("Tus Premios");
		arrangeAdapter();
		setElements();
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
	
	public void arrangeAdapter(){
		
		for(int i = 0; i < prizes.size(); i++){
			bizneprizes = new ArrayList<UserPrize>();
			if(prizes.get(i).isHead()){
				bizneprizes.add(prizes.get(i));
				biznes.add(prizes.get(i).getName());
				i++;
			}
			if(i < prizes.size()){
				while(/*!prizes.get(i).isHead() &&*/ i < prizes.size()){
					if(!prizes.get(i).isHead()){
						bizneprizes.add(prizes.get(i));
						i++;
					}else{
						i--;
						break;
					}
				}
			}
			allprizes.add((Object)bizneprizes);
		}
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_offers, menu);
		return true;
	}
	
	public void finishActivity() {
		// TODO Auto-generated method stub
		this.finish();
	}
	
	private void setElements(){
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finishActivity();
				}
			});
		
	   	ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.list01);

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		PrizesExpandableAdapter adapter = new PrizesExpandableAdapter(this,biznes,allprizes);

		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		
		if(biznes.size() > 0){
			expandableList.expandGroup(0);
		}
	     
	}
	
	public void redeemPrize(int prizeid){
		ProgressDialog pd = ProgressDialog.show( this, "Canjeando...", "Espere, por favor", true, false);
		
		String data = "&prizeid=" + prizeid;
		
		RedeemPrize prize = new RedeemPrize(this, pd);
		prize.execute(new String[] {data,"redeemPrize"});
	}
}