package helper.json;

import helper.threads.SendAlert;
import android.app.Activity;
import android.app.ProgressDialog;

public class Attention {

	int atid;
	String name;
	int bizid;
	int subbizid;
	
	public int getAttentionID(){
		return atid;
	}
	
	public String getName(){
		return name;
	}
	
	public int getBizId(){
		return bizid;
	}
	
	public void sendAttention(Activity act){
		ProgressDialog pd = ProgressDialog.show( act, "Recibiendo información ...", "Espere, por favor", true, false);
		
		String data = "&subbizid=" + subbizid + "&atid=" + atid;
		
		SendAlert getdata = new SendAlert(act, pd);
		getdata.execute(new String[] {data,"alertChild"});
	}
}