package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.ActivityGroup;
import helper.json.AndroidView;
import helper.threads.GetActivity;
import helper.threads.GetBizneData;
import helper.threads.GetPromos;
import helper.threads.SendAlert;
import helper.threads.SendCheckin;
import helper.threads.SendQualityValue;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class BizneDataActivity extends Activity {
	public AndroidView data;
	public static ActivityGroup selected_activity_group;
	public static int subbizid = 0;
	public int qualityvalue;
	public int bizid;
	public int drawablegp = 0;
	private int drawatt = 0;
	
	public BizneDataActivity(){
		data = GetBizneData.ans.getData();
		subbizid = GetBizneData.ans.getData().getSubBizId();
		bizid = MainActivity.answer.getBizId();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bizne_data);
		setElements();
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finishActivity();
				}
			});
	   	btn_home = (ImageButton) findViewById(R.id.btn_home);
	   	btn_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
	}
	
	public void finishActivity(){
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bizne_data, menu);
		return true;
	}
	
	private void setElements(){
		TextView tv;
		LinearLayout ll = (LinearLayout) findViewById(R.id.bizne_elements);
		
		// bizne name
		tv = (TextView)findViewById(R.id.bizne_name);
		tv.setVisibility(View.GONE);
		tv = (TextView)findViewById(R.id.lbl_name_section);
		tv.setText(data.getBizneName());
		
		//Select one
		tv = (TextView)findViewById(R.id.bizne_selectone);
		tv.setTypeface(null, Typeface.ITALIC);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		int p = (int) convertDpToPixel(5, this);
		params.setMargins(p, p, p, p);
		// Activity Groups
		for(int i = 0; i < data.getActivityGroups().size(); i++){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = getDrawableGroup();
			tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
			tv.setCompoundDrawablePadding(15);
			tv.setText(data.getActivityGroups().get(i).getName());
			final ActivityGroup act = data.getActivityGroups().get(i);
			if(data.getActivityGroups().get(i).isScheduled()){
				final int idg = data.getActivityGroups().get(i).getId();
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						openScheduledActivity(idg);
					}
				});
			}else{
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						selected_activity_group = act;
						goToGroupActivities();
					}
				});
			}
			ll.addView(tv);
		}
		// Activities
		for(int i = 0; i < data.getActivities().size(); i++){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = null;
			switch(data.getActivities().get(i).getType()){
			case 0: img = this.getResources().getDrawable( R.drawable.ic_survey );
				break;
			case 1:
				img = this.getResources().getDrawable( R.drawable.ic_trivia );
				break;
			case 2:
				img = this.getResources().getDrawable( R.drawable.ic_message );
				break;
			case 3:
				if(data.getActivities().get(i).isOffer()){
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
			final int points = data.getActivities().get(i).getPoint();
			final int actid = data.getActivities().get(i).getId();
			tv.setText(data.getActivities().get(i).getName());
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
		// Prizes
		if(data.getPrizes().size()>0){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = this.getResources().getDrawable( R.drawable.ic_gift2 );
			tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
			tv.setCompoundDrawablePadding(15);
			tv.setText("Premios");
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoPrizes();
				}
			});
			ll.addView(tv);
		}
		// Ofertas
		if(data.getOffers().size()>0){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = this.getResources().getDrawable( R.drawable.ic_sale );
			tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
			tv.setCompoundDrawablePadding(15);
			tv.setText("Ofertas");
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gotoOffers();
				}
			});
			ll.addView(tv);
		}
		// Checkin
		if(data.hasCheckins()){
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = this.getResources().getDrawable( R.drawable.ic_checkin );
			tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
			tv.setCompoundDrawablePadding(15);
			tv.setText("Me gusta");
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setCheckin();
				}
			});
			ll.addView(tv);
		}
		// Alert
		if(data.hasAlerts()){
			for(int i = 0; i < data.getAttentions().size(); i++){
				final int index = i;
				tv = new TextView(this);
				tv.setTextSize(18);
				tv.setLayoutParams(params);
				tv.setTypeface(null, Typeface.BOLD);
				tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				Drawable img = this.getDrawableAttention();
				tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
				tv.setCompoundDrawablePadding(15);
				tv.setText("Alerta: " + data.getAttentions().get(i).getName());
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						data.getAttentions().get(index).sendAttention(BizneDataActivity.this);
					}
				});
				ll.addView(tv);
			}
		}else{
			tv = new TextView(this);
			tv.setTextSize(18);
			tv.setLayoutParams(params);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			Drawable img = this.getResources().getDrawable( R.drawable.ic_alert );
			tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
			tv.setCompoundDrawablePadding(15);
			tv.setText("Atención");
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					sendBizneAlert();
				}
			});
			ll.addView(tv);
		}
		
		//Valuate
		tv = new TextView(this);
		tv.setTextSize(18);
		tv.setLayoutParams(params);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		Drawable img = this.getResources().getDrawable( R.drawable.ic_evaluate );
		tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
		tv.setCompoundDrawablePadding(15);
		tv.setText("Califica");
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		ll.addView(tv);
		
		// Extra tv for design
		tv = new TextView(this);
		tv.setTextSize(18);
		tv.setLayoutParams(params);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setGravity(Gravity.LEFT);
		tv.setText("");
		ll.addView(tv);
	}
	
	public void sendAlert(){
		ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&subbizid=" + subbizid;
		
		SendAlert getdata = new SendAlert(this, pd);
		getdata.execute(new String[] {data,"alertChild"});
	}
	
	public void sendBizneAlert(){
		ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&bizid=" + bizid + "&userid=" + c.getString(1).toString();
		
		SendAlert getdata = new SendAlert(this, pd);
		getdata.execute(new String[] {data,"alertBizne"});
	}
	
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}
	
	public void goToGroupActivities(){
		Intent intent = new Intent(this, ActivityGroupActivity.class);
		startActivity(intent);
	}

	private void openActivity(int actid){
		ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&actid=" + actid + "&subbizid=" + subbizid;
		
		GetActivity getdata = new GetActivity(this, pd);
		getdata.execute(new String[] {data,"getActivity"});
	}
	
	private void gotoOffers(){
		Intent intent = new Intent(this, OffersActivity.class);
		startActivity(intent);
	}
	
	private void setCheckin(){
		ProgressDialog pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		String data = "&bizid="+ bizid+
				"&userid=" + c.getString(1).toString();
		
		SendCheckin sendcheckin = new SendCheckin(this, pd);
		sendcheckin.execute(new String[] {data,"setCheckin"});
	}
	
	private void gotoPrizes(){
		ProgressDialog pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&bizid="+bizid + "&userid=" + c.getString(1).toString();
		
		GetPromos getpromos = new GetPromos(this, pd);
		getpromos.execute(new String[] {data,"getPromos"});
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
	
	private void openScheduledActivity(int idg){
		ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&idg=" + idg + "&subbizid=" + subbizid;;
		
		GetActivity getdata = new GetActivity(this, pd);
		getdata.execute(new String[] {data,"getAndroidScheduledActivity"});
	}
	
	private Drawable getDrawableGroup(){
		
		Drawable dble = null;
		
		if(drawablegp > 4){
			drawablegp = 0;
		}
		
		switch(drawablegp){
		case 0: dble = this.getResources().getDrawable( R.drawable.ic_group_r );
			break;
		case 1: dble = this.getResources().getDrawable( R.drawable.ic_group_b );
			break;
		case 2: dble = this.getResources().getDrawable( R.drawable.ic_group_y );
			break;
		case 3: dble = this.getResources().getDrawable( R.drawable.ic_group_g );
			break;
		case 4: dble = this.getResources().getDrawable( R.drawable.ic_group_p );
			break;
		}
		
		drawablegp++;
		
		return dble;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	public void showDialog(){
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.quality_dialog);
		dialog.setTitle("Selecciona la valoración");

//		// set the custom dialog components - text, image and button
//		TextView text = (TextView) dialog.findViewById(R.id.quality_dialog_title);
//		text.setText();
		TextView text;

		ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.btn_verygood);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				qualityvalue = 0;
				dialog.dismiss();
				setQuality(qualityvalue);
				
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_verygood);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qualityvalue = 0;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});
		
		dialogButton = (ImageButton) dialog.findViewById(R.id.btn_good);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				qualityvalue = 1;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_good);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qualityvalue = 1;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});
		
		dialogButton = (ImageButton) dialog.findViewById(R.id.btn_regular);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				qualityvalue = 2;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_regular);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qualityvalue = 2;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});
		
		
		
		dialogButton = (ImageButton) dialog.findViewById(R.id.btn_bad);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				qualityvalue = 3;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});
		
		text = (TextView) dialog.findViewById(R.id.txt_bad);
		text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				qualityvalue = 3;
				dialog.dismiss();
				setQuality(qualityvalue);
			}
		});

		dialog.show();
	}
	
	public void setQuality(int value){
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		ProgressDialog pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ bizid +
				"&value=" + value + "&userid=" + c.getString(1).toString();
		
		SendQualityValue sendquality = new SendQualityValue(this, pd);
		sendquality.execute(new String[] {data,"saveQuality"});
	}
	
	private Drawable getDrawableAttention(){
		
		Drawable dble = null;
		
		if(drawatt > 3){
			drawatt = 0;
		}
		
		switch(drawatt){
		case 0: dble = this.getResources().getDrawable( R.drawable.ic_alert );
			break;
		case 1: dble = this.getResources().getDrawable( R.drawable.ic_alert_b );
			break;
		case 2: dble = this.getResources().getDrawable( R.drawable.ic_alert_g );
			break;
		case 3: dble = this.getResources().getDrawable( R.drawable.ic_alert_r );
			break;
		case 4: dble = this.getResources().getDrawable( R.drawable.ic_alert );
			break;
		}
		
		drawatt++;
		
		return dble;
	}
}
