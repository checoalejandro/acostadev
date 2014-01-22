package com.testapp.test;

import static com.testapp.test.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.testapp.test.CommonUtilities.EXTRA_MESSAGE;

import java.util.List;

import helper.database.DBAdapter;
import helper.json.BizneInfoList;
import helper.json.ChatMessage;
import helper.threads.GetChat;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	
	List<ChatMessage> messages;
	ProgressDialog dialog;
	ListView list;
	ChatAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		DBAdapter database = new DBAdapter(this);
		database.open();
        Cursor c = database.getAllUsers();
        database.close();
        int userid = Integer.parseInt(c.getString(1).toString());
        Intent intent = getIntent();
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
//        String bizid = intent.getStringExtra("bizid");
//        String message = intent.getStringExtra("message");
        
		dialog = ProgressDialog.show(this, "Cargando...","Cargando chat", true);
		String data = "&userid=" + userid + "&bizid=" + ChatsActivity.bizid;
		
		GetChat chat = new GetChat(this, dialog);
		chat.execute(new String[] {data,"getChat"});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}
	
	public void setList(List<ChatMessage> messages){
		this.messages = messages;
		list = (ListView) findViewById(R.id.list);
        list.setClickable(true);

        adapter = new ChatAdapter(this, this.messages);
        list.setAdapter(adapter);
	}
	
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			String bizid = intent.getExtras().getString("bizid");
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());
			
			messages.add(new ChatMessage(newMessage));
			adapter.notifyDataSetChanged();
			
			// Releasing wake lock
			WakeLocker.release();
		}
	};
	

}
