package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
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
import com.testapp.test.Message;
import com.testapp.test.Trivia;

public class SendComment extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public int bizid;

	public SendComment (Activity act, ProgressDialog tempd, int bizid){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.bizid = bizid;
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "SendComment-doInBackground: Enviando respuestas al servidor");
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
			
			Log.d("SocialGo", "SendComment-doInBackground: Decodificando respuesta");
			
			SaveAnsResponse answer = gson.fromJson(response.toString(), SaveAnsResponse.class);
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){			
			result = "Communication error...";
			Log.e("SocialGo", "SendComment-doInBackground: fallo el envio del registo");
			Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			
		}
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "SendComment-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "SendComment-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
			openBizne();
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
			openBizne();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "SendComment-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}
	
	public void openBizne(){
//		this.activity.finish();
		
		DBAdapter database = new DBAdapter(this.activity);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		final int bizid = MainActivity.answer.getBizId();
		ProgressDialog pd;
 				pd = ProgressDialog.show( this.activity, "Recibiendo información ...", "Espere, por favor", true, false);
 				
 				String data = "&bizid=" + bizid +
 						"&userid=" + cursor.getString(1).toString();
 				
 				GetBizInfo getinfo = new GetBizInfo(this.activity, pd,true,false);
				getinfo.execute(new String[] {data,"getBizneInfo"});
	}
	


}