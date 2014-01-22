package com.testapp.test;

import java.util.ArrayList;

import org.w3c.dom.Document;

import helper.json.BizneInfoList;
import helper.tools.GMapV2Direction;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class BizneMapActivity extends FragmentActivity {
	
	// Google Map
    public GoogleMap googleMap;
    private GPSTracker gps;
    private Marker marker;
    private boolean ismarked = false;
    
    private double latitude;
    private double longitude;
    private double mylongitude;
    private double mylatitude;
    private String name;
    
    LatLng fromPosition;
    LatLng toPosition;
    
    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";
    
    BizneInfoList biz;
    Document doc;
    TextView title;
    
    // Acquire a reference to the system Location Manager
	LocationManager locationManager;
	
	LocationListener locationListener;
	
	public BizneMapActivity() {
		// TODO Auto-generated constructor stub
		this.latitude = BizneInfoActivity.latitude;
		this.longitude = BizneInfoActivity.longitude;
		this.name = BizneInfoActivity.name;
		this.doc = GMapV2Direction.doc;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		//Title textview
		title = (TextView)findViewById(R.id.lbl_name_section);
		title.setText(BizneInfoActivity.biznename);
		
		ImageButton location = (ImageButton) findViewById(R.id.btn_location);
		location.setVisibility(ImageButton.VISIBLE);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moveToMyPosition();
			}
		});
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_back);
//		btn_home.setBackgroundResource(R.drawable.btn_back_qrivo);
    	btn_home.setVisibility(ImageButton.VISIBLE);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
    	btn_home = (ImageButton) findViewById(R.id.btn_go_home);
    	btn_home.setVisibility(ImageButton.GONE);
		
		ImageButton route = (ImageButton) findViewById(R.id.btn_route);
		route.setVisibility(ImageButton.VISIBLE);
		route.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLocationListener();
			}
		});
		try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
	private void initilizeMap() {
        if (googleMap == null) {
        	
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.bizne_map))
                    .getMap();
                   
//        	drawRoute();
//            // Business marker
            setMarker();
//            // My marker
            
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 15) );
//            setZoom();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Lo sentimos! no es posible cargar los mapas", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
	
	private void loadRoute(){
//		drawRoute();
		setGPSLocation();
		ProgressDialog pd = new ProgressDialog(this);
		pd = ProgressDialog.show( this, "Cargando ruta...", "Espere, por favor", true, false);
		GMapV2Direction md = new GMapV2Direction(this,pd);
    	md.execute(new String[] {String.valueOf(mylatitude),String.valueOf(mylongitude),
    			String.valueOf(latitude),String.valueOf(longitude)});
		
		setZoom();
	}
	
	private void setZoom(){
		
		
		LatLng user = new LatLng(mylatitude, mylongitude);
		LatLng bizne = new LatLng(latitude,longitude);
		
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		b.include(user);
		b.include(bizne);
		    
		final LatLngBounds bounds = b.build();
		//Change the padding as per needed
	    try {
	        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	    } catch (IllegalStateException e) {
	        // layout not yet initialized
	        final View mapView = getSupportFragmentManager().findFragmentById(R.id.bizne_map)
                    .getView();
	        if (mapView.getViewTreeObserver().isAlive()) {
	            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

	                @SuppressLint("NewApi")
	                // We check which build version we are using.
	                @Override
	                public void onGlobalLayout() {
	                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.BASE) {
	                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	                    } else {
	                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	                    }
	                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	                }
	            });
	        }
	    }
	}
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    
    private void setMyPosition(double latitude, double longitude){
    	// create marker
    	MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).title("Tú");
    	marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
    	 
    	// adding marker
    	googleMap.addMarker(marker);

    }
    
    private void moveToMyPosition(){
    	// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.isGPSEnabled){
           mylatitude = gps.getLatitude();
           mylongitude = gps.getLongitude();
           
//           setMyPosition(mylatitude, mylongitude);
             
        }else{

            gps.showSettingsAlert();
            return;
        }
        
        if(!ismarked){
        	setMarker(gps.getLocation());
        }
        
    	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(mylatitude, mylongitude)).zoom(15).build();
 
    	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)); 
    }
 
    private void setMarker(){
    	
		// create marker
    	MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
    	
    	// adding marker
    	googleMap.addMarker(marker);

    }
    
    private void setGPSLocation(){
    	// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.isGPSEnabled){
           mylatitude = gps.getLatitude();
           mylongitude = gps.getLongitude();
           
//           setMyPosition(mylatitude, mylongitude);
             
        }else{

            gps.showSettingsAlert();
        }
    }
    
//    private void drawRoute(){
//    	GMapV2Direction md = new GMapV2Direction(this,null);
//        ArrayList<LatLng> directionPoint = md.getDirection(doc);
//        PolylineOptions rectLine = new PolylineOptions().width(3).color(
//                Color.RED);
//
//        for (int i = 0; i < directionPoint.size(); i++) {
//            rectLine.add(directionPoint.get(i));
//        }
//        googleMap.addPolyline(rectLine);
//    }
    
    private void showLocationChanged(Location location){
    	Toast.makeText(this, "Lat: " + location.getLatitude() + " Lng: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }
    
    private void setLocationListener(){
    	
    	
    	locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    	if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return;
        }
    	setGPSLocation();
    	setMarker(gps.getLocation());
    	locationListener = new LocationListener() {
    	    public void onLocationChanged(Location location) {
    	      // Called when a new location is found by the network location provider.
//    	      showLocationChanged(location);
    	    	setMarker(location);
    	    }

    	    public void onStatusChanged(String provider, int status, Bundle extras) {}

    	    public void onProviderEnabled(String provider) {}

    	    public void onProviderDisabled(String provider) {}
    	  };

    	// Register the listener with the Location Manager to receive location updates
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    	
    	loadRoute();
    }
    
    public void setMarker(Location location){
        if (marker == null) {
            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Tú").icon((BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_you))));
        } else {
            marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
        }
    }
    
    public void onPositionChanged(LatLng newPosition) {

    }
    
    
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Al parecer tu GPS está desactivado, ¿deseas ir a activarlo?")
               .setCancelable(false)
               .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                   }
               })
               .setNegativeButton("No", new DialogInterface.OnClickListener() {
                   public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                   }
               });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
