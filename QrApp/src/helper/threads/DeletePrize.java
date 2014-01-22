package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerActivity;
import helper.json.SaveAnsResponse;
import helper.json.UserPrize;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.PrizesExpandableAdapter;

public class DeletePrize extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	public static String jsonEncode;
	public static AnswerActivity act;
	private PrizesExpandableAdapter adapter;
	private UserPrize prize;
	int position;

	public DeletePrize (PrizesExpandableAdapter adapter, UserPrize prize, Activity act, int pos){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.adapter = adapter;
		this.prize = prize;
		this.position = pos;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "SendCheckin-doInBackground: Enviando respuestas al servidor");
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
			
			Log.d("SocialGo", "SendCheckin-doInBackground: Decodificando respuesta");
			
			SaveAnsResponse answer = gson.fromJson(response.toString(), SaveAnsResponse.class);
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){			
			result = "Communication error...";
			Log.e("SocialGo", "SendCheckin-doInBackground: fallo el envio del registo");
			Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			
		}
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "SendCheckin-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "SendCheckin-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			adapter.delete(prize,position);
//			pd.dismiss();
//			Toast.makeText(activity, "Te gusta este negocio" , Toast.LENGTH_LONG).show();
			
			
		}
		else{
//			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "SendCheckin-doInBackground: Error cargando actividad"+ e.getMessage());
//		  pd.dismiss();
//		  activity.finish();
	  }
	
//	  pd.dismiss();
	
	
	}


}