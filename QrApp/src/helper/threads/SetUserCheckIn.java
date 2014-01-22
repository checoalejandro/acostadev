package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerSetCheckIn;

import java.io.IOException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class SetUserCheckIn extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	String jsonEncode;

	public SetUserCheckIn (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "SetUserCheckIn-doInBackground: Enviando datos para login del usuario al servidor");
		String response = "";
		try {
			response = client.executeHttpRequest(data,method);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Gson gson = new Gson();
			Log.d("SocialGo", "SetUserCheckIn-doInBackground: Decodificando respuesta");
			AnswerSetCheckIn answer = gson.fromJson(response.toString(), AnswerSetCheckIn.class);
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			result = "Communication error...";
			Log.e("Socialdeals", "SetUserCheckIn-doInBackground: fallo el envio del registo");
			//Toast.makeText(this.activity.getApplicationContext(), "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("Socialdeals", "SetUserCheckIn-doInBackground: "+result.toString());
	
	try{
		Log.d("Socialdeals", "SetUserCheckIn-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_SHORT).show();
			
			/**open new activity **/
			
			try {  
				Class<?> clazz;
				clazz = Class.forName("com.mx.socialdealsapp.socialdeals" + ".PriceActivity");
				Intent intent = new Intent(activity, clazz);
				intent.putExtra("answerJsonEncode", this.jsonEncode);
				activity.startActivity(intent);
									
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Log.d("Socialdeals", "SetUserCheckIn-doInBackground: Error: "+e.toString());
				e.printStackTrace();
			}
			
			
			//activity.finish();
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_SHORT).show();
			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("Socialdeals", "SetUserCheckIn-doInBackground: Error Buscando restaurantes => "+ e.getMessage());
		  pd.dismiss();
		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}