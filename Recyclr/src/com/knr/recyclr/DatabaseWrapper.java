package com.knr.recyclr;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseWrapper {
	private Context context;
	private Activity activity;
	private Connection conn;
	
	public DatabaseWrapper (Context context, Activity activity, Connection conn) {
		this.context = context;
		this.activity = activity;
		this.conn = conn;
		
    	// Sample code for executing MySQL statements
    	// new MySQLTask(getApplicationContext(), this, conn).execute("select * from action");
	}
	
	public void getAction(UpcItem upc){
		String query = "SELECT recycle, trash FROM action where UPC = \"" + upc.number + "\"";
		new SQLSelect(context, activity, conn).execute(query);
	}
	
	public void setUpc(UpcItem upc){
		
	}
}

//Class for executing select SQL statements
class SQLSelect extends MySQLTask {
	public SQLSelect(Context context, Activity activity, Connection conn) {
		super(context, activity, conn);	
	}
	
    @Override
    protected int[] doInBackground(String... sql) {
    	try {
    		Statement stmt = conn.createStatement();
    		ResultSet results = stmt.executeQuery(sql[0]);
    		results.next();
    		int[] actions = new int[2];
    		if (results == null) {
    			actions[0] = -1;
    			actions[1] = -1;
    		}
    		else {
    			actions[0] = results.getInt("recycle");
    			actions[1] = results.getInt("trash");
    		}
    		stmt.close();
    		return actions;
    	} catch (SQLException ex) {
    	    // handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
        return null;
    }
	
	@Override
	protected void onPostExecute(int[] results) {
		try {
			System.out.println("@@@@@@@Recycle: " + results[0] + " Trash: " + results[1]);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}

// Class for executing insert SQL statements 
class SQLUpdate extends MySQLTask {
	public SQLUpdate(Context context, Activity activity, Connection conn) {
		super(context, activity, conn);	
	}
	
    @Override
    protected int[] doInBackground(String... sql) {
    	try {
    		Statement stmt = conn.createStatement();
    		stmt.executeUpdate(sql[0]);
    		stmt.close();
    	} catch (SQLException ex) {
    	    // handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	}
        return null;
    }
}

// Class for executing MySQL statements asynchronously
class MySQLTask extends AsyncTask<String, Void, int[]> {
	protected Context context;
	protected Activity activity;
	protected Connection conn;
	
	public MySQLTask(Context context, Activity activity, Connection conn) {
		this.context = context;
		this.activity = activity;
		this.conn = conn;
	}
	
    @Override
    protected int[] doInBackground(String... sql) {
        return null;
    }
}