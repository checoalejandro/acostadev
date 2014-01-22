package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerBizneList;
import helper.json.AnswerGetChat;
import helper.json.AnswerGetInfo;
import helper.json.AnswerSearchBiz;
import helper.json.BizneInfoList;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.BizneInfoActivity;
import com.testapp.test.BizneListActivity;
import com.testapp.test.ChatActivity;
import com.testapp.test.ChatsActivity;

public class GetChat extends AsyncTask <String, Void, String> {
	
	ChatActivity activity;
	Context c;
	boolean close = false;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerGetChat act;

	public GetChat (ChatActivity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetSearchString-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetSearchString-doInBackground: Decodificando respuesta");
			
			AnswerGetChat answer = gson.fromJson(response.toString(), AnswerGetChat.class);
			GetChat.act = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			if(answer.getMsg().equals("No se encontraron resultados")){
				isSuccessfull = false;
				
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			try{
				JSonError jError = gson.fromJson(response.toString(), JSonError.class);
				if(jError.getMsg().equals("Ocurrió un error") || jError.getMsg().equals("Error al obtener los resultados")){
					result = "Resultados no encontrados.";
					Log.e("SocialGo", "GetSearchString-doInBackground: No hay actividad");
		//				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
					isSuccessfull = false;
				}else{
					result = "Communication error...";
					Log.e("SocialGo", "GetSearchString-doInBackground: fallo el envio del registo");
					Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e2){
//				Toast.makeText(this.activity, "No se han encontrado resultados" , Toast.LENGTH_SHORT).show();
				result = "No se han encontrado resultados a esta búsqueda.";
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetSearchString-onPostExecute: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetSearchString-onPostExecute: Decodificando respuesta");
		
		
		if(isSuccessfull){
			pd.dismiss();
			activity.setList(act.getMessages());
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
			if(this.close)
				activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  pd.dismiss();
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetSearchString-onPostExecute: Error cargando actividad"+ e.getMessage());
//		  activity.finish();
	  }
	
	
	
	}


}