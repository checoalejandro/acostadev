package helper.threads;

import helper.http.HttpRequest;
import helper.json.AnswerGetParkings;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.testapp.test.ParkingsMapActivity;
import com.testapp.test.R;

public class GetParkings extends AsyncTask <String, Void, String> {
	
	ParkingsMapActivity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetParkings answer;
	boolean state = false;

	public GetParkings (ParkingsMapActivity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("Qrivo", "GetParkings-doInBackground: Enviando datos para recibir estacionamientos");
		String response = "";
		try {
			response = client.executeHttpRequest(data, method, true);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Log.d("Qrivo", "Error--> " + e2.getMessage());
		}
		Gson gson = new Gson();
		try {
			
			Log.d("Qrivo", "GetParkings-doInBackground: Decodificando respuesta");
			AnswerGetParkings answer = gson.fromJson(response.toString(), AnswerGetParkings.class);
			GetParkings.answer = answer;
			Log.v("Qrivo", "Se encontraron: " + answer.getParkings().getData().size() + " estacionamientos");
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			try{
				JSonError jError = gson.fromJson(response.toString(), JSonError.class);
				Toast.makeText(this.activity, jError.getMsg(), Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
				Log.v("Qrivo", jError.getMsg());
//				if(jError.getMsg().equals("Error al cargar la lista")){
//					result = "Error al cargar la lista";
//					Log.e("Qrivo", "GetParkings-doInBackground: " + e.getMessage());
//	//				Toast.makeText(this.activity, "No hay promociones asignadas" , Toast.LENGTH_SHORT).show();
//					isSuccessfull = false;
//				}else{
//					result = "Communication error...";
//					Log.e("Qrivo", "GetParkings-doInBackground: fallo el envio del registo");
//					Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
//				}
			}catch(Exception e2){
				isSuccessfull = false;
			}
			
		}

	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("Qrivo", "GetParkings-doInBackground: "+result.toString());
	
	try{
		Log.d("Qrivo", "GetParkings-doInBackground: Decodificando respuesta");
		
		if(isSuccessfull){  
			if(answer.getParkings().getData().size() == 0){
				pd.dismiss();
//				this.activity.finish();
				Toast.makeText(activity, "No existen estacionamientos cercanos", Toast.LENGTH_SHORT).show();
				return;
			}
//			this.activity.finish();
			pd.dismiss();
			Log.d("Qrivo", "Abriendo mapa de estacionamientos");
			activity.parkings = answer.getParkings().getData();
			activity.offset += answer.getParkings().getData().size();
			setMarkers();
//	    	Intent intent = new Intent(activity, ParkingsMapActivity.class);
//			activity.startActivity(intent);
		}
		else{
			pd.dismiss();
//			this.activity.finish();
//			Toast.makeText(activity, "No existen estacionamientos cercanos" , Toast.LENGTH_LONG).show();
			if(false/*activity.hasGPSDevice(activity)*/){
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setMessage("No existen estacionamientos cercanos a ti")
				        .setTitle("No se encontraron resultados")
				        .setCancelable(false)
				        .setNegativeButton("Aceptar",
				                new DialogInterface.OnClickListener() {
				                    public void onClick(DialogInterface dialog, int id) {
				                        dialog.cancel();
				                        activity.finish();
				                    }
				                });
				AlertDialog alert = builder.create();
				alert.show();
				activity.setMapOnRegion();
			}else{
				Toast.makeText(activity, "No existen estacionamientos cercanos a este punto" , Toast.LENGTH_LONG).show();
			}
		}
	
	  }
	  catch(Exception e){
		  pd.dismiss();
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("Qrivo", "GetParkings-doInBackground: Error cargando estacionamientos "+ e.getMessage());
	  }
	
	}

    private void setMarkers(){

    	for(int i = 0; i < answer.getParkings().getData().size(); i++){
    		// create marker
        	MarkerOptions marker = new MarkerOptions().position(new LatLng(answer.getParkings().getData().get(i).getLatitude(), answer.getParkings().getData().get(i).getLongitude())).title(answer.getParkings().getData().get(i).getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_lot));
        	// adding marker
        	activity.googleMap.addMarker(marker);        	
    	}
    	activity.setZoom(answer.getParkings().getData());
    	
    }

}