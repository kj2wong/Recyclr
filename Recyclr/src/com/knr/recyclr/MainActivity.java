package com.knr.recyclr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.DriverManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity implements OnClickListener{
	
	private ImageView scanBtn;
	private ImageView addBtn;
	private ImageView infoBtn;
	private int viewId;
	private Connection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        scanBtn = (ImageView)findViewById(R.id.search_button);
        scanBtn.setOnClickListener(this);
        
        addBtn = (ImageView)findViewById(R.id.add_item);
        addBtn.setOnClickListener(this);

        scanBtn.setOnClickListener(this);

        infoBtn = (ImageView)findViewById(R.id.indexes_button);
        infoBtn.setOnClickListener(this);
        
        // Startup JDBC driver
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// Connect to RDS Database
    	try {
    		JSONObject secret;
      		String user = null, pass = null, host = null;
        	try {
        		secret = new JSONObject(Helper.loadJSONFromAsset(getApplicationContext(), "config.json"));
        		user = secret.getString("db_user");
        		pass = secret.getString("db_pass");
        		host = secret.getString("db_host");
    		}
       		catch (JSONException e) {
       			// TODO: things
       		}
    	    this.conn = DriverManager.getConnection(
    	    		String.format("jdbc:mysql://%s?user=%s&password=%s", host, user, pass));
    	} catch (SQLException ex) {
    		// TODO: handle any errors
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	}
    	
    	// Sample code for executing MySQL statements
    	// new MySQLTask(getApplicationContext(), this, conn).execute("select * from action");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onClick(View v){
    	//respond to clicks
    	this.viewId = v.getId();
    	if(v.getId()==R.id.search_button || v.getId()==R.id.add_item){
    		//scan
    		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
    		scanIntegrator.initiateScan();
		}
    	if(v.getId()==R.id.indexes_button) {
    		// open another list activities that lists common materials
    	}
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	//retrieve scan result
    	IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanningResult != null && scanningResult.getContents() != null) {
    		//we have a result
    		String scanContent = scanningResult.getContents();
    		String parsed = scanContent.substring(1, scanContent.length()-1);
    		
    		if(this.viewId==R.id.search_button) {
    			new RetrieveUpcTask(getApplicationContext(), (Activity)this, conn).execute(parsed);
    		}
    		else if (this.viewId==R.id.add_item) {
    			//Creating the instance of PopupMenu  
                PopupMenu popup = new PopupMenu(MainActivity.this, this.addBtn);  
                //Inflating the Popup using xml file  
                popup.getMenuInflater().inflate(R.menu.item_classification, popup.getMenu());  
               
                //registering popup with OnMenuItemClickListener  
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
                 public boolean onMenuItemClick(MenuItem item) {  
                  Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();  
                  return true;  
                 }  
                });  
      
                popup.show();//showing popup menu  
    			// create a new class to add and update database
    		}
		}
    	else{
    	    Toast toast = Toast.makeText(getApplicationContext(), 
    	        "No scan data received!", Toast.LENGTH_SHORT);
    	    toast.show();
    	}
    }
}
