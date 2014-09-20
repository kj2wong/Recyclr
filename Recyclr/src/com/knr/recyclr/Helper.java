package com.knr.recyclr;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

public class Helper {
	public static String loadJSONFromAsset(Context context, String assetFileName) {
	    String json = null;
	    try {

	        InputStream is = context.getAssets().open(assetFileName);

	        int size = is.available();

	        byte[] buffer = new byte[size];

	        is.read(buffer);

	        is.close();

	        json = new String(buffer, "UTF-8");
	        System.out.println(json);

	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return null;
	    }
	    return json;

	}
}
