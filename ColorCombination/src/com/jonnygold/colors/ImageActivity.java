package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

public class ImageActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        
        PaletteView paletteView = (PaletteView)findViewById(R.id.view_palette);
        BlockBasic bb1 = new BlockBasic(Color.BLUE, 0.2f, 0);
        BlockBasic bb2 = new BlockBasic(Color.RED, 0.4f, 0);
        BlockBasic bb3 = new BlockBasic(Color.YELLOW, 0.4f, 0);
        
        List<BlockBasic> blocks = new ArrayList<BlockBasic>();
        blocks.add(bb1);
        blocks.add(bb2);
        blocks.add(bb3);
        
        Palette palette = new Palette(blocks, 3);
        
        paletteView.setPalette(palette);
	}
		
}
