package helper.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class WebPageQr implements GenericQrReader {


	@Override
	public void actionQr(Activity act, String result) {
		// TODO Auto-generated method stub
		if (!result.toLowerCase().startsWith("http://") && !result.toLowerCase().startsWith("https://")){
			result = "http://" + result;
		}
		if(result.startsWith("HTTP")){
			result = "http" + result.substring(4);
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(result));
		act.startActivity(intent);
	}

}
