package com.testapp.test;

import static com.testapp.test.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.testapp.test.CommonUtilities.EXTRA_MESSAGE;
import static com.testapp.test.CommonUtilities.SENDER_ID;

import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

import helper.database.DBAdapter;
import helper.json.AnswerActivity;
import helper.json.BizneInfo;
import helper.json.Game;
import helper.json.QrScan;
import helper.threads.GetActivity;
import helper.threads.GetActivityGroup;
import helper.threads.GetBestList;
import helper.threads.GetBizInfo;
import helper.threads.GetBizneData;
import helper.threads.GetBizneGroup;
import helper.threads.GetCategories;
import helper.threads.GetFavorites;
import helper.threads.GetOfferGroup;
import helper.threads.GetOffers;
import helper.threads.GetOnlyBizName;
import helper.threads.GetPrizes;
import helper.threads.GetPromos;
import helper.threads.SendCheckin;
import helper.threads.SendCheckinPromo;
import helper.threads.SendQualityChildValue;
import helper.threads.SendQualityValue;
import helper.tools.CheckDevices;
import helper.tools.QrReaderAdapter;
import helper.tools.TextQrReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static TextView lbl_test;
	public Button btn;
	QrReaderAdapter adapterQr = new QrReaderAdapter();
	
	Button btn_scan;
	ImageButton btn_help;
	Button btn_bestlist;
	ImageButton imgbtn;
	Button btn_maptips;
	Button btn_change_region;
	ImageButton btn_parking;
	ImageButton btn_taxi;
	ImageButton btn_prizes;
	ImageButton btn_offers;
	
	public static int userid;
	public static String name;
	public static String email;
	public static AnswerActivity act;
	public static Game main_game;
	public static QrScan answer;
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	public static BizneInfo lastbiz;
	public static int lastbizid;
	
	volatile public ProgressDialog pd;
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(CheckDevices.isNetworkAvailable(this)){
        	// Set all buttons
        	ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
        	btn_home.setVisibility(ImageButton.GONE);
        	btn_home.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(intent);
				}
			});
        	
        	btn_home = (ImageButton) findViewById(R.id.btn_home);
        	btn_home.setVisibility(ImageButton.GONE);
        	
        	ImageButton btn_fav = (ImageButton) findViewById(R.id.btn_favorites);
        	btn_fav.setVisibility(ImageButton.VISIBLE);
        	btn_fav.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
					startActivity(intent);
				}
			});
        	
        	// Check if is logged in
        	database.open();
            Cursor c = database.getAllUsers();
            database.close();
        	
        	if(c.getCount() > 0 ){
        		
        		userid = Integer.parseInt(c.getString(1).toString());
        		ImageButton btn_profile = (ImageButton)findViewById(R.id.btn_profile);
        		btn_profile.setVisibility(View.VISIBLE);
        		btn_profile.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), ChatsActivity.class);
						startActivity(intent);
					}
				});
        		
        		database.open();
                c = database.getAllCategories();
                database.close();
                
                if(c.getCount() == 0){
                	loadCategories();
                }
                
//                btn_change_region = (Button)findViewById(R.id.btn_change_city);
//                btn_change_region.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						changeRegion();
//					}
//				});
                
                btn_maptips = (Button)findViewById(R.id.btn_map_tips);
                btn_maptips.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						gotoMapTips();
					}
				});
        		
        		Button btn_favorites = (Button)findViewById(R.id.btn_favorites2);
        		btn_favorites.setOnClickListener(new View.OnClickListener() {
				
        			@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						gotoFavorites();
        				gotoBusinesses();
					}
				});
        		
        		btn_scan = (Button)findViewById(R.id.btn_scan_qr);
        		btn_scan.setOnClickListener(new View.OnClickListener() {
				
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						scanQr();
					}
				});
        		
        		btn_help = (ImageButton)findViewById(R.id.btn_help);
        		btn_help.setVisibility(ImageButton.VISIBLE);
        		btn_help.setOnClickListener(new View.OnClickListener() {
				
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
						startActivity(intent);
					}
				});
        		
        		btn_parking = (ImageButton)findViewById(R.id.btn_parking);
        		btn_parking.setVisibility(View.VISIBLE);
        		btn_parking.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), ParkingsMapActivity.class);
						startActivity(intent);
					}
				});
        		
        		btn_taxi = (ImageButton)findViewById(R.id.btn_taxi);
        		btn_taxi.setVisibility(View.VISIBLE);
        		btn_taxi.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), TaxisMapActivity.class);
						startActivity(intent);
//						getOffers();
					}
				});
        		
        		btn_offers = (ImageButton)findViewById(R.id.btn_offers);
//        		btn_offers.setVisibility(View.VISIBLE);
        		btn_offers.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getOffers();
					}
				});
        		
        		btn_prizes = (ImageButton)findViewById(R.id.btn_usrprizes);
        		btn_prizes.setVisibility(View.VISIBLE);
        		btn_prizes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getPrizes();
					}
				});
        	
        		Button btn_search = (Button) findViewById(R.id.main_btn_search);
        		btn_search.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getApplicationContext(), BusinessActivity.class);
						startActivity(intent);
					}
				});
        		
        		TextView txt_region = (TextView)findViewById(R.id.main_txt_region);
        		database.open();
        		cursor = database.getCity();
        		database.close();
        		txt_region.setText("Tu ciudad: " + cursor.getString(1));
        		
        		// Getting name, email from intent
        		Intent i = getIntent();
        		
        		name = i.getStringExtra("name");
        		email = i.getStringExtra("email");		
        		
        		// Make sure the device has the proper dependencies.
        		GCMRegistrar.checkDevice(this.getApplicationContext());

        		// Make sure the manifest was properly set - comment out this line
        		// while developing the app, then uncomment it when it's ready.
        		GCMRegistrar.checkManifest(this.getApplicationContext());

//        		lblMessage = (TextView) findViewById(R.id.lblMessage);
        		
        		registerReceiver(mHandleMessageReceiver, new IntentFilter(
        				DISPLAY_MESSAGE_ACTION));
        		
        		// Get GCM registration id
        		final String regId = GCMRegistrar.getRegistrationId(this);

        		// Check if regid already presents
        		if (regId.equals("")) {
        			// Registration is not present, register now with GCM			
        			GCMRegistrar.register(this.getApplicationContext(), SENDER_ID);
        		} else {
        			// Device is already registered on GCM
        			if (GCMRegistrar.isRegisteredOnServer(this.getApplicationContext())) {
        				// Skips registration.				
//        				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
        			} else {
        				// Try to register again, but not in the UI thread.
        				// It's also necessary to cancel the thread onDestroy(),
        				// hence the use of AsyncTask instead of a raw thread.
        				final Context context = this;
        				mRegisterTask = new AsyncTask<Void, Void, Void>() {

        					@Override
        					protected Void doInBackground(Void... params) {
        						// Register on our server
        						// On server creates a new user
        						ServerUtilities.register(context, name, email, regId,userid);
        						return null;
        					}

        					@Override
        					protected void onPostExecute(Void result) {
        						mRegisterTask = null;
        					}

        				};
        				mRegisterTask.execute(null, null, null);
        			}
        		}
        	}else{
        		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
        	}
        	
        	
        }else{
        	CheckDevices.showProviderError(this, "Para usar Qrivo necesitas conectarte a Internet.");
        }
        
    }

	public void loadCategories(){
    	
    	pd = ProgressDialog.show( this, "Espere, por favor", "Cargando categorías, esto tomará un tiempo.", true, false);
    	
    	
    	String data = "";
		
    	GetCategories getfavorites = new GetCategories(this, pd);
		getfavorites.execute(new String[] {data,"getCategories"});
    }
	
	private void scanQr(){

		try{
			Log.d("Qrivo", "Qrivo: iniciando qr");
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
	        startActivityForResult(intent, 0);
		}
		catch(Exception e){
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.initiateScan();
		}
        
    }
    
    private void gotoMapTips(){
    	Intent intent = new Intent(getApplicationContext(), MapTipsActivity.class);
		startActivity(intent);
    }
    
    private void gotoBusinesses(){
//    	Intent intent = new Intent(getApplicationContext(), MapActivity.class);
    	Intent intent = new Intent(getApplicationContext(), BusinessActivity.class);
//    	Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
		startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(1, 1, Menu.FIRST, "Salir").setIcon(R.drawable.btn_logout);
        return true;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	if (resultCode == RESULT_CANCELED) {
  	         // Handle cancel
  	    	  //finish();
  	      }else{
  	    	  
	    	String result = intent.getStringExtra("SCAN_RESULT");
	    	Gson gson = new Gson();
	    	try{
	    		if(adapterQr.getQr(this, result)){
	    			
	    		}else{
		    		answer = gson.fromJson(result, QrScan.class);
		    		if(!answer.type.equals("biznegroup") && !answer.type.equals("qrgame") && !answer.type.equals("bestlist") && !answer.type.equals("qualitygroup")){
			    		try{
			    			
			    			database.open();
				   	        cursor = database.getAllUsers();
				   	        database.close();
			    			
			    			String data = "&bizid=" + answer.getBizId() +
			    					"&userid=" + cursor.getString(1).toString();
			    			
		    				GetOnlyBizName getinfo = new GetOnlyBizName(this, pd);
		    				getinfo.execute(new String[] {data,"getBizneInfo"});
			    		}catch(Exception e){
			    			Log.d("Qrivo", "GCMConnection-onCreate: Error: "+e.toString());
			    		}
		    		}
	//	    		Toast.makeText(this, answer.toString() , Toast.LENGTH_LONG).show();
		    		database.open();
		   	        cursor = database.getAllUsers();
		   	        database.close();
		    		// If it is an activity...
		    		if(answer.getType() == 0){
		    			pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		    			try{
		    				String data = "&bizid=" + answer.getBizId() +
		    						"&userid=" + cursor.getString(1).toString();
		    				
		    				GetActivity getactivity = new GetActivity(this, pd );
	    		  			getactivity.execute(new String[] {data,"loadActivity"});
		    			}catch(Exception e){
		    				Log.d("Qrivo", "GCMConnection-onCreate: Error: "+e.toString());
		    				pd.cancel();
	    		  			e.printStackTrace();
	    		  			//finish();
		    			}
		    		// If it is a game...
		    		}else{
		    			if(answer.getType() == 1){
		    				pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
			    			try{
			    				String data = "&qrid="+answer.getQrId();
			    				GetBizInfo getinfo = new GetBizInfo(this, pd,false,true );
		    		  			getinfo.execute(new String[] {data,"getBizId"});
			    				
		    		  			data = "&qrid="+answer.getQrId() +
			    						"&userid=" + cursor.getString(1).toString();
		    		  			
		    		  			GetActivity getactivity = new GetActivity(this, pd );
		    		  			getactivity.execute(new String[] {data,"loadGame"});
		    		  			
		    		  			
		    		  			
		    		  			
			    			}catch(Exception e){
			    				Log.d("Socialdeals", "SetUserCheckIn-onCreate: Error: "+e.toString());
			    				pd.cancel();
		    		  			e.printStackTrace();
		    		  			//finish();
			    			}
		    			}else{
		    				if(answer.type.equals("quality")){
		    					
		    					pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		    					
		    					String data = "&bizid="+answer.getBizId() +
			    						"&value=" + answer.getValue() + "&userid=" + cursor.getString(1).toString();
		    					
		    					SendQualityValue sendquality = new SendQualityValue(this, pd);
		    					sendquality.execute(new String[] {data,"saveQuality"});
		    				}else{
		    					if(answer.type.equals("checkin")){
		    						pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
			    					
			    					String data = "&bizid="+answer.getBizId() +
				    						"&userid=" + cursor.getString(1).toString();
			    					
			    					SendCheckin sendcheckin = new SendCheckin(this, pd);
			    					sendcheckin.execute(new String[] {data,"setCheckin"});
		    					}else{
		    						if(answer.type.equals("promotion")){
		    							pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
				    					
				    					String data = "&bizid="+answer.getBizId() + "&userid=" + cursor.getString(1).toString();
				    					
				    					GetPromos getpromos = new GetPromos(this, pd);
				    					getpromos.execute(new String[] {data,"getPromos"});
		    						}else{
		    							if(answer.type.equals("checkinpromotion")){
		    	    						pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		    		    					
		    		    					String data = "&bizid="+answer.getBizId() +
		    			    						"&userid=" + cursor.getString(1).toString();
		    		    					
		    		    					SendCheckinPromo sendcheckinpromo = new SendCheckinPromo(this, pd);
		    		    					sendcheckinpromo.execute(new String[] {data,"pointsVisit"});
		    							}else{
		    								if(answer.type.equals("bizneinfo")){
		    									pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
			    		    					
			    		    					String data = "&bizid=" + answer.getBizId() +
			    			    						"&userid=" + cursor.getString(1).toString();
			    		    					
			    		    					GetBizInfo getinfo = new GetBizInfo(this, pd);
			    		    					getinfo.execute(new String[] {data,"getBizneInfo"});
		    								}else{
		    									if(answer.type.equals("bestlist")){
			    									pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
				    		    					
				    		    					String data = "&bestlistid=" + answer.getBestListId() +
				    			    						"&userid=" + cursor.getString(1).toString();
				    		    					
				    		    					GetBestList getlist = new GetBestList(this, pd);
				    		    					getlist.execute(new String[] {data,"getBizneBestList"});
		    									}else{
		    										if(answer.type.equals("qualitygroup")){
		    											pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
					    		    					
					    		    					String data = "&idg=" + answer.getIdGroup() + "&val=" + answer.getValue();
					    		    					
					    		    					GetBizneGroup getlist = new GetBizneGroup(this, pd);
					    		    					getlist.execute(new String[] {data,"getBizneGroup"});
		    										}else{
		    											if(answer.type.equals("qualitysubbizne")){
		    												pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		    						    					
		    						    					String data = "&subbizid="+answer.getSubBizId() +
		    							    						"&val=" + answer.getValue();
		    						    					
		    						    					SendQualityChildValue sendquality = new SendQualityChildValue(this, pd);
		    						    					sendquality.execute(new String[] {data,"setQualityChild"});
		    											}else{
		    												if(answer.type.equals("offergroup")){
		    													pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
			    						    					
			    						    					String data = "&groupid=" + answer.getGroupId();
			    						    					
			    						    					GetOfferGroup group = new GetOfferGroup(this, pd);
			    						    					group.execute(new String[] {data,"getOfferGroup"});
		    												}else{
		    													if(answer.type.equals("biznegroup")){
		    		    											pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		    					    		    					
		    					    		    					String data = "&idg=" + answer.getIdGroup() + "&val=0" + answer.getValue();
		    					    		    					
		    					    		    					GetBizneGroup getlist = new GetBizneGroup(this, pd,true);
		    					    		    					getlist.execute(new String[] {data,"getBizneGroup"});
		    													}else{
		    														if(answer.type.equals("androidviewchild")){
		    															pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
			    					    		    					
			    					    		    					String data = "&subbizid=" + answer.getSubBizId() + "&bizid=" + answer.getBizId();
			    					    		    					
			    					    		    					GetBizneData getdata = new GetBizneData(this, pd);
			    					    		    					getdata.execute(new String[] {data,"getSubBizData"});
		    														}else{
		    															if(answer.type.equals("androidview")){
		    																pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
				    					    		    					
				    					    		    					String data = "&bizid=" + answer.getBizId();
				    					    		    					
				    					    		    					GetBizneData getdata = new GetBizneData(this, pd);
				    					    		    					getdata.execute(new String[] {data,"getBizData"});
		    															}else{
		    																if(answer.type.equals("activityqr")){
		    																	openActivity(answer.getActivityId());
		    																}else{
		    																	if(answer.type.equals("activitygroup")){
		    																		openActivityGroup(answer.getIdGroup());
		    																	}
		    																}
		    															}
		    														}
		    													}
		    												}
		    											}
		    										}
		    									}
		    								}
		    							}
		    						}
		    					}
		    				}
		    			}
		    		}
		    		
	    		}
	    	}catch(Exception e){
	    		TextQrReader textqr = new TextQrReader();
	    		textqr.actionQr(this, result);
	    	}
	    	
	    	
	    	//GCMConnection.lbl_test.setText(qr.toString());
  	      }
    }
    
    public void openActivityGroup(int idg){
    	ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&idg=" + idg;
		
		GetActivityGroup getdata = new GetActivityGroup(this, pd);
		getdata.execute(new String[] {data,"getActivityGroup"});
    }
    
    public void openActivity(int actid){
		ProgressDialog pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&actid=" + actid;
		
		GetActivity getdata = new GetActivity(this, pd);
		getdata.execute(new String[] {data,"getActivity"});
	}
    
    public void startActivity(){ 
    	Intent launchGame = new Intent(this, Test.class);
    	startActivity(launchGame);
         
    }
    
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	
	    switch(item.getItemId())
	    {
		    case 1:
		    	// first we delete the table of users.
		    	database.open();
		        Cursor c = database.getAllUsers();
		        database.close();
		        if (c.moveToFirst()){
		        	   do{
		        	      String id = c.getString(0).toString();
		        	      database.open();
		  		          	database.deleteUser(Long.valueOf(id));
		  		          	database.deleteLists();
		  		          	database.deleteFavorites();
		  		          	database.deleteCities();
		  		          	database.deleteZones();
		  		          database.close();
		        	   }while(c.moveToNext());
		        }
		        
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(intent);
			finish();
		        		        	
	    		
		        
		        return true;		    
		    default:
		   	 return false;
		   	 
	
	    }
    }
    
    @SuppressWarnings("unused")
	private void gotoFavorites(){
    	pd = ProgressDialog.show( this, "Cargando...", "Espere, por favor", true, false);
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
    	
    	String data = "&userid=" + cursor.getString(1).toString();
		
		GetFavorites getfavorites = new GetFavorites(this, pd,true);
		getfavorites.execute(new String[] {data,"getFavorites"});
    }
    
    private void getOffers(){
    	pd = ProgressDialog.show( this, "Cargando...", "Espere, por favor", true, false);
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
    	
    	String data = "&userid=" + cursor.getString(1).toString();
		
		GetOffers getfavorites = new GetOffers(this, pd);
		getfavorites.execute(new String[] {data,"getOffers"});
    }
    
    private void getPrizes(){
    	pd = ProgressDialog.show( this, "Cargando...", "Espere, por favor", true, false);
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
    	
    	String data = "&userid=" + cursor.getString(1).toString();
		
		GetPrizes getfavorites = new GetPrizes(this, pd);
		getfavorites.execute(new String[] {data,"getPrizes"});
		
    }
    /**
    * Receiving push messages
	 * */
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			String bizid = intent.getExtras().getString("bizid");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			/**
			 * Take appropriate action on this message
			 * depending upon your app requirement
			 * For now i am just displaying it on the screen
			 * */
//			Intent i = new Intent(getApplicationContext(),ChatActivity.class);
//			i.putExtra("message", newMessage);
//			i.putExtra("bizid", bizid);
//			startActivity(i);
			// Showing received message
//			lblMessage.append(newMessage + "\n");			
//			Toast.makeText(getApplicationContext(), "Nuevo Mensaje: " + newMessage, Toast.LENGTH_LONG).show();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	
	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}
    
}
