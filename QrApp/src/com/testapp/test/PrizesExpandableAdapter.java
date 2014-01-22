package com.testapp.test;

import helper.database.DBAdapter;
import helper.json.QrScan;
import helper.json.UserPrize;
import helper.threads.DeleteFavorite;
import helper.threads.DeletePrize;
import helper.threads.GetBizInfo;
import helper.threads.RedeemPrize;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class PrizesExpandableAdapter extends BaseExpandableListAdapter implements OnClickListener{

	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems;
	private ArrayList<UserPrize> child;
	private Activity act;
	private Context context;

	public PrizesExpandableAdapter(Activity act, ArrayList<String> parents, ArrayList<Object> childern) {
		this.parentItems = parents;
		this.childtems = childern;
		this.act = act;
		this.context = act.getBaseContext();
	}

	public void setInflater(LayoutInflater inflater, Activity act) {
		this.inflater = inflater;
		this.act = act;
	}
	
	public void openBizne(int bizid){
		DBAdapter database = new DBAdapter(this.act);
		Cursor cursor = null;
		
		database.open();
        cursor = database.getAllUsers();
        database.close();
        
		ProgressDialog pd;
		pd = ProgressDialog.show( this.act, "Recibiendo información ...", "Espere, por favor", true, false);
		
		QrScan qrscan = new QrScan();
		qrscan.setBizId(bizid);
		MainActivity.answer = qrscan;
		
		String data = "&bizid=" + bizid +
				"&userid=" + cursor.getString(1).toString();
		
		GetBizInfo getinfo = new GetBizInfo(this.act, pd);
		getinfo.execute(new String[] {data,"getBizneInfo"});
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		child = (ArrayList<UserPrize>) childtems.get(groupPosition);

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.prize_row, null);
		}
		
		final UserPrize entry = child.get(childPosition);
		TextView tvBizneName = (TextView) convertView.findViewById(R.id.prizebize);
        tvBizneName.setText(entry.getName());
        tvBizneName.setVisibility(View.GONE);

        TextView tvPrize = (TextView) convertView.findViewById(R.id.prizename);
        tvPrize.setText(entry.getPrize());

        TextView tvDate = (TextView) convertView.findViewById(R.id.prizedate);
        tvDate.setText(entry.getDate());

        Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnRemove.setOnClickListener(this);
        btnRemove.setTag(entry);
        if(entry.isRedeemed()){
        	btnRemove.setText("Canjeado"); 
            btnRemove.setEnabled(false);
        }else{
        	btnRemove.setText("Canjear"); 
            btnRemove.setEnabled(true);
        }
        
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPrize(entry,groupPosition);
			}
		});
        
        convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deletePrize(entry,groupPosition);
				return false;
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
        ArrayList<String> list = (ArrayList<String>) childtems.get(group);
        list.remove(child);
        if(list.size() == 0){
        	removeGroup(group);
        }
        notifyDataSetChanged();
    }
    
    public void removeFavorite(int bizid){
    	
    	DBAdapter database = new DBAdapter(this.act);
    	Cursor cursor;
    	database.open();
        cursor = database.getAllUsers();
        database.close();
		
		ProgressDialog pd = ProgressDialog.show( act, "Eliminando de favoritos...", "Espere, por favor", true, false);
		
		String data = "&bizid="+ bizid + "&userid=" + cursor.getString(1).toString();
		
		DeleteFavorite setfavorite = new DeleteFavorite(act, pd,bizid);
		setfavorite.execute(new String[] {data,"deleteFavorite"});
    }
    
    public void requestDelete(UserPrize prize, int pos){
    	
    	String data = "&prizeid=" + prize.getId();
    	
    	DeletePrize delete = new DeletePrize(this, prize, act, pos);
    	delete.execute(new String[] {data,"deletePrize"});
    }
	
	public void deletePrize(final UserPrize prize, final int pos){

		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_ok_cancel);
		dialog.setTitle("Eliminar premio");

		// set the custom dialog components - text, image and button
		TextView tv = (TextView) dialog.findViewById(R.id.dialogtextMessage);
		tv.setText("¿Deseas eliminar " + prize.getPrize() +" de tus premios?");

		Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButtonOk.setText("Aceptar");
		dialogButtonOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				requestDelete(prize,pos);
				dialog.dismiss();
			}
		});
		
		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancelar);
		dialogButtonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
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

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		UserPrize entry = (UserPrize) view.getTag();
        ProgressDialog pd = ProgressDialog.show( this.act, "Canjeando...", "Espere, por favor", true, false);
		
		String data = "&prizeid=" + entry.getId();
		
		RedeemPrize prize = new RedeemPrize(this.act, pd);
		prize.execute(new String[] {data,"redeemPrize"});
		entry.redeemed = 1;
//        listPhonebook.remove(entry);
        // listPhonebook.remove(view.getId());
        notifyDataSetChanged();
	}
	
	public void showPrize(final UserPrize prize, final int pos){
		// custom dialog
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_showprize);
 
		TextView tvBizneName = (TextView) dialog.findViewById(R.id.prizebize);
        tvBizneName.setText(prize.getName());

        TextView tvPrize = (TextView) dialog.findViewById(R.id.prizename);
        tvPrize.setText(prize.getPrize());

        TextView tvDate = (TextView) dialog.findViewById(R.id.prizedate);
        tvDate.setText(prize.getDate());
        
        TextView tvDescription = (TextView) dialog.findViewById(R.id.prizedescription);
        tvDescription.setText(prize.getDescription());
        
        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
        
        Button btnDelete = (Button) dialog.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deletePrize(prize,pos);
				dialog.dismiss();
			}
		});
 
		dialog.show();
	}
	
	@SuppressWarnings("unchecked")
	public void delete(UserPrize prize, int pos){
		child = (ArrayList<UserPrize>) childtems.get(pos);
		child.remove(prize);
		notifyDataSetChanged();
	}

}
