package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerActivity;
import helper.json.AnswerAndroidView;
import helper.json.QrScan;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.BizneDataActivity;
import com.testapp.test.Comment;
import com.testapp.test.MainActivity;
import com.testapp.test.Message;
import com.testapp.test.QrGameActivity;
import com.testapp.test.Survey;
import com.testapp.test.Trivia;
import com.testapp.test.WebActivity;

public class GetBizneData extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerAndroidView ans;
	public static String url;

	public GetBizneData (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetBizneData-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetBizneData-doInBackground: Decodificando respuesta");
//			response = "{\"status\":1,\"msg\":\"Datos obtenidos\",\"data\":{\"bizname\":\"Papeleria (Arellano Zamora Jose Alberto)\",\"actgps\":[{\"idg\":\"18\",\"name\":\"Todos\",\"activities\":[{\"idactivity\":\"138\",\"name\":\"Video con puntos 2\",\"type\":\"5\"},{\"idactivity\":\"131\",\"name\":\"Prueba premios\",\"type\":\"0\"},{\"idactivity\":\"127\",\"name\":\"Mi trivia\",\"type\":\"1\"},{\"idactivity\":\"126\",\"name\":\"Comments\",\"type\":\"2\"}]},{\"idg\":\"17\",\"name\":\"Videos\",\"activities\":[{\"idactivity\":\"138\",\"name\":\"Video con puntos 2\",\"type\":\"5\"}]}],\"acts\":[{\"idactivity\":\"126\",\"name\":\"Comments\",\"type\":\"2\"},{\"idactivity\":\"127\",\"name\":\"Mi trivia\",\"type\":\"1\"},{\"idactivity\":\"131\",\"name\":\"Prueba premios\",\"type\":\"0\"},{\"idactivity\":\"139\",\"name\":\"mi oferta\",\"type\":\"3\"}],\"offers\":[{\"groupid\":\"2\",\"name\":\"Anaquel 2\",\"offers\":[{\"offerid\":\"2\",\"groupid\":\"2\",\"name\":\"Refrescos al 2x1 en todas las presentaci\u00f3nes de Coca-Cola. No aplica con otros descuentos u otras promociones. \u00danicamente en productos para consumir en el local\",\"start\":\"2013-12-19\",\"finish\":\"2013-12-31\"}]}],\"prizes\":[{\"idpromo\":\"3\",\"description\":\"Rebanada de Pastel\",\"points\":\"50\"},{\"idpromo\":\"4\",\"description\":\"Muffin de chocolate\",\"points\":\"40\"}],\"checkins\":1,\"alerts\":4}}";
			AnswerAndroidView answer = gson.fromJson(response.toString(), AnswerAndroidView.class);
			GetBizneData.ans = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
				
			}
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("No existe actividad")){
				result = "No existe actividad";
				Log.e("SocialGo", "GetBizneData-doInBackground: No hay actividad");
//				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetBizneData-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetBizneData-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetBizneData-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			Intent intent = new Intent(this.c, BizneDataActivity.class);
			this.activity.startActivity(intent);
			
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Falló la conexión, inténtelo de nuevo más tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetBizneData-doInBackground: Error cargando actividad "+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}