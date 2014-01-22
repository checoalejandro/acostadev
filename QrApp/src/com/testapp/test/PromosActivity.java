package com.testapp.test;

import java.util.ArrayList;

import com.google.gson.Gson;

import helper.database.DBAdapter;
import helper.json.PromoList;
import helper.threads.GetPromos;
import helper.threads.SendCheckOut;
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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PromosActivity extends Activity {
	PromoList promos;
	public static int userpoints;
	TextView points;
	ArrayList<CheckBox> cbPromos;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	volatile public ProgressDialog pd;
	
	@SuppressWarnings("static-access")
	public PromosActivity(){
		this.promos = GetPromos.act.getPromos();
		this.userpoints = GetPromos.act.getPoints();
		cbPromos = new ArrayList<CheckBox>();
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
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_home);
    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
    	btn_home = (ImageButton) findViewById(R.id.btn_go_home);
    	btn_home.setVisibility(View.VISIBLE);
    	btn_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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
	    ImageButton prize = (ImageButton) findViewById(R.id.btn_prizes3);
	    prize.setVisibility(ImageButton.GONE);
	    ImageButton checkout = (ImageButton) findViewById(R.id.btn_promo_redeem);
	    checkout.setVisibility(ImageButton.VISIBLE);
	    checkout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkOut();
			}
		});
//		setContentView(table);
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
