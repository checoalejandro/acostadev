package helper.threads;

import helper.json.AnswerActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.testapp.test.Trivia;

public class URLgetString extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	int bizid;
	public static String jsonEncode;
	public static AnswerActivity act;

	public URLgetString (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		URL yahoo = null;
		try {
			yahoo = new URL("http://ubitip.area51.socialdot.net/index.php?option=com_qr&controller=android&view=android&tmpl=component&format=raw&task=loadActivity&bizid=201125&userid=750");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	BufferedReader in = null;
		try {
			in = new BufferedReader(
			            new InputStreamReader(
			            yahoo.openStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	String inputLine;

    	String last = "";
    	try {
			while ((inputLine = in.readLine()) != null){
				last = last + inputLine;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return last;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("Socialdeals", "SetUserCheckIn-doInBackgrounds: "+result.toString());
	
	try{
		Log.d("Socialdeals", "SetUserCheckIn-doInBackground: Decodificando respuesta");
		
		
		if(isSuccessfull){
			
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_SHORT).show();
			
			/**open new activity **/
			
			try {  
//				Class<?> clazz;
//				clazz = Class.forName("com.mx.socialdealsapp.socialdeals" + ".PriceActivity");
				Intent intent = new Intent(activity, Trivia.class);
				//intent.putExtra("answerJsonEncode", this.jsonEncode);
				activity.startActivity(intent);
									
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("Socialdeals", "SetUserCheckIn-doInBackground: Error: "+e.toString());
				e.printStackTrace();
			}
			
			
			//activity.finish();
			
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, result.toString() , Toast.LENGTH_LONG).show();
			activity.finish();
		}
	
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("Socialdeals", "SetUserCheckIn-doInBackground: Error Buscando restaurantes => "+ e.getMessage());
		  pd.dismiss();
		  activity.finish();
	  }
	
	  pd.dismiss();
	
	
	}


}