package com.testapp.test;

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

public class ExpandableListAdapter extends BaseExpandableListAdapter {

  private final SparseArray<Group> groups;
  public LayoutInflater inflater;
  public Activity activity;

  public ExpandableListAdapter(Activity act, SparseArray<Group> groups) {
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
  public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    final String children = (String) getChild(groupPosition, childPosition);
    TextView text = null;
    
    if(children.contains("Más información del negocio:")){
    	final int bizid = Integer.parseInt(children.substring(28));
    	      convertView = inflater.inflate(R.layout.listrow_info, null);
    	    text = (TextView) convertView.findViewById(R.id.survey_txt_title);
    	    text.setText("Más información del negocio");
    	   text.setOnClickListener(new OnClickListener() {
    	      @Override
    	      public void onClick(View v) {
    	    	  
    	    	  	openBizne(bizid);

    	      }
    	    });
    }else{

	      convertView = inflater.inflate(R.layout.listrow_details, null);

	    text = (TextView) convertView.findViewById(R.id.survey_txt_title);
	    text.setText(children);
    }
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
      convertView = inflater.inflate(R.layout.listrow_group, null);
    }
    Group group = (Group) getGroup(groupPosition);
    ((CheckedTextView) convertView).setText(group.string);
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
