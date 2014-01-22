package com.testapp.test;

import java.util.ArrayList;

import helper.json.BestListBiznesList;
import helper.threads.GetBizneGroup;
import helper.threads.SendQualityGroupValue;
import helper.threads.SendQualityValue;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class QualityBizneGroupActivity extends Activity {

	BestListBiznesList list;
	volatile public ProgressDialog pd;
	ArrayList<QualityBizne> biznes = new ArrayList<QualityBizne>();
	
	public QualityBizneGroupActivity(){
		list = GetBizneGroup.act.getData();
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quality_bizne_group);
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
		
//		TextView tv = (TextView) findViewById(R.id.quality_title);
//		tv.setText(tv.getText() + " " + value);
		
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
		LinearLayout.LayoutParams params = new LayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		TextView tv;
		QualityBizne qb = null;
		params.setMargins(5,5,5,5);
		for(int i = 0; i < list.size(); i++){
			tv = new TextView(this);
			tv.setLayoutParams(params);
			tv.setPadding(5, 5, 5, 5);
			tv.setBackgroundResource(R.drawable.btn_favorite);
			
			final String bizname = list.get(i).getName();
			final int index = i;
			
			tv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showDialog(bizname,index);
				}
			});
			tv.setText(list.get(i).getName());
			qb = new QualityBizne(list.get(i).getBizId());
			biznes.add(qb);
			ll.addView(tv);
		}
		
	}
	
	public void showDialog(String bizname, final int index){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.quality_dialog);
		dialog.setTitle("Selecciona la valoración");

//		// set the custom dialog components - text, image and button
//		TextView text = (TextView) dialog.findViewById(R.id.quality_dialog_title);
//		text.setText();
		TextView text;

		ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.btn_verygood);
		final LinearLayout ll = (LinearLayout) findViewById(R.id.biznegroups_biznes);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				biznes.get(index).setValue(0);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_verygood);
				
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_verygood);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				biznes.get(index).setValue(1);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_verygood);
			}
		});
		
		dialogButton = (ImageButton) dialog.findViewById(R.id.btn_good);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				biznes.get(index).setValue(0);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_good);
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_good);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				biznes.get(index).setValue(1);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_good);
			}
		});
		
		dialogButton = (ImageButton) dialog.findViewById(R.id.btn_regular);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				biznes.get(index).setValue(2);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_regular);
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_regular);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				biznes.get(index).setValue(2);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_regular);
			}
		});
		
		
		
		dialogButton = (ImageButton) dialog.findViewById(R.id.btn_bad);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				biznes.get(index).setValue(3);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_bad);
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_bad);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				biznes.get(index).setValue(3);
				dialog.dismiss();
				TextView tv = (TextView) ll.getChildAt(index);
				tv.setBackgroundResource(R.drawable.ic_back_bad);
			}
		});

		dialog.show();
	}
	
	public void setQuality(){
		
		for(int i = 0; i < biznes.size(); i++){
			QualityBizne qb = biznes.get(i);
			if(qb.value != -1){
				pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
				
				String data = "&bizid="+ qb.bizid +
						"&value=" + qb.value;
				
				SendQualityGroupValue sendquality = new SendQualityGroupValue(this, pd,true);
				sendquality.execute(new String[] {data,"saveQuality"});
			}
		}
		pd.dismiss();
		Toast.makeText(this, "Gracias por tu opinión", Toast.LENGTH_LONG).show();
		this.finish();
	}
	
	public class QualityBizne{
		int value = -1;
		int bizid = 0;
		
		public QualityBizne(int v,int b){
			value = v;
			bizid = b;
		}
		
		public QualityBizne(int b){
			bizid = b;
		}
		
		public void setValue(int v){
			value = v;
		}
	}

}
