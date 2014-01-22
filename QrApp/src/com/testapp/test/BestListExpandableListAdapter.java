package com.testapp.test;

import helper.database.DBAdapter;
import helper.threads.DeleteFavorite;
import helper.threads.GetBestList;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;

public class BestListExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems, child;

	public BestListExpandableListAdapter(ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}
	
	public void openBestList(int bestlistid){
		
		DBAdapter database = new DBAdapter(activity);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		ProgressDialog pd;
		pd = ProgressDialog.show( activity, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&bestlistid=" + bestlistid + "&userid=" + cursor.getString(1).toString();;
		
		GetBestList getlist = new GetBestList(activity, pd,true);
		getlist.execute(new String[] {data,"getBizneBestList"});
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		child = (ArrayList<String>) childtems.get(groupPosition);

		TextView textView = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group, null);
		}

		textView = (TextView) convertView.findViewById(R.id.textView1);
		ImageButton deleteView = (ImageButton) convertView.findViewById(R.id.favorite_delete);
		deleteView.setVisibility(ImageButton.GONE);
		String children = child.get(childPosition);
		String tmptext = "";
	    int tmpbestlist = 0;
	    for(int i = 0; i < children.length(); i++){
	    	if(children.charAt(i) != 58){
	    		tmptext = tmptext + children.charAt(i);
	    	}else{
	    		tmpbestlist = Integer.parseInt(children.substring(i+1));
	    		break;
	    	}
	    }
	    final int bestlist = tmpbestlist;
		textView.setText(tmptext);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				openBestList(bestlist);
			}
		});
		return convertView;
	}
	
    public void removeGroup(int group) {
        //TODO: Remove the according group. Dont forget to remove the children aswell!
        Log.v("Adapter", "Removing group"+group);
        notifyDataSetChanged();
    } 

    public void removeChild(int group, int child) {
        //TODO: Remove the according child
        Log.v("Adapter", "Removing child "+child+" in group "+group);
        @SuppressWarnings("unchecked")
		ArrayList<String> list = (ArrayList<String>) childtems.get(group);
        list.remove(child);
        if(list.size() == 0){
        	removeGroup(group);
        }
        notifyDataSetChanged();
    }
    
    public void removeFavorite(int bizid){
    	
    	DBAdapter database = new DBAdapter(this.activity);
    	Cursor cursor;
    	database.open();
        cursor = database.getAllUsers();
        database.close();
		
		ProgressDialog pd = ProgressDialog.show( activity, "Eliminando de favoritos...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ bizid + "&userid=" + cursor.getString(1).toString();
		
		DeleteFavorite setfavorite = new DeleteFavorite(activity, pd,bizid);
		setfavorite.execute(new String[] {data,"deleteFavorite"});
    }
	
	public void showDialog(int grouppos, boolean islast, int position,final int bizid,final String text){

		final Dialog dialog = new Dialog(activity);
		dialog.setContentView(R.layout.dialog_ok_cancel);
		dialog.setTitle("Eliminar negocio");

		// set the custom dialog components - text, image and button
		TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
		tv.setText("¿Deseas eliminar " + text +" de tus favoritos?");

		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		final int pos = position;
		final int group = grouppos;
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeChild(group, pos);
            	removeFavorite(bizid);
            	dialog.hide();
			}
		});
		
		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancelar);
		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.hide();
			}
		});
		dialog.show();
	}
	
	public void notifyObj(){
		this.notifyDataSetChanged();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row, null);
		}

		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
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
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
