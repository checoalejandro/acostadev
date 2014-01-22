package helper.threads;

import java.util.List;

import helper.database.DBAdapter;
import helper.http.HttpRequest;
import helper.json.AnswerGetTips;
import helper.json.AnswerGetZones;
import helper.json.Zone;
import helper.tools.JSonError;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.testapp.test.BizneInfoActivity;
import com.testapp.test.MainActivity;
import com.testapp.test.R;

public class GetBizneTips extends AsyncTask <String, Void, String> {
	
	Activity activity;
	Context c;
	boolean isSuccessfull = false;
	boolean pdshown = true;
	volatile public ProgressDialog pd;
	public static String jsonEncode;
	public static AnswerGetTips answer;
	boolean state = false;

	public GetBizneTips (Activity act, ProgressDialog tempd){
		
		this.activity = act;
		this.c = act.getBaseContext();
		this.pd = tempd;
		
	}
	
	public GetBizneTips (Activity act){
		
		this.activity = act;
		this.c = act.getBaseContext();
		pdshown = false;
		
	}

	protected String doInBackground(String... params) {
		
		String data = params[0];
		String method = params[1];
		
		String result = "";
		
		HttpRequest client = new HttpRequest(c);
		Log.d("SocialGo", "GetBizneTips-doInBackground: Enviando datos para login del usuario al servidor");
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
			
			Log.d("SocialGo", "GetBizneTips-doInBackground: Decodificando respuesta");
			AnswerGetTips answer = gson.fromJson(response.toString(), AnswerGetTips.class);
			GetBizneTips.answer = answer;
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
					Log.e("SocialGo", "GetBizneTips-doInBackground: No hay actividad");
	//				Toast.makeText(this.activity, "No hay promociones asignadas" , Toast.LENGTH_SHORT).show();
					isSuccessfull = false;
				}else{
					result = "Communication error...";
					Log.e("SocialGo", "GetBizneTips-doInBackground: fallo el envio del registo");
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
	Log.d("SocialGo", "GetBizneTips-doInBackground: "+result.toString());
	
	try{
		Log.d("SocialGo", "GetBizneTips-doInBackground: Decodificando respuesta");
		isSuccessfull = true;
		if(isSuccessfull){  
			if(!pdshown){
				
				TextView tips = (TextView)activity.findViewById(R.id.bizneinfo_tips);
				tips.setText(answer.getData().size() + "\nTips");
				tips.setTextSize(12);
				
				LinearLayout ll = (LinearLayout) activity.findViewById(R.id.biz_tips);
//				TextView tv = new TextView(activity);
				
				BizneInfoActivity.tipssize = answer.getData().size();
				
				if(answer.getData().size() == 0){
//					tv.setText("Aún no hay tips de este negocio");
//					ll.addView(tv);
				}else{
					tips = (TextView)activity.findViewById(R.id.txt_noprizes);
					tips.setVisibility(View.GONE);
					for(int i=0; i < answer.getData().size(); i++){
						LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						LinearLayout tip = new LinearLayout(activity);
						tip.setOrientation(LinearLayout.VERTICAL);
						tip.setPadding(6, 6, 6, 6);
						tip.setLayoutParams(params);
						
						LinearLayout top_tip = new LinearLayout(activity);
						top_tip.setOrientation(LinearLayout.HORIZONTAL);
						top_tip.setPadding(0, 5, 5, 5);
						params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						top_tip.setLayoutParams(params);
						
						TextView username = new TextView(activity);
						username.setTextAppearance(activity, R.style.subtitle);
						username.setText(answer.getData().get(i).getUserName());
						username.setPadding(5, 5, 5, 5);
						params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						username.setLayoutParams(params);
						top_tip.addView(username);
						
						LinearLayout ratingL = new LinearLayout(activity);
						ratingL.setOrientation(LinearLayout.HORIZONTAL);
						ratingL.setPadding(5, 5, 2, 2);
						RatingBar rating = new RatingBar(activity, null, android.R.attr.ratingBarStyleSmall);
						float rat = answer.getData().get(i).getRating();
						params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						rating.setLayoutParams(params);
						
						rating.setStepSize((float) 1);
						rating.setNumStars(5);
						rating.setRating(rat);
						ratingL.addView(rating);
						tip.addView(ratingL);
						
						TextView comment = new TextView(activity);
						comment.setText(this.cutTip(answer.getData().get(i).getTip(), 75));
						comment.setPadding(5, 2, 5, 5);
						comment.setLayoutParams(params);
						final int index = i;
						comment.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								openComment(index);
							}
						});
						tip.addView(comment);
						
						
						
						
						ll.addView(tip);
					}
				}
			}else{
				if(answer.getData().size() == 0){
					pd.dismiss();
					Toast.makeText(activity, "Aún no hay tips para este negocio", Toast.LENGTH_SHORT).show();
				}else{
				
					final Dialog dialog = new Dialog(activity);
					dialog.setContentView(R.layout.dialog_bizne_tips);
					dialog.setTitle("Tips");
					
					LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.bizne_tips_layout);
					
					for(int i=0; i < answer.getData().size(); i++){
						LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
						LinearLayout tip = new LinearLayout(dialog.getContext());
						tip.setBackgroundResource(R.drawable.select_item);
						tip.setOrientation(LinearLayout.VERTICAL);
						tip.setPadding(10, 10, 10, 10);
						tip.setLayoutParams(params);
						
						TextView username = new TextView(activity);
						username.setTextAppearance(dialog.getContext(), R.style.subtitle);
						username.setText(answer.getData().get(i).getUserName());
						username.setPadding(5, 5, 5, 5);
						username.setLayoutParams(params);
						tip.addView(username);
						
						TextView comment = new TextView(activity);
						comment.setText(answer.getData().get(i).getTip());
						comment.setPadding(5, 5, 5, 5);
						comment.setLayoutParams(params);
						tip.addView(comment);
						
						LinearLayout ratingL = new LinearLayout(dialog.getContext());
						ratingL.setOrientation(LinearLayout.HORIZONTAL);
						ratingL.setPadding(5, 5, 5, 5);
						RatingBar rating = new RatingBar(dialog.getContext(), null, android.R.attr.ratingBarStyleSmall);
						float rat = answer.getData().get(i).getRating();
						params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						rating.setLayoutParams(params);
						
						rating.setStepSize((float) 1);
						rating.setNumStars(5);
						rating.setRating(rat);
						ratingL.addView(rating);
						tip.addView(ratingL);
						
						ll.addView(tip);
					}
					pd.dismiss();
					dialog.show();
				}
			}
			
		}
		else{
			if(pdshown)
				pd.dismiss();
			Toast.makeText(activity, "No existen tips almacenados." , Toast.LENGTH_LONG).show();
			
		}
	
	  }
	  catch(Exception e){
		  Toast.makeText(activity , "Fallo la conexión, intentelo de nuevo mas tarde" , Toast.LENGTH_SHORT).show();
		  Log.e("SocialGo", "GetBizneTips-doInBackground: Error cargando actividad"+ e.getMessage());
		  pd.dismiss();
	  }
	
	}
	
	private void openComment(int index){
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_showtip);
		
		LinearLayout ll = (LinearLayout)dialog.findViewById(R.id.linearLayout1);
		
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout tip = new LinearLayout(dialog.getContext());
//		tip.setBackgroundResource(R.drawable.select_item);
		tip.setOrientation(LinearLayout.VERTICAL);
		tip.setPadding(10, 10, 10, 10);
		tip.setLayoutParams(params);
		
		TextView username = new TextView(activity);
		username.setTextAppearance(dialog.getContext(), R.style.subtitle);
		username.setText(answer.getData().get(index).getUserName());
		username.setPadding(5, 5, 5, 5);
		username.setLayoutParams(params);
		tip.addView(username);
		
		LinearLayout ratingL = new LinearLayout(dialog.getContext());
		ratingL.setOrientation(LinearLayout.HORIZONTAL);
		ratingL.setPadding(5, 5, 5, 5);
		RatingBar rating = new RatingBar(dialog.getContext(), null, android.R.attr.ratingBarStyleSmall);
		float rat = answer.getData().get(index).getRating();
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rating.setLayoutParams(params);
		
		rating.setStepSize((float) 1);
		rating.setNumStars(5);
		rating.setRating(rat);
		ratingL.addView(rating);
		tip.addView(ratingL);
		
		TextView comment = new TextView(activity);
		comment.setText(answer.getData().get(index).getTip());
		comment.setPadding(5, 5, 5, 5);
		comment.setLayoutParams(params);
		tip.addView(comment);
		
		ll.addView(tip);
		
		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButton.setText("Cerrar");
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	private String cutTip(String tip, int charno){
		charno -= 3;
		String newtip = "";
		for(int i = 0; i < charno && i != tip.length(); i++){
			newtip = newtip + String.valueOf(tip.charAt(i));
		}
		
		if(newtip.length() == charno){
			newtip = newtip + "...";
		}
		
		return newtip;
	}


}