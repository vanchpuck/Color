package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jonnygold.quantizer.Histogram;
import com.jonnygold.quantizer.IsHistogram;
import com.jonnygold.quantizer.OrderedHistogram;
import com.jonnygold.quantizer.Quantizer;
import com.jonnygold.quantizer.RGBColor;
import com.jonnygold.quantizer.IsHistogram.IsBar;

import de.devmil.common.ui.color.ColorSelectorDialog;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageActivity extends Activity {

	private static final double DEFAULT_HISTOGRAM_BOUND = 0.0005;
	private static final int DEFAULT_QUANTIZATION_LEVEL = 5;
	private static final int DEFAULT_PALETE_BOUND = 5;
	
	private PaletteView paletteView;
	
	private ImageView imageView;
	
	private SaveStore store;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        
        paletteView = (PaletteView)findViewById(R.id.view_palette);
        imageView = (ImageView)findViewById(R.id.view_image);
        
        store = new SaveStore(this);
        
        // получаем URI выбранной картинки
//        Uri imageUri = getIntent().getParcelableExtra("uri");
//        if(imageUri == null){
//        	return;
//        }
        
        Bitmap image;
        
//        Uri selectedImageUri = getIntent().getParcelableExtra("uri");
//		 
//		String tempPath = getPath(selectedImageUri);
//		
//		BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//		
//		image = BitmapFactory.decodeFile(tempPath, btmapOptions);
//		
//		image = Bitmap.createScaledBitmap(image, image.getWidth()/4, image.getHeight()/4, true);
		
		image = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.himia);
		
		image = Bitmap.createScaledBitmap(image, image.getWidth()/14, image.getHeight()/14, true);
		
		imageView.setImageBitmap(image);
        
//        Bitmap image = (Bitmap)getIntent().getSerializableExtra("iamge");
//        
        
//        
        Histogram.Builder builder = new Histogram.Builder();

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getPixels(pixels, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        for(int pixel : pixels){
        	builder.addColor(new RGBColor(Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
        }
        
//        IsHistogram histogram = builder.build(DEFAULT_HISTOGRAM_BOUND);
        IsHistogram histogram = builder.build();
        
        Quantizer q = new Quantizer();
        IsHistogram palete = new OrderedHistogram(q.quantize(histogram, DEFAULT_QUANTIZATION_LEVEL));
        
        palete = filter(palete);
        
        IsHistogramConverter converter = new BoundedHistogramConverter(DEFAULT_PALETE_BOUND);
//        IsHistogramConverter converter = new HistogramConverter();
        
        paletteView.setPalette(converter.convert(palete));
	}
	
	public IsHistogram filter(IsHistogram palete) {
		Histogram.Builder builder = new Histogram.Builder();
		Collection<RGBColor> newCol = new ArrayList<RGBColor>();
		boolean flag = true;
		for(IsBar col1 : palete.getBars()){
			for(RGBColor col2 : newCol){
				if(isSimilar(col1.getColor(), col2)){
					flag = false;
					break;
				}
			}
			if(flag){
				newCol.add(col1.getColor());
				builder.addColor(col1.getColor(), col1.getCount());				
			}
			flag = true;
		}
		return new OrderedHistogram(builder.build());
	}
	
	private boolean isSimilar(RGBColor co1, RGBColor col2) {
		int r = Math.abs(co1.getRed() - col2.getRed());
		int g = Math.abs(co1.getGreen() - col2.getGreen());
		int b = Math.abs(co1.getBlue() - col2.getBlue());
		if(r > 27){
			return false;
		}  
		if (g > 27){
			return false;
		}  
		if (b > 30){
			return false;
		}
		return true;
	}
	
	public String getPath(Uri uri) {
        String[] projection = { MediaColumns.DATA };
        Cursor cursor = this
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_image, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_palete:
			savePalette();
			return true;
		case R.id.edit_palete:
			editPalette();
			return true;
		case R.id.share_palete:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void savePalette() {
		Intent intent = new Intent(this, SaveActivity.class);
		startActivityForResult(intent, SaveActivity.REQUEST_CODE_SAVE);
	}
	
	private void editPalette() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("blocks", paletteView.getPalette());
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK){
			// id ��������� � ������ ������
			long id = data.getLongExtra("id", -1);
			switch(requestCode){
				case SaveActivity.REQUEST_CODE_SAVE :
					store.openToWrite();
					String name = data.getExtras().getString("name");
					try {
						store.saveNew(name, paletteView.getPalette());
					} catch(SQLException exc) {
						new AlertDialog.Builder(this).
								setTitle("Ошибка сохранения").
								setMessage("Не удалось сохранить палитру.").
								create().show();
					}						
					store.close();
					break;
				default :
					break;
			}
		}
	}
		
}
