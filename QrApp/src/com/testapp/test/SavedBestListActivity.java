package com.testapp.test;

import helper.database.DBAdapter;
import helper.threads.GetBestList;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SavedBestListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_best_list);
		setElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_best_list, menu);
		return true;
	}
	
	public void setElements(){
		LinearLayout ll = (LinearLayout)findViewById(R.id.savedbestlists_bestlist);
		
		DBAdapter database = new DBAdapter(this);
		database.open();
		Cursor c = database.getAllBestLists();
		
		if(c.getCount() > 0){
		
			do{
				LinearLayout inner = new LinearLayout(this);
				inner.setOrientation(LinearLayout.HORIZONTAL);
				inner.setPadding(20, 10, 20, 10);
				LinearLayout left = new LinearLayout(this);
				left.setPadding(10, 5, 10, 5);
				left.setOrientation(LinearLayout.VERTICAL);
				LinearLayout right = new LinearLayout(this);
				right.setOrientation(LinearLayout.VERTICAL);
				inner.addView(right);
				inner.addView(left);
				inner.setBackgroundResource(R.drawable.rounded_bottom_edittext);
				
				Button button = new Button(this);
				button.setText("");
				button.setGravity(Gravity.RIGHT);
				button.setBackgroundResource(R.drawable.btn_bestlist_go);
				final int bestlistid = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_BESTLISTID)));
				left.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						openBestList(bestlistid);
					}
				});
				right.addView(button);
				
				TextView tv = new TextView(this);
				tv.setTextAppearance(this, R.style.subtitle);
				tv.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_LISTNAME)));
				left.addView(tv);
				tv = new TextView(this);
				tv.setText(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_DESC)));
				left.addView(tv);
				
				ll.addView(inner);
			}while(c.moveToNext());
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
		
		String data = "&bestlistid=" + bestlistid;
		
		GetBestList getlist = new GetBestList(this, pd,true);
		getlist.execute(new String[] {data,"getBizneBestList"});
	}

}
