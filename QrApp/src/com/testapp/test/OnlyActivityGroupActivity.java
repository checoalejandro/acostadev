package com.testapp.test;

import static com.testapp.test.BizneDataActivity.convertDpToPixel;
import helper.database.DBAdapter;
import helper.json.Acti;
import helper.json.ActivityGroup;
import helper.json.AndroidView;
import helper.threads.GetActivity;
import helper.threads.GetActivityGroup;
import helper.threads.GetBizneData;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class OnlyActivityGroupActivity extends Activity {

	ActivityGroup group;
	List<Acti> activities;
	
	public OnlyActivityGroupActivity(){
		group = GetActivityGroup.act.getGroup();
		activities = group.getActivities();
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_group);
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
		setElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_group, menu);
		return true;
	}
	
	private void setElements(){
		TextView tv;
		LinearLayout ll = (LinearLayout) findViewById(R.id.bizne_elements);
		
		// bizne name
		tv = (TextView)findViewById(R.id.bizne_name);
		tv.setText(group.getBizName());
		
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
		
		// Activity Groups
		for(int i = 0; i < activities.size(); i++){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER);
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