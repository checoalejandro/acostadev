package helper.threads;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import helper.http.HttpRequest;
import helper.json.AnswerGetRoutes;
import helper.json.Route;
import helper.json.Zone;
import helper.tools.JSonError;
import helper.tools.MyZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView.OnChildClickListener;

import com.google.gson.Gson;
import com.testapp.test.BusinessActivity;
import com.testapp.test.CategoriesExpandableListAdapter;
import com.testapp.test.R;
import com.testapp.test.ZoneExpandableListAdapter;

public class GetRoutes extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetRoutes answer;
	List<Route> routes;
	boolean state = false;
	
	ZoneExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
	List<Route> userroute;
	List<Route> feauturedroute;

	public GetRoutes (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetRoutes-doInBackground: Solicitando rutas");
		String response = "";
		try {
			response = client.executeHttpRequest(data, method);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Log.d("Test", "Error--> " + e2.getMessage());
		}
		Gson gson = new Gson();
		try {
			
			Log.d("SocialGo", "GetRoutes-doInBackground: Decodificando respuesta");
			AnswerGetRoutes answer = gson.fromJson(response.toString(), AnswerGetRoutes.class);
			GetRoutes.answer = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			
			JSonError jError = gson.fromJson(response.toString(), JSonError.class);
			if(jError.getMsg().equals("No tienes rutas almacenadas")){
				result = "No tienes rutas almacenadas, para crearlas ve a qrivo.com";
				Log.e("SocialGo", "GetRoutes-doInBackground: No hay rutas");
				Toast.makeText(this.activity, result , Toast.LENGTH_SHORT).show();
				isSuccessfull = false;
			}else{
				result = "Communication error...";
				Log.e("SocialGo", "GetRoutes-doInBackground: fallo el envio del registo");
				Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
			}
			
		}

	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("SocialGo", "GetRoutes-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetRoutes-doInBackground: Decodificando respuesta");
		
		if(isSuccessfull){  
			
			routes = answer.getData();
			
			final Dialog dialog = new Dialog(this.activity);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setContentView(R.layout.dialog_select_route);
//			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int dp = dpToPx(10);
			
			Button close = (Button)dialog.findViewById(R.id.btn_close);
			close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			
			if(routes.size() > 0){
				expListView = (ExpandableListView) dialog.findViewById(R.id.routesExp);
		        listDataHeader = new ArrayList<String>();
		        listDataChild = new HashMap<String, List<String>>();
		        List<String> userroutes = new ArrayList<String>();
		        List<String> featuredroutes = new ArrayList<String>();
		        userroute = new ArrayList<Route>();
		        feauturedroute = new ArrayList<Route>();
		        
		        for(int i = 0; i < routes.size(); i++){
			        if(routes.get(i).isUser()){
			        	userroutes.add(routes.get(i).getName());
			        	userroute.add(routes.get(i));
			        }else{
			        	featuredroutes.add(routes.get(i).getName());
			        	feauturedroute.add(routes.get(i));
			        }
				}
		        listDataHeader.add("Tus Rutas");
	        	listDataChild.put(listDataHeader.get(0), userroutes); // Header, Child data
		        if(featuredroutes.size() > 0){
		        	listDataHeader.add("Rutas Populares");
		        	listDataChild.put(listDataHeader.get(1), featuredroutes);
		        }
				
		        
				listAdapter = new ZoneExpandableListAdapter(activity, listDataHeader, listDataChild);
				
		        // setting list adapter
		        expListView.setAdapter(listAdapter);
		        expListView.setOnChildClickListener(new OnChildClickListener() {
		        	 
		            @Override
		            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		            	if(groupPosition == 0){
//		            		BusinessActivity.routeid = userroute.get(childPosition).getId();
		            		BusinessActivity.isAdmin = false;
		            		setRouteId(userroute.get(childPosition));
		            	}else{
		            		BusinessActivity.isAdmin = true;
//		            		BusinessActivity.routeid = feauturedroute.get(childPosition).getId();
		            		setRouteId(feauturedroute.get(childPosition));
		            	}
//		            	Button btn_zone = (Button) activity.findViewById(R.id.btn_zone);
//						btn_zone.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
						dialog.dismiss();
		                return false;
		            }
		        });
		        expListView.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {
						// TODO Auto-generated method stub
//						Toast.makeText(arg1.getContext(), "int: " + arg2 + "long:" + arg3, Toast.LENGTH_LONG).show();
						if(!(arg2 > (userroute.size() + 1))){
							AlertDialog.Builder builder = new AlertDialog.Builder(arg1.getContext());
							builder.setMessage("¿Desea eliminar " + listDataChild.get(listDataHeader.get(0)).get(arg2 - 1) + "?")
							        .setTitle("Advertencia")
							        .setCancelable(false)
							        .setNegativeButton("Cancelar",
							                new DialogInterface.OnClickListener() {
							                    public void onClick(DialogInterface dialog, int id) {
							                        dialog.cancel();
							                    }
							                })
							        .setPositiveButton("Aceptar",
							                new DialogInterface.OnClickListener() {
							                    public void onClick(DialogInterface dialog, int id) {
//							                    	deleteZone(listDataChild.get(listDataHeader.get(0)).get(arg2 - 1));
							                    	deleteRoute(userroute.get(arg2-1).getId());
							                    }
							                });
							AlertDialog alert = builder.create();
							alert.show();
							
						}
						return false;
					}
				});
//				LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.routes);
//				for(int i = 0; i < routes.size(); i++){
//					Button route = new Button(dialog.getContext());
//					route.setBackgroundResource(R.drawable.select_item);
//					route.setLayoutParams(params);
//					final int index = i;
//					final String name = routes.get(i).getName();
//					route.setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							setRouteId(index);
//
//							BusinessActivity.search_type = "route";
//							Button btn_gps = (Button) activity.findViewById(R.id.btn_gps);
//					        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
//					        
//					        Button btn_zone = (Button) activity.findViewById(R.id.btn_zone);
//					        btn_zone.setBackgroundResource(R.drawable.rounded_bottom_edittext);
//					        
//					        Button btn_street = (Button) activity.findViewById(R.id.btn_street);
//					        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
//					        
//					        Button btn_route = (Button) activity.findViewById(R.id.btn_route);
//					        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
//					        
//					        Button btn_unfilter = (Button) activity.findViewById(R.id.btn_unfilter);
//					        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext);
//					        
//					        TextView tv = (TextView)activity.findViewById(R.id.business_distance_text);
//					        tv.setText(R.string.business_dis);
//					        
//					        LinearLayout ll = (LinearLayout)activity.findViewById(R.id.business_distance_layout);
//					        ll.setVisibility(LinearLayout.VISIBLE);
//					        
//					        Spinner dist = (Spinner)activity.findViewById(R.id.spin_distance);
//					        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, android.R.id.text1);
//					        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//					        dist.setAdapter(spinnerAdapter);
//					        spinnerAdapter.add("0 m");
//					        spinnerAdapter.add("100 m");
//					        spinnerAdapter.add("500 m");
//					        spinnerAdapter.add("1 km");
//					        spinnerAdapter.notifyDataSetChanged();
//					        BusinessActivity.distance_type = 1;
//							dialog.dismiss();
//						}
//					});
//					route.setOnLongClickListener(new OnLongClickListener() {
//						
//						@Override
//						public boolean onLongClick(View v) {
//							// TODO Auto-generated method stub
//							final Dialog dialog = new Dialog(v.getContext());
//							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//							dialog.setContentView(R.layout.dialog_ok_cancel);
//							
//							Button close = (Button)dialog.findViewById(R.id.btn_close);
//							close.setOnClickListener(new OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									dialog.dismiss();
//								}
//							});
//							// set the custom dialog components - text, image and button
//							TextView text = (TextView) dialog.findViewById(R.id.dialogtextMessage);
//							text.setText("¿Deseas eliminar la ruta " + name + "?");
//							
//							Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
//							// if button is clicked, close the custom dialog
//							dialogButtonOk.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									dialog.dismiss();
//									deleteRoute(routes.get(index).getId());
//								}
//							});
//							
//							Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancelar);
//							// if button is clicked, close the custom dialog
//							dialogButtonCancel.setOnClickListener(new OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									dialog.dismiss();
//								}
//							});
//				 
//							dialog.show();
//							return false;
//						}
//					});
//					route.setText(routes.get(i).getName());
//					route.setPadding(dp, dp, dp, dp);
//					ll.addView(route);
//				}
			}else{
				
			}
			pd.dismiss();
			dialog.show();
		}
		else{
			pd.dismiss();
			Toast.makeText(activity, "No tienes rutas almacenadas, para crearlas ve a qrivo.com" , Toast.LENGTH_LONG).show();
		}
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetRoutes-doInBackground: Error cargando rutas"+ e.getMessage());
		  pd.dismiss();
	  }
		
	}
	
    public void deleteRoute(int routeid){
    	
    	ProgressDialog pd = ProgressDialog.show( activity, "Eliminando la ruta", "Espere por favor", true, false);
    	
    	String data = "&routeid=" + routeid;
    	
    	DeleteZone deletezone = new DeleteZone(activity, pd);
    	deletezone.execute(new String[] {data,"deleteRoute"});
    }
	
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.activity.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
        return px;
    }
	
	private void setRouteId(Route r){
		
		BusinessActivity.routeid = r.getId();
		Button btn_zone = (Button) activity.findViewById(R.id.btn_route);
		btn_zone.setText(r.getName());
		
		BusinessActivity.search_type = "route";
		Button btn_gps = (Button) activity.findViewById(R.id.btn_gps);
        btn_gps.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        btn_zone = (Button) activity.findViewById(R.id.btn_zone);
        btn_zone.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_street = (Button) activity.findViewById(R.id.btn_street);
        btn_street.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        Button btn_route = (Button) activity.findViewById(R.id.btn_route);
        btn_route.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
        
        Button btn_unfilter = (Button) activity.findViewById(R.id.btn_unfilter);
        btn_unfilter.setBackgroundResource(R.drawable.rounded_bottom_edittext);
        
        TextView tv = (TextView)activity.findViewById(R.id.business_distance_text);
        tv.setText(R.string.business_dis);
        
        LinearLayout ll = (LinearLayout)activity.findViewById(R.id.business_distance_layout);
        ll.setVisibility(LinearLayout.VISIBLE);
        
        Spinner dist = (Spinner)activity.findViewById(R.id.spin_distance);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dist.setAdapter(spinnerAdapter);
        spinnerAdapter.add("0 m");
        spinnerAdapter.add("100 m");
        spinnerAdapter.add("500 m");
        spinnerAdapter.add("1 km");
        spinnerAdapter.notifyDataSetChanged();
        BusinessActivity.distance_type = 1;
	}


}