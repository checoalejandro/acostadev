package helper.threads;

import java.util.List;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.ActivityGroup;
import helper.json.AnswerGetBizOffers;
import helper.json.AnswerGetTips;
import helper.json.AnswerGetZones;
import helper.json.Zone;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.google.gson.Gson;
import com.testapp.test.ActivityGroupActivity;
import com.testapp.test.BizneDataActivity;
import com.testapp.test.BizneInfoActivity;
import com.testapp.test.BizneInfoOfferGroupActivity;
import com.testapp.test.MainActivity;
import com.testapp.test.R;

public class GetBizneOffers extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	boolean pdshown = true;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetBizOffers answer;
	boolean state = false;
	public int drawablegp = 0;
	public static ActivityGroup selected_activity_group;

	public GetBizneOffers (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	
	public GetBizneOffers (Activity act){
		
		this.activity = act;
		this.c = act.getBaseContext();
		pdshown = false;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("Qrivo", "GetBizneOffers-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("Qrivo", "GetBizneOffers-doInBackground: Decodificando respuesta");
			AnswerGetBizOffers answer = gson.fromJson(response.toString(), AnswerGetBizOffers.class);
			GetBizneOffers.answer = answer;
			if(answer.getStatus() == 1){
				isSuccessfull = true;
				jsonEncode = response.toString();
			}
			
	   		result = answer.getMsg();
		}
		catch(Exception e){
			try{
				JSonError jError = gson.fromJson(response.toString(), JSonError.class);
				if(jError.getMsg().equals("Erro al cargar la lista")){
					result = "Error al cargar la lista";
					Log.e("Qrivo", "GetBizneOffers-doInBackground: No hay actividad");
	//				Toast.makeText(this.activity, "No hay promociones asignadas" , Toast.LENGTH_SHORT).show();
					isSuccessfull = false;
				}else{
					result = "Communication error...";
					Log.e("Qrivo", "GetBizneOffers-doInBackground: fallo el envio del registo");
					Toast.makeText(this.activity, "Fallo el envio del incidente" , Toast.LENGTH_SHORT).show();
				}
			}catch(Exception e2){
				isSuccessfull = false;
			}
			
		}

	    return result;
	}

	protected void onPostExecute(String result) {
	// result is the value returned from doInBackground
	Log.d("Qrivo", "GetBizneOffers-doInBackground: "+result.toString());
	
	try{
		Log.d("Qrivo", "GetBizneOffers-doInBackground: Decodificando respuesta");
		isSuccessfull = true;
		if(isSuccessfull){
			if(answer.getData().getGroups().size() > 0 || answer.getData().getOffers().size() > 0){
				// Set title of offers
				BizneInfoActivity.offerssize = (answer.getData().getGroups().size() + answer.getData().getOffers().size());
				TextView tv_title = (TextView)activity.findViewById(R.id.bizneinfo_offers);
				tv_title.setTextSize(12);
				tv_title.setText(BizneInfoActivity.offerssize + "\nOfertas");
				
				tv_title = (TextView)activity.findViewById(R.id.comment_instructions);
				tv_title.setVisibility(View.GONE);
				tv_title = (TextView)activity.findViewById(R.id.txt_noprizes);
				tv_title.setVisibility(View.GONE);
				
				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
				int p = (int) BizneDataActivity.convertDpToPixel(5, activity);
				params.setMargins(p, p, p, p);
				TextView tv;
				LinearLayout ll = (LinearLayout) activity.findViewById(R.id.biz_promotions);
				
				for(int i = 0; i < answer.getData().getGroups().size(); i++){
					tv = new TextView(activity);
					tv.setTextSize(18);
					tv.setLayoutParams(params);
					tv.setTypeface(null, Typeface.BOLD);
					tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
					Drawable img = getDrawableGroup();
					tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
					tv.setCompoundDrawablePadding(15);
					tv.setText(answer.getData().getGroups().get(i).getName());
					final ActivityGroup act = answer.getData().getGroups().get(i);
					final int index = i;
					if(answer.getData().getGroups().get(i).isScheduled()){
						final int idg = answer.getData().getGroups().get(i).getId();
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								openScheduledActivity(idg);
							}
						});
					}else{
						tv.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								BizneInfoActivity.offergroup_name = answer.getData().getGroups().get(index).getName();
								selected_activity_group = act;
								goToGroupActivities();
							}
						});
					}
					ll.addView(tv);
				}
				
				// Activities
				for(int i = 0; i < answer.getData().getOffers().size(); i++){
					tv = new TextView(this.activity);
					tv.setTextSize(18);
					tv.setLayoutParams(params);
					tv.setTypeface(null, Typeface.BOLD);
					tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
					Drawable img = null;
					switch(answer.getData().getOffers().get(i).getType()){
					case 0: img = this.activity.getResources().getDrawable( R.drawable.ic_survey );
						break;
					case 1:
						img = this.activity.getResources().getDrawable( R.drawable.ic_trivia );
						break;
					case 2:
						img = this.activity.getResources().getDrawable( R.drawable.ic_message );
						break;
					case 3:
						if(answer.getData().getOffers().get(i).isOffer()){
							img = this.activity.getResources().getDrawable( R.drawable.ic_sale);
						}else{
							img = this.activity.getResources().getDrawable( R.drawable.ic_message );
						}
						break;
					case 6:
						img = this.activity.getResources().getDrawable( R.drawable.ic_message );
						break;
					case 4:
						img = this.activity.getResources().getDrawable( R.drawable.ic_link );
						break;
					case 5:
						img = this.activity.getResources().getDrawable( R.drawable.ic_video );
						break;
					}
					tv.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null);
					tv.setCompoundDrawablePadding(15);
					final int points = answer.getData().getOffers().get(i).getPoint();
					final int actid = answer.getData().getOffers().get(i).getId();
					tv.setText(answer.getData().getOffers().get(i).getName());
					tv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(points > 0)
								showMessagePoints(points,actid);
							else
								openActivity(actid);
						}
					});
					ll.addView(tv);
				}
			}else{
				TextView tv_title = (TextView)activity.findViewById(R.id.txt_nooffers);
//				tv_title.setText("No existen ofertas por el momento");
				tv_title.setVisibility(View.VISIBLE);
				
				tv_title = (TextView)activity.findViewById(R.id.bizneinfo_offers);
				tv_title.setTextSize(12);
				tv_title.setText(BizneInfoActivity.offerssize + "\nOfertas");
			}
		}
		else{
			if(pdshown)
				pd.dismiss();
			Toast.makeText(activity, "No existen ofertas almacenados." , Toast.LENGTH_LONG).show();
			
		}
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("Qrivo", "GetBizneOffers-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
	  }
	
	}
	
	private Drawable getDrawableGroup(){
		
		Drawable dble = null;
		
		if(drawablegp > 4){
			drawablegp = 0;
		}
		
		switch(drawablegp){
		case 0: dble = this.activity.getResources().getDrawable( R.drawable.ic_group_r );
			break;
		case 1: dble = this.activity.getResources().getDrawable( R.drawable.ic_group_b );
			break;
		case 2: dble = this.activity.getResources().getDrawable( R.drawable.ic_group_y );
			break;
		case 3: dble = this.activity.getResources().getDrawable( R.drawable.ic_group_g );
			break;
		case 4: dble = this.activity.getResources().getDrawable( R.drawable.ic_group_p );
			break;
		}
		
		drawablegp++;
		
		return dble;
	}

	private void openScheduledActivity(int idg){
		ProgressDialog pd = ProgressDialog.show( this.activity, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this.activity);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&idg=" + idg;
		
		GetActivity getdata = new GetActivity(this.activity, pd);
		getdata.execute(new String[] {data,"getAndroidScheduledActivity"});
	}
	
	public void goToGroupActivities(){
		
		Intent intent = new Intent(this.activity, BizneInfoOfferGroupActivity.class);
		activity.startActivity(intent);
	}
	
	private void showMessagePoints(int points,final int actid){
		final Dialog dialog = new Dialog(this.activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_ok);

		// set the custom dialog components - text, image and button
		TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
		tv.setText("Esta actividad te dará " + points + " puntos por contestarla.");

		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				finishActivity();
				openActivity(actid);
			}
		});
		
		dialog.show();
	}
	
	private void openActivity(int actid){
		ProgressDialog pd = ProgressDialog.show( this.activity, "Recibiendo información ...", "Espere, por favor", true, false);
		
		DBAdapter db = new DBAdapter(this.activity);
		db.open();
		Cursor c = db.getAllUsers();
		db.close();
		
		String data = "&userid=" + c.getString(1).toString() + "&actid=" + actid;
		
		GetActivity getdata = new GetActivity(this.activity, pd);
		getdata.execute(new String[] {data,"getActivity"});
	}
}