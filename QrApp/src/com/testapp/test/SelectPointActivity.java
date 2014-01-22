package com.testapp.test;

import helper.database.DBAdapter;
import helper.threads.GetParkings;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectPointActivity extends FragmentActivity {
	
	private GoogleMap googleMap;
	DBAdapter database = new DBAdapter(this);
	
	public static double latitude;
	public static double longitude;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_zone, menu);
		return true;
	}
	
	public void save(){
		
//		if(mymarker != null){
//			latitude = mymarker.getPosition().latitude;
//			longitude = mymarker.getPosition().longitude;
//			
//			String data = "&lat=" + latitude + "&lng=" + longitude;
//			ProgressDialog pd = ProgressDialog.show( this, "Recibiendo estacionamientos...", "Espere, por favor", true, false);
//			
//			GetParkings getparkings = new GetParkings(this, pd);
//			getparkings.execute(new String[] {data,"getParkingsByLocation"});
//			
//		}
//		else
//			Toast.makeText(this, "Asegúrate de asignar una posición en el mapa", Toast.LENGTH_LONG).show();
	}
	
	private void setElements(){
		
		database.open();
		Cursor reg = database.getCity();
		database.close();
		if(reg.getCount() > 0){
			 reg_latitude = Double.parseDouble(reg.getString(3));
		     reg_longitude = Double.parseDouble(reg.getString(4));
		}else{
			Toast.makeText(this, "Error al obtener los puntos de la región. Selecciona una región en el menú principal", Toast.LENGTH_LONG).show();
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
				save();
				
				
			}
		});
		
		ImageButton btn_location = (ImageButton) findViewById(R.id.btn_location);
		btn_location.setVisibility(ImageButton.GONE);
		
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
			mymarker.title("Nuevo Punto");
			googleMap.addMarker(mymarker);
		}else{
			googleMap.clear();
//			setMyLocationMarker();
			mymarker = new MarkerOptions().position(point);
			mymarker.draggable(true);
			mymarker.title("Nuevo Punto");
			googleMap.addMarker(mymarker);
		}
	}

}
