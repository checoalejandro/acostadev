package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.Promo;
import helper.json.PromoList;
import helper.threads.GetPromos;
import helper.threads.SendCheckOut;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PassportActivity extends Activity {
	List<Promo> promos;
	public static int userpoints;
	TextView points;
	ArrayList<CheckBox> cbPromos;
	public DBAdapter database = new DBAdapter(this);
	String bizname;
	Cursor cursor;
	
	ImageView btn_back;
	
	volatile public ProgressDialog pd;
	
	@SuppressWarnings("static-access")
	public PassportActivity(){
		this.promos = BizneInfoActivity.selected_passport.getPrizes();
		this.userpoints = BizneInfoActivity.usrpts;
		cbPromos = new ArrayList<CheckBox>();
		bizname = BizneInfoActivity.biznename;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promos);
		setElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.promos, menu);
		return true;
	}
	
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	@SuppressWarnings("static-access")
	public void setElements(){
		
		btn_back = (ImageView)findViewById(R.id.btn_go_home);
		btn_back.setVisibility(View.GONE);
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});
		btn_back = (ImageView)findViewById(R.id.btn_home);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		
		TextView tv2 = (TextView)findViewById(R.id.group_name);
		tv2.setText(BizneInfoActivity.selected_passport.getName());
		
		//Bizname
		tv2 = (TextView)findViewById(R.id.lbl_name_section);
		tv2.setText(bizname);
		
		tv2 = (TextView)findViewById(R.id.bizne_name);
		tv2.setVisibility(View.GONE);
		tv2.setText(BizneInfoActivity.biznename);
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
//    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		points = (TextView) findViewById(R.id.lbl_promos_points);
		points.setText(String.valueOf(this.userpoints));
		LinearLayout ll = (LinearLayout) findViewById(R.id.promos_layout);
		
		TableLayout table = new TableLayout(this);  
	    table.setStretchAllColumns(true);  
	    table.setShrinkAllColumns(true); 
	    
	    CheckBox cb;
		TextView tv;
		
		TableRow row = new TableRow(this);
		tv = new TextView(this);
	    tv.setText("Promoción");
	    row.addView(tv);
	    tv = new TextView(this);
	    tv.setText("Puntos");
	    tv.setGravity(Gravity.CENTER_HORIZONTAL);
	    row.addView(tv);
	    
	    table.addView(row);  
	    
	    for(int i = 0; i < this.promos.size(); i++){
	    	
		    
		    row = new TableRow(this);
		    cb = new CheckBox(this);
		    cb.setText(this.promos.get(i).getDescription());
//		    PromoListener pl = new PromoListener(cb, promos.get(i),this);
//		    cb.setOnClickListener(pl);
		    
		    this.cbPromos.add(cb);
		    row.addView(cb);
		    tv = new TextView(this);
		    tv.setText(String.valueOf(this.promos.get(i).getPoints()));
		    tv.setGravity(Gravity.CENTER_HORIZONTAL);
		    row.addView(tv);
		    
		    table.addView(row);  
	 
		    
	    }
	    
	    
	    ll.addView(table); 
	    Button btn_redeem = (Button)findViewById(R.id.button_redeem);
	    btn_redeem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkOut();
			}
		});
	    
	    Button btn_cancel = (Button)findViewById(R.id.button_cancel);
	    btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});
	    
	    ImageButton prize = (ImageButton) findViewById(R.id.btn_prizes3);
	    prize.setVisibility(ImageButton.GONE);

	}
	
	public void finishActivity(){
		this.finish();
	}

	
	public void checkUserPoints(){
		int checkout = 0;
		for(int i = 0; i < this.promos.size(); i++){
			if(this.cbPromos.get(i).isChecked()){
				checkout = checkout + promos.get(i).getPoints();
			}else{
				checkout = checkout + promos.get(i).getPoints();
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void checkOut(){
		try{
			

			int counter = 0;
			int total = 0;
			for(int i = 0; i < this.promos.size(); i++){
				if(this.cbPromos.get(i).isChecked()){
					counter++;
					total = total + promos.get(i).getPoints();
				}
			}
			
			if(this.userpoints - total < 0){
				Toast.makeText(this, "No tienes suficientes puntos." , Toast.LENGTH_LONG).show();
				return;
			}
			
			if(counter == 0){
				Toast.makeText(this, "Selecciona por lo menos uno." , Toast.LENGTH_LONG).show();
				return;
			}
			
			int array[] = new int[counter];
			int bizid = 0;
			int j = 0;
			for(int i = 0; i < this.cbPromos.size(); i++){
				if( this.cbPromos.get(i).isChecked()){
					array[j] = promos.get(i).getId();
					j++;
					bizid = promos.get(i).getBizId();
				}
			}
			
//			BizneInfoActivity.textview.setText("Este negocio tiene " + BizneInfoActivity.promossize + " promociones, " + BizneInfoActivity.promossize + " premios y tú tienes " + BizneInfoActivity.usrpts + " puntos. \nPodrías sumar puntos por comprar aquí.");
			Gson gson = new Gson();
			pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
			database.open();
   	        cursor = database.getAllUsers();
   	        database.close();
			String data = "&userid=" + cursor.getString(1).toString() + "&bizid=" + String.valueOf(bizid) + "&idspromo=" + gson.toJson(array);
			
			SendCheckOut checkout = new SendCheckOut(this, pd,bizid);
			checkout.execute(new String[] {data,"redeemPromos"});
			
		}catch(Exception e){
			
		}
	}
}
