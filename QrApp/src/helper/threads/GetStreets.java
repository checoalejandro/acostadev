package helper.threads;

import java.util.List;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerGetStreets;
import helper.json.Street;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class GetStreets extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetStreets answer;
	List<Street> streets;
	boolean state = false;

	public GetStreets (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetStreets-doInBackground: Solicitando calles");
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
			
			Log.d("SocialGo", "GetStreets-doInBackground: Decodificando respuesta");
			AnswerGetStreets answer = gson.fromJson(response.toString(), AnswerGetStreets.class);
			GetStreets.answer = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("Error al obtener las calles")){
				result = "Error al obtener las calles";
				Log.e("SocialGo", "GetStreets-doInBackground: No hay calles");
				Toast.makeText(this.activity, result , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetStreets-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el la solicitud de las calles" , Toast.LENGTH_SHORT).show();
			}
			
		}

	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetStreets-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetStreets-doInBackground: Decodificando respuesta");
		
		if(isSuccessfull){  
			
			streets = answer.getData();
			DBAdapter db = new DBAdapter(activity);
			db.open();
			for (int i = 0; i < streets.size(); i++){
				db.insertStreet(streets.get(i).getName(), streets.get(i).getId());
			}
			db.close();
			
		}
		else{
//			pd.dismiss();
//			Toast.makeText(activity, "No hay calles." , Toast.LENGTH_LONG).show();
		}
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetStreets-doInBackground: Error cargando calles"+ e.getMessage());
//		  pd.dismiss();
	  }
		
	}

}