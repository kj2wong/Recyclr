package com.knr.recyclr;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseWrapper {
	
}

class MySQLTask extends AsyncTask<String, Void, String> {
	private Context context;
	private Activity activity;
	private Connection conn;
	
	public MySQLTask(Context context, Activity activity, Connection conn) {
		this.context = context;
		this.activity = activity;
		this.conn = conn;
	}
	
    @Override
    protected String doInBackground(String... sql) {
    	try {
    		Statement stmt = conn.createStatement();
    		stmt.execute(sql[0]);
    		stmt.close();
    	} catch (SQLException ex) {
    	    // handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	}
        return null;
    }

    protected void onPostExecute(String result) {
    	if(result != null) {
	        //TextView descText = (TextView) activity.findViewById(R.id.scan_description);
	        //descText.setText("DESCRIPTION: " + result);
    	}
    }
}