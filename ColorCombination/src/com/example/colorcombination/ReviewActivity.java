package com.example.colorcombination;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ReviewActivity extends Activity {

	private class ColorBlock extends View{

		private float height;
		
		public ColorBlock(Context context, ColorCombinationView.BlockBasic bBasic) {
			super(context);
			this.height = bBasic.getHeight();
			setBackgroundColor(bBasic.getColor());
		}
		
		public float getHeightRate(){
			return this.height;
		}
				
		public void setHeight(int height){
			this.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, height));
		}
		
	}
	
	private class ReviewPane extends LinearLayout{

		public ReviewPane(Context context) {
			super(context);
			this.setOrientation(VERTICAL);
			this.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT));
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			// TODO Auto-generated method stub
			super.onSizeChanged(w, h, oldw, oldh);
			ColorBlock block;
			for(int i=0; i<getChildCount(); i++){
				block = ((ColorBlock)this.getChildAt(i));
				block.setHeight((int)(h*block.getHeightRate()));
			}
		}
		
	}
	
	private ReviewPane rPane;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rPane = new ReviewPane(this);
		setContentView(rPane);	
		getActionBar().hide();

		Parcelable[] basics = getIntent().getExtras().getParcelableArray(getPackageName()+".basics");

		ColorCombinationView.BlockBasic bBasic = null;
		for(Parcelable p : basics){
			bBasic = (ColorCombinationView.BlockBasic)p;
//			Log.w("COLOR^", bBasic.getColor()+"");
			rPane.addView(new ColorBlock(this, bBasic));
		}
				
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
