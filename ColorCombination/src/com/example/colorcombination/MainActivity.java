package com.example.colorcombination;

import android.os.Bundle;
import android.app.Activity;
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

	ColorCombinationView colors;
	int[] colorTab = new int[]{Color.BLACK, Color.RED, Color.CYAN, Color.MAGENTA, Color.GRAY};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        colors = (ColorCombinationView) findViewById(R.id.combination_view);
        
        
//        getActionBar().hide();
        
//		b.setOnDragListener(new View.OnDragListener() {
//			
//			@Override
//			public boolean onDrag(View v, DragEvent event) {
//				final int action = event.getAction();
//		
//				// Handles each of the expected events
//				switch(action) {
//				 	case DragEvent.ACTION_DRAG_STARTED :
//				 		return true;
//				 		
//				 	case DragEvent.ACTION_DRAG_ENTERED :
//				 		//v.setColorFilter(Color.RED);
//				 		return true;
//				 		
//				 	case DragEvent.ACTION_DRAG_LOCATION :
//				 		return true;
//				 		
//				 	case DragEvent.ACTION_DRAG_EXITED :
//				 		return true;
//				 		
//				 	case DragEvent.ACTION_DROP:
//				 		// Âğîäå êàê ïîëó÷àåì âüşøêó, êîòîğóş ïåğåòàñêèâàåì
//						View view = (View) event.getLocalState();
//						LinearLayout owner = (LinearLayout) view.getParent();
//						owner.removeView(view);
//						return true;
//					
//					case DragEvent.ACTION_DRAG_ENDED :
//						return true;
//						
//					default:
//						Log.w("ColorDragDrop", "Uknown drag action");
//					 		return false;
//				}
//			}
//		});

//        colors.addColor(Color.BLACK);
//        colors.addColor(Color.GRAY);
//        
//        Log.w("###", colors.getHeight()+"");
        
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
				/* ÇÀÏÓÑÊÀÅÌ ÍÎÂÓŞ ÀÊÒÈÂÈÒÈ ÍÀ ÏÎËÍÛÉ İÊĞÀÍ */
				return true;
			case R.id.save:
				/* ÇÀÏÓÑÊÀÅÌ ÄÈÀËÎÃ ÑÎÕĞÀÍÅÍÈß */
				return true;
			case R.id.load:
				/* ÇÀÏÓÑÊÀÅÌ ÄÈÀËÎÃ ÇÀÃĞÓÇÊÈ */
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }

    
}
