package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerGetInfo;
import helper.json.AnswerGetProfile;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.BizneInfoActivity;
import com.testapp.test.ProfileActivity;
import com.testapp.test.QrResultsActivity;
import com.testapp.test.R;

public class GetProfile extends AsyncTask <String, Void, String> {
	
	ProfileActivity activity;
	Context c;
	boolean close = false;
	boolean open = false;
	boolean isSuccessfull = false;
	boolean qrresult = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerGetProfile act;

	public GetProfile (ProfileActivity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetBizInfo-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetBizInfo-doInBackground: Decodificando respuesta");
			
			AnswerGetProfile answer = gson.fromJson(response.toString(), AnswerGetProfile.class);
			GetProfile.act = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("Ocurrió un error")){
				result = "Ocurrió un error.";
				Log.e("SocialGo", "GetBizInfo-doInBackground: No hay actividad");
//				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetBizInfo-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetBizInfo-onPostExecute: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetBizInfo-onPostExecute: Decodificando respuesta");
		
		
		if(isSuccessfull){
			pd.dismiss();
			TextView tvName = (TextView) activity.findViewById(R.id.txtuser);
			TextView tvUserName = (TextView) activity.findViewById(R.id.txtusername);
			TextView tvEmail = (TextView) activity.findViewById(R.id.txtemail);
			
			tvName.setText(act.getProfile().getName());
			tvUserName.setText(act.getProfile().getUserName());
			tvEmail.setText(act.getProfile().getEmail());
			
			activity.ll.setVisibility(View.VISIBLE);
			
		}
		else{
			pd.dismiss();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetActivity-onPostExecute: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	 if(pd != null)
		pd.dismiss();
	
	
	}


}