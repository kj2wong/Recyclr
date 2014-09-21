package com.knr.recyclr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends ActionBarActivity implements OnClickListener{
	
	private LinearLayout scanBtn;
	private LinearLayout addBtn;
	private LinearLayout infoBtn;
	private int viewId;
	private Connection conn = null;
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	public final static String UPC_IDENTIFIER = "com.example.myfirstapp.IDENTIFIER";
	public static String upcCode;
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().hide();
        scanBtn = (LinearLayout)findViewById(R.id.search_section_btn);
        scanBtn.setOnClickListener(this);
        
        addBtn = (LinearLayout)findViewById(R.id.additem_section_btn);
        addBtn.setOnClickListener(this);

        scanBtn.setOnClickListener(this);

        infoBtn = (LinearLayout)findViewById(R.id.nobarcode_section_btn);
        infoBtn.setOnClickListener(this);
        
        // Startup JDBC driver
    	try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	// Connect to RDS Database
    	new DatabaseConnection(this).execute();

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
    	if(v.getId()==R.id.search_section_btn || v.getId()==R.id.additem_section_btn){
    		//scan
    		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
    		scanIntegrator.initiateScan();
		}
    	else if (v.getId()==R.id.nobarcode_section_btn) {
    		Intent intent = new Intent(this, NobarcodeActivity.class);
        	startActivity(intent);
    	}
	}
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	//retrieve scan result
    	IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanningResult != null && scanningResult.getContents() != null) {
    		//we have a result
    		String scanContent = scanningResult.getContents();
    		String parsed = scanContent.substring(1, scanContent.length()-1);
    		upcCode = parsed;
    		if(this.viewId==R.id.search_section_btn) {
    			DatabaseWrapper db_wrap = new DatabaseWrapper(
    					getApplicationContext(), (Activity)this, conn);
        		db_wrap.getAction(parsed);
    		}
    		else if (this.viewId==R.id.additem_section_btn) {
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
