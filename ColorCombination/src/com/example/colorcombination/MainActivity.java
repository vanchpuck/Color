package com.example.colorcombination;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

//        colors.addColor(Color.BLACK);
//        colors.addColor(Color.GRAY);
//        
//        Log.w("###", colors.getHeight()+"");
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    int pos = 0;
    public void addColor(View v){
    	colors.addColor(colorTab[pos]);
    	pos++;
    }

    public void delColor(View v){
    	colors.removeColor(0);
    }
    
    public void copyColor(View v){
    	colors.setColor();
    }
    
}
