package helper.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class TelephoneQrReader implements GenericQrReader{

	@Override
	public void actionQr(Activity act, String result) {
		// TODO Auto-generated method stub
		String number = result.substring(4);
		try {
	        Intent callIntent = new Intent(Intent.ACTION_CALL);
	        callIntent.setData(Uri.parse("tel:" + number));
	        act.startActivity(callIntent);
	    } catch (ActivityNotFoundException e) {
	        Log.e("QrApp", "Call failed", e);
	    }
	}

}
