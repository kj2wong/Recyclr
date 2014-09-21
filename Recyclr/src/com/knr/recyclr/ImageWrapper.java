package com.knr.recyclr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageWrapper extends AsyncTask<String, Void, Bitmap>{
	private int target;
	private Context context;
	private Activity activity;
	
	public ImageWrapper(Context context, Activity activity, int viewId) {
		this.context = context;
		this.activity = activity;
		this.target = viewId;
	}

	@Override
	protected Bitmap doInBackground(String... desc) {
		// Setup and format URL
		String encoded = null;
		try {
			encoded = URLEncoder.encode(desc[0], "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// Can be safely ignored because UTF-8 is always supported
		}
		String url = String.format(
			"http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=%s", encoded);

		// Issue request for results
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		String imgUrl = null;
		try {
			response = httpClient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
  	        	ByteArrayOutputStream out = new ByteArrayOutputStream();
   	        	response.getEntity().writeTo(out);
   	        	String responseString = out.toString();
   	        	out.close();
   	        	JSONObject json = new JSONObject(responseString);
   	        	json = json.getJSONObject("responseData").getJSONArray("results").getJSONObject(0);
   	        	imgUrl = json.getString("tbUrl");
			} else {
				response.getEntity().getContent().close();
				return null;
			}
		} catch (ClientProtocolException e) {
   	    	System.out.println("ClientProtocolException: " + e.toString());
   	    	return null;
   	    } catch (IOException e) {
   	    	System.out.println("IOException: " + e.toString());
   	    	return null;
   	    } catch (JSONException e) {
   	    	System.out.println("JSONException: " + e.toString());
   	    	return null;
   	    }
		
		// Retrieve image from given location
		HttpClient imgClient = new DefaultHttpClient();
	    HttpResponse imgResponse = null;
   	    try { 	
   	    	imgResponse = imgClient.execute(new HttpGet(imgUrl));
   	   	    StatusLine imgStatusLine = imgResponse.getStatusLine();
   	   	    // OK HttpStatus, read image
   	        if(imgStatusLine.getStatusCode() == HttpStatus.SC_OK){
   	        	BufferedHttpEntity entity = new BufferedHttpEntity(imgResponse.getEntity());
   		        InputStream input = entity.getContent();
   		        Bitmap bitmap = BitmapFactory.decodeStream(input);
   		        input.close();
   		        return bitmap;
   	        } else {
      	        //Closes the connection.
      	        imgResponse.getEntity().getContent().close();
   	        }
   	    } catch (ClientProtocolException e) {
   	    	System.out.println("ClientProtocolException: " + e.toString());
   	    } catch (IOException e) {
   	    	System.out.println("IOException: " + e.toString());
   	    }
		return null;
	}
	
	@Override
	protected void onPostExecute(Bitmap img) {
		if(img == null) {
			// no image found
		} else {
			// Update image on UI
			ImageView view = (ImageView) activity.findViewById(target);
			view.setImageBitmap(img);
		}
	}
}
