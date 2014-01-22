package com.testapp.test;

import helper.json.BestListBiznesList;
import helper.threads.GetBizneGroup;
import helper.threads.SendQualityValue;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QualityGroupActivity extends Activity {
	
	BestListBiznesList list;
	volatile public ProgressDialog pd;
	
	public QualityGroupActivity(){
		this.list = GetBizneGroup.act.getData();
	}
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quality_group);
		setElements();
		ImageButton home = (ImageButton)findViewById(R.id.btn_home);
		home.setVisibility(ImageButton.VISIBLE);
		home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		home = (ImageButton)findViewById(R.id.btn_go_home);
		home.setVisibility(ImageButton.VISIBLE);
		home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quality_group, menu);
		return true;
	}
	
	public void setElements(){
		
		String value = "";
		
		switch(MainActivity.answer.getValue()){
		case 0: value = "excelente"; break;
		case 1: value = "bueno"; break;
		case 2: value = "regular"; break;
		case 3: value = "malo"; break;
		}
		
		TextView tv = (TextView) findViewById(R.id.quality_title);
		tv.setText(tv.getText() + " " + value);
		
		ImageButton check = (ImageButton)findViewById(R.id.btn_check);
		check.setVisibility(ImageButton.VISIBLE);
		check.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setQuality();
			}
		});
		LinearLayout ll = (LinearLayout) findViewById(R.id.biznegroups_biznes);
		CheckBox btn;
		for(int i = 0; i < list.size(); i++){
			btn = new CheckBox(this);
			btn.setTextAppearance(this, R.style.category_style);
			
//			btn.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			btn.setText(list.get(i).getName());
			
			ll.addView(btn);
		}
		
	}
	
	public void setQuality(){
		int bizid = 0;
		LinearLayout ll = (LinearLayout) findViewById(R.id.biznegroups_biznes);
		for(int i = 0; i < list.size(); i++){
			CheckBox cb = (CheckBox) ll.getChildAt(i);
			if(cb.isChecked()){
				bizid = list.get(i).getBizId();
				pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
				
				String data = "&bizid="+ bizid +
						"&value=" + MainActivity.answer.getValue();
				
				SendQualityValue sendquality = new SendQualityValue(this, pd,true);
				sendquality.execute(new String[] {data,"saveQuality"});
			}
		}
		Toast.makeText(this, "Gracias por tu opinión", Toast.LENGTH_LONG).show();
		this.finish();
	}

}
