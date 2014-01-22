package com.testapp.test;


import helper.threads.LoginUser;
import helper.threads.RegisterNewUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

final class LoginListenerClick implements OnClickListener{
	
	Activity activity = null;
	RegisterActivity regActivity = null;
	LoginActivity logActivity = null;
	
	public LoginListenerClick(Activity act){
		this.activity = act;
	}
	
	public LoginListenerClick(RegisterActivity act){
		this.regActivity = act;
	}
	
	public LoginListenerClick(LoginActivity log){
		this.logActivity = log;
	}
	
		
	String globalMsg;
	@Override
	public void onClick(View v) {
		Log.d("Qrivo", "LoginListenerClick-onClick: si dio un click");
		View parent = (View)v.getParent();
		
		switch (v.getId()) {
			
		case R.id.btn_login:
				try {
					Log.d("Qrivo", "LoginListenerClick-onClick: iniciando actividad principal");
					EditText userNameText = (EditText)this.logActivity.findViewById(R.id.username);
					String username = userNameText.getText().toString();
					
					EditText passwordText = (EditText)this.logActivity.findViewById(R.id.password);
					String password = passwordText.getText().toString();
					
					logActivity.pd = ProgressDialog.show(this.logActivity, "Cargando ...", "Login", true, false);
					
					String data = "&password="+password
								+"&username="+username;
							
					LoginUser userLoginThread = new LoginUser(this.logActivity,logActivity.pd);
					userLoginThread.execute(new String[] {data,"loginUser"});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("Qrivo", "LoginListenerClick-onClick: Error: "+e.toString());
					e.printStackTrace();
				} 
				
				
				
				break;
				
//			case R.id.btn_go_to_login:
//				Log.d("Qrivo", "LoginListenerClick-onClick: iniciando pantalla de login");
//	        	try {  
//					Class<?> clazz;
//					clazz = Class.forName("com.standardapp.standard" + ".LoginActivity");
//					Intent intent = new Intent(parent.getContext().getApplicationContext(), clazz);
//					//parent.getContext().startActivity(intent);
//					activity.startActivity(intent);
//					activity.finish();
//										
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					Log.d("Qrivo", "LoginListenerClick-onClick: Error: "+e.toString());
//					e.printStackTrace();
//				} 
//				
//				break;
			
	        case R.id.btn_go_to_register:
	        	Log.d("Qrivo", "LoginListenerClick-onClick: iniciando actividad de registro");
			Intent intent = new Intent(parent.getContext().getApplicationContext(), RegisterActivity.class);
			
			
			if(activity != null){
				activity.startActivity(intent);
//				activity.finish();
			}else if(logActivity != null){
				logActivity.startActivity(intent);
//				logActivity.finish();
			}else{
				parent.getContext().startActivity(intent);
			} 
	        	
	
	        	break;
	        	
	        case R.id.btn_register:
	        	
	        	Log.d("Qrivo", "LoginListenerClick-onClick: Verificando valores");
	    		
	    		EditText userNameText = (EditText) parent.findViewById(R.id.username);
	    		String userName = userNameText.getText().toString();
	    		
	    		EditText emailText = (EditText)parent.findViewById(R.id.email);
	    		String email = emailText.getText().toString();
	    		
	    		EditText passText = (EditText) parent.findViewById(R.id.password);
	    		String password = passText.getText().toString();
			
				if(!isValidEmail(email)){
					showAlert("El correo electrónico no tiene un formato valido", v);
					return;
				}
				
				
				try{
					Log.d("Qrivo", "LoginListenerClick-onClick: Datos listos y correctos para dar de alta al usuario");
					String data = "&username="+userName
							+"&password="+password
							+"&email="+email
							;
						this.regActivity.pd = ProgressDialog.show(this.regActivity, "Registrando Usuario ...", "Espere, por favor", true, false);
						RegisterNewUser newUserThread = new RegisterNewUser(this.regActivity);
						newUserThread.execute(new String[] {data,"registerUser"});
					
					
				}
				catch(Exception e)
				{
					//Exception handling
					Log.e("Qrivo", "LoginListenerClick-onClick: Algo salio mal a la hora de de registrar usuario");
			        e.printStackTrace();
				}
			
	    		
	        	
	        	break;
	        
	        
	        	
	        default:
	        	Log.d("Qrivo", "LoginListenerClick-onClick: no encontro el id del elemento click");
	            break;
        }
		
		
	}
	
		
	public static boolean isValidEmail(String strEmail)
	{
		String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"; 
		Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(strEmail);
        
        return m.find();

	}
	
	
	@SuppressLint("NewApi")
	public boolean isEmptyText(String userName, String email, String pass ){
		if(userName.isEmpty()){
			globalMsg = "El nombre de usuario esta vacio, favor de llenar el campo";
			return true;
		}
		
		else if(email.isEmpty()){
			globalMsg = "El email esta vacio, favor de llenar el campo";
			return true;
		}
		
		else if(pass.isEmpty()){
			globalMsg = "Favor de poner una contraseña";
			return true;
		}
		else{
		
			return false;
		}
		
	}
	
	public void showAlert(String msg, View v){
		AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getContext());
        // Set the message to display
        alertbox.setMessage(msg);
        // Add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            // Click listener on the neutral button of alert box
            public void onClick(DialogInterface arg0, int arg1) {
            	
            }
        });
         // show the alert box
        alertbox.show();
	}


}
