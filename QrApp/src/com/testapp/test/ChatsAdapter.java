package com.testapp.test;

import helper.json.BizneInfo;
import helper.json.BizneInfoList;
import helper.threads.GetChat;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatsAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private Activity act;
    private BizneInfoList list;
    String namebiz = "";

    public ChatsAdapter(Activity act, BizneInfoList list) {
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
        BizneInfo entry = list.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chats_row, null);
        }
        
        TextView tvBizne = (TextView)convertView.findViewById(R.id.txtbizne);
        TextView tvTime = (TextView)convertView.findViewById(R.id.txttime);
        
        tvBizne.setText(entry.getName());
        tvTime.setText(entry.getTime());
        
        convertView.setOnClickListener(this);
        convertView.setTag(entry);
        
        return convertView;
    }

    @Override
    public void onClick(View view) {
    	
        BizneInfo entry = (BizneInfo) view.getTag();
        ChatsActivity.bizid = entry.getBizId();
		
        Intent i = new Intent(this.act,ChatActivity.class);
        act.startActivity(i);
        notifyDataSetChanged();

    }

}