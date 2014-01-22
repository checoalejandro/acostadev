package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.BizneInfoList;
import helper.json.QrScan;
import helper.threads.GetBizInfo;
import helper.threads.GetSearchString;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageButton;
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
import com.google.zxing.qrcode.encoder.QRCode;


public class MapActivity extends FragmentActivity {
	
	// Google Map
    private GoogleMap googleMap;
    private GPSTracker gps;
    
    private double latitude;
    private double longitude;
    
    private MarkerOptions mymarker;
    
    BizneInfoList biz;
	
	public MapActivity() {
		// TODO Auto-generated constructor stub
		this.biz = BizneListActivity.maplist;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
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
	private void initilizeMap() {
        if (googleMap == null) {
        	
        	
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.bizne_map))
                    .getMap();
            
            googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				
				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
					openBizne(arg0);
				}
			});
            setMarkers();
            setMyPosition(BusinessActivity.latitude,BusinessActivity.longitude);
            setGPSLocation();
            googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude) , 12) );
            setZoom();
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Lo sentimos! no es posible cargar los mapas", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
	
	private void setZoom(){
		
		if(biz.size() == 1){
			googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(biz.get(0).getLatitude(),biz.get(0).getLongitude()) , 15) );
			return;
		}
		
		LatLngBounds.Builder b = new LatLngBounds.Builder();
		
		for(int i=0; i < biz.size(); i++){
			LatLng point = new LatLng(biz.get(i).getLatitude(), biz.get(i).getLongitude());
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

	                @SuppressWarnings("deprecation")
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
	
	private void openBizne(Marker marker){
		for(int i = 0; i < biz.size(); i++){
			if(marker.getTitle().equals(biz.get(i).getName())){
				DBAdapter database = new DBAdapter(this);
				database.open();
	   	        Cursor cursor = database.getAllUsers();
	   	        database.close();
	   	        
	   	        ProgressDialog pd;
    			
	   	        pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
				
				String data = "&bizid=" + biz.get(i).getBizId() +
						"&userid=" + cursor.getString(1).toString();
				
				QrScan qr = new QrScan();
				qr.setBizId(biz.get(i).getBizId());
				MainActivity.answer = qr;
				
				GetBizInfo getinfo = new GetBizInfo(this, pd);
				getinfo.execute(new String[] {data,"getBizneInfo"});
				
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
    	mymarker = new MarkerOptions().position(new LatLng(latitude,longitude)).title("Tú").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_you));
    	 
    	// adding marker
    	googleMap.addMarker(mymarker);
    	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(BusinessActivity.latitude, BusinessActivity.longitude)).zoom(15).build();
 
    	googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
 
    private void setMarkers(){
    	
    	for(int i = 0; i < biz.size(); i++){
    		// create marker
        	MarkerOptions marker = new MarkerOptions().position(new LatLng(biz.get(i).getLatitude(), biz.get(i).getLongitude())).title(biz.get(i).getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
        	
        	// adding marker
        	googleMap.addMarker(marker);
    	}
    	
    	
    }
    
    private void setGPSLocation(){
    	// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.canGetLocation()){
             
           latitude = gps.getLatitude();
           longitude = gps.getLongitude();
           
           
             
        }else{

            gps.showSettingsAlert();
        }
    }
    
    
    
}
