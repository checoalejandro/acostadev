package com.testapp.test;

import helper.json.Game;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PostQuestion extends Activity {
	Game g;
	Button save;
	public PostQuestion(){
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int result = getIntent().getIntExtra("result", 0);
		setContentView(R.layout.activity_locations);
		setResults(result);
		//setLocations();
		
//		save = (Button)findViewById(R.id.button1);
//		save.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				returnMain();
//			}
//		});
		
	}
	
	public void setResults(int result){
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.game_locations, null);

        // Find the ScrollView 
        ScrollView sv = (ScrollView) v.findViewById(R.id.biz_scroll);

        // Create a LinearLayout element
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView tv = new TextView(this);
        
		if(result == 0){
			tv = new TextView(this);
	        tv.setText("Incorrect!");
	        ll.addView(tv);
	        for(int i = 0; i < QrGameQuestion.questionQrGame.getAnsNumber(); i++){
	        	if(QrGameQuestion.questionQrGame.getAnswerByIndex(i).isCorrect()){
			        // Add text
			        tv = new TextView(this);
			        tv.setText("Correct Answer: "+QrGameQuestion.questionQrGame.getAnswerByIndex(i).getAnswerText());
			        ll.addView(tv);
	        	}
	        }
		}else{
			if(result == 1){
				tv = new TextView(this);
		        tv.setText("Correct!");
		        ll.addView(tv);
			}else{
				tv = new TextView(this);
		        tv.setText("Ya contestaste esta pregunta");
		        ll.addView(tv);
			}
		}
		tv = new TextView(this);
        tv.setText(" ");
        ll.addView(tv);
        tv = new TextView(this);
        tv.setText("Locations");
        ll.addView(tv);
//		for(int i = 0; i < g.getGame().size(); i++){
//	        // Add text
//	        TextView loc = new TextView(this);
//	        loc.setText("QR"+(i+1)+": "+((g.getGame().get(i).discovered == 0)?g.getGame().get(i).location : "Discovered"));
//	        ll.addView(loc);
//        }

        // Add the LinearLayout element to the ScrollView
        sv.addView(ll);

        // Display the view
        setContentView(v);
		
	}
	
	public void returnMain(){
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locations, menu);
		return true;
	}
	
    public void setLocations(){
    	
    	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.game_locations, null);

        // Find the ScrollView 
        ScrollView sv = (ScrollView) v.findViewById(R.id.biz_scroll);

        // Create a LinearLayout element
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        
//        for(int i = 0; i < g.getGame().size(); i++){
//	        // Add text
//	        TextView loc = new TextView(this);
//	        loc.setText("Location: "+g.getGame().get(i).location);
//	        ll.addView(loc);
//        }

        // Add the LinearLayout element to the ScrollView
        sv.addView(ll);

        // Display the view
        setContentView(v);
    }

}
