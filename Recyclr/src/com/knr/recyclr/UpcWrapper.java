package com.knr.recyclr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

class RetrieveUpcTask extends AsyncTask<String, Void, UpcItem> {
	private Context context;
	private Activity activity;
	
	public RetrieveUpcTask(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
	}

    protected UpcItem doInBackground(String... upcCodes) {
    	// format URL for API call
   		JSONObject secret;
  		String apiKey;
    	try {
    		secret = new JSONObject(Helper.loadJSONFromAsset(context, "config.json"));
    		apiKey = secret.getString("upc_api_key");
		}
   		catch (JSONException e) {
   			return null;
   		}
   		String url = String.format("http://api.upcdatabase.org/json/%s/%s", 
    		apiKey, upcCodes[0]);
    	HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = null;
   		try {
   			response = httpclient.execute(new HttpGet(url));
   		} catch (ClientProtocolException e) {
   			// TODO Auto-generated catch block
    		e.printStackTrace();
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
   			e.printStackTrace();
   		}
   	    StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	    ByteArrayOutputStream out = new ByteArrayOutputStream();
    	    try {
				response.getEntity().writeTo(out);
   		        out.close();
   			} catch (IOException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
    		}
    	    String responseString = out.toString();
    	    return new UpcItem(responseString);
	    } else {
   	        //Closes the connection.
   	        try {
   				response.getEntity().getContent().close();
   			} catch (IllegalStateException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
        }
    	return null;
    }
    
    protected void onPostExecute(UpcItem item) {
    	TextView descText = (TextView) activity.findViewById(R.id.scan_description);
    	descText.setText("DESCRIPTION: " + item.description);
    }
}