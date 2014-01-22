package com.testapp.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import helper.database.DBAdapter;
import helper.json.ActivityGroup;
import helper.json.BizneInfo;
import helper.json.Passport;
import helper.json.PromoList;
import helper.json.PromotionList;
import helper.json.QrScan;
import helper.threads.GetActivity;
import helper.threads.GetActivityGroup;
import helper.threads.GetBestList;
import helper.threads.GetBizInfo;
import helper.threads.GetBizneData;
import helper.threads.GetBizneGroup;
import helper.threads.GetBizneInteractions;
import helper.threads.GetBizneOffers;
import helper.threads.GetBiznePassports;
import helper.threads.GetBizneTips;
import helper.threads.GetOfferGroup;
import helper.threads.GetOnlyBizName;
import helper.threads.GetPrizes;
import helper.threads.GetPromos;
import helper.threads.SendCheckin;
import helper.threads.SendCheckinPromo;
import helper.threads.SendQualityChildValue;
import helper.threads.SendQualityValue;
import helper.threads.SendTip;
import helper.threads.SetFavorite;
import helper.threads.SetLike;
import helper.tools.QrReaderAdapter;
import helper.tools.TextQrReader;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class BizneInfoActivity extends Activity {
	PromoList promos;
	PromotionList promotions;
	BizneInfo info;
	public Intent callIntent;
	int userpoints;
	QrReaderAdapter adapterQr = new QrReaderAdapter();
	
	public static QrScan answer;
	public static String biznename = "";
	public static int promossize;
	public static int interactionssize = 0;
	public static int tipssize = 0;
	public static int promotionssize;
	public static int offerssize = 0;
	public static int usrpts;
	public static int passportsize = 0;
	public static double latitude;
	public static double longitude;
	public static String name;
	public static ActivityGroup selected_group = null;
	public static String offergroup_name = "";
	public static Passport selected_passport;
//	public static TextView textview = new TextView(null);
	
	ImageButton btn_prizes;
	Button btn_map;
	Button btn_tip;
	Button btn_like;
	Button btn_checkin;
	ScrollView scroll;
	ScrollView childscroll;
	
	
	public DBAdapter database = new DBAdapter(this);
	Cursor cursor;
	
	volatile public ProgressDialog pd;
	
	public BizneInfoActivity(){
		this.promos = GetBizInfo.act.getPrizes();
		this.promotions = GetBizInfo.act.getPromotions();
		this.info = GetBizInfo.act.getBizneInfo();
		this.userpoints = GetBizInfo.act.getUserPoints();
		this.biznename = info.getName();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bizne_info);
		String data = "&bizid=" + MainActivity.answer.getBizId();
		GetBizneTips tips = new GetBizneTips(this);
		tips.execute(new String[] {data,"getTips"});
		GetBizneOffers offers = new GetBizneOffers(this);
		offers.execute(new String[] {data,"getBizneOffers"});
		GetBiznePassports passports = new GetBiznePassports(this);
		passports.execute(new String[] {data,"getPassports"});
		GetBizneInteractions interactions = new GetBizneInteractions(this);
		interactions.execute(new String[] {data,"getBizneInteractions"});
		setElements();
//		BizneInfoActivity.isrunning = true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bizne_info, menu);
		if(info.getTelephone().length() > 0)
			menu.add(1, 1, Menu.FIRST, "Llamar");
		if(info.getEmail().length() > 0)
			menu.add(2, 2, Menu.FIRST, "Enviar Correo");
		return true;
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}
	
    public void openMap(){

		Intent intent = new Intent(this,BizneMapActivity.class);
		startActivity(intent);
	}
	
	public void openTipDialog(){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_tips);
		dialog.setTitle("Selecciona una opción");
		
		Button dialogButton = (Button) dialog.findViewById(R.id.dialog_tips_new);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openNewTipDialog();
				dialog.dismiss();
			}
		});
		
		dialogButton = (Button) dialog.findViewById(R.id.dialog_tips_get);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewTips();
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public void openNewTipDialog(){
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_tip);
		dialog.setTitle("Nuevo tip");
		RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.ratingbar);
		ratingbar.setStepSize(1);
		
		// Set biz info
		final int bizid = MainActivity.answer.getBizId();
		final double lat = info.getLatitude();
		final double lng = info.getLongitude();
		
		Button close = (Button)dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		Button dialogButton = (Button) dialog.findViewById(R.id.btn_send_tip);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				RatingBar ratingbar = (RatingBar) dialog.findViewById(R.id.ratingbar);
				ratingbar.setStepSize(1);
				EditText text = (EditText) dialog.findViewById(R.id.tip_comment);
				final String comment = text.getText().toString();
				final int rating = (int) ratingbar.getRating();
				sendTip(comment,bizid,lat,lng,rating);
				
			}
		});

		dialog.show();
	}
	
	public void like(){
		
		if(GetBizInfo.act.IsLiked()){
			Toast.makeText(this, "Ya te gusta este negocio", Toast.LENGTH_SHORT).show();
			return;
		}
		
		ProgressDialog pd = ProgressDialog.show(this, "Enviando me gusta", "Espere, por favor", true, false);
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
		
		String data = "&bizid=" + MainActivity.answer.getBizId() + "&userid=" + cursor.getString(1).toString();
		
		SetLike like = new SetLike(this, pd);
		like.execute(new String[] {data,"like"});
	}
	
	public void viewTips(){
		
		ProgressDialog pd = ProgressDialog.show(this, "Cargando tips", "Espere, por favor", true, false); 
		
		String data = "&bizid=" + MainActivity.answer.getBizId();
		
		GetBizneTips tips = new GetBizneTips(this, pd);
		tips.execute(new String[] {data,"getTips"});
	}
	
	public void sendTip(String comment,int bizid,double lat, double lng, int rating){
		DBAdapter database = new DBAdapter(this);

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		database.open();
	        cursor = database.getAllUsers();
	        database.close();
		
		String data = "&userid=" + cursor.getString(1).toString() 
					+ "&comment=" + comment
					+ "&bizid=" + bizid 
					+ "&lat=" + lat
					+ "&lng=" + lng
					+ "&rating=" + rating
					+ "&duration=00:00:00"
					+ "&time=" + dateFormat.format(date);
		
		ProgressDialog pd = ProgressDialog.show(this, "Enviando Tip", "Espere, por favor", true, false); 
		
		SendTip tip = new SendTip(this, pd);
		tip.execute(new String[] {data,"saveTip"});
	}
	
	public void setElements(){
//		Typeface font_oldsansblack = Typeface.createFromAsset(getAssets(), "gnuolane.ttf");
		
		Button txt_usr_points = (Button)findViewById(R.id.txt_userpoints2);
		txt_usr_points.setVisibility(View.VISIBLE);
//		txt_usr_points.setTypeface(font_oldsansblack);
		txt_usr_points.setText(GetBizInfo.act.getUserPoints() + "");
		txt_usr_points.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showItems(1);
			}
		});
		
		Button txt_usr_prizes = (Button)findViewById(R.id.txt_userprizes2);
		txt_usr_prizes.setVisibility(View.VISIBLE);
		txt_usr_prizes.setText(GetBizInfo.act.getPrizesCount() + "");
		txt_usr_prizes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getPrizes();
			}
		});
		
		btn_checkin = (Button) findViewById(R.id.business_checkin);
		btn_checkin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setCheckin();
			}
		});
		
		btn_tip = (Button) findViewById(R.id.business_tip);
		btn_tip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openNewTipDialog();
			}
		});
		
		btn_map = (Button) findViewById(R.id.business_map);
		btn_map.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openMap();
			}
		});
		
		btn_like = (Button) findViewById(R.id.business_like);
		btn_like.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				like();
			}
		});
		
		ImageButton btn_add_favorite = (ImageButton) findViewById(R.id.btn_add_favorite);
		btn_add_favorite.setVisibility(ImageButton.VISIBLE);
		btn_add_favorite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				confirmFavorite();
			}
		});
		
		ImageButton btn_home = (ImageButton) findViewById(R.id.btn_go_home);
    	btn_home.setVisibility(ImageButton.GONE);
    	btn_home = (ImageButton) findViewById(R.id.btn_home);
    	btn_home.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gohome();
			}
		});
    	
    	ImageButton back = (ImageButton) findViewById(R.id.btn_back);
//    	back.setBackgroundResource(R.drawable.btn_back_qrivo);
		back.setVisibility(ImageButton.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				setGPSLocation();
				finish();
			}
		});
    	
    	ImageButton btn_scan = (ImageButton) findViewById(R.id.btn_bizinfo_scanqr);
    	btn_scan.setVisibility(ImageButton.VISIBLE);
    	btn_scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scanQr();
			}
		});
    	
		btn_prizes = (ImageButton) findViewById(R.id.btn_prizes3);
		btn_prizes.setVisibility(ImageButton.GONE);
		btn_prizes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				goToPrizes();
			}
		});
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.biz_layout_elements);
		TableLayout table = new TableLayout(this);  
	    table.setStretchAllColumns(true);  
	    table.setShrinkAllColumns(true); 
	    
		TextView tv;
		
		tv = (TextView)findViewById(R.id.bizneinfo_tips_text);
		tv.setVisibility(View.VISIBLE);
		if(info.getLikes() == 0){	
			tv.setText("Sé el primero en dar me gusta a este negocio");
		}else{
			tv.setText("A " + info.getLikes() + " personas les gusta este negocio");
		}
		
		tv = (TextView)findViewById(R.id.lbl_bizname);
		tv.setVisibility(View.GONE);
		tv = (TextView)findViewById(R.id.lbl_name_section);
		tv.setText(this.info.getName());
		
		
		
		tv = (TextView)findViewById(R.id.lbl_category);
//		tv.setTypeface(font_oldsansblack);
		tv.setText(this.info.getCategory());
		if(info.getCategory() == null){
			tv.setVisibility(TextView.GONE);
		}else{
			if(info.getCategory().equals("")){
				tv.setVisibility(TextView.GONE);
			}
		}
		
		tv = (TextView)findViewById(R.id.lbl_address);
		tv.setText(this.info.getAddress() + (this.info.getRegion() != null ?"\n" + this.info.getRegion():""));
		// Telephone
		Button btn_call = (Button)findViewById(R.id.btn_call);
		tv = (TextView)findViewById(R.id.lbl_telephone);
		if(info.getTelephone().equals("") || info.getTelephone() == null){
			btn_call.setVisibility(Button.GONE);
			tv.setVisibility(TextView.GONE);
		}
		tv.setText(this.info.getTelephone());
		btn_call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        showTelephone();
			}
		});
		// E-mail
		Button btn_mail = (Button)findViewById(R.id.btn_mail);
		tv = (TextView)findViewById(R.id.lbl_email);
		tv.setText(this.info.getEmail());
		if(info.getEmail().equals("") || info.getEmail() == null){
			btn_mail.setVisibility(Button.GONE);
			tv.setVisibility(TextView.GONE);
		}
		btn_mail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendEmail();
			}
		});
		
		BizneInfoActivity.promotionssize = (this.promotions == null ? 0 : this.promotions.size());
		BizneInfoActivity.promossize = (this.promos == null ? 0 : this.promos.size());
		BizneInfoActivity.usrpts = this.userpoints;
		
		tv = (TextView)findViewById(R.id.bizneinfo_offers);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showItems(0);
				
			}
		});
		offerssize = 0;
		tv.setText(offerssize + "\nOfertas");
		tv.setTextSize(10);
		tv.setText("Cargando...");
		passportsize = 0;
		tv = (TextView)findViewById(R.id.bizneinfo_prizes);
		tv.setText(passportsize + "\nPlanes");
		tv.setTextSize(10);
		tv.setText("Cargando");
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showItems(1);
			}
		});
		
		tv = (TextView)findViewById(R.id.bizinfo_points_text);
		tv.setTextAppearance(this, R.style.subtitle);
		tv.setText("En este negocio tienes " + userpoints + " puntos.");
		
		tv = (TextView)findViewById(R.id.bizneinfo_points);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showItems(3);
			}
		});
		
		tv = (TextView)findViewById(R.id.bizneinfo_tips);
		tv.setTextSize(10);
		tv.setText("Cargando");
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showItems(2);
			}
		});
		
		tv = (TextView)findViewById(R.id.bizneinfo_points);
		tv.setTextSize(10);
//		tv.setText(usrpts + "\nInteracciones");
		tv.setText("Cargando");
		
		TableRow row;
	    
	    ll = (LinearLayout) findViewById(R.id.biz_promotions);
	    
	    if(this.promotions != null){
	    
		    for(int i = 0; i < this.promotions.size(); i++){
		    	row = new TableRow(this);
				tv = new TextView(this);
			    tv.setText(this.promotions.get(i).getPromotion());
			    row.addView(tv); 
			    
			    table.addView(row);
		    }
	    }else{
	    	row = new TableRow(this);
			tv = new TextView(this);
//		    tv.setText("No hay promociones aún");
			tv.setVisibility(View.GONE);
		    row.addView(tv); 
		    
		    table.addView(row);
	    }
	    
	    ll.addView(table);
	    
	    longitude = info.getLongitude();
	    latitude = info.getLatitude();
	    name = info.getName();
	    
	}
	
	public void goToPrizes(){
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
		
		pd = ProgressDialog.show( this, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ MainActivity.answer.getBizId() + "&userid=" + cursor.getString(1).toString();
		
		GetPromos getpromos = new GetPromos(this, pd);
		getpromos.execute(new String[] {data,"getPromos"});
	}
	
	public void callPhone(){
	    try {
	        callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:"+this.info.getTelephone()));
	        startActivity(callIntent);
	    } catch (ActivityNotFoundException activityException) {
	        Log.e("dialing-example", "Call failed", activityException);
	    }
	}
	
	public void showItems(int item){
		
		LinearLayout ll = null;
		TextView itemstitle = null;
		TextView tv;
		ScrollView scroll;
		
		switch(item){
		case 0:
			scroll = (ScrollView)findViewById(R.id.promos_scroll);
			scroll.setVisibility(View.VISIBLE);
			
			scroll = (ScrollView)findViewById(R.id.prizes_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.tips_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.interactions_scroll);
			scroll.setVisibility(View.GONE);
			
			tv = (TextView)findViewById(R.id.bizneinfo_offers);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
			
			tv = (TextView)findViewById(R.id.comment_instructions);
			tv.setVisibility(View.GONE);
			
			tv = (TextView)findViewById(R.id.bizneinfo_prizes);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_tips);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_points);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			// Offers
			ll = (LinearLayout) findViewById(R.id.biz_promotions);
		    ll.setVisibility(LinearLayout.VISIBLE);
		    if(BizneInfoActivity.offerssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nooffers);
		    	itemstitle.setVisibility(View.VISIBLE);
		    }
		    // Prizes
		    ll = (LinearLayout) findViewById(R.id.biz_prizes);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.passportsize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_noprizes);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Tips
		    ll = (LinearLayout) findViewById(R.id.biz_tips);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.tipssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_notips);
		    	itemstitle.setVisibility(View.GONE);
		    }
		 // Points
		    if(BizneInfoActivity.interactionssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nointeractions);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    ll = (LinearLayout) findViewById(R.id.biz_interactions);
		    ll.setVisibility(LinearLayout.GONE);
			break;
		case 1:
			scroll = (ScrollView)findViewById(R.id.promos_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.prizes_scroll);
			scroll.setVisibility(View.VISIBLE);
			
			scroll = (ScrollView)findViewById(R.id.tips_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.interactions_scroll);
			scroll.setVisibility(View.GONE);
			
			tv = (TextView)findViewById(R.id.bizneinfo_offers);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_prizes);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
			
			tv = (TextView)findViewById(R.id.bizneinfo_tips);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_points);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			// Offers
			ll = (LinearLayout) findViewById(R.id.biz_promotions);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.offerssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nooffers);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Prizes
		    ll = (LinearLayout) findViewById(R.id.biz_prizes);
		    ll.setVisibility(LinearLayout.VISIBLE);
		    if(BizneInfoActivity.passportsize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_noprizes);
		    	itemstitle.setVisibility(View.VISIBLE);
		    }
		    // Tips
		    ll = (LinearLayout) findViewById(R.id.biz_tips);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.tipssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_notips);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Points
		    if(BizneInfoActivity.interactionssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nointeractions);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    ll = (LinearLayout) findViewById(R.id.biz_interactions);
		    ll.setVisibility(LinearLayout.GONE);
			break;
		case 2:
			
			scroll = (ScrollView)findViewById(R.id.prizes_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.promos_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.tips_scroll);
			scroll.setVisibility(View.VISIBLE);
			
			scroll = (ScrollView)findViewById(R.id.interactions_scroll);
			scroll.setVisibility(View.GONE);
			
			tv = (TextView)findViewById(R.id.bizneinfo_offers);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_prizes);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_tips);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
			
			tv = (TextView)findViewById(R.id.bizneinfo_points);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			// Offers
			ll = (LinearLayout) findViewById(R.id.biz_promotions);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.offerssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nooffers);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Prizes
		    ll = (LinearLayout) findViewById(R.id.biz_prizes);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.passportsize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_noprizes);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Tips
		    ll = (LinearLayout) findViewById(R.id.biz_tips);
		    ll.setVisibility(LinearLayout.VISIBLE);
		    if(BizneInfoActivity.tipssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_notips);
		    	itemstitle.setVisibility(View.VISIBLE);
		    }
		    if(info.getLikes() == 0){
		    	tv = (TextView)findViewById(R.id.bizneinfo_tips_text);
				tv.setVisibility(View.GONE);
		    }else{
		    	tv = (TextView)findViewById(R.id.bizneinfo_tips_text);
				tv.setVisibility(View.VISIBLE);
		    }
		 // Points
		    if(BizneInfoActivity.interactionssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nointeractions);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    ll = (LinearLayout) findViewById(R.id.biz_interactions);
		    ll.setVisibility(LinearLayout.GONE);
			break;
		case 3:
			
			scroll = (ScrollView)findViewById(R.id.promos_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.prizes_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.tips_scroll);
			scroll.setVisibility(View.GONE);
			
			scroll = (ScrollView)findViewById(R.id.interactions_scroll);
			scroll.setVisibility(View.VISIBLE);
			
			tv = (TextView)findViewById(R.id.bizneinfo_offers);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_prizes);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_tips);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext);
			
			tv = (TextView)findViewById(R.id.bizneinfo_points);
			tv.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
			
			// Offers
			ll = (LinearLayout) findViewById(R.id.biz_promotions);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.offerssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nooffers);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Prizes
		    ll = (LinearLayout) findViewById(R.id.biz_prizes);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.passportsize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_noprizes);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    // Tips
		    ll = (LinearLayout) findViewById(R.id.biz_tips);
		    ll.setVisibility(LinearLayout.GONE);
		    if(BizneInfoActivity.tipssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_notips);
		    	itemstitle.setVisibility(View.GONE);
		    }
		    tv = (TextView)findViewById(R.id.bizneinfo_tips_text);
			tv.setVisibility(View.GONE);
		    // Points
		    if(BizneInfoActivity.interactionssize == 0){
		    	itemstitle = (TextView)findViewById(R.id.txt_nointeractions);
		    	itemstitle.setVisibility(View.VISIBLE);
		    }
		    ll = (LinearLayout) findViewById(R.id.biz_interactions);
		    ll.setVisibility(LinearLayout.VISIBLE);
			break;
		}
	}
	
	public void setCheckin(){
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
		
		pd = ProgressDialog.show( this, "Enviando información ...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ info.getBizId() +
				"&userid=" + cursor.getString(1).toString();
		
		SendCheckin sendcheckin = new SendCheckin(this, pd);
		sendcheckin.execute(new String[] {data,"setCheckin"});
	}
	
	private void showTelephone(){
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_bizne_telephone);
 
		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.txt_telephone);
		text.setText("Tel: " + info.getTelephone());

		Button call = (Button) dialog.findViewById(R.id.btn_call);
		// if button is clicked, close the custom dialog
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				callPhone();
			}
		});
		
		Button close = (Button)dialog.findViewById(R.id.btn_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
 
		dialog.show();
	}
	
	public void sendEmail(){
		if(this.info.getEmail().length() > 0){
		
			try{
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[]{this.info.getEmail()});		  
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Selecciona tu cliente de correo :"));
			}catch(ActivityNotFoundException activityException){
				Log.e("BizneInfo", "Email failed", activityException);
			}
		}else{
			Toast.makeText(this, "El negocio no cuenta con correo electrónico" , Toast.LENGTH_LONG).show();
		}
	}

    public boolean onOptionsItemSelected(MenuItem item)
    {
    	
	    switch(item.getItemId())
	    {
		    case 1:
		    	// first we delete the table of users.
		    	callPhone();
		        		        	
		        return true;		
		    case 2:
		    	sendEmail();
		    	return true;
		    default:
		   	 return false;
		   	 
	
	    }
    }
    
    public void confirmFavorite(){
    	if(!isFavorite()){
	    	AlertDialog.Builder builder =
	                new AlertDialog.Builder(this);
	 
	        builder.setMessage("¿Deseas agregar " + info.getName() + " a tus favoritos?")
	        .setTitle("Agregar a favoritos")
	        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
	               public void onClick(DialogInterface dialog, int id) {
	                    Log.i("Dialogos", "Confirmacion Aceptada.");
	                        dialog.cancel();
	                        setFavorite();
	                   }
	               })
	        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                        Log.i("Dialogos", "Confirmacion Cancelada.");
	                        dialog.cancel();
	                   }
	               });
	        builder.show();
    	}else{
    		Toast.makeText(this, "Ya está en tus favoritos." , Toast.LENGTH_LONG).show();
    	}
    }
    
    public boolean isFavorite(){
    	database.open();
    	cursor = database.getFavorite(MainActivity.answer.getBizId());
    	database.close();
    	if(cursor.getCount() > 0){
    		return true;
    	}
    	return false;
    }
    
    public void setFavorite(){
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
		
		pd = ProgressDialog.show( this, "Agregando a favoritos...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ MainActivity.answer.getBizId() + "&userid=" + cursor.getString(1).toString() + "&name=" + info.getName() + "&catid=" + info.getCatId();
		
		SetFavorite setfavorite = new SetFavorite(this, pd,this.info);
		setfavorite.execute(new String[] {data,"setFavorite"});
    }
    
    private void scanQr(){
    	Log.d("Socialdeals", "SocialdealsListenerClick-onClick: iniciando qr");
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        this.startActivityForResult(intent, 0);
        
    }
    
    private void getPrizes(){
    	pd = ProgressDialog.show( this, "Cargando...", "Espere, por favor", true, false);
    	
    	database.open();
        cursor = database.getAllUsers();
        database.close();
    	
    	String data = "&userid=" + cursor.getString(1).toString() + "&bizid="+ info.getBizId();
		
		GetPrizes getfavorites = new GetPrizes(this, pd);
		getfavorites.execute(new String[] {data,"getUserBiznePrizes"});
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
			    				QrScan qrscan = new QrScan();
			    				qrscan.setQrId(answer.getQrId());
			    				MainActivity.answer = qrscan;
			    				String data = "&qrid="+MainActivity.answer.getQrId();
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
				    					    		    					
				    					    		    					String data = "&subbizid=" + answer.getSubBizId() + "&bizid=" + answer.getBizId();
				    					    		    					
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

}
