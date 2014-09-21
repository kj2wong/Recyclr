package com.knr.recyclr;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;

public class DatabaseWrapper {
	private Context context;
	private Activity activity;
	private Connection conn;
	private UpcItem item;
	
	public enum DisposeType {
		Recycle, Trash
	};
	
	public DatabaseWrapper (Context context, Activity activity, Connection conn) {
		this.context = context;
		this.activity = activity;
		this.conn = conn;
		
    	// Sample code for executing MySQL statements
    	// new MySQLTask(getApplicationContext(), this, conn).execute("select * from action");
	}
	
	public void getAction(String upcNumber){
		String query = "SELECT recycle, trash FROM action where UPC = \"" + upcNumber + "\"";
		new SQLSelect(context, activity, conn).execute(query);
	}
	
	public void setUpc(String upcNumber, Boolean trash){
		new SQLUpdate(context, activity, conn, upcNumber, trash).execute();
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
    		try {
    			actions[0] = results.getInt("recycle");
    		}
    		catch (Exception e) {
    			actions[0] = -1;
    		}
    		try {
    			actions[1] = results.getInt("trash");
    		}
    		catch (Exception e){
    			actions[1] = -1;
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
			String disposalType = "Sorry, we don't know yet!";
			if(results[0] != -1 || results[1] != -1 ) {
				if(results[0] > results[1]) {
					disposalType = "Recycle";
				}
				if(results[1] > results[0]) {
					disposalType = "Trash";
				}
			}
			String message = "Disposal Type: " + disposalType;
			// set the text view
		    TextView textView = (TextView)activity.findViewById(R.id.itemDisposalLabel);
		    textView.setText(message);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
}

// Class for executing insert SQL statements 
class SQLUpdate extends MySQLTask {
	private String itemNum;
	private Boolean trash;
	
	public SQLUpdate(Context context, Activity activity, Connection conn, String itemNum, Boolean trash) {
		super(context, activity, conn);	
		this.itemNum = itemNum;
		this.trash = trash;
	}
	
    @Override
    protected int[] doInBackground(String... sql) {
    	try {
    		Statement stmt = conn.createStatement();
    		String query = "SELECT recycle, trash FROM action where UPC = \"" + itemNum + "\"";
    		ResultSet results = stmt.executeQuery(query);
    		results.next();
    		int[] actions = new int[2];
    		String update = "";
    		try {
    			actions[0] = results.getInt("recycle");
    			actions[1] = results.getInt("trash");
    			// Update values 
    			if(trash) {
    				update = "update action set trash = trash+1 where upc = \"" + itemNum + "\"";
    			} else {
    				update = "update action set recycle = recycle+1 where upc = \"" + itemNum + "\"";
    			}
    		}
    		catch (Exception e) {
    			// Insert new
    			update = String.format("insert into action (upc, recycle, trash) values (\"%s\", \"%s\", \"%s\")", 
    					itemNum, !trash ? 1:0, trash ? 1:0);
    		}
    		stmt.execute(update);
    		stmt.close();
    		return actions;
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