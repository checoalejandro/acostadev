package helper.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.testapp.test.BizneInfoActivity;
import com.testapp.test.BizneMapActivity;
import com.testapp.test.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class GMapV2Direction extends AsyncTask<String, Void, Document>{
	public final static String MODE_DRIVING = "driving";
	public final static String MODE_WALKING = "walking";
	
	public static Document doc;
	BizneMapActivity act;
	ProgressDialog pd;
	public GMapV2Direction(BizneMapActivity act, ProgressDialog pd) {
		this.pd = pd;
		this.act = act;
	}
	
	public Document getDocument(LatLng start, LatLng end, String mode) throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		StrictMode.ThreadPolicy policy = new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String hosturl="https://maps.googleapis.com/maps/api/directions/xml?";
        String newstring = "origin=" + start.latitude + "," + start.longitude 
        + "&destination=" + end.latitude + ","+ end.longitude + "&sensor=true";
        //37.255666,-121.967683
        //37.258507,-121.967533
        //String newstring = "origin=37.2345487,-121.5840723&destination=37.236064,-121.961595&sensor=true";
        //String newurl = URLEncoder.encode(newstring, "UTF-8");
        String finalurl = hosturl + newstring;
        System.out.println("web service call: "+finalurl);
        HttpPost httpPost = new HttpPost(finalurl);
        HttpResponse response = httpClient.execute(httpPost, localContext);
        InputStream in = response.getEntity().getContent();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(in);
        System.out.println(" Returned valid document");
        return doc;
	}
	
	public String getDurationText(Document doc) {
	    try {
	
	        NodeList nl1 = doc.getElementsByTagName("duration");
	        Node node1 = nl1.item(0);
	        NodeList nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "text"));
	        Log.i("DurationText", node2.getTextContent());
	        return node2.getTextContent();
	    } catch (Exception e) {
	        return "0";
	    }
	}
	
	public int getDurationValue(Document doc) {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("duration");
	        Node node1 = nl1.item(0);
	        NodeList nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
	        Log.i("DurationValue", node2.getTextContent());
	        return Integer.parseInt(node2.getTextContent());
	    } catch (Exception e) {
	        return -1;
	    }
	}
	
	public String getDistanceText(Document doc) {
	    /*
	     * while (en.hasMoreElements()) { type type = (type) en.nextElement();
	     * 
	     * }
	     */
	
	    try {
	        NodeList nl1;
	        nl1 = doc.getElementsByTagName("distance");
	
	        Node node1 = nl1.item(nl1.getLength() - 1);
	        NodeList nl2 = null;
	        nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
	        Log.d("DistanceText", node2.getTextContent());
	        return node2.getTextContent();
	    } catch (Exception e) {
	        return "-1";
	    }
	
	    /*
	     * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
	     * doc.getElementsByTagName("distance");
	     * 
	     * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
	     * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
	     * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
	     * node2.getTextContent()); return node2.getTextContent(); } else return
	     * "-1";} else return "-1";
	     */
	}
	
	public int getDistanceValue(Document doc) {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("distance");
	        Node node1 = null;
	        node1 = nl1.item(nl1.getLength() - 1);
	        NodeList nl2 = node1.getChildNodes();
	        Node node2 = nl2.item(getNodeIndex(nl2, "value"));
	        Log.i("DistanceValue", node2.getTextContent());
	        return Integer.parseInt(node2.getTextContent());
	    } catch (Exception e) {
	        return -1;
	    }
	    /*
	     * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
	     * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
	     * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
	     * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
	     * node2.getTextContent()); return
	     * Integer.parseInt(node2.getTextContent()); } else return 0;
	     */
	}
	
	public String getStartAddress(Document doc) {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("start_address");
	        Node node1 = nl1.item(0);
	        Log.i("StartAddress", node1.getTextContent());
	        return node1.getTextContent();
	    } catch (Exception e) {
	        return "-1";
	    }
	
	}
	
	public String getEndAddress(Document doc) {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("end_address");
	        Node node1 = nl1.item(0);
	        Log.i("StartAddress", node1.getTextContent());
	        return node1.getTextContent();
	    } catch (Exception e) {
	        return "-1";        
	}
	}
	public String getCopyRights(Document doc) {
	    try {
	        NodeList nl1 = doc.getElementsByTagName("copyrights");
	        Node node1 = nl1.item(0);
	        Log.i("CopyRights", node1.getTextContent());
	        return node1.getTextContent();
	    } catch (Exception e) {
	    return "-1";
	    }
	
	}
	
	public ArrayList<LatLng> getDirection(Document doc) {
	    NodeList nl1, nl2, nl3;
	    ArrayList<LatLng> listGeopoints = new ArrayList<LatLng>();
	    nl1 = doc.getElementsByTagName("step");
	    if (nl1.getLength() > 0) {
	        for (int i = 0; i < nl1.getLength(); i++) {
	            Node node1 = nl1.item(i);
	            nl2 = node1.getChildNodes();
	
	            Node locationNode = nl2
	                    .item(getNodeIndex(nl2, "start_location"));
	            nl3 = locationNode.getChildNodes();
	            Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            double lat = Double.parseDouble(latNode.getTextContent());
	            Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            double lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));
	
	            locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "points"));
	            ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
	            for (int j = 0; j < arr.size(); j++) {
	                listGeopoints.add(new LatLng(arr.get(j).latitude, arr
	                        .get(j).longitude));
	            }
	
	            locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
	            nl3 = locationNode.getChildNodes();
	            latNode = nl3.item(getNodeIndex(nl3, "lat"));
	            lat = Double.parseDouble(latNode.getTextContent());
	            lngNode = nl3.item(getNodeIndex(nl3, "lng"));
	            lng = Double.parseDouble(lngNode.getTextContent());
	            listGeopoints.add(new LatLng(lat, lng));
	        }
	    }
	
	    return listGeopoints;
	}
	
	private int getNodeIndex(NodeList nl, String nodename) {
	    for (int i = 0; i < nl.getLength(); i++) {
	        if (nl.item(i).getNodeName().equals(nodename))
	            return i;
	    }
	    return -1;
	}
	
	private ArrayList<LatLng> decodePoly(String encoded) {
	    ArrayList<LatLng> poly = new ArrayList<LatLng>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;
	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;
	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;
	
	        LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
	        poly.add(position);
	    }
	    return poly;
	}

	@Override
	protected Document doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		
		LatLng fromPosition = new LatLng(Double.parseDouble(arg0[0]), Double.parseDouble(arg0[1]));
		LatLng toPosition = new LatLng(Double.parseDouble(arg0[2]), Double.parseDouble(arg0[3]));
		
		Document doc = null;
		try {
			doc = getDocument(fromPosition, toPosition,
			        GMapV2Direction.MODE_DRIVING);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GMapV2Direction.doc = doc;
		
		return doc;
	}
	
	@Override
	protected void onPostExecute(Document result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(doc != null){
			pd.dismiss();
			ArrayList<LatLng> directionPoint = getDirection(doc);
	        PolylineOptions rectLine = new PolylineOptions().width(3).color(
	                Color.RED);

	        for (int i = 0; i < directionPoint.size(); i++) {
	            rectLine.add(directionPoint.get(i));
	        }
	        
	        act.googleMap.addPolyline(rectLine);
//			Intent intent = new Intent(act.getApplicationContext(), BizneMapActivity.class);
//			act.startActivity(intent);
		}else{
			Toast.makeText(act, "Lo sentimos, no se ha podido crear la ruta.", Toast.LENGTH_SHORT).show();
		}
	}
}