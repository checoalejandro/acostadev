package com.testapp.test;

import helper.database.DBAdapter;
import helper.threads.GetFavorites;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryBizneActivity extends Activity {
	
	SparseArray<Group2> groups = new SparseArray<Group2>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_bizne);
		setElements();
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.categories_listview);
	    CategoriesExpandableListAdapter adapter = new CategoriesExpandableListAdapter(this,groups,null);
	    listView.setAdapter(adapter);
	}

	private void setElements() {
		// TODO Auto-generated method stub
		
		DBAdapter database = new DBAdapter(this);
		database.open();
        Cursor c = database.getAllCategories();
        
        int i = 0;
        Group2 group = new Group2(" " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)));
    	int par_index = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
    	Cursor s = database.getCategories(par_index);
    	String parcat = "Buscar en " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + par_index;
    	group.children.add(parcat);
    	while(s.moveToNext()){
    		group.children.add(s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
    		
    	}
    	groups.append(i, group);
        i++;
        
        while(c.moveToNext()){
        	group = new Group2(" " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)));
        	par_index = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
        	s = database.getCategories(par_index);
        	parcat = "Buscar en " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + par_index;
        	group.children.add(parcat);
        	while(s.moveToNext()){
        		group.children.add(s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
        		
        	}
        	groups.append(i, group);
        	i++;
        }
		database.close();
		
		ImageButton btn_addfav = (ImageButton)findViewById(R.id.btn_add_favorite);
		btn_addfav.setVisibility(ImageButton.GONE);
		
		ImageButton btn_prizes = (ImageButton)findViewById(R.id.btn_prizes3);
		btn_prizes.setVisibility(ImageButton.GONE);
	}
	
	public void searchCategory(String cat){
		Toast.makeText(this, cat, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.category_bizne, menu);
		return true;
	}

}
