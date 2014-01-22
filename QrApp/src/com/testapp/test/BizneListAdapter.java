package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.BizneInfo;
import helper.json.BizneInfoList;
import helper.json.QrScan;
import helper.threads.GetBizInfo;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BizneListAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private Activity act;
    private BizneInfoList list;

    public BizneListAdapter(Activity act, BizneInfoList list) {
        this.context = act;
        this.list = list;
        this.act = act;
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
            convertView = inflater.inflate(R.layout.bizne_row, null);
        }
        TextView tvBizneName = (TextView) convertView.findViewById(R.id.bizne);
        tvBizneName.setText(entry.getName());

        TextView  tvRegOriginal = (TextView) convertView.findViewById(R.id.reg_original);
        tvRegOriginal.setText(entry.getRegion());

        ImageView catImg = (ImageView)convertView.findViewById(R.id.image_cat);
        catImg.setImageDrawable(BizneListActivity.getCategoryDrawable(act, entry.getCatId()));
        
        convertView.setOnClickListener(this);
        convertView.setTag(entry);

        // btnRemove.setId(position);
        

        return convertView;
    }

    @Override
    public void onClick(View view) {
        BizneInfo entry = (BizneInfo) view.getTag();
        QrScan qrscan = new QrScan();
		qrscan.setBizId(entry.getBizId());
		MainActivity.answer = qrscan;
		
		DBAdapter database = new DBAdapter(this.act);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		ProgressDialog pd;
		pd = ProgressDialog.show( this.act, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&bizid=" + entry.getBizId() +
				"&userid=" + cursor.getString(1).toString();
		
		GetBizInfo getinfo = new GetBizInfo(this.act, pd);
		getinfo.execute(new String[] {data,"getBizneInfo"});

    }

}