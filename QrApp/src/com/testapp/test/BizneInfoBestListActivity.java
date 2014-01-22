package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.BizneInfo;
import helper.threads.GetOnlyBizInfo;
import helper.threads.GetPromos;
import helper.threads.SetFavorite;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BizneInfoBestListActivity extends Activity {
	
	BizneInfo info;
	public Intent callIntent;
	
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	volatile public ProgressDialog pd;
	
	public BizneInfoBestListActivity(){
		this.info = GetOnlyBizInfo.act.getBizneInfo();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bizne_info_best_list);
		setElements();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bizne_info_best_list, menu);
		
		if(info.getTelephone().length() > 0)
			menu.add(1, 1, Menu.FIRST, "Llamar");
		
		if(info.getEmail().length() > 0)
			menu.add(2, 2, Menu.FIRST, "Enviar Correo");
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
    	
		ImageButton btn_prizes = (ImageButton) findViewById(R.id.btn_prizes3);
		btn_prizes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goToPrizes();
			}
		});
		
		ImageButton btn_add_favorite = (ImageButton) findViewById(R.id.btn_add_favorite);
		btn_add_favorite.setVisibility(ImageButton.VISIBLE);
		btn_add_favorite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setFavorite();
			}
		});
    	
		TextView tv = (TextView) findViewById(R.id.lbl_bizname);
		tv.setText(info.getName());
		tv = (TextView) findViewById(R.id.lbl_category);
		tv.setText(info.getCategory());
		tv = (TextView) findViewById(R.id.lbl_address);
		tv.setText(info.getAddress());
		tv = (TextView) findViewById(R.id.lbl_telephone);
		tv.setText(info.getTelephone());
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        callPhone();
			}
		});
		tv = (TextView) findViewById(R.id.lbl_email);
		if(info.getEmail() == ""){
			tv.setVisibility(TextView.GONE);
		}
		
		tv.setText(info.getEmail());
	}
	
	public void callPhone(){
	    try {
	        callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:"+this.info.getTelephone()));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	        Log.e("dialing-example", "Call failed", activityException);
	    }
	}
	
	public void sendEmail(){
		if(this.info.getEmail().length() > 0){
		
			try{
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[]{this.info.getEmail()});		  
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Selecciona tu cliente de correo :"));
			}catch(ActivityNotFoundException activityException){
				Log.e("BizneInfo", "Email failed", activityException);
			}
		}else{
			Toast.makeText(this, "El negocio no cuenta con correo electrónico" , Toast.LENGTH_LONG).show();
		}
	}
	
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	
	    switch(item.getItemId())
	    {
		    case 1:
		    	// first we delete the table of users.
		    	callPhone();
		        return true;
		    case 2:
		    	sendEmail();
		    	return true;
		    default:
		   	 return false;
		   	 
	
	    }
    }

	public void goToPrizes(){
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
		
		pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ MainActivity.answer.getBizId() + "&userid=" + cursor.getString(1).toString();
		
		GetPromos getpromos = new GetPromos(this, pd);
		getpromos.execute(new String[] {data,"getPromos"});
	}
    
    public void setFavorite(){
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
		
		pd = ProgressDialog.show( this, "Agregando a favoritos...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ MainActivity.answer.getBizId() + "&userid=" + cursor.getString(1).toString();
		
		SetFavorite setfavorite = new SetFavorite(this, pd,this.info);
		setfavorite.execute(new String[] {data,"setFavorite"});
    }
}
