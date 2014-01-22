package helper.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class ContactQrReader implements GenericQrReader {

	@Override
	public void actionQr(Activity act, String result) {
		// TODO Auto-generated method stub
		try{
			File myFile = new File("/sdcard/contact.vcf");
	        myFile.createNewFile();
	        FileOutputStream fOut = new FileOutputStream(myFile);
	        OutputStreamWriter myOutWriter = 
	                                new OutputStreamWriter(fOut);
	        myOutWriter.append(result);
	        myOutWriter.close();
	        fOut.close();
	    } catch (Exception e) {
	        
	    }
			Intent i = new Intent();
			i.setAction(android.content.Intent.ACTION_VIEW);
			i.setDataAndType(Uri.parse("file:///mnt/sdcard/contact.vcf"), "text/x-vcard");
			act.startActivity(i);
		}

}
