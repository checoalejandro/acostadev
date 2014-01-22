package helper.threads;

import com.google.gson.Gson;
import com.testapp.test.MainActivity;
import com.testapp.test.R;
import com.testapp.test.RegisterActivity;

import helper.json.AnswerRegisterUser;
import helper.http.HttpRequest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterNewUser extends AsyncTask <String, Void, String>{
	
	Activity act;
	Context c;
	RegisterActivity activity;
	Boolean openLoginActivity = false;
		
	public RegisterNewUser(RegisterActivity temp){
		this.c = temp.getApplicationContext();
		this.activity = temp;
		this.act = temp;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(act);
		Log.d("Qrivo", "RegisterNewUser-doInBackground: Enviando registro de usuario al servidor");
		
		
		try {
			String response = client.executeHttpRequest(data,method);
    		Gson gson = new Gson();
    		Log.d("Qrivo", "RegisterNewUser-doInBackground: Decodificando respuesta");
    		AnswerRegisterUser answer = gson.fromJson(response.toString(), AnswerRegisterUser.class);
    		if(answer.getStatus() == 1){
    			openLoginActivity = true;
    			
    		}
       		result = answer.getMsg();
		}
		catch(Exception e){
			result = "Hubo un error, favor de intentar de nuevo";
			Log.e("Qrivo", "RegisterNewUser-doInBackground: fallo el envio del registo");
			//Toast.makeText(this.activity.getApplicationContext(), "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
		}
		//this.activity.countGetIncidents = 0;
        return result;
	
	}

 
 
	protected void onPostExecute(String result) {
		// result is the value returned from doInBackground
		activity.pd.dismiss();
		Toast.makeText(c, result.toString() , Toast.LENGTH_LONG).show();
		if(openLoginActivity){
			Log.d("Qrivo", "RegisterNewUser-onPostExecute: Inicia configuración para ejcutar la Actividad Qrivo");
			
      
//			Intent intent = new Intent(c, GCMConnection.class);
//			activity.startActivity(intent);
			activity.finish(); 
		}else{
			final Dialog dialog = new Dialog(activity, R.style.FullHeightDialog);
			dialog.setContentView(R.layout.custom_dialog_message);
			dialog.setCancelable(true);
			//dialog.setTitle("Message");
			
			TextView text = (TextView)dialog.findViewById(R.id.text_msg_dialog);
			text.setText(result.toString());
			ImageButton btn = (ImageButton)dialog.findViewById(R.id.dialog_btn_close);
			
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {					
					dialog.dismiss();
				}
			});
			
			dialog.show();
		}
	}

}
