package com.knr.recyclr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class JSONHelper {
	// Reads a JSON file and returns a JSON object
	// Scanner iterates over tokens in the stream, 
	// separates tokens using "beginning of the input boundary" (\A) 
	// thus giving us only one token for the entire contents of the stream.
	@SuppressWarnings("resource")
	private static JSONObject ReadJSON(Context context, String assetFileName) {
		try {
			InputStream stream = context.getAssets().open(assetFileName);
        	Scanner scan = new Scanner(stream, "UTF-8").useDelimiter("\\A");
        	String json = scan.hasNext() ? scan.next() : "";
        	scan.close();
        	stream.close();
			return new JSONObject(json);
    	} catch (IOException e) {
        	System.out.println("IOException: " + e.toString());
    	} catch (JSONException e) {
    		System.out.println("JSONException: " + e.toString());
    	}
        return null;
	}
	
	// Reads an entry of a JSON asset
	public static String loadFromAsset(Context context, String assetFileName, String name) {
	    JSONObject json = ReadJSON(context, assetFileName);
	    try {
			return json.getString(name);
	    } catch (JSONException e) {
    		System.out.println("JSONException: " + e.toString());
    	}
	    return null;
	}
	
	// Reads an array of JSON entries from the asset
	public static String[] loadMultipleFromAsset(Context context, String assetFileName, String[] names) {
	    JSONObject json = ReadJSON(context, assetFileName);
	    String[] results = new String[names.length];
	    try {
	    	for(int i = 0; i < names.length; ++i) {
	    		results[i] = json.getString(names[i]);
	    	}
			return results;
	    } catch (JSONException e) {
    		System.out.println("JSONException: " + e.toString());
    	}
	    return null;
	}
}
