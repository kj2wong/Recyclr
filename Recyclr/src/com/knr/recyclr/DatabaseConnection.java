package com.knr.recyclr;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import android.os.AsyncTask;

public class DatabaseConnection extends AsyncTask<Void, Void, Connection> {
	private MainActivity activity;
	
	public DatabaseConnection(MainActivity activity) {
		this.activity = activity;
	}
	
	@Override
	protected Connection doInBackground(Void... args) {
		// Connect to RDS Database
		try {
			String[] names = {"db_host", "db_user", "db_pass"};
			String[] params = JSONHelper.loadMultipleFromAsset(activity.getApplicationContext(), 
					"config.json", names);
		    return DriverManager.getConnection(
		    		String.format("jdbc:mysql://%s?user=%s&password=%s", 
		    				params[0], params[1], params[2]));
		} catch (SQLException ex) {
			// TODO: handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Connection conn) {
		MainActivity.setConn(conn);
	}
}
