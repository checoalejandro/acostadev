package com.testapp.test;

import helper.json.Favorite;
import helper.json.Favorites;
import helper.json.FavoritesList;
import helper.threads.GetFavorites;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

public class FavoritesActivity extends Activity {
	FavoritesList favorites;
	SparseArray<Group2> groups = new SparseArray<Group2>();
	
	public FavoritesActivity(){
		this.favorites = GetFavorites.act.getData();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorites);
		setElements();
		ExpandableListView listView = (ExpandableListView) findViewById(R.id.favorites_listview);
	    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,groups);
	    listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorites, menu);
		
		return true;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void setElements(){
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		
//		if(favorites != null){
//			
//			for (int j = 0; j < favorites.size(); j++) {
//				
//				Favorite favorite;
//				
//				Group2 group = new Group2(favorites.get(j).getName().toString());
//				
//				if(favorites.get(j).getFavorites().size() > 0){
//
//					for (int i = 0; i < favorites.get(j).getFavorites().size(); i++) {
//						group.children.add(favorites.get(j).getFavorites().get(i).getName());
//					}
//					
//					groups.append(j, group);
//				}else{
//					
//
//				}
//			}
////		}
//    	for (int j = 0; j < favorites.size(); j++) {
//    	      Group2 group = new Group2(" " + favorites.get(j).getName().toString());
//    	      for (int i = 0; i < favorites.get(j).getFavorites().size(); i++) {
//    	        group.children.add(favorites.get(j).getFavorites().get(i).getName() + ":"+ favorites.get(j).getFavorites().get(i).getBizId());
//    	      }
//    	      groups.append(j, group);
//    	    }
	}

}
