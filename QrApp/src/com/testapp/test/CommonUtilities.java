package com.testapp.test;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	
	// give your server registration url here
    static final String SERVER_URL = "http://qrivo.com/index.php?option=com_qr&controller=android&view=android&tmpl=component&format=raw&task=registerDevice"; 

    // Google project id
    static final String SENDER_ID = "961195781909"; 

    /**
     * Tag used on log messages.
     */
    static final String TAG = "AndroidHive GCM";

    static final String DISPLAY_MESSAGE_ACTION =
            "com.gcm.messaging.DISPLAY_MESSAGE";

    static final String EXTRA_MESSAGE = "message";
    static final String EXTRA_BIZID = "bizid";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
