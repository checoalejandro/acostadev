package com.testapp.test;

import helper.database.DBAdapter;
import helper.threads.SaveZone;
import helper.tools.MyZone;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class NewZoneActivity extends FragmentActivity {
	
	private GoogleMap googleMap;
	private GPSTracker gps;
	DBAdapter database = new DBAdapter(this);
	
	private double latitude;
	private double longitude;
	private String name;
	
	public double reg_latitude;
	public double reg_longitude;
	public int regid;
	
	private MarkerOptions mymarker;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_zone);
		setElements();
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
//		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_zone, menu);
		return true;
	}
	
	public void loadZone(){
		MyZone zone = new MyZone(mymarker.getPosition().latitude, mymarker.getPosition().longitude);
		BusinessActivity.selected_zone = zone;
		BusinessActivity.search_type = "zone";
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra("zoneChanged", "");
		setResult(this.RESULT_OK, resultIntent);
		finish();
	}
	
	public void save(String name){
		
		if(name.length() > 3 && mymarker != null)
			saveZone(name, mymarker.getPosition().latitude, mymarker.getPosition().longitude);
		else
			Toast.makeText(this, "Asegúrate de asignar un nombre mayor a tres caracteres y una posición en el mapa", Toast.LENGTH_LONG).show();
	}
	
	public void saveSelectedZone(){
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_save_zone);
		dialog.setTitle("Guardar zona");
 
		EditText name = (EditText)dialog.findViewById(R.id.new_zone_name);
		name.setHint("Nombre de la zona");
 
			Button dialogButton = (Button) dialog.findViewById(R.id.dialog_new_zone_save);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					EditText name = (EditText)dialog.findViewById(R.id.new_zone_name);
//					name.setHint("Nombre de la zona");
					save(name.getText().toString());
					
				}
			});
			
			dialogButton = (Button) dialog.findViewById(R.id.dialog_new_zone_cancel);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadZone();
					dialog.dismiss();
					
				}
			});
 
			dialog.show();
	}
	
	private void setElements(){
		
		// Set Title
		TextView title = (TextView)findViewById(R.id.lbl_name_section);
		title.setText("Nueva Zona");
		
		database.open();
		Cursor reg = database.getCity();
		database.close();
		if(reg.getCount() > 0){
			 reg_latitude = Double.parseDouble(reg.getString(3));
		     reg_longitude = Double.parseDouble(reg.getString(4));
		}
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
	   	btn_home.setVisibility(ImageButton.VISIBLE);
	   	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	   	
	   	btn_home = (ImageButton) findViewById(R.id.btn_home);
	   	btn_home.setOnClickListener(new OnClickListener() {
			
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
				confirmDialog();
				
				
			}
		});
		
		ImageButton btn_location = (ImageButton) findViewById(R.id.btn_location);
		btn_location.setVisibility(ImageButton.GONE);
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
        	
        	
//        	setGPSLocation();
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.bizne_map))
                    .getMap();
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(reg_latitude,reg_longitude) , 12) );
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    // TODO Auto-generated method stub
                	addMarker(point);
                    
                }
            });
            // Place my location marker
            
//            moveToMyPosition();
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
    	Cursor reg = database.getCity();
	    Cursor cursor = database.getAllUsers();
	    database.close();
    	
//    	ProgressDialog pd = ProgressDialog.show( this, "Guardando Zona...", "Espere, por favor", true, false);
    	
    	String data = "&name=" + name +
    				 "&lat=" + lat +
    				 "&lng=" + lng +
    				 "&regid=" + reg.getString(2).toString() +
    				 "&userid=" + cursor.getString(1).toString();
    	
    	SaveZone savezone = new SaveZone(this,null);
    	savezone.execute(new String[] {data,"saveZone"});
    	
		MyZone zone = new MyZone(mymarker.getPosition().latitude, mymarker.getPosition().longitude);
		BusinessActivity.selected_zone = zone;
		BusinessActivity.search_type = "zone";
    	
    	Intent resultIntent = getIntent();
		resultIntent.putExtra("newZoneAdded", name);
		setResult(this.RESULT_OK, resultIntent);
    	
    	this.finish();
    }
    
    private void confirmDialog(){
    	
    	if(mymarker != null){
	    	// custom dialog
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_confirm_save);
			
	 
			Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialog_confirm_yes);
			// if button is clicked, close the custom dialog
			dialogButtonYes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					saveSelectedZone();
				}
			});
			
			Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialog_confirm_no);
			// if button is clicked, close the custom dialog
			dialogButtonNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					loadZone();
					dialog.dismiss();
				}
			});
	 
			dialog.show();
    	}else{
    		Toast.makeText(this, "Asegúrate de asignar una posición en el mapa", Toast.LENGTH_LONG).show();
    	}
    }

}
