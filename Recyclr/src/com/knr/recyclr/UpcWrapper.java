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

import java.sql.Connection;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

class RetrieveUpcTask extends AsyncTask<String, Void, UpcItem> {
	private Context context;
	private Activity activity;
	private int viewId;
	
	public RetrieveUpcTask(Context context, Activity activity, int view) {
		this.context = context;
		this.activity = activity;
		this.viewId = view;
	}

    protected UpcItem doInBackground(String... upcCodes) {
    	// format URL for API call
   		String url = String.format("http://api.upcdatabase.org/json/%s/%s", 
   				JSONHelper.loadFromAsset(context, "config.json", "upc_api_key"), upcCodes[0]);
    	HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = null;
   	    try { 	
   	    	response = httpclient.execute(new HttpGet(url));
   	   	    StatusLine statusLine = response.getStatusLine();
   	   	    // OK HttpStatus, read item
   	        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
   	        	ByteArrayOutputStream out = new ByteArrayOutputStream();
   	        	response.getEntity().writeTo(out);
   	        	String responseString = out.toString();
   	        	out.close();
   	        	return new UpcItem(responseString);
   	        } else {
      	        //Closes the connection.
      	        response.getEntity().getContent().close();
   	        }
   	    } catch (ClientProtocolException e) {
   	    	System.out.println("ClientProtocolException: " + e.toString());
   	    } catch (IOException e) {
   	    	System.out.println("IOException: " + e.toString());
   	    }
    	return null;
    }
    
    protected void onPostExecute(UpcItem item) {
    	// search bar-code against database
    	// create alert message describing recyclable/garbage/compost
    	if (item == null) {
    		// do something
    	} else {
    		new ImageWrapper(context, activity, viewId).execute(item.itemName + " " + item.description);
    		TextView description = (TextView)activity.findViewById(R.id.itemDescriptionLabel);
    		description.setText(item.description);
    		
    		TextView itemName = (TextView)activity.findViewById(R.id.itemNameLabel);
    		itemName.setText(item.itemName);
    		
    	}
    }
}