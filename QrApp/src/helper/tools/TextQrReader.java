package helper.tools;

import com.testapp.test.R;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TextQrReader implements GenericQrReader {

	@Override
	public void actionQr(Activity act, String result) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(act);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_qr_text);
		dialog.setTitle("QR de Texto");

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.dialog_qr_textview);
		text.setText(result);

		Button dialogButton = (Button) dialog.findViewById(R.id.dialog_qr_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	

}
