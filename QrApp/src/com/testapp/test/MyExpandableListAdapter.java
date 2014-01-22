package com.testapp.test;

import com.google.zxing.qrcode.encoder.QRCode;

import helper.database.DBAdapter;
import helper.json.QrScan;
import helper.threads.GetBizInfo;
import helper.threads.GetOnlyBizInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	  private final SparseArray<Group2> groups;
	  public LayoutInflater inflater;
	  public Activity activity;

	  public MyExpandableListAdapter(Activity act, SparseArray<Group2> groups) {
	    activity = act;
	    this.groups = groups;
	    inflater = act.getLayoutInflater();
	  }

	  @Override
	  public Object getChild(int groupPosition, int childPosition) {
	    return groups.get(groupPosition).children.get(childPosition);
	  }

	  @Override
	  public long getChildId(int groupPosition, int childPosition) {
	    return 0;
	  }

	  @Override
	  public View getChildView(int groupPosition, final int childPosition,
	      boolean isLastChild, View convertView, ViewGroup parent) {
	    final String children = (String) getChild(groupPosition, childPosition);
	    String tmptext = "";
	    int tmpbizid = 0;
	    for(int i = 0; i < children.length(); i++){
	    	if(children.charAt(i) != 58){
	    		tmptext = tmptext + children.charAt(i);
	    	}else{
	    		tmpbizid = Integer.parseInt(children.substring(i+1));
	    		break;
	    	}
	    }
	    final String texts = tmptext;
	    final int bizid = tmpbizid;
	    TextView text = null;
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.listrow_details2, null);
	    }
	    text = (TextView) convertView.findViewById(R.id.textView1);
	    text.setText(texts);
	    convertView.setOnClickListener(new OnClickListener() {
	      @Override
	      public void onClick(View v) {
	    	  openBizne(bizid);
	      }
	    });
	    return convertView;
	  }
	  
		public void openBizne(int bizid){
			 
			DBAdapter database = new DBAdapter(this.activity);
			Cursor cursor = null;
			
			database.open();
	        cursor = database.getAllUsers();
	        database.close();
	        
			ProgressDialog pd;
	 				pd = ProgressDialog.show( this.activity, "Cargando negocio...", "Espere, por favor", true, false);
	 				
	 				String data = "&bizid=" + bizid +
	 						"&userid=" + cursor.getString(1).toString();
	 				
	 				QrScan qrscan = new QrScan();
	 				qrscan.setBizId(bizid);
	 				MainActivity.answer = qrscan;
	 				
	 				GetBizInfo getinfo = new GetBizInfo(this.activity, pd);
					getinfo.execute(new String[] {data,"getBizneInfo"});
		}

	  @Override
	  public int getChildrenCount(int groupPosition) {
	    return groups.get(groupPosition).children.size();
	  }

	  @Override
	  public Object getGroup(int groupPosition) {
	    return groups.get(groupPosition);
	  }

	  @Override
	  public int getGroupCount() {
	    return groups.size();
	  }

	  @Override
	  public void onGroupCollapsed(int groupPosition) {
	    super.onGroupCollapsed(groupPosition);
	  }

	  @Override
	  public void onGroupExpanded(int groupPosition) {
	    super.onGroupExpanded(groupPosition);
	  }

	  @Override
	  public long getGroupId(int groupPosition) {
	    return 0;
	  }

	  @Override
	  public View getGroupView(int groupPosition, boolean isExpanded,
	      View convertView, ViewGroup parent) {
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.listrow_group2, null);
	    }
	    Group2 group2 = (Group2) getGroup(groupPosition);
	    ((CheckedTextView) convertView).setText(group2.string);
	    ((CheckedTextView) convertView).setChecked(isExpanded);
	    return convertView;
	  }

	  @Override
	  public boolean hasStableIds() {
	    return false;
	  }

	  @Override
	  public boolean isChildSelectable(int groupPosition, int childPosition) {
	    return false;
	  }
	} 