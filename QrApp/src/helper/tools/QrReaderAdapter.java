package helper.tools;

import android.app.Activity;

public class QrReaderAdapter {

	public boolean getQr(Activity act, String result){
		
		if(result.contains("SMS")){
			SMSQrReader sms = new SMSQrReader();
	    	sms.actionQr(act, result);
	    	return true;
		}else{
			if(result.contains("VCARD") || result.contains("MCARD")){
				ContactQrReader contact = new ContactQrReader();
		    	contact.actionQr(act, result);
		    	return true;
			}else{
				if(result.toLowerCase().contains("http://") || result.toLowerCase().contains("www.") || result.toLowerCase().contains("https://")){
//					result = result.toLowerCase();
					WebPageQr web = new WebPageQr();
					web.actionQr(act, result);
					return true;
				}else{
					if(result.contains("TEL")){
						TelephoneQrReader phone = new TelephoneQrReader();
				    	phone.actionQr(act, result);
				    	return true;
					}else{
						return false;
					}
				}
			}
		}
	}
}
