package helper.threads;

import java.util.List;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerGetZones;
import helper.json.Zone;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

public class GetZones extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetZones answer;
	boolean state = false;

	public GetZones (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetZones-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetZones-doInBackground: Decodificando respuesta");
//			response ="{\"status\":1,\"msg\":\"Zonas obtenidas\",\"data\":[{\"id\":\"5\",\"name\":\"Alexander\",\"lng\":\"-103.37974637747\",\"lat\":\"20.636069082128\",\"type\":\"user\"},{\"id\":\"8\",\"name\":\"cafeter\u00eda\",\"lng\":\"-103.36811263114\",\"lat\":\"20.667024406508\",\"type\":\"user\"},{\"id\":\"9\",\"name\":\"julio\",\"lng\":\"-103.37974637747\",\"lat\":\"20.636069082128\",\"type\":\"user\"},{\"id\":\"4\",\"name\":\"Providencia\",\"lng\":\"20\",\"lat\":\"50\",\"type\":\"default\"}]}";
			AnswerGetZones answer = gson.fromJson(response.toString(), AnswerGetZones.class);
			GetZones.answer = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			try{
				JSonError jError = gson.fromJson(response.toString(), JSonError.class);
				if(jError.getMsg().equals("Erro al cargar la lista")){
					result = "Error al cargar la lista";
					Log.e("SocialGo", "GetZones-doInBackground: No hay actividad");
	//				Toast.makeText(this.activity, "No hay promociones asignadas" , Toast.LENGTH_SHORT).show();
					isSuccessfull = false;
				}else{
					result = "Communication error...";
					Log.e("SocialGo", "GetZones-doInBackground: fallo el envio del registo");
					Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e2){
				isSuccessfull = false;
			}
			
		}

	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetZones-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetZones-doInBackground: Decodificando respuesta");
		
		if(isSuccessfull){  
			
			List<Zone> zones = answer.getData();
			DBAdapter database = new DBAdapter(this.activity);
			
			database.open();
			database.deleteZones();
			for(int i = 0; i < zones.size(); i++){
				database.insertZone(zones.get(i).getName(), zones.get(i).getLatitude(), zones.get(i).getLongitude(), zones.get(i).getId(), zones.get(i).getType());
			}
			
			database.close();
		}
		else{
//			Toast.makeText(activity, "No existen zonas almacenadas." , Toast.LENGTH_LONG).show();
		}
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetZones-doInBackground: Error cargando actividad"+ e.getMessage());
	  }
	
	}


}