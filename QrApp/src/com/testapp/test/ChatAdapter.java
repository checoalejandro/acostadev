package com.testapp.test;

import helper.json.BizneInfo;
import helper.json.BizneInfoList;
import helper.json.ChatMessage;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private Activity act;
    private List<ChatMessage> list;
    String namebiz = "";

    public ChatAdapter(Activity act, List<ChatMessage> list) {
    	this.act = act;
        this.context = act.getBaseContext();
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
    	ChatMessage entry = list.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_row, null);
        }
        
        TextView tvBizne = (TextView)convertView.findViewById(R.id.txtbizne);
        TextView tvMessage = (TextView)convertView.findViewById(R.id.txtmsg);
        TextView tvTime = (TextView)convertView.findViewById(R.id.txttime);
        
        if(!entry.isUser()){
        	tvBizne.setText(entry.getName());
        }
        else{
        	tvBizne.setText("Tú");
        }
        tvMessage.setText(entry.getMessage());
        tvTime.setText(entry.getTime());
        
//        convertView.setOnClickListener(this);
        
        return convertView;
    }

    @Override
    public void onClick(View view) {
    	
        BizneInfo entry = (BizneInfo) view.getTag();
		
        notifyDataSetChanged();

    }

}