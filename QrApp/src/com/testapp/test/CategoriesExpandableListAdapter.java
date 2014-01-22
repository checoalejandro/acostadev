package com.testapp.test;

import helper.database.DBAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class CategoriesExpandableListAdapter extends BaseExpandableListAdapter {

	  private final SparseArray<Group2> groups;
	  public LayoutInflater inflater;
	  public Activity activity;
	  Dialog dialog;

	  public CategoriesExpandableListAdapter(Activity act, SparseArray<Group2> groups,Dialog d) {
	    activity = act;
	    this.groups = groups;
	    inflater = act.getLayoutInflater();
	    this.dialog = d;
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
	    int tmpcatid = 0;
	    for(int i = 0; i < children.length(); i++){
	    	if(children.charAt(i) != 58){
	    		tmptext = tmptext + children.charAt(i);
	    	}else{
	    		tmpcatid = Integer.parseInt(children.substring(i+1));
	    		break;
	    	}
	    }
	    final String texts = tmptext;
	    final int catid = tmpcatid;
	    final boolean subcat;
	    TextView text = null;
	    if (convertView == null) {
	      convertView = inflater.inflate(R.layout.listrow_details2, null);
	    }
	    text = (TextView) convertView.findViewById(R.id.textView1);
	    if(tmptext.contains("Todo en")){
	    	subcat = false;
	    	text.setTextAppearance(convertView.getContext(), R.style.parent_cat);
	    }else{
	    	subcat = true;
	    	text.setTextAppearance(convertView.getContext(), android.R.style.TextAppearance_Small);
	    }
	    
	   
	    
	    text.setText(texts);
	    convertView.setOnClickListener(new OnClickListener() {
	      @Override
	      public void onClick(View v) {
	    	  setId(catid,texts,subcat);
	    	  
	      }
	    });
	    return convertView;
	  }
	  
	  public void setId(int id,String name,boolean subcat){
		  
		  if(subcat){
			  DBAdapter database = new DBAdapter(activity);
			  database.open();
			  Cursor c = database.getParentID(id);
			  database.close();
			  int par = Integer.parseInt(c.getString(0));
			  BusinessActivity.subcat = id;
			  BusinessActivity.catid = par;
		  }else{
			  BusinessActivity.subcat = 0;
			  BusinessActivity.catid = id;
		  }
		  
		  
		  BusinessActivity.catname = name;
		  
		  Button btn_cat = (Button) activity.findViewById(R.id.btn_categories);
		  btn_cat.setBackgroundResource(R.drawable.rounded_bottom_edittext_selected);
		  btn_cat.setText(name);
		  this.dialog.dismiss();
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
	    ((CheckedTextView) convertView).setText(" " +group2.string);
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