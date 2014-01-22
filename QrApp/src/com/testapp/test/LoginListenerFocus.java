package com.testapp.test;


import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

final class LoginListenerFocus implements OnFocusChangeListener{

		
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			
			Log.d("Socialdeals", "LoginListenerFocus-onFocusChange: si tiene el foco");
			View parent = (View)v.getParent();
			
			switch (v.getId()) {
			
				case R.id.username:
		        	Log.d("Socialdeals", "LoginListenerClick-onClick: borrando datos de username");
		        	EditText editText = (EditText)parent.findViewById(R.id.username);
		        	String usrContent = parent.getContext().getString(R.string.label_login_username);
		        	if(usrContent.equals(editText.getText().toString())){
		        		editText.setText("");
		        	}
	
	        	break;
		        		        
		        case R.id.password:
		        	Log.d("Socialdeals", "LoginListenerFocus-onFocusChange: borrando datos de password");
		        	EditText editText2 = (EditText)parent.findViewById(R.id.password);
		        	String pswContent = parent.getContext().getString(R.string.label_login_password);
		        	if(pswContent.equals(editText2.getText().toString())){
		        		editText2.setText("");
		        		editText2.setTransformationMethod(new PasswordTransformationMethod());
		        	}
		        	
		        	break;
		        	
		        case R.id.email:
		        	Log.d("Socialdeals", "LoginListenerClick-onClick: borrando datos de email");
		        	EditText editText3 = (EditText)parent.findViewById(R.id.email);
		        	String emailContent = parent.getContext().getString(R.string.label_login_email);
		        	if(emailContent.equals(editText3.getText().toString())){
		        		editText3.setText("");
		        	}
		        	
		        	break;
		        	
//		        case R.id.search:
//		        	Log.d("Socialdeals", "LoginListenerClick-onClick: borrando datos de search");
//		        	EditText editText4 = (EditText)parent.findViewById(R.id.search);
//		        	String searchContent = parent.getContext().getString(R.string.label_search);
//		        	String searchContent2 = parent.getContext().getString(R.string.label_search_friends);
//		        	if(searchContent.equals(editText4.getText().toString()) || searchContent2.equals(editText4.getText().toString()) ){
//		        		editText4.setText("");
//		        	}
//		        	
//		        	break;
		        	
		        	
		        default:
		        	Log.d("Socialdeals", "LoginListenerFocus-onFocusChange: no encontro el id del elemento");
		            break;
	        }
			
		}

	}

}
