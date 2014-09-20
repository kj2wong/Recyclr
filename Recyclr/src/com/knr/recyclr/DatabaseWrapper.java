package com.knr.recyclr;

import org.json.JSONException;
import org.json.JSONObject;

import com.temboo.Library.MySQL.RunCommand;
import com.temboo.Library.MySQL.RunCommand.RunCommandInputSet;
import com.temboo.Library.MySQL.RunCommand.RunCommandResultSet;
import com.temboo.core.TembooSession;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class DatabaseWrapper {
	
}

class MySQLTask extends AsyncTask<String, Void, RunCommandResultSet> {
	private Context context;
	private Activity activity;
	
	public MySQLTask(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
	}
	
    @Override
    protected RunCommandResultSet doInBackground(String... sql) {
        try {
        	JSONObject secret;
      		String apiKey;
        	try {
        		secret = new JSONObject(Helper.loadJSONFromAsset(context, "config.json"));
        		apiKey = secret.getString("temboo_key");
    		}
       		catch (JSONException e) {
       			System.out.println(e.toString());
       			return null;
       		}
            TembooSession session = new TembooSession("recyclr", "Recyclr", apiKey);
            RunCommand runCommandChoreo = new RunCommand(session);
            // Get an InputSet object for the choreo
            RunCommandInputSet runCommandInputs = runCommandChoreo.newInputSet();
            runCommandInputs.setCredential("MysqlCredentials");
            // Set SQL statement
            runCommandInputs.set_SQL(sql[0]);
            // Execute Choreo
            return runCommandChoreo.execute(runCommandInputs);
        } catch(Exception e) {
            // if an exception occurred, log it
            System.out.printf(this.getClass().toString(), e.getMessage());
        }
        return null;
    }

    protected void onPostExecute(RunCommandResultSet result) {
    	if(!result.equals(null)) {
	        if(result.get_ResultData().isEmpty()) {
	        	// completed insert statement
	        } else {
	        	// completed select statement
	        	TextView descText = (TextView) activity.findViewById(R.id.scan_description);
	        	descText.setText("DESCRIPTION: " + result.get_ResultData());
	        }
    	}
    }
}