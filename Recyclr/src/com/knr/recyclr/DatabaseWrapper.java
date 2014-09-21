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
	
	public DatabaseWrapper (Context context, Activity activity, Connection conn){
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
}

//Class for executing select SQL statements
class SQLSelect extends MySQLTask {
	public SQLSelect(Context context, Activity activity, Connection conn) {
		super(context, activity, conn);	
	}
	
    @Override
    protected ResultSet doInBackground(String... sql) {
    	try {
    		Statement stmt = conn.createStatement();
    		ResultSet results = stmt.executeQuery(sql[0]);
    		stmt.close();
    		return results;
    	} catch (SQLException ex) {
    	    // handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	}
        return null;
    }
	
	@Override
	protected void onPostExecute(ResultSet result) {
		try {
			while(result.next()){
				int recycle_count = result.getInt("recycle");
				int trash_count = result.getInt("trash");
				System.out.println("@@@@@@@Recycle: " + recycle_count + " Trash: " + trash_count);
			}
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
    protected ResultSet doInBackground(String... sql) {
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
class MySQLTask extends AsyncTask<String, Void, ResultSet> {
	protected Context context;
	protected Activity activity;
	protected Connection conn;
	
	public MySQLTask(Context context, Activity activity, Connection conn) {
		this.context = context;
		this.activity = activity;
		this.conn = conn;
	}
	
    @Override
    protected ResultSet doInBackground(String... sql) {
        return null;
    }
}