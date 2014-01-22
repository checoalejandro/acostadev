package helper.threads;

import helper.http.HttpRequest;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class ThreadTest extends AsyncTask<String, Void, String>{

	Activity activity;
	
	@Override
	protected String doInBackground(String... params) {
		String data = params[0];
		String method = params[1];
		String result = "";
				
		try {
			HttpRequest client = new HttpRequest(activity);
			result = client.executeHttpRequest(data, method);
		} catch (Exception e) {
			Log.d("testapp", e.getMessage());
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {	
		super.onPostExecute(result);
		
		
	}

	
}
