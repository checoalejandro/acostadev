package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.BizneInfoList;
import helper.threads.GetChats;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ListView;

public class ChatsActivity extends Activity {

	public BizneInfoList biz;
	public static int bizid = 0;
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats);
		
		DBAdapter database = new DBAdapter(this);
		database.open();
        Cursor c = database.getAllUsers();
        database.close();
        int userid = Integer.parseInt(c.getString(1).toString());
		
		dialog = ProgressDialog.show(this, "Cargando...","Cargando chats", true);
		String data = "&userid=" + userid;
		
		GetChats chats = new GetChats(this, dialog);
		chats.execute(new String[] {data,"getChats"});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
	public void setList(BizneInfoList biz){
		this.biz = biz;
		ListView list = (ListView) findViewById(R.id.list);
        list.setClickable(true);

        ChatsAdapter adapter = new ChatsAdapter(this, this.biz);
        list.setAdapter(adapter);
	}

}
