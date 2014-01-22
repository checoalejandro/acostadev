package com.testapp.test;

import com.google.gson.Gson;

import helper.database.DBAdapter;
import helper.json.QrScan;
import helper.threads.SetUserCheckIn;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class ScanQrActivity extends Activity{
	volatile public ProgressDialog pd;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	QrScan answer;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	boolean isPlainText = false;
    	
    	 if (requestCode == 0) {
    		 if (resultCode == RESULT_OK) {
       	         String contents = intent.getStringExtra("SCAN_RESULT");
       	         
       	         
    	   	     try {
    	  			Gson gson = new Gson();
    	  			Log.d("Socialdeals", "SetUserCheckIn-doInBackground: Decodificando respuesta");
    	  			answer = gson.fromJson(contents.toString(), QrScan.class);
    	  			
    	   	     }
    		  	 catch(Exception e){
    		  			
    	  			Log.e("Socialdeals", "SetUserCheckIn-doInBackground: fallo el envio del registo");
    	  			//Toast.makeText(this, "QR no valido, intentelo de nuevo" , Toast.LENGTH_SHORT).show();
    	  			isPlainText = true;
    	  			//finish();
    		  	 }
    	   	     
    	   	     database.open();
    	         cursor = database.getAllUsers();
    	         database.close();
    	   	     
    	   	     if(isPlainText == false && answer.getType() == 2 ){
    	   	    	pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
    		  		try{	
    		  			
    		  			String data = "userid="+cursor.getString(1).toString()
    		  						+ "&sharedid="+answer.getBizId()
    		  						;
    		  			
    		  			SetUserCheckIn checkin = new SetUserCheckIn(this, pd );
    		  			checkin.execute(new String[] {data,"checkIn"});
    		  			
    		  		} catch (Exception e) {
    		  			// TODO Auto-generated catch block
    		  			//pd.dismiss();
    		  			Log.d("Socialdeals", "SetUserCheckIn-onCreate: Error: "+e.toString());
    		  			e.printStackTrace();
    		  			finish();
    		  		}
       	        
    	   	     }
    	   	     
    	   	     if(isPlainText == false && answer.getType() == 2){
    	   	    	//Toast.makeText(this, contents.toString(), Toast.LENGTH_LONG).show();
    	   	    	pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
    		  		try{
    		  			
    		  		} catch (Exception e) {
    		  			// TODO Auto-generated catch block
    		  			//pd.dismiss();
    		  			Log.d("Socialdeals", "SetUserCheckIn-onCreate: Error: "+e.toString());
    		  			e.printStackTrace();
    		  			finish();
    		  		}
    	   	     }
    	   	     
    	   	     
    	   	     if(isPlainText == true){
    	   	    	 
    	   	    	if(contents.startsWith("http://") || contents.startsWith("https://") || contents.startsWith("www.")){
    	   	    	
	    	   	    	try{
	    	   	    		//
	    	   	    		if (!contents.startsWith("http://") && !contents.startsWith("https://")){
	    	   	    		   contents = "http://" + contents;
	    	   	    		}
	    	   	    		//URL url = new URL(contents.toString());
	    	   	    		//if the application doen crash it means that is a valid url 
//	    	   	    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(contents.toString()));
//	    	   	    		startActivity(browserIntent);
	
	    	   	    	}
	    	   	    	catch(Exception e){
	    	   	    		e.printStackTrace();
	    	   	    	}
    	   	    	
    	   	    	}
    	   	    	else{
    	   	    		Toast.makeText(this, contents.toString(), Toast.LENGTH_LONG).show();
//    	   	    		try {  
//    						Class<?> clazz;
//    						clazz = Class.forName("com.mx.socialdealsapp.socialdeals" + ".QrMessageActivity");
//    						Intent intent2 = new Intent(getApplicationContext(), clazz);
//    						intent2.putExtra("qr_message", contents.toString());
//    						startActivity(intent2);
//    											
//    					} catch (ClassNotFoundException e) {
//    						// TODO Auto-generated catch block
//    						Log.d("Socialdeals", "SocialdealsListenerClick-onClick: Error: "+e.toString());
//    						e.printStackTrace();
//    					}
    	   	    	}
    	   	    	
    	   	    	
    	   	     }
       	         
       	         
       	          
       	         // Handle successful scan
       	      } else if (resultCode == RESULT_CANCELED) {
       	         // Handle cancel
       	    	  //finish();
       	      }

    	   }
    }

}
