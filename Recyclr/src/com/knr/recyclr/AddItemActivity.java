package com.knr.recyclr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

public class AddItemActivity extends ActionBarActivity {
	private boolean isTrash;
	private String upcCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		getActionBar().hide();
		
		Intent intent = getIntent();
		upcCode = intent.getStringExtra(MainActivity.UPC_IDENTIFIER);
	    
	    new RetrieveUpcTask(getApplicationContext(), (Activity)this, R.id.itemImagePlaceholder).execute(upcCode);
	    DatabaseWrapper db_wrap = new DatabaseWrapper(getApplicationContext(), (Activity)this, MainActivity.getConn());
		db_wrap.getAction(upcCode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
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
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_recycle:
	            if (checked)
	                isTrash = false;
	            break;
	        case R.id.radio_trash:
	            if (checked)
	                isTrash = true;
	            break;
	    }
	}
	
	public void onSubmitButtonClicked(View view)
	{
		DatabaseWrapper db_wrap = new DatabaseWrapper(getApplicationContext(), (Activity)this, MainActivity.getConn());
		db_wrap.setUpc(upcCode, isTrash);
		finish();
	}
}
