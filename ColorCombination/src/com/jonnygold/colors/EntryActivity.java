package com.jonnygold.colors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		startActivity(intent);
	}
		
}
