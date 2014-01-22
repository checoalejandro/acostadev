package com.testapp.test;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class RegisterActivity extends Activity {

	public EditText editTextUsername ;
	public EditText editTextPassword ;
	public EditText editTextEmail;
	public ImageButton btn_back;
	
	public Button btnRegister;
	
	volatile public ProgressDialog pd;

	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginListenerFocus listenerFocus = new LoginListenerFocus();
        LoginListenerClick listenerClick = new LoginListenerClick(this);
        
        editTextUsername = (EditText)findViewById(R.id.username);
        editTextUsername.setOnFocusChangeListener(listenerFocus);
        
        editTextPassword = (EditText)findViewById(R.id.password);
        editTextPassword.setOnFocusChangeListener(listenerFocus);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextEmail.setOnFocusChangeListener(listenerFocus);
        
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(listenerClick);
        
	}
    
}
