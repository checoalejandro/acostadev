package com.testapp.test;

import helper.json.UserPrize;
import helper.threads.RedeemPrize;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class PrizesAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private Activity act;
    private List<UserPrize> listPhonebook;
    String namebiz = "";

    public PrizesAdapter(Activity act, List<UserPrize> listPhonebook) {
    	this.act = act;
        this.context = act.getBaseContext();
        this.listPhonebook = listPhonebook;
    }

    public int getCount() {
        return listPhonebook.size();
    }

    public Object getItem(int position) {
        return listPhonebook.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        UserPrize entry = listPhonebook.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.prize_row, null);
        }
       
        TextView tvBizneName = (TextView) convertView.findViewById(R.id.prizebize);
        tvBizneName.setText(entry.getName());
        
        if(entry.isHead()){
        	tvBizneName.setVisibility(View.VISIBLE);
        }else{
        	tvBizneName.setVisibility(View.GONE);
        }

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
        
        return convertView;
    }

    @Override
    public void onClick(View view) {
    	
        UserPrize entry = (UserPrize) view.getTag();
        ProgressDialog pd = ProgressDialog.show( this.act, "Canjeando...", "Espere, por favor", true, false);
		
		String data = "&prizeid=" + entry.getId();
		
		RedeemPrize prize = new RedeemPrize(this.act, pd);
		prize.execute(new String[] {data,"redeemPrize"});
		for(int i = 0; i < listPhonebook.size(); i++){
			if(listPhonebook.get(i).getDate().equals(entry.getDate())){
				listPhonebook.get(i).redeemed = 1;
				break;
			}
		}
//        listPhonebook.remove(entry);
        // listPhonebook.remove(view.getId());
        notifyDataSetChanged();

    }

}