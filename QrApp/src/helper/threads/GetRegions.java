package helper.threads;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import helper.database.DBAdapter;
import helper.json.AnswerGetRegions;
import helper.json.Region;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.testapp.test.MainActivity;
import com.testapp.test.R;
import com.testapp.test.RegisterDevActivity;

public class GetRegions extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	private boolean changeonly = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetRegions act;
	String name;
	String email;
	int userid;
	// Listview Adapter
    ArrayAdapter<String> adapter;
    List<Region> regions;

	public GetRegions (Activity act, ProgressDialog tempd, String name, String email, int userid){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.name = name;
		this.email = email;
		this.userid = userid;
		
	}
	
	public GetRegions (Activity act, ProgressDialog tempd,boolean changeonly){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		this.changeonly = changeonly;
		
	}
	
	public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = activity.getAssets().open("regions.json");

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
		
		String result = "";
		
//		HttpRequest client = new HttpRequest(c);
//		Log.d("SocialGo", "GetRegions-doInBackground: Solicitando datos");
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
			
			Log.d("SocialGo", "GetRegions-doInBackground: Decodificando respuesta");
			AnswerGetRegions answer = gson.fromJson(response.toString(), AnswerGetRegions.class);
			GetRegions.act = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("Error al obtener las regiones")){
				result = "Error al obtener las regiones";
				Log.e("SocialGo", "GetRegions-doInBackground: No hay actividad");
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetRegions-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}

	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetRegions-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetRegions-doInBackground: Decodificando respuesta");
		
		if(isSuccessfull){  
			// List view
	        final ListView lv;
	        regions = act.getData();
	        
	        // Search EditText
	        EditText inputSearch;
	    	
	    	final Dialog dialog = new Dialog(activity);
	    	
	    	// All tasks for first selection of city
	    	if(!changeonly){
	    		dialog.setCancelable(false);
	    		dialog.setCanceledOnTouchOutside(false);
	    		pd.dismiss();
	    	}
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			dialog.setContentView(R.layout.dialog_select_city);
			WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
			
			EditText search = (EditText)dialog.findViewById(R.id.inputSearch);
			search.setHint("Nombre de la ciudad");
			
			TextView tv = (TextView)dialog.findViewById(R.id.textView1);
//			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
			tv.setText("Selecciona tu ciudad:");
			
			Button close = (Button)dialog.findViewById(R.id.btn_close);
			if(changeonly){
				close.setVisibility(Button.VISIBLE);
			}else{
				close.setVisibility(Button.GONE);
			}
			
			close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			wmlp.gravity = Gravity.TOP | Gravity.CENTER;
			wmlp.y = 50;   //y position

			String[] regionnames = new String[regions.size()];
			
			for(int i=0; i<regions.size();i++){
				regionnames[i] = regions.get(i).getRegion();
			}
			
			lv = (ListView) dialog.findViewById(R.id.list_view2);
			inputSearch = (EditText) dialog.findViewById(R.id.inputSearch);
			
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Region reg = getRegion(reemplazarCaracteresRaros((String)lv.getItemAtPosition(arg2)));
					getZones(reg.getId());
					
					if(changeonly){
						changeRegion(reg);
					}else{
						saveCity(reg);
					}
					dialog.dismiss();
				}
			});
	        
	        // Adding items to listview
	        adapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.dropdown_item, R.id.product_name, regionnames);
	        adapter.getFilter().filter("%A");
	        lv.setVisibility(View.GONE);
	        lv.setAdapter(adapter);
	        
	        inputSearch.addTextChangedListener(new TextWatcher() {
	             
	            @Override
	            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	                // When user changed the Text
	            	if(cs.length() >= 4){
	            		lv.setVisibility(View.VISIBLE);
	            		GetRegions.this.adapter.getFilter().filter(cs);
	            		
	            	}else{
	            		GetRegions.this.adapter.getFilter().filter("%A");
	            	}
	            }
	             
	            @Override
	            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                    int arg3) {
	                // TODO Auto-generated method stub
	            }

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
				}

	        });    
			dialog.show();
		}
		else{
			Toast.makeText(activity, "No hay regiones disponibles." , Toast.LENGTH_LONG).show();
			
		}
//		pd.dismiss();
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetRegions-doInBackground: Error cargando actividad"+ e.getMessage());
//		  pd.dismiss();
	  }

	}
	
	private void getZones(int regid){
		DBAdapter db = new DBAdapter(activity);
		db.open();
		db.deleteZones();
		Cursor cursor = db.getAllUsers();
		db.close();
		String data = "&userid=" + cursor.getString(1).toString() + "&regid=" + regid;
		
		GetZones getzones = new GetZones(this.activity, null);
    	getzones.execute(new String[] {data,"getZones"});
	}

    private Region getRegion(String name){
    	for(int i = 0; i < regions.size(); i++){
    		if(reemplazarCaracteresRaros(regions.get(i).getRegion()).equals(name)){
    			return regions.get(i);
    		}
    	}
    	return null;
    }
	
	public String reemplazarCaracteresRaros(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}
	
	private void saveCity(Region r){
		DBAdapter database = new DBAdapter(this.activity);
		Integer cityid = r.getId();
		String name = r.getRegion();
		database.open();
		database.insertCity(name,cityid,r.getLatitude(),r.getLongitude());
		database.close();
		getStreets(r.getId());
		setTmpRegion();
		System.gc();
		RegisterDevActivity gcm = new RegisterDevActivity();
		gcm.registerGCM(activity, this.name, email, userid);
//		Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
//		activity.startActivity(intent);
//		activity.finish(); 
		
	}
	
	private void getStreets(int regid){
		
		String data = "&regid=" + regid;
		
    	GetStreets streets = new GetStreets(activity, null);
    	streets.execute(new String[] {data,"getStreets"});
	}
	
	private void changeRegion(Region r){
		DBAdapter database = new DBAdapter(this.activity);
		Integer cityid = r.getId();
		String name = r.getRegion();
		database.open();
		database.deleteCities();
		database.deleteStreets();
		database.insertCity(name,cityid,r.getLatitude(),r.getLongitude());
		database.close();
		getStreets(r.getId());
		
		changeText();
		trimCache(activity);
//		deleteCache(activity);
		System.gc();
		Toast.makeText(activity, "Región cambiada a " + name, Toast.LENGTH_SHORT).show();
	}
	
	public static void deleteCache(Context context) {
	    try {
	        File dir = context.getCacheDir();
	        if (dir != null && dir.isDirectory()) {
	            boolean del = deleteDir(dir);
	            if(del){
	            	Log.i("Qrivo", "Cache cleared");
	            }else{
	            	Log.i("Qrivo", "Cache not cleared");
	            }
	        }
	    } catch (Exception e) {
	    	Log.e("Qrivo", "Cache clearing error: " + e.getMessage());
	    }
	}

	public static boolean deleteDir(File dir) {
	    if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    return dir.delete();
	}
	
	public static void trimCache(Context context) {
	    File dir = context.getCacheDir();
	    if(dir!= null && dir.isDirectory()){
	        File[] children = dir.listFiles();
	        if (children == null) {
	            // Either dir does not exist or is not a directory
	        } else {
	            File temp;
	            for (int i = 0; i < children.length; i++) {
	                temp = children[i];
	                temp.delete();
	            }
	        }

	    }

	} 
	
	private void changeText(){
		DBAdapter database = new DBAdapter(this.activity);
		Cursor cursor;
		Button btn_region = (Button)activity.findViewById(R.id.btn_business_change_city);
		database.open();
		cursor = database.getCity();
		database.close();
		btn_region.setText("Cambiar ciudad: " + cursor.getString(1));
	}
	
	private void setTmpRegion(){
		DBAdapter database = new DBAdapter(this.activity);
		database.open();
		Cursor c = database.getTmpZone();
		if(c.getCount() == 0){
			database.insertTMPZone(0, 0);
		}
		database.close();
	}

}