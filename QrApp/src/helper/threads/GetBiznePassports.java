package helper.threads;

import java.util.List;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.ActivityGroup;
import helper.json.AnswerGetBizOffers;
import helper.json.AnswerGetPassports;
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
import com.testapp.test.PassportActivity;
import com.testapp.test.R;

public class GetBiznePassports extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	boolean pdshown = true;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetPassports answer;
	public static ActivityGroup selected_activity_group;

	public GetBiznePassports (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	
	public GetBiznePassports (Activity act){
		
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
			AnswerGetPassports answer = gson.fromJson(response.toString(), AnswerGetPassports.class);
			GetBiznePassports.answer = answer;
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
			
			TextView title;
			title = (TextView)activity.findViewById(R.id.bestlist_lbl_listname);
			if(answer.getPassports().size() == 0){
				title.setText("No hay planes por el momento");
				title.setVisibility(View.GONE);
			}else{
				title = (TextView)activity.findViewById(R.id.txt_noprizes);
				title.setVisibility(View.GONE);
			}
			
			LinearLayout ll = (LinearLayout) activity.findViewById(R.id.biz_prizes);
			ll.setVisibility(View.GONE);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			BizneInfoActivity.passportsize = answer.getPassports().size();
			TextView itemstitle = (TextView)activity.findViewById(R.id.bizneinfo_prizes);
			itemstitle.setText(answer.getPassports().size() + "\nPlanes");
			itemstitle.setTextSize(12);
			int p = (int) BizneDataActivity.convertDpToPixel(5, activity);
			params.setMargins(p, p, p, p);
			TextView tv;
			
			for(int i = 0; i < answer.getPassports().size(); i++){
				tv = new TextView(activity);
				tv.setTextSize(18);
				tv.setLayoutParams(params);
				tv.setTypeface(null, Typeface.BOLD);
				tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				tv.setText(answer.getPassports().get(i).getName());
				final int index = i;
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BizneInfoActivity.selected_passport = answer.getPassports().get(index);
						gotoPassort();
					}
				});
				ll.addView(tv);
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
	
	private void gotoPassort(){
		Intent intent = new Intent(this.activity, PassportActivity.class);
		activity.startActivity(intent);
	}
}