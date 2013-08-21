package com.example.colorcombination;

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
	int[] colorTab = new int[]{Color.BLACK, Color.RED, Color.CYAN, Color.MAGENTA, Color.GRAY};
	
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
    
    int pos = 0;
    public void addColor(View v){
    	colors.addColor(colorTab[pos]);
    	pos++;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.add_color:
				new ColorPicker(MainActivity.this, colors, Color.WHITE).show();
				return true;
			case R.id.clear:
				colors.removeAllColors();
				return true;
			case R.id.expand:
				/* «¿œ”— ¿≈Ã ÕŒ¬”ﬁ ¿ “»¬»“» Õ¿ œŒÀÕ€… › –¿Õ */
				return true;
			case R.id.save:
				saveColors();
				return true;
			case R.id.load:
				/* «¿œ”— ¿≈Ã ƒ»¿ÀŒ√ «¿√–”« » */
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }

    public void saveColors(){
    	Log.w("SAVE", "SAVE BEGIN");
		Intent intent = new Intent(this, SaveActivity.class);
		startActivityForResult(intent, REQUEST_CODE_SAVE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.w("SAVE ON RESULT", "BEGIN");
		String name = data.getExtras().getString("name");
		Log.w("SAVE ON RESULT", name);
		if (data == null){
			Log.w("SAVE ON RESULT", "DATA IS NULL");
		}
		if(resultCode == RESULT_OK){
			Log.w("SAVE ON RESULT", "IF OK");
			switch(requestCode){
				case REQUEST_CODE_SAVE :
					Log.w("SAVE ON RESULT", "REQUEST_CODE_SAVE");
//					Log.w("SAVE_RESULT_ID", data.getLongExtra("id", -1)+"");
//					Log.w("SAVE_RESULT_NAME", data.getStringExtra("name"));
					store.openToWrite();
					store.save(name, colors);
					Log.w("SAVE ON RESULT", "DONE");
//					if(data.getLongExtra("id", -1) != -1){
//						store.save(data.getLongExtra("id", -1), colors);
//					}
//					else{
//						store.save(data.getStringExtra("name"), colors);
//					}
					break;
				case REQUEST_CODE_LOAD : 
					Log.w("LOAD_RESULT", data.getLongExtra("id", -1)+"");
					long id = data.getLongExtra("id", -1);
					// œÓ‰ÛÏ‡Ú¸, ÍÛ‰‡ Á‡ÚÓÎÍ‡Ú¸ SaveStore
					SaveStore store = new SaveStore(this);
					/* SaveStore */store.load(id, colors);
					store.close();
					break;
			}
		}
	}

//	public void loadColors(View v){
//		Intent intent = new Intent(this, LoadActivity.class);
//		startActivityForResult(intent, REQUEST_CODE_LOAD);
//	}
    
}
