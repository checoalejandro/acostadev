package helper.threads;


import com.google.gson.Gson;
import com.testapp.test.LoginActivity;
import com.testapp.test.R;

import helper.json.AnswerRegisterUser;
import helper.json.User;
import helper.database.DBAdapter;
import helper.http.HttpRequest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class LoginUser extends AsyncTask <String, Void, String> {
	
	Activity act;
	LoginActivity activity;
	Context c ;
	boolean openLoginActivity = false;
	User user;
	ProgressDialog pd;
	
	public LoginUser(LoginActivity act, ProgressDialog pd){
		this.activity = act;
		this.c = this.activity.getBaseContext();
		this.act = act;
		this.pd = pd;
	}
	
	protected String doInBackground(String... params) {
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(act);
		Log.d("Qrivo", "LoginUser-doInBackground: Enviando datos para login del usuario al servidor");
		
		
		try {
			String response = client.executeHttpRequest(data,method);
    		Gson gson = new Gson();
    		Log.d("Qrivo", "LoginUser-doInBackground: Decodificando respuesta");
    		AnswerRegisterUser answer = gson.fromJson(response.toString(), AnswerRegisterUser.class);
    		if(answer.getStatus() == 1){
    			openLoginActivity = true;
    		}
       		user = answer.getData();
       		result = answer.getMsg();
		}
		catch(Exception e){
			result = activity.getString(R.string.error_incident_comunication);
			Log.e("Qrivo", "LoginUser-doInBackground: fallo el envio del registo");
			//Toast.makeText(this.activity.getApplicationContext(), "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
		}
		//this.activity.countGetIncidents = 0;
        return result;
	}
	
	
	protected void onPostExecute(String result) {
//		// result is the value returned from doInBackground
//						
//		// insert in the database
		if( user.getUserId() > 0){
			activity.database.open();
			long id;
			id = activity.database.insertUser(user.getUserId(), user.getUserName(), user.getName(), user.getPassword(), user.getEmail());
			activity.database.close();
			
			activity.database.open();
			Cursor c = activity.database.getUser(id);
			activity.database.close();
			if(!c.getString(1).equals("0")){
				Log.d("Qrivo", "LoginUser-onPostExecute: Inicia configuración para ejcutar la Actividad Qrivo");
				
				ProgressDialog pd;
				DBAdapter database = new DBAdapter(this.activity);
				database.open();
	            Cursor cursor = database.getAllUsers();
	            database.close();
				
				pd = ProgressDialog.show( this.act, "Recibiendo datos de usuario...", "Espere, por favor", true, false);
				
				String data = "&userid=" + cursor.getString(1).toString();
				
				GetBestLists getlists = new GetBestLists(this.activity,null);
				getlists.execute(new String[] {data,"getVisitedBestLists"});
				
		    	data = "&userid=" + cursor.getString(1).toString();
				
		    	GetFavorites getfavorites = new GetFavorites(this.activity, null);
				getfavorites.execute(new String[] {data,"getFavorites"});
				
		    	GetRegions getregions = new GetRegions(this.activity, this.pd,user.getUserName(),user.getEmail(),user.getUserId());
		    	getregions.execute(new String[] {data,"getRegions"});
					
				pd.dismiss();
				
			}
			else{
				Toast.makeText(this.activity.getApplicationContext(), result.toString() , Toast.LENGTH_LONG).show();
				activity.pd.dismiss();
			}
			
		}
		else{
			
			Toast.makeText(this.activity.getApplicationContext(), "Error en el inicio de sesión, verifica que hayas ingresado el usuario y contraseña correcta." , Toast.LENGTH_LONG).show();
			activity.pd.dismiss();
			Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
			activity.startActivity(intent);
			activity.finish(); 
		}
		
	}
	



}
