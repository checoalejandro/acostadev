package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerActivity;
import helper.json.SaveAnsResponse;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.MainActivity;

public class SendQualityChildValue extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	boolean pdmessage = false;
	volatile public ProgressDialog pd;
	
	public static String jsonEncode;
	public static AnswerActivity act;

	public SendQualityChildValue (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	public SendQualityChildValue (Activity act, ProgressDialog tempd, boolean pdmessage){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.pdmessage = pdmessage;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "SendQualityChildValue-doInBackground: Enviando respuestas al servidor");
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
			
			Log.d("SocialGo", "SendQualityChildValue-doInBackground: Decodificando respuesta");
			
			SaveAnsResponse answer = gson.fromJson(response.toString(), SaveAnsResponse.class);
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){			
			result = "Communication error...";
			Log.e("SocialGo", "SendQualityChildValue-doInBackground: fallo el envio del registo");
			Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			
		}
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "SendQualityChildValue-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "SendQualityChildValue-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			if(!pdmessage)
				Toast.makeText(activity, "Gracias por tu opinión." , Toast.LENGTH_LONG).show();
			
			DBAdapter database = new DBAdapter(activity);
			database.open();
   	        Cursor cursor = database.getAllUsers();
   	        database.close();
   	        
   	        ProgressDialog pd;
   	        pd = ProgressDialog.show( activity, "Recibiendo información ...", "Espere, por favor", true, false);
			String data = "&bizid=" + MainActivity.answer.getBizId() +
					"&userid=" + cursor.getString(1).toString();
			
			GetBizInfo getinfo = new GetBizInfo(activity, pd);
			getinfo.execute(new String[] {data,"getBizneInfo"});
			
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "SendQualityChildValue-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
	  }
	
	  pd.dismiss();
	
	
	}


}