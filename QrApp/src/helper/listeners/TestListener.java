package helper.listeners;

import com.testapp.test.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class TestListener implements OnClickListener{

	Activity activity;
	
	public TestListener(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.id_btn_test:

			
			break;

		default:
			break;
		}
	}

}
