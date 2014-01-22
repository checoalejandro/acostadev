package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.OfferGroup;
import helper.threads.GetBizInfo;
import helper.threads.GetOfferGroup;
import helper.threads.GetOnlyBizName;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class OfferGroupActivity extends Activity {
	
	OfferGroup group;
	
	public OfferGroupActivity(){
		this.group = GetOfferGroup.act.getOfferGroup();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_group);
		setElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.offer_group, menu);
		return true;
	}
	
	private void setElements(){
		
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
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		
		TextView tv = (TextView) findViewById(R.id.offergroup_bizname);
		tv.setText(GetOnlyBizName.act.getBizneInfo().getName());
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openBizne();
			}
		});
		
		tv = (TextView) findViewById(R.id.offergroup_place);
		tv.setText(tv.getText() + " " + group.getName());
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.offergroup_offers);
		LinearLayout.LayoutParams params = new LayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		params.setMargins(5,5,5,5);
		for(int i=0; i < group.getOffers().size(); i++){
			tv = new TextView(this);
			tv.setText(group.getOffers().get(i).getName());
			tv.setBackgroundResource(R.drawable.search_edittext);
			tv.setTextAppearance(this, R.style.subtitle);
			int paddingPixel = 5;
			float density = this.getResources().getDisplayMetrics().density;
			int paddingDp = (int)(paddingPixel * density);
			tv.setPadding(paddingDp,paddingDp,paddingDp,paddingDp);
			tv.setLayoutParams(params);
			ll.addView(tv);
		}
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void openBizne(){
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
 				
 				GetBizInfo getinfo = new GetBizInfo(this, pd);
				getinfo.execute(new String[] {data,"getBizneInfo"});
	}

}
