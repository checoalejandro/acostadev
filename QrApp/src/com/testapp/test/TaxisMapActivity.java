package com.testapp.test;

import java.util.List;

import helper.database.DBAdapter;
import helper.json.Taxi;
import helper.threads.GetTaxis;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class TaxisMapActivity extends FragmentActivity {
	
	// Google Map
    public GoogleMap googleMap;
    public GPSTracker gps;
    
    public int offset = 0;
    private double latitude;
    private double longitude;
    private boolean first = true;
	public double reg_latitude;
	public double reg_longitude;
    private MarkerOptions mymarker;
    private Marker marker;
    
    public static Taxi selected_taxi = null;
    
    public List<Taxi> taxis;
	
	public TaxisMapActivity() {
		// TODO Auto-generated constructor stub
//		this.taxis = GetParkings.answer.getData();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		setElements();
		
		TextView tv = (TextView) findViewById(R.id.txt_msgmap);
		tv.setVisibility(TextView.VISIBLE);
		
		ImageButton location = (ImageButton) findViewById(R.id.btn_location);
		location.setVisibility(ImageButton.GONE);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moveToMyPosition();
			}
		});
		ImageButton back = (ImageButton) findViewById(R.id.btn_back);
		back.setVisibility(ImageButton.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		ImageButton btn_ok = (ImageButton) findViewById(R.id.btn_check);
//		btn_ok.setVisibility(ImageButton.GONE);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
				
				
			}
		});
		
		ImageButton home = (ImageButton) findViewById(R.id.btn_go_home);
		home.setVisibility(ImageButton.GONE);
		home = (ImageButton) findViewById(R.id.btn_home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		try {
            // Loading map
            initilizeMap();
            
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
 
    /**
     * function to load map. If map is not created it will create it for you
     * */
	@SuppressWarnings("unused")
	private void initilizeMap() {
        if (googleMap == null) {
        	
        	
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.bizne_map))
                    .getMap();

            googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
//					openBizne(arg0);
					openTaxi(arg0);
				}
			});
//            setMarkers();
//            setMyPosition(latitude,SelectPointActivity.longitude);
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(reg_latitude,reg_longitude) , 12) );
            if(true/*!hasGPSDevice(getApplicationContext())*/){
            	ImageButton btn_ok = (ImageButton) findViewById(R.id.btn_check);
        		btn_ok.setVisibility(ImageButton.VISIBLE);
            	googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        // TODO Auto-generated method stub
                    	
//                    	setMarker()
                    	if(first){
                    		addMarker(point);
                    		first = false;
                    	}else{
                    		addMarker2(point);
                    	}
                        
                    }
                });
            }else{
            	ImageButton btn_ok = (ImageButton) findViewById(R.id.btn_check);
        		btn_ok.setVisibility(ImageButton.GONE);
            	setGPSLocation();
            	
            }
            
//            setZoom();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Lo sentimos! no es posible cargar los mapas", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
	
	public void setZoom(List<Taxi> taxis){
		
		if(taxis.size() == 1){
			googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(taxis.get(0).getLatitude(),taxis.get(0).getLongitude()) , 15) );
			return;
		}
		
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		
		for(int i=0; i < taxis.size(); i++){
			LatLng point = new LatLng(taxis.get(i).getLatitude(), taxis.get(i).getLongitude());
			b.include(point);
		}
		    
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
	
	private void openTaxi(Marker marker){
		for(int i = 0; i < taxis.size(); i++){
			if(marker.getTitle().equals(taxis.get(i).getName())){
				selected_taxi = taxis.get(i);
				Intent a = new Intent(this,TaxiActivity.class);
		        startActivity(a);
				break;
			}
		}
	}
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    
    private void moveToMyPosition(){
    	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(15).build();
 
    	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    
    private void setMyPosition(double latitude, double longitude){
    	// create marker
    	mymarker = new MarkerOptions().position(new LatLng(latitude,longitude)).title("Tú");
    	mymarker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
    	 
    	// adding marker
    	googleMap.addMarker(mymarker);
    	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(BusinessActivity.latitude, BusinessActivity.longitude)).zoom(15).build();
 
    	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
 
    @SuppressWarnings("unused")
	private void setMarkers(){
    	
    	for(int i = 0; i < taxis.size(); i++){
    		// create marker
        	MarkerOptions marker = new MarkerOptions().position(new LatLng(taxis.get(i).getLatitude(), taxis.get(i).getLongitude())).title(taxis.get(i).getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_lot));
        	// adding marker
        	googleMap.addMarker(marker);
    	}
    	
    	
    }
    
	public void save(){
		
		if(mymarker != null){
			latitude = mymarker.getPosition().latitude;
			longitude = mymarker.getPosition().longitude;
			
			DBAdapter db = new DBAdapter(this);
			db.open();
			Cursor reg = db.getCity();
			db.close();
			int regid = Integer.parseInt(reg.getString(2));
			
			String data = "&lat=" + latitude + "&lng=" + longitude + "&offset=" + offset + "&regid=" + regid;
			ProgressDialog pd = ProgressDialog.show( this, "Recibiendo sitios...", "Espere, por favor", true, false);
			
			GetTaxis getTaxis = new GetTaxis(this, pd);
			getTaxis.execute(new String[] {data,"getTaxiDispatchesByLocation"});
			
		}
		else
			Toast.makeText(this, "Asegúrate de asignar una posición en el mapa", Toast.LENGTH_LONG).show();
	}
	
	public void setMapOnRegion(){
		googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(reg_latitude,reg_longitude) , 12) );
	}
	
	private void setElements(){
		DBAdapter database = new DBAdapter(this);
		database.open();
		Cursor reg = database.getCity();
		database.close();
		if(reg.getCount() > 0){
			 reg_latitude = Double.parseDouble(reg.getString(3));
		     reg_longitude = Double.parseDouble(reg.getString(4));
		}else{
			Toast.makeText(this, "Error al obtener los puntos de la región. Selecciona una región en el menú principal", Toast.LENGTH_LONG).show();
		}
		
//		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
//	   	btn_home.setVisibility(ImageButton.VISIBLE);
//	   	btn_home.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					gohome();
//				}
//			});
		
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
	
	private void addMarker2(final LatLng point){
		AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);  
        dialogo1.setTitle("Nueva búsqueda");  
        dialogo1.setMessage("¿Deseas realizar una nueva búsqueda y limpiar la actual?");            
        dialogo1.setCancelable(false);  
        dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialogo1, int id) {  
        		if(mymarker == null){
        			mymarker = new MarkerOptions().position(point);
        			mymarker.draggable(true);
        			mymarker.title("Nuevo Punto");
        			googleMap.addMarker(mymarker);
        		}else{
        			googleMap.clear();
//        			setMyLocationMarker();
        			mymarker = new MarkerOptions().position(point);
        			mymarker.draggable(true);
        			mymarker.title("Nuevo Punto");
        			googleMap.addMarker(mymarker);
        		}  
            }  
        });  
        dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialogo1, int id) {  
                return;
            }  
        });            
        dialogo1.show(); 

	}
	
	public boolean hasGPSDevice(Context context){
	    final LocationManager mgr = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    if ( mgr == null ) return false;
	    final List<String> providers = mgr.getAllProviders();
	    if ( providers == null ) return false;
	    return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void setGPSLocation(){
    	// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.isGPSEnabled){
           latitude = gps.getLatitude();
           longitude = gps.getLongitude();
           setMyPosition(latitude, longitude);
           save();
//           setMyPosition(mylatitude, mylongitude);
             
        }else{

            gps.showSettingsAlert();
        }
    }
    
    public void setMarker(Location location){
        if (marker == null) {
            marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Alex"));
        } else {
            marker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
        }
    }

}
