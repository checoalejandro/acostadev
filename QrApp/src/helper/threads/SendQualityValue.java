package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerActivity;
import helper.json.SaveAnsResponse;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class SendQualityValue extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	boolean pdmessage = false;
	volatile public ProgressDialog pd;
	
	public static String jsonEncode;
	public static AnswerActivity act;

	public SendQualityValue (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	public SendQualityValue (Activity act, ProgressDialog tempd, boolean pdmessage){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.pdmessage = pdmessage;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "SendAnswers-doInBackground: Enviando respuestas al servidor");
		String response = "";
		try {
			response = client.executeHttpRequest(data, method);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Log.d("Test", "Error--> " + e2.getMessage());
		}
		Gson gson = new Gson();
		try {
			
			Log.d("SocialGo", "GetActivity-doInBackground: Decodificando respuesta");
			
			SaveAnsResponse answer = gson.fromJson(response.toString(), SaveAnsResponse.class);
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){			
			result = "Communication error...";
			Log.e("SocialGo", "SendAnswers-doInBackground: fallo el envio del registo");
			Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			
		}
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetActivity-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetActivity-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			if(!pdmessage)
				Toast.makeText(activity, "Gracias por tu opinión." , Toast.LENGTH_LONG).show();
			
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetActivity-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}