package com.testapp.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.database.DBAdapter;
import helper.json.Street;
import helper.json.Zone;
import helper.threads.DeleteZone;
import helper.threads.GetRegions;
import helper.threads.GetRoutes;
import helper.threads.GetSearchString;
import helper.tools.MyZone;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class BusinessActivity extends Activity {
	
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	volatile public ProgressDialog pd;
	SparseArray<Group2> groups = new SparseArray<Group2>();
	ArrayAdapter<String> spinnerAdapter;
	public static String data = "";
	public static int distance_type;
	
	public static int catid = 0;
	public static int subcat = 0;
	public static String catname = "";
	
	public static double latitude;
	public static double longitude;
	public static int limitstart = 0;
	
	private ArrayList<MyZone> zoneslist = new ArrayList<MyZone>();
	private ArrayList<Street> streetslist = new ArrayList<Street>();
	public static MyZone selected_zone;
	public static String search_string = "";
	public static String search_type = "";
	public static int search_distance;
	public static int routeid = -1;
	public static boolean isAdmin = false;
	public static int streetid = -1;
	
	public static double reg_latitude;
	public static double reg_longitude;
	
	ZoneExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
	List<MyZone> userzone;
	List<MyZone> feauturedzone;
    
	Button btn_changecity;
	
	// Listview Adapter
    ArrayAdapter<String> adapter;
	
	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		 
		btn_changecity = (Button) findViewById(R.id.btn_business_change_city);
		btn_changecity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeRegion();
			}
			
		});
		database.open();
		cursor = database.getCity();
		database.close();
		btn_changecity.setText("Cambiar ciudad: " + cursor.getString(1));
		
		setElements();
		setBottomBar();
	}

	private void setBottomBar() {
		// TODO Auto-generated method stub
		Button search = (Button) findViewById(R.id.btn_search_bus);
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.business, menu);
		return true;
	}
	
	
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
	@SuppressLint("CutPasteId")
	private void setElements(){
		
		// Set Title
		TextView title = (TextView)findViewById(R.id.lbl_name_section);
		title.setText("Negocios");
		
		BusinessActivity.catid = 0;
		BusinessActivity.subcat = 0;
		
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
					gohome();
				}
			});
	   	btn_home = (ImageButton) findViewById(R.id.btn_home);
		btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
		
		ImageButton btn_addfav = (ImageButton)findViewById(R.id.btn_add_favorite);
		btn_addfav.setVisibility(ImageButton.GONE);
		
		ImageButton btn_prizes = (ImageButton)findViewById(R.id.btn_prizes3);
		btn_prizes.setVisibility(ImageButton.GONE);
		
		ImageButton btn_favorites = (ImageButton)findViewById(R.id.btn_favorites);
		btn_favorites.setVisibility(ImageButton.VISIBLE);
		btn_favorites.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoFavorites();
			}
		});

		Button unfilter = (Button)findViewById(R.id.btn_unfilter);
		unfilter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Button btn_gps = (Button) findViewById(R.id.btn_gps);
		        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_zone2 = (Button) findViewById(R.id.btn_zone);
		        btn_zone2.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_street = (Button) findViewById(R.id.btn_street);
		        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_route = (Button) findViewById(R.id.btn_route);
		        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_unfilter = (Button) findViewById(R.id.btn_unfilter);
		        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
		        
		        search_type = "";
			}
		});
		
		Button btn_cat = (Button) findViewById(R.id.btn_categories);
		btn_cat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoCategories();
			}
		});
		
		Button btn_gps = (Button) findViewById(R.id.btn_gps);
		btn_gps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setGPSLocation();
			}
		});
		
		Button btn_zone = (Button) findViewById(R.id.btn_zone);
		btn_zone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setZone();
			}
		});
		
		Button btn_route = (Button) findViewById(R.id.btn_route);
		btn_route.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setRoute();
			}
		});
		
		Button btn_street = (Button) findViewById(R.id.btn_street);
		btn_street.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setStreet();
			}
		});
		
		EditText search = (EditText)findViewById(R.id.txt_search);
		search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				search();
				return true;
			}
		});
		
		setGPSLocation();
		btn_gps = (Button) findViewById(R.id.btn_gps);
        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_zone2 = (Button) findViewById(R.id.btn_zone);
        btn_zone2.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        btn_street = (Button) findViewById(R.id.btn_street);
        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        btn_route = (Button) findViewById(R.id.btn_route);
        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_unfilter = (Button) findViewById(R.id.btn_unfilter);
        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
        
        search_type = "";

	}
	
	private void resetSearch(){
		search_distance = 0;
		search_string = "";
		limitstart = 0;
	}
	
	private void search(){
		
		resetSearch();
		search_string = "";
		EditText edt = (EditText) findViewById(R.id.txt_search);
		String search = edt.getText().toString();
		
		if(search.equals("") && catid == 0){
			Toast.makeText(this, "Ingresa texto a buscar y/o selecciona una categoría.", Toast.LENGTH_LONG).show();
			return;
		}
		
		Spinner distance_spin = (Spinner) findViewById(R.id.spin_distance);
		int distance = (Integer) distance_spin.getSelectedItemPosition();
		search_string = search;
		switch(distance_type){
		case 0:switch(distance){
					case 0: distance = 100; break;
					case 1: distance = 500; break;
					case 2: distance = 1000; break;
					case 3: distance = 2000; break;
					case 4: distance = 5000; break;
				}
				break;
		case 1:switch(distance){
					case 0: distance = 50; break;
					case 1: distance = 100; break;
					case 2: distance = 500; break;
					case 3: distance = 1000; break;
				}
				break;
		}
		
		search_distance = distance;
		
		if(!(search_type.equals(""))){
			
			
			
			if(search_type.equals("gps")){
			
				if(!(search.equals("") && catid == 0 && subcat == 0)){
					
					pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
			    	
			    	database.open();
			        cursor = database.getAllUsers();
			        Cursor reg = database.getCity();
			        int regid = Integer.parseInt(reg.getString(2));
			        database.close();
			    	
			    	data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat + "&type=" + search_type + "&lat=" + latitude + "&lng=" + longitude + "&limit=50&limitstart=" + limitstart + "&distance=" + distance;
					
					GetSearchString getsearch = new GetSearchString(this, pd);
					getsearch.execute(new String[] {data,"search"});
				}else{
					Toast.makeText(this, "Ingresa texto a buscar y/o selecciona una categoría.", Toast.LENGTH_LONG).show();
				}
			}else{
				
				if(search_type.equals("zone") && selected_zone != null){
				
					pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
			    	
			    	database.open();
			        cursor = database.getAllUsers();
			        Cursor reg = database.getCity();
			        int regid = Integer.parseInt(reg.getString(2));
			        database.close();
			    	
			    	data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat + "&type=gps" + "&lat=" + selected_zone.getLatitude() + "&lng=" + selected_zone.getLongitude() + "&limit=50&limitstart=" + limitstart + "&distance=" + distance;
					
					GetSearchString getsearch = new GetSearchString(this, pd);
					getsearch.execute(new String[] {data,"search"});
				}else{
					if(search_type.equals("route") && routeid != -1){
						pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
				    	
				    	database.open();
				    	cursor = database.getAllUsers();
				        Cursor reg = database.getCity();
				        int regid = Integer.parseInt(reg.getString(2));
				        database.close();
				    	if(isAdmin){
				    		data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat + "&type=route" + "&lat=&lng=&limit=50&limitstart=" + limitstart + "&distance=" + distance + "&routeid=" + routeid + "&userid=747";
				    	}else{
				    		data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat + "&type=route" + "&lat=&lng=&limit=50&limitstart=" + limitstart + "&distance=" + distance + "&routeid=" + routeid + "&userid=" + cursor.getString(1).toString();
				    	}
						GetSearchString getsearch = new GetSearchString(this, pd);
						getsearch.execute(new String[] {data,"search"});
					}else{
						if(search_type.equals("street") && streetid != -1){
							pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
					    	
					    	database.open();
					    	cursor = database.getAllUsers();
					        Cursor reg = database.getCity();
					        int regid = Integer.parseInt(reg.getString(2));
					        database.close();
					    	
					    	data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat + "&type=street" + "&lat=&lng=&limit=50&limitstart=" + limitstart + "&distance=" + distance + "&macroid=" + streetid;
							
							GetSearchString getsearch = new GetSearchString(this, pd);
							getsearch.execute(new String[] {data,"search"});
						}else{
							pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
					    	
					    	database.open();
					        cursor = database.getAllUsers();
					        Cursor reg = database.getCity();
					        int regid = Integer.parseInt(reg.getString(2));
					        database.close();
					    	
					    	data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat + "&type=" + search_type + "&lat=" + latitude + "&lng=" + longitude + "&limit=100&limitstart=" + limitstart + "&distance=10000";
							
							GetSearchString getsearch = new GetSearchString(this, pd);
							getsearch.execute(new String[] {data,"search"});
						}
					}
				}
			}
		}else{
//			Toast.makeText(this, "Selecciona un método de búsqueda, por zona, calle, ruta o GPS.", Toast.LENGTH_LONG).show();
			pd = ProgressDialog.show( this, "Buscando...", "Espere, por favor", true, false);
	    	
	    	database.open();
	        cursor = database.getAllUsers();
	        Cursor reg = database.getCity();
	        int regid = Integer.parseInt(reg.getString(2));
	        database.close();

	    	data = "&string=" + search + "&regid=" + regid + "&cat1=" + catid + "&cat2=" + subcat  + "&limit=100&limitstart=" + limitstart;
			
			GetSearchString getsearch = new GetSearchString(this, pd);
			getsearch.execute(new String[] {data,"search"});
		}
	}
	
	private void gotoCategories(){

		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_category_select);
		dialog.setTitle("Selecciona una categoría");
		Button close = (Button)dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		Button none = (Button)dialog.findViewById(R.id.btn_descategorize);
		none.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clearCat();
				dialog.dismiss();
			}
		});
		setCategories();
		ExpandableListView listView = (ExpandableListView) dialog.findViewById(R.id.categories_listview);
	    CategoriesExpandableListAdapter adapter = new CategoriesExpandableListAdapter(this,groups,dialog);
	    listView.setAdapter(adapter);
		dialog.show();
	}
	
	private void clearCat(){
		BusinessActivity.catname = "";
		catid = 0;
		subcat = 0;
		  
		Button btn_cat = (Button) findViewById(R.id.btn_categories);
		btn_cat.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		btn_cat.setText("Categorías");
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
		
		ImageButton btn_addfav = (ImageButton)findViewById(R.id.btn_add_favorite);
		btn_addfav.setVisibility(ImageButton.GONE);
		
		ImageButton btn_prizes = (ImageButton)findViewById(R.id.btn_prizes3);
		btn_prizes.setVisibility(ImageButton.GONE);
	}
	
    private void gotoFavorites(){
    	Intent intent = new Intent(getApplicationContext(), FavoriteActivity.class);
		startActivity(intent);
    }
    
    private void openStreetDialog(){
    	
        // List view
        final ListView lv;
        
        // Search EditText
        EditText inputSearch;
    	
    	final Dialog dialog = new Dialog(this);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_select_street);
		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
		Button close = (Button)dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		wmlp.gravity = Gravity.TOP | Gravity.CENTER;
		wmlp.y = 50;   //y position
		
		database.open();
		Cursor streets = database.getAllStreets();
		database.close();
		String[] streetnames = null;
		if(streets.getCount() > 0){
			int i = 0;
			streetnames = new String[streets.getCount()];
			streetslist.clear();
			do{
				
				final String name = reemplazarCaracteresRaros(streets.getString(1));
				streetnames[i] = name;
				Street str = new Street(Integer.parseInt(streets.getString(0)),streets.getString(1));
				streetslist.add(str);
				i++;
			}while(streets.moveToNext());
		}else{
			Toast.makeText(this, "Aún no hay calles asignadas para la ciudad escogida.", Toast.LENGTH_LONG).show();
			return;
		}
		lv = (ListView) dialog.findViewById(R.id.list_view2);
		inputSearch = (EditText) dialog.findViewById(R.id.inputSearch);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("CutPasteId")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Street mystreet = getStreet((String)lv.getItemAtPosition(arg2));
	            streetid = mystreet.getId();
				Button btn_zone = (Button) findViewById(R.id.btn_street);
				btn_zone.setText(mystreet.getName());
				search_type = "street";
				Button btn_gps = (Button) findViewById(R.id.btn_gps);
		        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_zone2 = (Button) findViewById(R.id.btn_zone);
		        btn_zone2.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_street = (Button) findViewById(R.id.btn_street);
		        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
		        
		        Button btn_route = (Button) findViewById(R.id.btn_route);
		        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_unfilter = (Button) findViewById(R.id.btn_unfilter);
		        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        TextView tv = (TextView)findViewById(R.id.business_distance_text);
		        tv.setText(R.string.business_dis);
		        
		        LinearLayout ll = (LinearLayout)findViewById(R.id.business_distance_layout);
		        ll.setVisibility(LinearLayout.VISIBLE);
		        
		        Spinner dist = (Spinner)findViewById(R.id.spin_distance);
		        spinnerAdapter = new ArrayAdapter<String>(arg1.getContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
		        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        dist.setAdapter(spinnerAdapter);
		        spinnerAdapter.add("0 m");
		        spinnerAdapter.add("100 m");
		        spinnerAdapter.add("500 m");
		        spinnerAdapter.add("1 km");
		        spinnerAdapter.notifyDataSetChanged();
		        distance_type = 1;
				dialog.dismiss();
			}
		});
        
        // Adding items to listview
        adapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.dropdown_item, R.id.product_name, streetnames);
        lv.setAdapter(adapter);     

        inputSearch.addTextChangedListener(new TextWatcher() {
             
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	BusinessActivity.this.adapter.getFilter().filter(cs); 
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
    
    private Street getStreet(String name){
    	for(int i = 0; i < streetslist.size(); i++){
    		if(reemplazarCaracteresRaros(streetslist.get(i).getName()).equals(name)){
    			return streetslist.get(i);
    		}
    	}
    	return null;
    }
    
    private void openZoneDialog(){
    	
    	final Dialog dialog = new Dialog(this);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_select_zone);
		
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		database.open();
		Cursor zones = database.getAllZones();
		database.close();
		
		Button close = (Button)dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		Button newzone = (Button)dialog.findViewById(R.id.btn_new_zone);
		newzone.setBackgroundResource(R.drawable.select_item);
//		newzone.setLayoutParams(params);
		int dp = dpToPx(10);
		newzone.setPadding(0, dp, 0, dp);
		newzone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Button btn_zone = (Button) findViewById(R.id.btn_zone);
				btn_zone.setText("Zona");
				selected_zone = null;
				gotoNewZone();
				dialog.dismiss();
			}
		});
		newzone.setText("Nueva Zona");
		newzone.setTextAppearance(this, R.style.subtitle);
		
		if(zones.getCount() > 0){
			zoneslist.clear();
			// get the listview
	        expListView = (ExpandableListView) dialog.findViewById(R.id.zonesExp);
	        listDataHeader = new ArrayList<String>();
	        listDataChild = new HashMap<String, List<String>>();
	        
	        List<String> userzones = new ArrayList<String>();
	        List<String> featuredzones = new ArrayList<String>();
	        userzone = new ArrayList<MyZone>();
	        feauturedzone = new ArrayList<MyZone>();
			do{

		        Zone zone = new Zone(Double.parseDouble(zones.getString(2)), Double.parseDouble(zones.getString(3)), Integer.parseInt(zones.getString(4)), zones.getString(1), zones.getString(5));
		        
		        if(zone.isUser()){
		        	userzones.add(zone.getName());
		        	userzone.add(new MyZone(zone.getLatitude(), zone.getLongitude()));
		        }else{
		        	featuredzones.add(zone.getName());
		        	feauturedzone.add(new MyZone(zone.getLatitude(), zone.getLongitude()));
		        }
			}while(zones.moveToNext());
			listDataHeader.add("Tus zonas");
			listDataChild.put(listDataHeader.get(0), userzones); // Header, Child data
			if(feauturedzone.size() > 0){
				listDataHeader.add("Zonas Populares");
				listDataChild.put(listDataHeader.get(1), featuredzones);
			}
			listAdapter = new ZoneExpandableListAdapter(this, listDataHeader, listDataChild);
			
	        // setting list adapter
	        expListView.setAdapter(listAdapter);
	        expListView.setOnChildClickListener(new OnChildClickListener() {
	        	 
	            @Override
	            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
	            	if(groupPosition == 0){
	            		selected_zone = userzone.get(childPosition);
	            	}else{
	            		selected_zone = feauturedzone.get(childPosition);
	            	}
	            	Button btn_zone = (Button) findViewById(R.id.btn_zone);
					btn_zone.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
					dialog.dismiss();
					setZoneSearch();
	                return false;
	            }
	        });
	        expListView.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {
					// TODO Auto-generated method stub
//					Toast.makeText(arg1.getContext(), "int: " + arg2 + "long:" + arg3, Toast.LENGTH_LONG).show();
					if(!(arg2 > (userzone.size() + 1))){
						AlertDialog.Builder builder = new AlertDialog.Builder(arg1.getContext());
						builder.setMessage("¿Desea eliminar " + listDataChild.get(listDataHeader.get(0)).get(arg2 - 1) + "?")
						        .setTitle("Advertencia")
						        .setCancelable(false)
						        .setNegativeButton("Cancelar",
						                new DialogInterface.OnClickListener() {
						                    public void onClick(DialogInterface dialog2, int id) {
						                        dialog2.cancel();
						                    }
						                })
						        .setPositiveButton("Aceptar",
						                new DialogInterface.OnClickListener() {
						                    public void onClick(DialogInterface dialog2, int id) {
						                    	deleteZone(listDataChild.get(listDataHeader.get(0)).get(arg2 - 1));
						                    	dialog.dismiss();
						                    }
						                });
						AlertDialog alert = builder.create();
						alert.show();
						
					}
					return false;
				}
			});
		}
		
		dialog.show();
    	
    }
    
    public void deleteZone(String name){
    	
    	database.open();
	    cursor = database.getAllUsers();
	    database.close();
    	
    	ProgressDialog pd = ProgressDialog.show( this, "Eliminando la zona", "Espere por favor", true, false);
    	
    	String data = "&name=" + name + "&userid=" + cursor.getString(1).toString();
    	
    	DeleteZone deletezone = new DeleteZone(this, pd);
    	deletezone.execute(new String[] {data,"deleteZone"});
    	
    	database.open();
    	database.deleteZone(name);
    	database.close();
    }
    
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
        return px;
    }
    
    private void gotoNewZone(){
    	Intent i = new Intent(this,NewZoneActivity.class);    
    	startActivityForResult(i, 1);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_CANCELED) {
    		
    	}else{
	    	super.onActivityResult(requestCode, resultCode, data);
			if(data.getExtras().containsKey("zoneChanged")){
				Button btn_zone = (Button) findViewById(R.id.btn_zone);
				btn_zone.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
				
		    	Button btn_gps = (Button) findViewById(R.id.btn_gps);
		        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_street = (Button) findViewById(R.id.btn_street);
		        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_route = (Button) findViewById(R.id.btn_route);
		        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        Button btn_unfilter = (Button) findViewById(R.id.btn_unfilter);
		        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext);
		        
		        TextView tv = (TextView)findViewById(R.id.business_distance_text);
		        tv.setText(R.string.business_radio);
		        
		        Spinner dist = (Spinner)findViewById(R.id.spin_distance);
		        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
		        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		        dist.setAdapter(spinnerAdapter);
		        spinnerAdapter.add("100 m");
		        spinnerAdapter.add("500 m");
		        spinnerAdapter.add("1 km");
		        spinnerAdapter.add("2 km");
		        spinnerAdapter.add("5 km");
		        spinnerAdapter.notifyDataSetChanged();
		        distance_type = 0;
		        
		        search_type = "zone";
				
			}else{
				if(data.getExtras().containsKey("newZoneAdded")){
					String name = data.getStringExtra("newZoneAdded");
					Button btn_zone = (Button) findViewById(R.id.btn_zone);
					btn_zone.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
					btn_zone.setText(name);
					
			    	Button btn_gps = (Button) findViewById(R.id.btn_gps);
			        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			        
			        Button btn_street = (Button) findViewById(R.id.btn_street);
			        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			        
			        Button btn_route = (Button) findViewById(R.id.btn_route);
			        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			        
			        TextView tv = (TextView)findViewById(R.id.business_distance_text);
			        tv.setText(R.string.business_radio);
			        
			        Spinner dist = (Spinner)findViewById(R.id.spin_distance);
			        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
			        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			        dist.setAdapter(spinnerAdapter);
			        spinnerAdapter.add("100 m");
			        spinnerAdapter.add("500 m");
			        spinnerAdapter.add("1 km");
			        spinnerAdapter.add("2 km");
			        spinnerAdapter.add("5 km");
			        spinnerAdapter.notifyDataSetChanged();
			        distance_type = 0;
			        
			        search_type = "zone";
				}
			}
    	}
    }
    
    private void setGPSLocation(){
    	// create class object
        gps = new GPSTracker(this);

        // check if GPS enabled     
        if(gps.canGetLocation()){
             
           latitude = gps.getLatitude();
           longitude = gps.getLongitude();
             
           Button btn_gps = (Button) findViewById(R.id.btn_gps);
           btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
           
           Button btn_zone = (Button) findViewById(R.id.btn_zone);
           btn_zone.setBackgroundResource(R.drawable.rounded_bottom_edittext);
           
           Button btn_street = (Button) findViewById(R.id.btn_street);
           btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
           
           Button btn_route = (Button) findViewById(R.id.btn_route);
           btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
           
           Button btn_unfilter = (Button) findViewById(R.id.btn_unfilter);
	       btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext);
           
           TextView tv = (TextView)findViewById(R.id.business_distance_text);
           tv.setText(R.string.business_radio);
           
           Spinner dist = (Spinner)findViewById(R.id.spin_distance);
           spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
           spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           dist.setAdapter(spinnerAdapter);
           spinnerAdapter.add("100 m");
           spinnerAdapter.add("500 m");
           spinnerAdapter.add("1 km");
           spinnerAdapter.add("2 km");
           spinnerAdapter.add("5 km");
           spinnerAdapter.notifyDataSetChanged();
           distance_type = 0;
           
           search_type = "gps";
        }else{

            gps.showSettingsAlert();
        }
    }
    
    private void setZone(){
    	
        
        openZoneDialog();
    }
    
    private void setZoneSearch(){
    	Button btn_gps = (Button) findViewById(R.id.btn_gps);
        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_zone = (Button) findViewById(R.id.btn_zone);
        btn_zone.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
        
        Button btn_street = (Button) findViewById(R.id.btn_street);
        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_route = (Button) findViewById(R.id.btn_route);
        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_unfilter = (Button) findViewById(R.id.btn_unfilter);
        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        TextView tv = (TextView)findViewById(R.id.business_distance_text);
        tv.setText(R.string.business_radio);
        
        LinearLayout ll = (LinearLayout)findViewById(R.id.business_distance_layout);
        ll.setVisibility(LinearLayout.VISIBLE);
        
        Spinner dist = (Spinner)findViewById(R.id.spin_distance);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dist.setAdapter(spinnerAdapter);
        spinnerAdapter.add("100 m");
        spinnerAdapter.add("500 m");
        spinnerAdapter.add("1 km");
        spinnerAdapter.add("2 km");
        spinnerAdapter.add("5 km");
        spinnerAdapter.notifyDataSetChanged();
        distance_type = 0;
        
        search_type = "zone";
    }
    
    private void setStreet(){
        
        openStreetDialog();
    }
    
    private void setRoute(){
    	
        
        pd = ProgressDialog.show( this, "Rutas...", "Obteniendo rutas", true, false);
        
        database.open();
        cursor = database.getAllUsers();
        Cursor city = database.getCity();
        database.close();
    	
    	String data = "&userid=" + cursor.getString(1).toString() + "&regid=" + city.getString(2).toString();
        
        GetRoutes routes = new GetRoutes(this, pd);
        routes.execute(new String[] {data,"getRoutes"});
    }
    
	private void changeRegion(){
//		ProgressDialog pd;
		DBAdapter database = new DBAdapter(this);
		database.open();
        Cursor cursor = database.getAllUsers();
        database.close();
		
//		pd = ProgressDialog.show( this, "Recibiendo datos de usuario...", "Espere, por favor", true, false);
		
		String data = "&userid=" + cursor.getString(1).toString();
		GetRegions getregions = new GetRegions(this, null,true);
    	getregions.execute(new String[] {data,"getRegions"});
	}
    
}
