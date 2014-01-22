package helper.tools;


import com.testapp.test.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class CheckDevices {
	
	static String msgSdCard;
	static String msgProvider;
	
	public static Boolean checkSDCard(Context mContext){

	    String auxSDCardStatus = Environment.getExternalStorageState();
	    Log.d("Socialdeals", "CheckDevices-checkSDCard: obteniendo status de la sdcard");	
	    if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED)){
	    	msgSdCard = mContext.getString(R.string.info_sdcard_available);
	    	Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.info_sdcard_available));
	    	return true;
	        
	    }
	    else if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
	    	msgSdCard = mContext.getString(R.string.warning_sdcard_readonly);
	        Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.warning_sdcard_readonly));
	        return true;
	    }
	    else if(auxSDCardStatus.equals(Environment.MEDIA_NOFS)){
	    	msgSdCard = mContext.getString(R.string.error_sdcard_not_works);
	        Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.error_sdcard_not_works));
	        return false;
	    }

	    else if(auxSDCardStatus.equals(Environment.MEDIA_REMOVED)){
	    	msgSdCard = mContext.getString( R.string.error_sdcard_removed);
	        Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.error_sdcard_removed));
	        return false;
	    }
	    else if(auxSDCardStatus.equals(Environment.MEDIA_SHARED)){
	    	msgSdCard = mContext.getString(R.string.error_sdcard_shared);
	        Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.error_sdcard_shared));
	        return false;
	    }
	    else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTABLE)){
	    	msgSdCard = mContext.getString( R.string.error_sdcard_unmountable);
	        Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.error_sdcard_unmountable));
	        return false;
	    }
	    else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTED)){
	    	msgSdCard = mContext.getString(R.string.error_sdcard_unmounted);
	        Log.d("Socialdeals", "CheckDevices-checkSDCard: "+mContext.getString(R.string.error_sdcard_unmounted));
	        return false;
	    }
	    
	    return true;
	}
	
	public static void showSdcardError(final Activity activity){
		AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        // Set the message to display
        alertbox.setMessage(msgSdCard);
        // Add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            // Click listener on the neutral button of alert box
            public void onClick(DialogInterface arg0, int arg1) {
            	activity.finish();
            }
        });
         // show the alert box
        alertbox.show();
	}
	
	public static Boolean checkProvider(LocationManager locManager, String provider, Context mContext){
		 
		if(provider == null){
			Log.d("Socialdeals", "CheckDevices-checkGps: "+ mContext.getString(R.string.error_provider_null));
			msgProvider = mContext.getString(R.string.error_provider_null);
			return false;
		}
		
		if(provider.equals("gps")){
			if (!locManager.isProviderEnabled(provider)){ 
				msgProvider = mContext.getString(R.string.warning_gps_not_available);
				Log.d("Socialdeals", "CheckDevices-checkProvider: "+mContext.getString(R.string.warning_gps_not_available));
				return false;
			}
			else{
				msgProvider = mContext.getString(R.string.info_gps_available);
				Log.d("Socialdeals", "CheckDevices-checkProvider: "+ mContext.getString(R.string.info_gps_available));
				return true;
			}
		}
		
		if(provider.equals("network")){
			
			if (!locManager.isProviderEnabled(provider)){ 
				msgProvider = mContext.getString(R.string.warning_network_not_available);
				Log.d("Socialdeals", "CheckDevices-checkProvider: "+mContext.getString(R.string.warning_network_not_available));
				return false;
			}
			else{
				msgProvider = mContext.getString(R.string.info_network_available);
				Log.d("Socialdeals", "CheckDevices-checkProvider: "+ mContext.getString(R.string.info_network_available));
				return true;
			}
		}
		Log.d("Socialdeals", "CheckDevices-checkProvider: "+ mContext.getString(R.string.error_provider_unknown));
		msgProvider = mContext.getString(R.string.error_provider_unknown);
		return false;

	}
	
	public static void showProviderError(final Activity activity){
		AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        // Set the message to display
        alertbox.setMessage(msgProvider);
        // Add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            // Click listener on the neutral button of alert box
            public void onClick(DialogInterface arg0, int arg1) {
            	activity.finish();
            }
        });
         // show the alert box
        alertbox.show();
	}
	
	public static void showProviderError(final Activity activity, String msg){
		AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        // Set the message to display
        alertbox.setMessage(msg);
        // Add a neutral button to the alert box and assign a click listener
        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            // Click listener on the neutral button of alert box
            public void onClick(DialogInterface arg0, int arg1) {
            	activity.finish();
            }
        });
         // show the alert box
        alertbox.show();
	}
	
	
	public static boolean isNetworkAvailable(Activity activity) {
		
		Context context = activity.getApplicationContext();
	    ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // if no network is available networkInfo will be null, otherwise check if we are connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }else{
	    	Toast.makeText(context,context.getString(R.string.warning_fails_internet_connection) , Toast.LENGTH_SHORT).show();
	    	return false;
	    }
	}
	
    public static void createGpsDisabledAlert(final Activity activity){  
    
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);  
    
    builder.setMessage(activity.getString(R.string.info_gps_not_available))  
         .setCancelable(false)  
         .setPositiveButton("Activar GPS",  
              new DialogInterface.OnClickListener(){  
              public void onClick(DialogInterface dialog, int id){  
                   showGpsOptions(activity);
                   activity.finish();
              }  
         });  
         builder.setNegativeButton("Salir",  
              new DialogInterface.OnClickListener(){  
              public void onClick(DialogInterface dialog, int id){ 
            	  //activity.finish(); 
            	  dialog.cancel();
              }  
         });  
    AlertDialog alert = builder.create();  
    alert.show();  
    }
    
    private static void showGpsOptions(final Activity activity){  
        Intent gpsOptionsIntent = new Intent(  
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
        activity.startActivity(gpsOptionsIntent);  
    } 


}
