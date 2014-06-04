package com.jonnygold.colors;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.View;

public class EntryActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
	}
	
	public void createPalette(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	public void getFromGallery(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 0);
	}
	
	public void getTest(View view) {
		Intent intent = new Intent(this, ImageActivity.class);
		intent.putExtra("imageId", R.drawable.rus);
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Intent intent = new Intent(this, ImageActivity.class);
		if (resultCode == RESULT_OK) {
			Uri selectedImageUri = data.getData();
//			 
//			String tempPath = getPath(selectedImageUri);
//			Bitmap bm;
//			BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//			
//			bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
			
			intent.putExtra("uri", selectedImageUri);
//			intent.putExtra("image", "dddsf");
			startActivity(intent);
        }
	}
			
}
