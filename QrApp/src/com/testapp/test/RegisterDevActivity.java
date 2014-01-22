package com.testapp.test;

import static com.testapp.test.CommonUtilities.SENDER_ID;
import static com.testapp.test.CommonUtilities.SERVER_URL;

import android.app.Activity;
import android.content.Intent;

public class RegisterDevActivity{
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();
	
	// Internet detector
	ConnectionDetector cd;
	
	public void registerGCM(Activity c, String name, String email, int userid){

		cd = new ConnectionDetector(c.getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(c,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}

		// Check if GCM configuration is set
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(c, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			 return;
		}
		
		// Check if user filled the form
		if(name.trim().length() > 0 && email.trim().length() > 0){
			// Launch Main Activity
			Intent i = new Intent(c, MainActivity.class);
			
			// Registering user on our server					
			// Sending registraiton details to GCMConnection
			i.putExtra("name", name);
			i.putExtra("email", email);
			c.startActivity(i);
			c.finish();
		}else{
			// user doen't filled that data
			// ask him to fill the form
			alert.showAlertDialog(c, "Registration Error!", "Please enter your details", false);
		}
	}

}
