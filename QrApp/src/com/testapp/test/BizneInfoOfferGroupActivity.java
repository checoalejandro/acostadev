package com.testapp.test;

import static com.testapp.test.BizneDataActivity.convertDpToPixel;
import helper.database.DBAdapter;
import helper.json.Acti;
import helper.json.ActivityGroup;
import helper.json.BizneInfo;
import helper.threads.GetActivity;
import helper.threads.GetBizneOffers;

import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class BizneInfoOfferGroupActivity extends Activity {

	ActivityGroup group;
	List<Acti> activities;
	String groupname;
	String bizname;
	ImageView btn_back;
	
	public BizneInfoOfferGroupActivity(){
		group = GetBizneOffers.selected_activity_group;
		activities = group.getActivities();
		bizname = group.getBizName();
		groupname = group.getName();
		if(GetBizneOffers.answer.getData().getName() != null)
			MainActivity.lastbiz = new BizneInfo(GetBizneOffers.answer.getData().getName());
		else
			MainActivity.lastbiz = new BizneInfo(BizneInfoActivity.biznename);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_group);
		setElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_group, menu);
		return true;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	private void setElements(){
		
		btn_back = (ImageView)findViewById(R.id.btn_go_home);
		btn_back.setVisibility(View.GONE);
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});
		btn_back = (ImageView)findViewById(R.id.btn_home);
		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		
		TextView tv;
		LinearLayout ll = (LinearLayout) findViewById(R.id.bizne_elements);
		
		// bizne name
		tv = (TextView)findViewById(R.id.bizne_name);
		tv.setVisibility(View.GONE);
		tv = (TextView)findViewById(R.id.lbl_name_section);
		tv.setText(GetBizneOffers.answer.getData().getName());
		
		// Hide "select one"
		tv = (TextView)findViewById(R.id.textView1);
		tv.setVisibility(View.GONE);
		
		tv = (TextView)findViewById(R.id.group_name);
		tv.setText(this.groupname);
		tv.setVisibility(View.VISIBLE);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		int p = (int) convertDpToPixel(5, this);
		params.setMargins(p, p, p, p);
		
		if(activities.size() == 0){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER);
			tv.setText("Este grupo está vacío");
			ll.addView(tv);
			return;
		}
		
		// Activities
		for(int i = 0; i < activities.size(); i++){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = null;
			switch(activities.get(i).getType()){
			case 0: img = this.getResources().getDrawable( R.drawable.ic_survey );
				break;
			case 1:
				img = this.getResources().getDrawable( R.drawable.ic_trivia );
				break;
			case 2:
				img = this.getResources().getDrawable( R.drawable.ic_message );
				break;
			case 3:
				if(activities.get(i).isOffer()){
					img = this.getResources().getDrawable( R.drawable.ic_sale);
				}else{
					img = this.getResources().getDrawable( R.drawable.ic_message );
				}
				break;
			case 6:
				img = this.getResources().getDrawable( R.drawable.ic_message );
				break;
			case 4:
				img = this.getResources().getDrawable( R.drawable.ic_link );
				break;
			case 5:
				img = this.getResources().getDrawable( R.drawable.ic_video );
				break;
			}
			tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
			tv.setCompoundDrawablePadding(15);
			tv.setText(activities.get(i).getName());
			final int points = activities.get(i).getPoint();
			final int actid = activities.get(i).getId();
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(points > 0)
						showMessagePoints(points,actid);
					else
						openActivity(actid);
				}
			});
			ll.addView(tv);
		}
	}
	
	private void showMessagePoints(int points,final int actid){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_ok);

		// set the custom dialog components - text, image and button
		TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
		tv.setText("Esta actividad te dará " + points + " puntos por contestarla.");

		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				finishActivity();
				openActivity(actid);
			}
		});
		
		dialog.show();
	}
	
	private void finishActivity(){
		finish();
	}
	
	private void openActivity(int actid){
		ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&actid=" + actid;
		
		GetActivity getdata = new GetActivity(this, pd);
		getdata.execute(new String[] {data,"getActivity"});
	}
}