package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerActivity;
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
import com.testapp.test.Comment;
import com.testapp.test.MainActivity;
import com.testapp.test.Message;
import com.testapp.test.QrGameActivity;
import com.testapp.test.Survey;
import com.testapp.test.Trivia;
import com.testapp.test.WebActivity;

public class GetActivity extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerActivity act;
	public static String url;

	public GetActivity (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetActivity-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			AnswerActivity answer = gson.fromJson(response.toString(), AnswerActivity.class);
			GetActivity.act = answer;
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
				Log.e("SocialGo", "GetActivity-doInBackground: No hay actividad");
//				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = jError.getMsg();
				Log.e("SocialGo", "GetActivity-doInBackground: fallo el envio del registo");
//				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetActivity-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetActivity-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			
			/**open new activity **/
			
			try {  
				Intent intent;
				switch(act.getActivity().getType()){
				case 0: 
					intent = new Intent(activity, Survey.class);
					activity.startActivity(intent);
					break;
				case 1: 
					intent = new Intent(activity, Trivia.class);
					activity.startActivity(intent);
					break;
				case 2:
					intent = new Intent(activity, Comment.class);
					activity.startActivity(intent);
					break;
				case 3:
					intent = new Intent(activity, Message.class);
					activity.startActivity(intent);
					break;
				case 4:
					
					url = act.getData().getLink();
					intent = new Intent(activity, WebActivity.class);
					activity.startActivity(intent);
//					intent = new Intent(Intent.ACTION_VIEW);
//					intent.setData(Uri.parse(act.getData().getLink()));
//					activity.startActivity(intent);
					break;
				case 5:
					
//					url = act.getData().getLink();
//					intent = new Intent(activity, WebActivity.class);
//					activity.startActivity(intent);
					intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(act.getData().getLink()));
					activity.startActivity(intent);
					break;
				case 6:
					intent = new Intent(activity, QrGameActivity.class);
					activity.startActivity(intent);
				}
									
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("SocialGo", "GetActivity-doInBackground: Error: "+e.toString());
				e.printStackTrace();
			}
			
			
//			activity.finish();
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetActivity-doInBackground: Error cargando actividad "+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}