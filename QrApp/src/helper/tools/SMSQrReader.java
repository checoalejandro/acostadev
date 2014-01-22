package helper.tools;

import android.app.Activity;
import android.telephony.SmsManager;

public class SMSQrReader implements GenericQrReader{

	@Override
	public void actionQr(Activity act, String result) {
		// TODO Auto-generated method stub

	    String phoneNumber = result.substring(5, 16);
	    String message = result.substring(17);

	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
	}

}
