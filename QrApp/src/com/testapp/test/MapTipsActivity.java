package com.testapp.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import helper.database.DBAdapter;
import helper.threads.SaveZone;
import helper.threads.SendTip;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MapTipsActivity extends FragmentActivity {

	private GoogleMap googleMap;
	private GPSTracker gps;
	DBAdapter database = new DBAdapter(this);
	SparseArray<Group2> groups = new SparseArray<Group2>();
	
	private double latitude;
	private double longitude;
	private String name;
	
	public static int catid;
	
	private MarkerOptions mymarker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_tips);
		setElements();
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_zone, menu);
		return true;
	}
	
	public void loadZone(){
		helper.tools.MyZone zone = new helper.tools.MyZone(mymarker.getPosition().latitude, mymarker.getPosition().longitude);
//		BusinessActivity.selected_zone = zone;
		BusinessActivity.search_type = "zone";
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra("zoneChanged", "");
		setResult(this.RESULT_OK, resultIntent);
		finish();
	}
	
	public void save(String name){
		
		if(mymarker != null)
			saveZone(name, mymarker.getPosition().latitude, mymarker.getPosition().longitude);
		else
			Toast.makeText(this, "Asegúrate de asignar una posición en el mapa", Toast.LENGTH_LONG).show();
	}
	
	public void sendTip(){
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_maptips);
		dialog.setTitle("Agregar nuevo tip");
 
		
		Button dialogButtonCategory = (Button) dialog.findViewById(R.id.maptip_category);
		// if button is clicked, close the custom dialog
		dialogButtonCategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setCategories();
				
				
			}
		});
 
			Button dialogButton = (Button) dialog.findViewById(R.id.btn_send_maptip);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					EditText edtip = (EditText)dialog.findViewById(R.id.tip_comment);
					String tip = edtip.getText().toString();
					Spinner duration = (Spinner) dialog.findViewById(R.id.maptip_time_spinner);
					int dur = duration.getSelectedItemPosition();
					RatingBar rating = (RatingBar)dialog.findViewById(R.id.ratingbar);
					int rat = (int) rating.getRating();
					sendMapTip(tip,rat,BusinessActivity.catid,dur);
					
				}
			});
 
			dialog.show();
	}
	
	private void setElements(){
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					gohome();
				}
			});
		
		ImageButton btn_ok = (ImageButton) findViewById(R.id.btn_check);
		btn_ok.setVisibility(ImageButton.VISIBLE);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendTip();
				
				
			}
		});
		
		ImageButton btn_location = (ImageButton) findViewById(R.id.btn_location);
		btn_location.setVisibility(ImageButton.VISIBLE);
		btn_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moveToMyPosition();
			}
		});
		initilizeMap();
	}
	
	private void initilizeMap() {
        if (googleMap == null) {
        	
        	
        	setGPSLocation();
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tips_map))
                    .getMap();
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 12) );
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    // TODO Auto-generated method stub
                	addMarker(point);
                    
                }
            });
            // Place my location marker
            
            moveToMyPosition(); 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Lo sentimos! no es posible cargar los mapas", Toast.LENGTH_SHORT)
                        .show();
            } 
        }
    }
	
	private void addMarker(LatLng point){
		if(mymarker == null){
			mymarker = new MarkerOptions().position(point);
			mymarker.draggable(true);
			mymarker.title("Nueva Zona");
			googleMap.addMarker(mymarker);
		}else{
			googleMap.clear();
//			setMyLocationMarker();
			mymarker = new MarkerOptions().position(point);
			mymarker.draggable(true);
			mymarker.title("Nueva Zona");
			googleMap.addMarker(mymarker);
		}
	}
    
    private void setGPSLocation(){
    	// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.canGetLocation()){
             
           latitude = gps.getLatitude();
           longitude = gps.getLongitude();
           
//           setMyPosition(latitude, longitude);
             
        }else{

            gps.showSettingsAlert();
        }
    }
    
    private void moveToMyPosition(){
    	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();
 
    	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); 
    }
    
    private void saveZone(String name, double lat, double lng){
    	database.open();
    	database.insertZone(name, lat, lng,0,"user");
    	database.close();
    	Toast.makeText(this, "¡Creada!", Toast.LENGTH_LONG).show();
    	
    	database.open();
	    Cursor cursor = database.getAllUsers();
	    database.close();
    	
//    	ProgressDialog pd = ProgressDialog.show( this, "Guardando Zona...", "Espere, por favor", true, false);
    	
    	String data = "&name=" + name +
    				 "&lat=" + lat +
    				 "&lng=" + lng +
    				 "&userid=" + cursor.getString(1).toString();
    	
    	SaveZone savezone = new SaveZone(this,null);
    	savezone.execute(new String[] {data,"saveZone"});
    	
    	helper.tools.MyZone zone = new helper.tools.MyZone(mymarker.getPosition().latitude, mymarker.getPosition().longitude);
//		BusinessActivity.selected_zone = zone;
		BusinessActivity.search_type = "zone";
    	
    	Intent resultIntent = getIntent();
		resultIntent.putExtra("newZoneAdded", name);
		setResult(this.RESULT_OK, resultIntent);
    	
    	this.finish();
    }
    
    private void sendMapTip(String tip, int rat, int catid, int dur){
    	
    	String duration = getDuration(dur);
    	
    	DBAdapter database = new DBAdapter(this);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		database.open();
	    Cursor cursor = database.getAllUsers();
	    database.close();
		
		String data = "&userid=" + cursor.getString(1).toString() 
					+ "&comment=" + tip
					+ "&bizid=" + MainActivity.answer.getBizId()
					+ "&lat=" + mymarker.getPosition().latitude
					+ "&lng=" + mymarker.getPosition().longitude
					+ "&rating=" + rat
					+ "&duration=" + duration
					+ "&time=" + dateFormat.format(date);
		
		ProgressDialog pd = ProgressDialog.show(this, "Enviando Tip", "Espere, por favor", true, false); 
		
		SendTip maptip = new SendTip(this, pd);
		maptip.execute(new String[] {data,"saveMapTip"});
    }

	private String getDuration(int dur) {
		// TODO Auto-generated method stub
		String duration = "";
		
		switch(dur){
		case 0: duration = "00:00:00"; break;
		case 1: duration = "00:30:00"; break;
		case 2: duration = "01:00:00"; break;
		case 3: duration = "02:00:00"; break;
		case 4: duration = "03:00:00"; break;
		case 5: duration = "04:00:00"; break;
		case 6: duration = "05:00:00"; break;
		case 7: duration = "06:00:00"; break;
		case 8: duration = "07:00:00"; break;
		case 9: duration = "08:00:00"; break;
		case 10: duration = "09:00:00"; break;
		case 11: duration = "10:00:00"; break;
		case 12: duration = "11:00:00"; break;
		case 13: duration = "12:00:00"; break;
		case 14: duration = "13:00:00"; break;
		case 15: duration = "14:00:00"; break;
		case 16: duration = "15:00:00"; break;
		case 17: duration = "16:00:00"; break;
		case 18: duration = "17:00:00"; break;
		case 19: duration = "18:00:00"; break;
		case 20: duration = "19:00:00"; break;
		case 21: duration = "20:00:00"; break;
		case 22: duration = "21:00:00"; break;
		case 23: duration = "22:00:00"; break;
		case 24: duration = "23:00:00"; break;
		case 25: duration = "24:00:00"; break;
		}
		
		return duration;
	}
	
	private void setCategories() {
		// TODO Auto-generated method stub
		
		DBAdapter database = new DBAdapter(this);
		database.open();
        Cursor c = database.getAllCategories();
        
        int i = 0;
        Group2 group = new Group2(" " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)));
    	int par_index = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
    	Cursor s = database.getCategories(par_index);
    	String parcat = "Todo en " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + par_index;
    	group.children.add(parcat);
    	while(s.moveToNext()){
    		group.children.add(s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
    		
    	}
    	groups.append(i, group);
        i++;
        
        while(c.moveToNext()){
        	group = new Group2(" " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)));
        	par_index = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
        	s = database.getCategories(par_index);
        	parcat = "Todo en " + c.getString(c.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + par_index;
        	group.children.add(parcat);
        	while(s.moveToNext()){
        		group.children.add(s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATNAME)) + ":" + s.getString(s.getColumnIndexOrThrow(DBAdapter.KEY_CATID)));
        		
        	}
        	groups.append(i, group);
        	i++;
        }
		database.close();
		
	}
}
