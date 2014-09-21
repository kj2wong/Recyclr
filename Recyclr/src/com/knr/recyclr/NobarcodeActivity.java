package com.knr.recyclr;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NobarcodeActivity extends ActionBarActivity {
	ListView listView ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nobarcode);
		getActionBar().hide();
		
		// Get ListView object from xml
        listView = (ListView) findViewById(R.id.list_nobarcodeitems);
        
        // Defined Array values to show in ListView
        ArrayList<NoBarcodeItem> values = new ArrayList<NoBarcodeItem>();
        values.add(new NoBarcodeItem("Paper", NoBarcodeItem.DisposeType.Recycle, null));
        values.add(new NoBarcodeItem("Plastic Containers", NoBarcodeItem.DisposeType.Recycle, null));
        values.add(new NoBarcodeItem("Plastic Packaging", NoBarcodeItem.DisposeType.Trash, null));
        values.add(new NoBarcodeItem("Aluminum & Tin Cans", NoBarcodeItem.DisposeType.Recycle, null));
        values.add(new NoBarcodeItem("Metal", NoBarcodeItem.DisposeType.Recycle, null));
        values.add(new NoBarcodeItem("Carboard", NoBarcodeItem.DisposeType.Recycle, null));
        values.add(new NoBarcodeItem("Glass Botles", NoBarcodeItem.DisposeType.Recycle, null));
        values.add(new NoBarcodeItem("Paper", NoBarcodeItem.DisposeType.Recycle, null));
        
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<NoBarcodeItem> adapter = new ArrayAdapter<NoBarcodeItem>(
        		this, R.layout.recyclr_listview, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter); 
        
        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view,
             int position, long id) {
            
           // ListView Clicked item index
           int itemPosition = position;
           
           // ListView Clicked item value
           NoBarcodeItem  itemValue = (NoBarcodeItem) listView.getItemAtPosition(position);
              
            // Show Alert 
            Toast.makeText(getApplicationContext(),
              "Position :"+itemPosition+"  ListItem : " +itemValue.toString() , Toast.LENGTH_LONG)
              .show();
          }
        }); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nobarcode, menu);
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
}
