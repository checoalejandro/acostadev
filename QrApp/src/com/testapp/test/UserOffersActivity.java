package com.testapp.test;

import helper.json.UserOffer;
import helper.threads.GetOffers;
import helper.threads.RedeemPrize;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class UserOffersActivity extends Activity {
	
	List<UserOffer> offers;
	public UserOffersActivity(){
		offers = GetOffers.ans.getData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_offers);
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
	
	private void setElements(){
		
		// get a reference for the TableLayout
        TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
        
        TableLayout.LayoutParams tableRowParams=
        		  new TableLayout.LayoutParams
        		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);

        		int leftMargin=10;
        		int topMargin=2;
        		int rightMargin=10;
        		int bottomMargin=2;

        		tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        		
        
        // HEADINGS
        TableRow head = new TableRow(this);
        
        TextView tv = new TextView(this);
//        // Bizname
//        tv.setText("Negocio");
//        head.addView(tv);
        
        tv = new TextView(this);
        // Offer
        tv.setText("Oferta");
        head.addView(tv);
        
        tv = new TextView(this);
        // Expira
        tv.setText("Vigencia");
        head.addView(tv);
        
        tv = new TextView(this);
        // REDEEM
        tv.setText("Canejar");
        head.addView(tv);
        
        head.setLayoutParams(tableRowParams);
        table.addView(head,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        for(int i = 0; i < offers.size(); i++){
         
	        // create a new TableRow
	        TableRow row = new TableRow(this);
	         
	        // create a new TextView
	        TextView t = new TextView(this);
	        // bizname       
//	        t.setText(offers.get(i).getName());
//	        row.addView(t);
	        
	        t = new TextView(this);
	        // offer      
	        t.setText(offers.get(i).getOffer());
	        row.addView(t);
	        
	        t = new TextView(this);
	        // expiration     
	        t.setText(" " + offers.get(i).getExpiration());
	        row.addView(t);
	        
	        if(offers.get(i).isRedeemed()){
	        	t = new TextView(this);
	        	t.setText(" Canjeado");
	        	row.addView(t);
	        }else{
	        	Button b = new Button(this);
	        	b.setText("Canjear");
	        	b.setBackgroundResource(R.drawable.btn_register_bg);
	        	final int offerid = offers.get(i).getId();
	        	b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						redeemOffer(offerid);
					}
				});
	        	row.addView(b);
	        }
	        row.setLayoutParams(tableRowParams);   
	        // add the TableRow to the TableLayout
	        table.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
	}
	
	public void redeemOffer(int offerid){
		ProgressDialog pd = ProgressDialog.show( this, "Canjeando...", "Espere, por favor", true, false);
		
		String data = "&offerid=" + offerid;
		
		RedeemPrize prize = new RedeemPrize(this, pd);
		prize.execute(new String[] {data,"redeemOffer"});
	}

}
