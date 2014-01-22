package helper.threads;

import java.io.IOException;
import java.io.InputStream;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerActivity;
import helper.json.AnswerGetCategories;
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

public class GetCategories extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerGetCategories act;

	public GetCategories (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	
	public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = activity.getAssets().open("categories.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
//		HttpRequest client = new HttpRequest(c);
//		Log.d("SocialGo", "GetCategories-doInBackground: Enviando datos para login del usuario al servidor");
		String response = loadJSONFromAsset();
//		try {
//			response = client.executeHttpRequest(data, method);
//		} catch (Exception e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//			Log.d("Test", "Error--> " + e2.getMessage());
//		}
		Gson gson = new Gson();
		try {
			
			Log.d("SocialGo", "GetCategories-doInBackground: Decodificando respuesta");
			
			AnswerGetCategories answer = gson.fromJson(response.toString(), AnswerGetCategories.class);
			GetCategories.act = answer;
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
				Log.e("SocialGo", "GetCategories-doInBackground: No hay actividad");
//				Toast.makeText(this.activity, "No hay actividad asignada" , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetCategories-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}
		//this.activity.countGetIncidents = 0;
	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetCategories-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetCategories-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			
			/**open new activity **/
			
			DBAdapter database = new DBAdapter(this.c);
			database.open();
			for(int i = 0; i < act.getData().size(); i++){
				database.insertCategory(act.getData().get(i).getCatId(), act.getData().get(i).getName());
				if(!(act.getData().get(i).getSubcategories() == null))
					for(int j = 0; j < act.getData().get(i).getSubcategories().size(); j++){
					
						database.insertSubCategory(act.getData().get(i).getSubcategories().get(j).getCatId(), act.getData().get(i).getSubcategories().get(j).getName(), act.getData().get(i).getCatId());
				}
			}
			database.close();
			Toast.makeText(this.activity, "Categorías cargadas exitosamente", Toast.LENGTH_SHORT).show();
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
//			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetCategories-doInBackground: Error cargando actividad "+ e.getMessage());
		  pd.dismiss();
//		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}