package com.testapp.test;

import helper.json.Promo;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

public class PromoListener implements OnClickListener{
	Promo promo;
	CheckBox cb;
	Activity act;
	
	public PromoListener(CheckBox cb, Promo promo, Activity act){
		this.cb = cb;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(cb.isChecked()){
			PromosActivity.userpoints = PromosActivity.userpoints - promo.getPoints();
			if(PromosActivity.userpoints < 0){
				Toast.makeText(this.act, "No tienes puntos para completar.", Toast.LENGTH_LONG).show();
				PromosActivity.userpoints = PromosActivity.userpoints - promo.getPoints();
				cb.setChecked(false);
			}
		}else{
			
		}
	}

}
