package com.jonnygold.colors;

import com.jonnygold.colors.R;

import de.devmil.common.ui.color.ColorSelectorDialog;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Intent;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
//        if(!savedInstanceState.isEmpty()){
////        	int c = savedInstanceState.getInt("color", 0);
////	        if(c != 0){
////	        	colors.addColor(c);
////	        }
//        }
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
//    	Log.w("onSave", "SAVE");
    	outState.putParcelableArray(getPackageName()+".basics", colors.getBlockBasics());
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
//    	Log.w("onRestore", "RESTORE");
    	Parcelable[] basics = savedInstanceState.getParcelableArray(getPackageName()+".basics");
    	
    	BlockBasic bBasic = null;
    	for(int i=0; i<basics.length; i++){
    		bBasic = (BlockBasic) basics[i];
    		colors.addColor(bBasic.getColor());
    		colors.getColorBlock(i).setHeight(bBasic.getActHeight());
    	}
    	
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onPostCreate(savedInstanceState);
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
//				new ColorPicker(MainActivity.this, colors, getResources().getColor(R.color.background)).show();
				new ColorSelectorDialog(this, colors, getResources().getColor(R.color.background)).show();
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
//    	Log.w("SAVE", "SAVE BEGIN");
		Intent intent = new Intent(this, SaveActivity.class);
		startActivityForResult(intent, REQUEST_CODE_SAVE);
	}
    
    protected void loadColors(){
//    	Log.w("LOAD", "LOAD BEGIN");
		Intent intent = new Intent(this, LoadActivity.class);
		startActivityForResult(intent, REQUEST_CODE_LOAD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK){
			// id ��������� � ������ ������
			long id = data.getLongExtra("id", -1);
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
					store.openToRead();
					store.load(id, colors);
					store.close();
					break;
			}
		}
	}
	
}
