package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class PaletteView extends LinearLayout {

	private static class ColorBlock extends View{

		private float height;
		
		public ColorBlock(Context context, BlockBasic bBasic) {
			super(context);
			this.height = bBasic.getHeight();
			setBackgroundColor(bBasic.getColor());
			this.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, this.height));
		}
		
		public float getHeightRate(){
			return this.height;
		}
				
		public void setHeight(int height){
			this.setLayoutParams(new LinearLayout.LayoutParams(height, LayoutParams.MATCH_PARENT));
		}
		
	}
	
	private Collection<BlockBasic> palette;
	
	public PaletteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setOrientation(HORIZONTAL);
	}

	public PaletteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOrientation(HORIZONTAL);
	}

	public PaletteView(Context context) {
		super(context);
		this.setOrientation(HORIZONTAL);
	}
	
	public void setPalette(Collection<BlockBasic> palette) {
		this.palette = new ArrayList<BlockBasic>(palette);
		updatePalette();
	}
	
	public void updatePalette() {
		if(palette == null) {
			return;
		}
		for(BlockBasic block : palette) {
			addColorBlock(block);
		}
	}
	
	private void addColorBlock(BlockBasic block) {
		addView(new ColorBlock(getContext(), block));
	}
	
//	@Override
//	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		super.onSizeChanged(w, h, oldw, oldh);
//		ColorBlock block;
//		for(int i=0; i<getChildCount(); i++){
//			block = ((ColorBlock)this.getChildAt(i));
//			block.setHeight((int)(h*block.getHeightRate()));
//		}
//	}

}
