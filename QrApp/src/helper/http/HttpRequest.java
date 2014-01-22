package helper.http;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.testapp.test.R;

public class HttpRequest {

	private static final int BUFFSIZE = 16384;
	Activity activity;
	Context c;
	public HttpRequest(Context c) {
		super();
		this.c = c;
	}		
	
	/**
     * Helper function used to communicate with the server by sending/receiving
     * POST commands.
     * @param data String representing the command and (possibly) arguments.
     * @return String response from the server.
	 * @throws IOException 
     */
    public String executeHttpRequest(String data, String method) throws IOException {
    	
        String result = "";
        try {
        		URL url = null;
        		
//        		url = new URL(activity.getString(R.string.http_server_standard)+"&task="+method.toString()+data);
        		String urlS = "http://qrivo.com/index.php?option=com_qr&controller=android&view=android&tmpl=component&format=raw&task="+method;
//        		String urlS = "http://ubitip.com/index.php?option=com_qr&controller=android&view=android&tmpl=component&format=raw&task="+method;
//        		String urlS = "http://ubitip.area51.socialdot.net/index.php?option=com_qr&controller=android&view=android&tmpl=component&format=raw&task="+method;
        		url = new URL(urlS);
                URLConnection connection = url.openConnection();
                //HttpURLConnection connection = (HttpURLConnection) url.openConnection();//---- n_n
                Log.d("Test", "HttpRequest-executeHttpRequest: Se inicia la configuración de la conección");
                
                /*
                 * We need to make sure we specify that we want to provide input and
                 * get output from this connection. We also want to disable caching,
                 * so that we get the most up-to-date result. And, we need to 
                 * specify the correct content type for our data.
                 */
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
               // connection.setInstanceFollowRedirects(false); //---n_n 
                //connection.setRequestMethod("POST"); //---n_n
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(20000);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                connection.setRequestProperty("charset", "utf-8");//---n_n
               // connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));//---n_n

                // Send the POST data
                DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
                dataOut.writeUTF(data);
                dataOut.flush();
                dataOut.close();
                //connection.disconnect();//---n_n
                Log.d("StandardApp", "HttpRequest-executeHttpRequest: tratando de enviar datos");
                
                // get the response from the server and store it in result
                DataInputStream dataIn = new DataInputStream(connection.getInputStream()); 
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(dataIn));
                StringBuilder builder = new StringBuilder(BUFFSIZE);

                String inputLine = null;
                try {
                    while ((inputLine = reader.readLine()) != null) {
                        builder.append(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        dataIn.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                result = builder.toString();
                
                
                dataIn.close();
        }catch (IOException e) {
                /*
                 * In case of an error, we're going to return a null String. This
                 * can be changed to a specific error message format if the client
                 * wants to do some error handling. For our simple app, we're just
                 * going to use the null to communicate a general error in
                 * retrieving the data.
                 */
        		Log.d("StandardApp", "HttpRequest-executeHttpRequest: "+ e.getMessage());
                e.printStackTrace();
                result = "{\"data\":[],\"msg\":\"El servidor no responde\",\"status\":0}";
        }
        Log.d("StandardApp", "HttpRequest-executeHttpRequest: Se logro la conección .... recibiendo datos");
        Log.d("StandardApp", "HttpRequest-executeHttpRequest: "+result.toString());
        return result;
    }
    
public String executeHttpRequest(String data, String method, boolean socialgo) throws IOException {
    	
        String result = "";
        try {
        		URL url = null;
        		
//        		url = new URL(activity.getString(R.string.http_server_standard)+"&task="+method.toString()+data);
        		String urlS = "http://ubitips.com/index.php?option=com_socialgo&view=androidn&tmpl=component&format=raw&task=androidn."+method;
//        		String urlS = "http://ubitip.area51.socialdot.net/index.php?option=com_qr&controller=android&view=android&tmpl=component&format=raw&task="+method;
        		url = new URL(urlS);
                URLConnection connection = url.openConnection();
                //HttpURLConnection connection = (HttpURLConnection) url.openConnection();//---- n_n
                Log.d("Test", "HttpRequest-executeHttpRequest: Se inicia la configuración de la conección");
                
                /*
                 * We need to make sure we specify that we want to provide input and
                 * get output from this connection. We also want to disable caching,
                 * so that we get the most up-to-date result. And, we need to 
                 * specify the correct content type for our data.
                 */
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
               // connection.setInstanceFollowRedirects(false); //---n_n 
                //connection.setRequestMethod("POST"); //---n_n
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(20000);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                connection.setRequestProperty("charset", "utf-8");//---n_n
               // connection.setRequestProperty("Content-Length", "" + Integer.toString(data.getBytes().length));//---n_n

                // Send the POST data
                DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
                dataOut.writeUTF(data);
                dataOut.flush();
                dataOut.close();
                //connection.disconnect();//---n_n
                Log.d("StandardApp", "HttpRequest-executeHttpRequest: tratando de enviar datos");
                
                // get the response from the server and store it in result
                DataInputStream dataIn = new DataInputStream(connection.getInputStream()); 
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(dataIn));
                StringBuilder builder = new StringBuilder(BUFFSIZE);

                String inputLine = null;
                try {
                    while ((inputLine = reader.readLine()) != null) {
                        builder.append(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        dataIn.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                result = builder.toString();
                
                
                dataIn.close();
        }catch (IOException e) {
                /*
                 * In case of an error, we're going to return a null String. This
                 * can be changed to a specific error message format if the client
                 * wants to do some error handling. For our simple app, we're just
                 * going to use the null to communicate a general error in
                 * retrieving the data.
                 */
        		Log.d("StandardApp", "HttpRequest-executeHttpRequest: "+ e.getMessage());
                e.printStackTrace();
                result = "{\"data\":[],\"msg\":\"El servidor no responde\",\"status\":0}";
        }
        Log.d("StandardApp", "HttpRequest-executeHttpRequest: Se logro la conección .... recibiendo datos");
        Log.d("StandardApp", "HttpRequest-executeHttpRequest: "+result.toString());
        return result;
    }
    
    
    public void sendFile(){
    	
    	Log.d("StandardApp", "HttpRequest-sendFile: enviando archivo");
    	HttpURLConnection connection = null;
    	DataOutputStream outputStream = null;
    	//DataInputStream inputStream = null;
    	
    	Context context = activity.getBaseContext();
    	File externalDir = Environment.getExternalStorageDirectory();
    
    	String pathToOurFile = externalDir.getAbsoluteFile()+File.separator+ context.getString(R.string.storage_foldername)+File.separator+context.getString(R.string.storage_filename);
    	String urlServer = activity.getString(R.string.http_server_standard)+"&task=saveFile";
    	String lineEnd = "\r\n";
    	String twoHyphens = "--";
    	String boundary =  "*****";

    	int bytesRead, bytesAvailable, bufferSize;
    	byte[] buffer;
    	int maxBufferSize = 1*1024*1024;

    	try
    	{
	    	FileInputStream fileInputStream = new FileInputStream(new File(pathToOurFile) );
	
	    	URL url = new URL(urlServer);
	    	connection = (HttpURLConnection) url.openConnection();
	
	    	// Allow Inputs & Outputs
	    	connection.setDoInput(true);
	    	connection.setDoOutput(true);
	    	connection.setUseCaches(false);
	    	connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
	
	    	// Enable POST method
	    	connection.setRequestMethod("POST");
	
	    	connection.setRequestProperty("Connection", "Keep-Alive");
	    	connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
	
	    	outputStream = new DataOutputStream( connection.getOutputStream() );
	    	outputStream.writeBytes(twoHyphens + boundary + lineEnd);
	    	outputStream.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
	    	outputStream.writeBytes(lineEnd);
	
	    	bytesAvailable = fileInputStream.available();
	    	bufferSize = Math.min(bytesAvailable, maxBufferSize);
	    	buffer = new byte[bufferSize];
	
	    	// Read file
	    	bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	
	    	while (bytesRead > 0)
	    	{
		    	outputStream.write(buffer, 0, bufferSize);
		    	bytesAvailable = fileInputStream.available();
		    	bufferSize = Math.min(bytesAvailable, maxBufferSize);
		    	bytesRead = fileInputStream.read(buffer, 0, bufferSize);
	    	}
	
	    	outputStream.writeBytes(lineEnd);
	    	outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	
	    	// Responses from the server (code and message)
	    	/*int serverResponseCode = connection.getResponseCode();
	    	String serverResponseMessage = connection.getResponseMessage();*/
	    	Log.d("StandardApp", "HttpRequest-sendFile: "+ connection.getResponseMessage());
	    	Log.d("StandardApp", "HttpRequest-sendFile info: "+ connection.getResponseCode());
	    	fileInputStream.close();
	    	outputStream.flush();
	    	outputStream.close();
    	}
    	catch (IOException e)
    	{
    		//Exception handling
    		Log.d("Socialgo", "HttpRequest-sendFile: "+ e.getMessage());
            e.printStackTrace();
            //Toast.makeText(this.activity.getApplicationContext(), "Fallo el envio del paquete de datos" , Toast.LENGTH_SHORT).show();
            
    	}
    }
}
