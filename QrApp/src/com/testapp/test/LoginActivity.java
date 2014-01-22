package com.testapp.test;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import helper.database.DBAdapter;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	public EditText editTextUsername ;
	public EditText editTextPassword ;
	public Button btnRegister;
	public Button btnLogin;
	public Button btnFacebookConnect;
	
	volatile public ProgressDialog pd;
	//volatile public ProgressDialog pd2;
	volatile public DBAdapter database = new DBAdapter(this);

	//**** ther next variables are a requirement to allow facebook connect ****/
	
	
	
	//private final Handler mFacebookHandler = new Handler();
//	private FacebookConnector facebookConnector;
	
    final Runnable mUpdateFacebookNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
        }
    };
	
	//************* End the facebook variables ******/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
//        this.facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, getApplicationContext(), FACEBOOK_PERMISSION);
        
        
        LoginListenerClick listenerClick = new LoginListenerClick(this);
        LoginListenerFocus listenerFocus = new LoginListenerFocus();
        
//        FacebookOnClickListener listenerFacebook = new FacebookOnClickListener(this, facebookConnector);
        
        btnFacebookConnect = (Button) findViewById(R.id.btn_facebook_connect_login);
//        btnFacebookConnect.setOnClickListener(listenerFacebook);
        
        editTextUsername = (EditText)findViewById(R.id.username);
        editTextUsername.setOnFocusChangeListener(listenerFocus);
        
        editTextPassword = (EditText)findViewById(R.id.password);
        editTextPassword.setOnFocusChangeListener(listenerFocus);
        
        btnRegister = (Button) findViewById(R.id.btn_go_to_register);
        btnRegister.setOnClickListener(listenerClick);
        
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(listenerClick);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
	}
    
}
