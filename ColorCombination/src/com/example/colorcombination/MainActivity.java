package com.example.colorcombination;

import java.io.ObjectOutputStream.PutField;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v4.app.NavUtils;

public class MainActivity extends Activity {

	private final int REQUEST_CODE_SAVE = 0;
	private final int REQUEST_CODE_LOAD = 1;
	
	private SaveStore store;
	
	ColorCombinationView colors;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        store = new SaveStore(this);
        
        colors = (ColorCombinationView) findViewById(R.id.combination_view);
        
    }

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
        
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.add_color:
				new ColorPicker(MainActivity.this, colors, Color.GREEN).show();
				return true;
			case R.id.clear:
				colors.removeAllColors();
				return true;
			case R.id.expand:
				review();
				return true;
			case R.id.save:
				saveColors();
				return true;
			case R.id.load:
				loadColors();
//				store.openToRead();
//				store.load(2, colors);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }

    protected void review(){
    	Intent intent = new Intent(this, ReviewActivity.class);
    	intent.putExtra(getPackageName()+".basics", colors.getBlockBasics());
    	startActivity(intent);
    }
    
    protected void saveColors(){
    	Log.w("SAVE", "SAVE BEGIN");
		Intent intent = new Intent(this, SaveActivity.class);
		startActivityForResult(intent, REQUEST_CODE_SAVE);
	}
    
    protected void loadColors(){
    	Log.w("LOAD", "LOAD BEGIN");
		Intent intent = new Intent(this, LoadActivity.class);
		startActivityForResult(intent, REQUEST_CODE_LOAD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK){
			// id выбранной в списке записи
			long id = data.getLongExtra("id", -1);
			Log.w("SAVE ON RESULT", "id="+id);
			switch(requestCode){
				case REQUEST_CODE_SAVE :
					store.openToWrite();
					String name = data.getExtras().getString("name");
					if(id == -1)
						store.save(name, colors);
					else
						store.save(id, name, colors);
					store.close();
					break;
				case REQUEST_CODE_LOAD : 
					Log.w("LOAD_RESULT", data.getLongExtra("id", -1)+"");
					store.openToRead();
					store.load(id, colors);
					store.close();
					break;
			}
		}
	}

    
}
