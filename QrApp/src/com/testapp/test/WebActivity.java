package com.testapp.test;

import helper.threads.GetActivity;
import helper.threads.GetOnlyBizName;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

public class WebActivity extends Activity {
	
	WebView webView;
	String url;
	
	public WebActivity(){
		this.url = GetActivity.url;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		ImageButton back = (ImageButton) findViewById(R.id.btn_back);
		back.setVisibility(ImageButton.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				setGPSLocation();
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
		TextView tv = (TextView) findViewById(R.id.lbl_name_section);
		tv.setText(GetOnlyBizName.act.getBizneInfo().getName());
		
		tv = (TextView) findViewById(R.id.web_name);
		tv.setText(GetActivity.act.getActivity().getName());
		
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setSupportZoom(true);
        webView.loadUrl(GetActivity.url);
	}
	
	public void gohome(){
		Intent a = new Intent(this,MainActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web, menu);
		
		menu.add(1, 1, Menu.FIRST, "Abrir en otro navegador");
		
		
		return true;
	}
	
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	Intent intent;
	    switch(item.getItemId())
	    {
	    
		    case 1:
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(GetActivity.act.getData().getLink()));
				this.startActivity(intent);
		        return true;		    
		    default:
		   	 return false;
		   	 
	
	    }
    }
	
	private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
