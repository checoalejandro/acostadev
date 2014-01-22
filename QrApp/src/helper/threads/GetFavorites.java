package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerFavorites;
import helper.json.Favorites;
import helper.json.FavoritesList;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.BusinessActivity;
import com.testapp.test.FavoritesActivity;
import com.testapp.test.MainActivity;

public class GetFavorites extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerFavorites act;
	boolean state = false;

	public GetFavorites (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	
	public GetFavorites (Activity act, ProgressDialog tempd, boolean state){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.state = state;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetPromos-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetPromos-doInBackground: Decodificando respuesta");
			
			AnswerFavorites answer = gson.fromJson(response.toString(), AnswerFavorites.class);
			GetFavorites.act = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("Erro al cargar la lista")){
				result = "Error al cargar la lista";
				Log.e("SocialGo", "GetPromos-doInBackground: No hay actividad");
//				Toast.makeText(this.activity, "No hay promociones asignadas" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetPromos-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetPromos-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetPromos-doInBackground: Decodificando respuesta");
		
		if(isSuccessfull){  
			FavoritesList fav = act.getData();
			DBAdapter database = new DBAdapter(this.activity);
			database.open();
			for(int i = 0; i < fav.size(); i++){
				database.insertFavorite(fav.get(i).getBizId(), fav.get(i).getCatId(), fav.get(i).getName());
			}
			database.close();
			
		}
		else{
//			pd.dismiss();
			Toast.makeText(activity, "No hay favoritos registrados." , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetPromos-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
//	  pd.dismiss();
	
	
	}


}