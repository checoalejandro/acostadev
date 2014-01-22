package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerBizneList;
import helper.json.AnswerGetBestLists;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.BestListActivity;

public class GetBestLists extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bestlistid;
	public static String jsonEncode;
	public static AnswerGetBestLists act;

	public GetBestLists (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetBestList-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetBestList-doInBackground: Decodificando respuesta");
			
			AnswerGetBestLists answer = gson.fromJson(response.toString(), AnswerGetBestLists.class);
			GetBestLists.act = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("Erro al cargar la lista")){
				result = "Ocurrió un error.";
				Log.e("SocialGo", "GetBestList-doInBackground: No existe la lista de negocios");
				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetBestList-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetBestList-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetBestList-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
//			pd.dismiss();
			
			DBAdapter database = new DBAdapter(this.activity);
			
			database.open();
			for (int i = 0; i < act.getData().size(); i++){
				database.insertBestList(act.getData().get(i).getBestListId(), act.getData().get(i).getName(), act.getData().get(i).getDescription());
			}
			database.close();
			/**open new activity **/
			
//			activity.finish();
			
		}
		else{
//			pd.dismiss();
//			Toast.makeText(activity, "No existe la lista de negocios" , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetBestList-doInBackground: Error cargando actividad"+ e.getMessage());
//		  pd.dismiss();
//		  activity.finish();
	  }
	
//	  pd.dismiss();
	
	
	}


}