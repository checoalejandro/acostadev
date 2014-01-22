package com.testapp.test;

import java.util.ArrayList;

import helper.database.DBAdapter;
import helper.json.QrScan;
import helper.threads.GetBestList;
import helper.threads.GetBizInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ViewSwitcher;

public class FavoriteActivity extends Activity {
	
	Cursor c;
	DBAdapter database;
	private ArrayList<String> parentItems = new ArrayList<String>();
	private ArrayList<Object> childItems = new ArrayList<Object>();
	private ArrayList<Subcat> subcats = new ArrayList<FavoriteActivity.Subcat>();
	private ArrayList<String> parentItemsbest = new ArrayList<String>();
	private ArrayList<Object> childItemsbest = new ArrayList<Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		getFavorites();
		setElements();
		ExpandableListView expandableList = (ExpandableListView) findViewById(R.id.favorite_list);

		expandableList.setDividerHeight(2);
		expandableList.setGroupIndicator(null);
		expandableList.setClickable(true);

		FavoritesExpandableListAdapter adapter = new FavoritesExpandableListAdapter(parentItems, childItems);

		adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableList.setAdapter(adapter);
		
		ExpandableListView expandableListBest = (ExpandableListView) findViewById(R.id.bestlist_list);

		expandableListBest.setDividerHeight(2);
		expandableListBest.setGroupIndicator(null);
		expandableListBest.setClickable(true);

		BestListExpandableListAdapter adapterbest = new BestListExpandableListAdapter(parentItemsbest, childItemsbest);

		adapterbest.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
		expandableListBest.setAdapter(adapterbest);

	}

	private void getFavorites() {
		// TODO Auto-generated method stub
		database = new DBAdapter(this);
		database.open();
		c = database.getAllFavorites();
		database.close();
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	private void setElements() {
		// TODO Auto-generated method stub
		
		// Set Title
		TextView title = (TextView)findViewById(R.id.lbl_name_section);
		title.setText("Tus Favoritos");
		
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
		
		ArrayList<String> strings = new ArrayList<String>();
		for(int i = 0; i < c.getCount(); i++){
			Subcat subcat;
			String parent;
			String child = c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_NAME));
			final int bizid  = c.getInt(c.getColumnIndex("bizid"));
			
			int cat_id = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
			database.open();
			Cursor s = database.getAllSubCategories();
			database.close();
			for(int j = 0; j < s.getCount(); j++){
				parent = s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME));
				int subcat_id = Integer.parseInt(s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
				subcat = new Subcat(parent,subcat_id);
				if(subcat_id == cat_id){
					if(!strings.contains(parent)){
						strings.add(parent);
						subcats.add(subcat);
					}
					break;
				}
				s.moveToNext();
			}
			
			c.moveToNext();
		
		}
		
		for(int k = 0; k < subcats.size(); k++){
			parentItems.add(subcats.get(k).name);
			ArrayList<String> child = new ArrayList<String>();
			
			
			database.open();
			c = database.getAllFavorites();
			database.close();
			
			do{
				int cat_id = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
				if(cat_id == subcats.get(k).catid){
					child.add(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_NAME)) + ":" + c.getInt(c.getColumnIndex("bizid")));
					
				}
			}while(c.moveToNext());
			
			childItems.add(child);
		}
        
        setBestLists();
		
	}
	
	public void setBestLists(){
		
		DBAdapter database = new DBAdapter(this);
		database.open();
		Cursor c = database.getAllBestLists();
		if(c.getCount() != 0){
			parentItemsbest.add("Listas de promociones");
			ArrayList<String> child = new ArrayList<String>();
			do{
				int bestlistid = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_BESTLISTID)));
				child.add(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_LISTNAME)) + ":" + bestlistid);
			}while(c.moveToNext());
			childItemsbest.add(child);
		}
		
		database.close();
	}
	
	public void openBestList(int bestlistid){
		
		DBAdapter database = new DBAdapter(this);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		ProgressDialog pd;
		pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&bestlistid=" + bestlistid + "&userid=" + cursor.getString(1).toString();;
		
		GetBestList getlist = new GetBestList(this, pd,true);
		getlist.execute(new String[] {data,"getBizneBestList"});
	}
	
	public void openBizne(int bizid){
		DBAdapter database = new DBAdapter(this);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		ProgressDialog pd;
		pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		QrScan qrscan = new QrScan();
		qrscan.setBizId(bizid);
		MainActivity.answer = qrscan;
		
		String data = "&bizid=" + bizid +
				"&userid=" + cursor.getString(1).toString();
		
		GetBizInfo getinfo = new GetBizInfo(this, pd);
		getinfo.execute(new String[] {data,"getBizneInfo"});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorite, menu);
		return true;
	}
	
	public class Favorite{
		String name;
		int bizid;
		int catid;
		
		public Favorite(String n, int b, int c){
			this.bizid = b;
			this.catid = c;
			this.name = n;
		}
	}
	
	public class Subcat{
		public int catid;
		public String name;
		
		Subcat (String n, int c){
			this.catid = c;
			this.name = n;
		}
	}
}
