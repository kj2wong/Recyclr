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

import android.content.Context;

public class UpcWrapper {	
	public static UpcItem getItem(Context context, String upcCode) {
		// format URL for API call
		String url = String.format("http://http://api.upcdatabase.org/json/%s/%s", 
				context.getString(R.string.api_key, upcCode));
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
	    } else{
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
}
