package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerBizneList;
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

public class GetBestList extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	boolean save =false;
	int bestlistid;
	public static String jsonEncode;
	public static AnswerBizneList act;

	public GetBestList (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	
	public GetBestList (Activity act, ProgressDialog tempd, boolean save){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.save = save;
		
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
			
			AnswerBizneList answer = gson.fromJson(response.toString(), AnswerBizneList.class);
			GetBestList.act = answer;
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
				Toast.makeText(this.activity, "No existe la lista" , Toast.LENGTH_SHORT).show();
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
			
			pd.dismiss();
			
			if(!this.save){
				if(!result.equals("Ya está almacenada")){
					DBAdapter database = new DBAdapter(this.activity);
					
					database.open();
					Cursor c = database.getBestList(act.getBestList().getBestListId());
					if(c.getCount() == 0)
						database.insertBestList(act.getBestList().getBestListId(), act.getBestList().getName(), act.getBestList().getDescription());
					database.close();
					Toast.makeText(activity, "Se ha almacenado esta lista en tus favoritos" , Toast.LENGTH_LONG).show();
				}
			}
			/**open new activity **/
			
			Intent intent;
			intent = new Intent(activity, BestListActivity.class);
			activity.startActivity(intent);
			
//			activity.finish();
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, "No existe la lista de negocios" , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetBestList-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}