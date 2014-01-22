package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.BizneInfo;
import helper.json.SaveAnsResponse;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.MainActivity;

public class SetFavorite extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	
	BizneInfo info;

	public SetFavorite (Activity act, ProgressDialog tempd, BizneInfo info){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.info = info;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "SendComment-doInBackground: Enviando respuestas al servidor");
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
			
			Log.d("SocialGo", "SendComment-doInBackground: Decodificando respuesta");
			
			SaveAnsResponse answer = gson.fromJson(response.toString(), SaveAnsResponse.class);
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){			
			result = "Communication error...";
			Log.e("SocialGo", "SendComment-doInBackground: fallo el envio del registo");
			Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			
		}
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "SendComment-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "SendComment-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			if(!result.toString().equals("Este negocio ya está en tus Favoritos")){
			
				DBAdapter database = new DBAdapter(this.activity);
				database.open();
				database.insertFavorite(MainActivity.answer.getBizId(), info.getCatId(),info.getName());
				database.close();
			}
			
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "SendComment-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
	  }
	
	  pd.dismiss();
	
	
	}


}