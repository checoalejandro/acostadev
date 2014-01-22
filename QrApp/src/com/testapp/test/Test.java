package com.testapp.test;

import helper.json.AnswerActivity;
import helper.threads.GetActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.gson.Gson;

public class Test extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		TextView tv = (TextView) findViewById(R.id.lbl_test);
		Gson gson = new Gson();
		@SuppressWarnings("unused")
		AnswerActivity answer = gson.fromJson(GetActivity.jsonEncode.toString(), AnswerActivity.class);
		String text = "";
		for(int i = 0; i < 0; i++){
			text = text + Results.useranswers.get(i).toString() + "\n";
		}
		tv.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

}
