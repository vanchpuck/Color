package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.List;

import com.jonnygold.quantizer.Histogram;
import com.jonnygold.quantizer.IsHistogram;
import com.jonnygold.quantizer.OrderedHistogram;
import com.jonnygold.quantizer.Quantizer;
import com.jonnygold.quantizer.RGBColor;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	private static final double DEFAULT_HISTOGRAM_BOUND = 0.0001;
	private static final int DEFAULT_QUANTIZATION_LEVEL = 3;
	private static final int DEFAULT_PALETE_BOUND = 5;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        
        PaletteView paletteView = (PaletteView)findViewById(R.id.view_palette);
        ImageView imageView = (ImageView)findViewById(R.id.view_image);
        
        Uri selectedImageUri = getIntent().getParcelableExtra("uri");
		 
		String tempPath = getPath(selectedImageUri);
		Bitmap image;
		BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
		
		image = BitmapFactory.decodeFile(tempPath, btmapOptions);
		
		image = Bitmap.createScaledBitmap(image, image.getWidth()/4, image.getHeight()/4, true);
		
		imageView.setImageBitmap(image);
        
//        Bitmap image = (Bitmap)getIntent().getSerializableExtra("iamge");
//        
//        Bitmap image = BitmapFactory.decodeResource(this.getResources(),
//                R.drawable.himia);
//        
        Histogram.Builder builder = new Histogram.Builder();

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        for(int pixel : pixels){
        	builder.addColor(new RGBColor(Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
        }
        
        IsHistogram histogram = builder.build(DEFAULT_HISTOGRAM_BOUND);
        
        Quantizer q = new Quantizer();
        IsHistogram palete = new OrderedHistogram(q.quantize(histogram, DEFAULT_QUANTIZATION_LEVEL));
        
        IsHistogramConverter converter = new BoundedHistogramConverter(DEFAULT_PALETE_BOUND);
        
        paletteView.setPalette(converter.convert(palete));
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = this
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
		
}
