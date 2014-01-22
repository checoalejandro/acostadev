package helper.threads;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.ActivityGroup;
import helper.json.AnswerGetActivityGroup;
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
import com.testapp.test.ActivityGroupActivity;
import com.testapp.test.BizneDataActivity;
import com.testapp.test.OnlyActivityGroupActivity;

public class GetActivityGroup extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	ActivityGroup group;
	public static String jsonEncode;
	public static AnswerGetActivityGroup act;
	public static String url;

	public GetActivityGroup (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("Qrivo", "GetActivityGroup-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("Qrivo", "GetActivityGroup-doInBackground: Decodificando respuesta");
			
			AnswerGetActivityGroup answer = gson.fromJson(response.toString(), AnswerGetActivityGroup.class);
			GetActivityGroup.act = answer;
			group = act.getGroup();
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
				Log.e("Qrivo", "GetActivityGroup-doInBackground: No hay actividad");
//				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("Qrivo", "GetActivityGroup-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("Qrivo", "GetActivityGroup-doInBackground: "+result.toString());
	
	try{
		Log.d("Qrivo", "GetActivityGroup-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			
			/**open new activity **/
			
			try {  
				if(group.isScheduled()){
					final int idg = group.getId();
					openScheduledActivity(idg);
				}else{
					BizneDataActivity.selected_activity_group = group;
					goToGroupActivities();
				}
									
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Qrivo", "GetActivityGroup-doInBackground: Error: "+e.toString());
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
		  Log.e("Qrivo", "GetActivityGroup-doInBackground: Error cargando actividad "+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}
	
	private void openScheduledActivity(int idg){
		ProgressDialog pd = ProgressDialog.show( this.activity, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this.activity);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&idg=" + idg;
		
		GetActivity getdata = new GetActivity(activity, pd);
		getdata.execute(new String[] {data,"getAndroidScheduledActivity"});
	}

	public void goToGroupActivities(){
		Intent intent = new Intent(this.activity, OnlyActivityGroupActivity.class);
		activity.startActivity(intent);
	}

}