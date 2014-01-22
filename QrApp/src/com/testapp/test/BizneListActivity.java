package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.BizneInfoList;
import helper.json.QrScan;
import helper.threads.GetBizInfo;
import helper.threads.GetSearchString;
import helper.tools.MyZone;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BizneListActivity extends Activity {
	
	MyZone selected_zone;
	Cursor cursor;
	DBAdapter database = new DBAdapter(this);
	BizneInfoList biz;
	public static BizneInfoList maplist;
	
	ImageButton next, previous;
	
	public BizneListActivity() {
		// TODO Auto-generated constructor stub
		this.biz = GetSearchString.act.getData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bizne_list);
		next = (ImageButton) findViewById(R.id.btn_next);
		previous = (ImageButton) findViewById(R.id.btn_previous);
		setElements();
		
		ImageButton btn_map = (ImageButton) findViewById(R.id.btn_map);
		btn_map.setVisibility(ImageButton.VISIBLE);
		btn_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				maplist = biz;
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(intent);
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
		getMenuInflater().inflate(R.menu.bizne_list, menu);
		return true;
	}

	private void setElements(){
		
		if(BusinessActivity.search_type.equals("") && biz.size() == 100){
			TextView max = (TextView)findViewById(R.id.bizne_list_maxbus);
			max.setVisibility(TextView.VISIBLE);
		}
		
		
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.GONE);
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
					finish();
				}
			});
	   	
	   	TextView tv;
	   	ListView list = (ListView) findViewById(R.id.ListView01);
        list.setClickable(true);

        BizneListAdapter adapter = new BizneListAdapter(this, this.biz);
        list.setAdapter(adapter);
        
        BusinessActivity.limitstart += biz.size();
		if(biz.size() == 50 && !BusinessActivity.search_type.equals("")){
			OnClickListener l = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					search();
				}
			};
			
			OnClickListener b = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			};
			
			next.setOnClickListener(l);
			previous.setOnClickListener(b);
			next.setVisibility(View.VISIBLE);
			previous.setVisibility(View.VISIBLE);

		}else{
			if(biz.size() == 100 && BusinessActivity.search_type.equals("")){

				OnClickListener l = new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						search();
					}
				};
				
				OnClickListener b = new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				};

				next.setOnClickListener(l);
				previous.setOnClickListener(b);
				next.setVisibility(View.VISIBLE);
				previous.setVisibility(View.VISIBLE);
			}
		}
		
		tv = (TextView) findViewById(R.id.bizlist_text_results);
		tv.setText("Se encontraron " + biz.size() + " negocios");
		
		tv = (TextView) findViewById(R.id.lbl_name_section);
		tv.setText("Tu búsqueda");
		
		ImageButton btn_addfav = (ImageButton)findViewById(R.id.btn_add_favorite);
		btn_addfav.setVisibility(ImageButton.GONE);
		
		ImageButton btn_prizes = (ImageButton)findViewById(R.id.btn_prizes3);
		btn_prizes.setVisibility(ImageButton.GONE);
	}
	
	private void search(){
		if(!(BusinessActivity.search_type == "")){
			ProgressDialog pd;
			pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
	    	DBAdapter database = new DBAdapter(this);
			if(BusinessActivity.search_type.equals("gps")){
			
				if(!(BusinessActivity.search_string.equals("") && BusinessActivity.catid == 0 && BusinessActivity.subcat == 0)){
					
					
					
			    	database.open();
			        Cursor reg = database.getCity();
			        int regid = Integer.parseInt(reg.getString(2));
			        database.close();
			    	
			    	BusinessActivity.data = "&string=" + BusinessActivity.search_string + "&regid=" + regid + "&cat1=" + BusinessActivity.catid + "&cat2=" + BusinessActivity.subcat + "&type=" + BusinessActivity.search_type + "&lat=" + BusinessActivity.latitude + "&lng=" + BusinessActivity.longitude + "&limit=50&limitstart=" + BusinessActivity.limitstart + "&distance=" + BusinessActivity.search_distance;
					
					GetSearchString getsearch = new GetSearchString(this, pd);
					getsearch.execute(new String[] {BusinessActivity.data,"search"});
				}else{
					Toast.makeText(this, "Ingresa texto a buscar y/o selecciona una categoría.", Toast.LENGTH_LONG).show();
				}
			}else{
				database.open();
				cursor = database.getTmpZone();
				database.close();
				
				selected_zone = new MyZone(Double.parseDouble(cursor.getString(0)),Double.parseDouble(cursor.getString(1)));
				if(BusinessActivity.search_type.equals("zone") && selected_zone.isSet()){

			    	database.open();
			        Cursor reg = database.getCity();
			        int regid = Integer.parseInt(reg.getString(2));
			        database.close();
			    	
			    	BusinessActivity.data = "&string=" + BusinessActivity.search_string + "&regid=" + regid + "&cat1=" + BusinessActivity.catid + "&cat2=" + BusinessActivity.subcat + "&type=gps" + "&lat=" + selected_zone.getLatitude() + "&lng=" + selected_zone.getLongitude() + "&limit=50&limitstart=" + BusinessActivity.limitstart + "&distance=" + BusinessActivity.search_distance;;
					
					GetSearchString getsearch = new GetSearchString(this, pd);
					getsearch.execute(new String[] {BusinessActivity.data,"search"});
				}else{
					if(BusinessActivity.search_type.equals("route")){
				
				    	database.open();
				    	Cursor cursor = database.getAllUsers();
				        Cursor reg = database.getCity();
				        int regid = Integer.parseInt(reg.getString(2));
				        database.close();
				    	
				        BusinessActivity.data = "&string=" + BusinessActivity.search_string + "&regid=" + regid + "&cat1=" + BusinessActivity.catid + "&cat2=" + BusinessActivity.subcat + "&type=route" + "&lat=&lng=&limit=50&limitstart=" + BusinessActivity.limitstart + "&distance=" + BusinessActivity.search_distance + "&routeid=" + BusinessActivity.routeid + "&userid=" + cursor.getString(1).toString();
						
						GetSearchString getsearch = new GetSearchString(this, pd);
						getsearch.execute(new String[] {BusinessActivity.data,"search"});
					}else{
				    	
				    	database.open();
				    	Cursor reg = database.getCity();
				        int regid = Integer.parseInt(reg.getString(2));
				        database.close();
				    	
				        BusinessActivity.data = "&string=" + BusinessActivity.search_string + "&regid=" + regid + "&cat1=" + BusinessActivity.catid + "&cat2=" + BusinessActivity.subcat + "&type=street" + "&lat=&lng=&limit=50&limitstart=" + BusinessActivity.limitstart + "&distance=" + BusinessActivity.search_distance + "&macroid=" + BusinessActivity.streetid;
						
						GetSearchString getsearch = new GetSearchString(this, pd);
						getsearch.execute(new String[] {BusinessActivity.data,"search"});
					}
					
				}
			}
		}else{

			ProgressDialog pd;
			pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
	    	DBAdapter database = new DBAdapter(this);
	    	
	    	database.open();
	        Cursor reg = database.getCity();
	        int regid = Integer.parseInt(reg.getString(2));
	        database.close();
	        
	        BusinessActivity.data = "&string=" + BusinessActivity.search_string + "&regid=" + regid + "&cat1=" + BusinessActivity.catid + "&cat2=" + BusinessActivity.subcat + "&limit=100&limitstart=" + BusinessActivity.limitstart;
	    	
	        GetSearchString getsearch = new GetSearchString(this, pd);
			getsearch.execute(new String[] {BusinessActivity.data,"search"});
		}
	}
	
	public void openBizne(int bizid){
		
		QrScan qrscan = new QrScan();
		qrscan.setBizId(bizid);
		MainActivity.answer = qrscan;
		
		DBAdapter database = new DBAdapter(this);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		ProgressDialog pd;
 				pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
 				
 				String data = "&bizid=" + bizid +
 						"&userid=" + cursor.getString(1).toString();
 				
 				GetBizInfo getinfo = new GetBizInfo(this, pd);
				getinfo.execute(new String[] {data,"getBizneInfo"});
	}
	
	public static Drawable getCategoryDrawable(Activity act, int catid){
		
		Drawable drawable = null;
		
		switch(catid){
		case 2: drawable = act.getResources().getDrawable(R.drawable.ic_cat_shows);
			break;
		case 15: drawable = act.getResources().getDrawable(R.drawable.ic_cat_enterteinment);
			break;
		case 32: drawable = act.getResources().getDrawable(R.drawable.ic_cat_cars);
			break;
		case 42: drawable = act.getResources().getDrawable(R.drawable.ic_cat_beauty);
			break;
		case 51: drawable = act.getResources().getDrawable(R.drawable.ic_cat_snacks);
			break;
		case 57: drawable = act.getResources().getDrawable(R.drawable.ic_cat_educations);
			break;
		case 73: drawable = act.getResources().getDrawable(R.drawable.ic_cat_travels);
			break;
		case 80: drawable = act.getResources().getDrawable(R.drawable.ic_cat_inmobi);
			break;
		case 85: drawable = act.getResources().getDrawable(R.drawable.ic_cat_pets);
			break;
		case 90: drawable = act.getResources().getDrawable(R.drawable.ic_cat_comm);
			break;
		case 94: drawable = act.getResources().getDrawable(R.drawable.ic_cat_religion);
			break;
		case 96: drawable = act.getResources().getDrawable(R.drawable.ic_cat_events);
			break;
		case 101: drawable = act.getResources().getDrawable(R.drawable.ic_cat_food);
			break;
		case 181: drawable = act.getResources().getDrawable(R.drawable.ic_cat_health);
			break;
		case 199: drawable = act.getResources().getDrawable(R.drawable.ic_cat_home);
			break;
		case 212: drawable = act.getResources().getDrawable(R.drawable.ic_cat_professional);
			break;
		case 221: drawable = act.getResources().getDrawable(R.drawable.ic_cat_finance);
			break;
		case 227: drawable = act.getResources().getDrawable(R.drawable.ic_cat_local);
			break;
		case 235: drawable = act.getResources().getDrawable(R.drawable.ic_cat_public);
			break;
		case 240: drawable = act.getResources().getDrawable(R.drawable.ic_cat_shopping);
			break;
		case 279: drawable = act.getResources().getDrawable(R.drawable.ic_cat_nightlife);
			break;
		case 363: drawable = act.getResources().getDrawable(R.drawable.ic_cat_techno);
			break;
		case 364: drawable = act.getResources().getDrawable(R.drawable.ic_cat_clothes);
			break;
		case 365: drawable = act.getResources().getDrawable(R.drawable.ic_cat_stores);
			break;
		case 388: drawable = act.getResources().getDrawable(R.drawable.ic_cat_sports);
			break;
			default: drawable = act.getResources().getDrawable(R.drawable.ic_store);
				break;
		}
		
		return drawable;
	}

}
