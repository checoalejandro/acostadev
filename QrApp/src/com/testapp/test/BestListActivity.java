package com.testapp.test;

import helper.json.BestList;
import helper.json.BestListBiznes;
import helper.threads.GetBestList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

public class BestListActivity extends Activity {
	
	BestList bestlist;
	BestListBiznes bizne;
	SparseArray<Group> groups = new SparseArray<Group>();
	
	public BestListActivity(){
		this.bestlist = GetBestList.act.getBestList();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_best_list);
		setElements();
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
	    ExpandableListAdapter adapter = new ExpandableListAdapter(this,groups);
	    listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.best_list, menu);
		return true;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void finishActivity(){
		this.finish();
	}
	
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
    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});

		TextView tv = (TextView) findViewById(R.id.bestlist_lbl_listname);
		tv.setText(bestlist.getDescription());
		
		tv = (TextView) findViewById(R.id.bestlist_title);
		tv.setText(bestlist.getName());
		
		if(bestlist.getBiznes() != null){
			
			for (int j = 0; j < bestlist.getBiznes().size(); j++) {
				
				bizne = bestlist.getBiznes().get(j);
				Group group = new Group("  " + bizne.getName(),bizne.getBizId());
				
				if(bizne.getPromotions() != null){
				group.children.add("Más información del negocio" + ":" + bizne.getBizId());
					for (int i = 0; i < bizne.getPromotions().size(); i++) {
						group.children.add(bizne.getPromotions().get(i).getPromotion());
					}
					
					groups.append(j, group);
				}else{
					group.children.add("Más información del negocio" + ":" + bizne.getBizId());
					group.children.add("Aún no hay promociones");
					groups.append(j, group);
				}
			}
		}
	}

}
