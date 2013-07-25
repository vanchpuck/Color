package com.example.colorcombination;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class ColorCombinationView extends LinearLayout{

	private class ResizersController{
		
		private LinearLayout leftResizersPane;
		private LinearLayout rightResizersPane;
		
		private ResizersController(){
			leftResizersPane = new LinearLayout(getContext());
			rightResizersPane = new LinearLayout(getContext());
			
			leftResizersPane.setOrientation(VERTICAL);
//			leftResizersPane.setBackgroundColor(Color.BLUE);		
			leftResizersPane.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 0.2f));
			
			rightResizersPane.setOrientation(VERTICAL);
//			rightResizersPane.setBackgroundColor(Color.BLUE);		
			rightResizersPane.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 0.2f));
			
			ResizersPair firstPair = addPair();
			firstPair.leftResizer.setMargin((Resizer.HEIGHT>>1)*-1);
			firstPair.rightResizer.setMargin((Resizer.HEIGHT>>1)*-1);
			firstPair.rightResizer.setBackgroundColor(Color.LTGRAY);
						
		}
		
		public ResizersPair addPair(){
			
			Resizer left = new Resizer(getContext());
			Resizer right = new Resizer(getContext());
			
			leftResizersPane.addView(left);
			rightResizersPane.addView(right);
			
			return new ResizersPair(left, right);
		}
		
		public ResizersPair addPair(int idx){
			
			Resizer left = new Resizer(getContext());
			Resizer right = new Resizer(getContext());
			
			leftResizersPane.addView(left, idx);
			rightResizersPane.addView(right, idx);
			
			return new ResizersPair(left, right);
		}
		
		public void removePair(int idx){
			leftResizersPane.removeViewAt(idx);
			rightResizersPane.removeViewAt(idx);
		}
		
		public void setPair(int idx){
			
		}
				
		public ResizersPair getPair(int idx){
			return new ResizersPair((Resizer)leftResizersPane.getChildAt(idx), (Resizer)rightResizersPane.getChildAt(idx));
		}
		
		public LinearLayout getLeftPane(){
			return leftResizersPane;
		}
		
		public LinearLayout getRightPane(){
			return rightResizersPane;
		}
	}
	
	private class ResizersPair{		
		
		private Resizer leftResizer;
		private Resizer rightResizer;
		
		public ResizersPair(Resizer left, Resizer right){
			this.leftResizer = left;
			this.rightResizer = right;
		}
	}
	
	private class Resizer extends View{

		public final static int HEIGHT = 30;
		
		
		public Resizer(Context context) {
			super(context);
			this.setBackgroundColor(Color.YELLOW);
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, HEIGHT));
		}
		
		public void setMargin(int margin){
			LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, HEIGHT);
			lParams.setMargins(0, margin, 0, 0);
			
			this.setLayoutParams(lParams);
		}
		
		int startY = 0;
		@Override
		public boolean onTouchEvent(MotionEvent event){ 
			
			int idx = ((LinearLayout)this.getParent()).indexOfChild(this);
			
			int action = MotionEventCompat.getActionMasked(event);
//			Log.w("IDX", this.idx+"");
	        
			
			
		    switch(action) {
		    	case (MotionEvent.ACTION_DOWN) :
		    		startY = (int)event.getY();
		        case (MotionEvent.ACTION_MOVE) :
//		        	int a = (int)MotionEventCompat.getX(event, index);
		        	
		        
		        	int y = (int)(event.getY()-startY);
//		        	Log.w("event.getY()", event.getY()+"");
//		        	Log.w("startY", startY+"");
		        	Log.w("y", y+"");
		        
		        	ColorBlock block = (ColorBlock)(colorsPane.getChildAt(idx-1));
		        	if(block == null){
		        		Log.w("BLOCK1", "NULL");
		        	}
		        	block.setHeight((block.getHeight()+y));
		        	
		        	block = (ColorBlock)(colorsPane.getChildAt(idx));
		        	if(block == null){
		        		Log.w("BLOCK2", "NULL");
		        	}
		        	block.setHeight((block.getHeight()-y));
		        	
		        	break;
		        default :
		        	super.onTouchEvent(event);
		    }
			return true;
		}
		
	}
	
	
	private class ColorsPane extends LinearLayout{

		
		
//		@Override
//		protected void onLayout(boolean changed, int l, int t, int r, int b) {
//			// TODO Auto-generated method stub
//			super.onLayout(changed, l, t, r, b);
//			Log.w("onLayout", "### changed="+changed+" l="+l+" t="+t+" r="+r+" b="+b+" ###");
//		}
		
		public ColorsPane(Context context, AttributeSet attrs) {
			super(context,attrs);
			this.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 1f));
			this.setOrientation(VERTICAL);
		}

		public void addColor(ColorBlock color){
			
			int childCount = getChildCount();
			
			int newHeight = 0;
			ColorBlock cBlock = null;;
			if(color.getHeight() == 0){
				Log.w("0","0");
				// Вычислим высоту нового блока		
				newHeight = this.getHeight()/(childCount+1);
				color.setHeight(newHeight);
				
				// Пройдёмся по всем блокам и изменим размеры
				for(int i=0; i<childCount; i++){	
					cBlock = (ColorBlock)this.getChildAt(i);
					cBlock.setHeight(cBlock.getHeight() - newHeight/(childCount));
//					cBlock.setHeight(cBlock.getHeight() - cBlock.getHeight()/this.getHeight()*newHeight);
				}
			}
			
			
			
			// Добавляем блок
			super.addView(color);
		}
		
		public void removeColor(int idx){
			ColorBlock block = (ColorBlock) this.getChildAt(idx);
			
			int height = block.getHeight();			
			
			this.removeViewAt(idx);
			
			int childCount = getChildCount();
			
			ColorBlock cBlock;
			for(int i=0; i<childCount; i++){
				cBlock = (ColorBlock)this.getChildAt(i);
				cBlock.setHeight(cBlock.getHeight() + height/childCount);
			}			
			
		}
		
		public void setColorBlock(ColorBlock color, int idx){
			
		}

		
	}
	
	private class ColorBlock extends View{
		
		public ColorBlock(Context context, int color) {
			super(context);
			this.setBackgroundColor(color);
			
			this.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					//ClipData cData = ClipData.newPlainText("TEST", "text");
					//cData.addItem(new ClipData.Item(vie+""));
					
					View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
					
					v.startDrag(null, shadow, v, 0);
					
					Log.w("DRAG", "START");
					
					return true;
				}
			});
		}
		
		@Override
		public void onSizeChanged(int w, int h, int oldw, int oldh){
						
			int index = ((LinearLayout)this.getParent()).indexOfChild(this);
			
			Log.w("IDX", index+"");
			
			int margin = h-Resizer.HEIGHT;

			ResizersPair pair = resController.getPair(index+1);
			
			pair.rightResizer.setMargin(margin);
			
			pair.leftResizer.setMargin(margin);
		}
		
		public void setHeight(int height){
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, height));
		}
		
//		public void setHeight(float height){
//			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, height));
//		}
		
	}
	
//	private LinearLayout leftResizersPane;
//	private LinearLayout rightResizersPane;
	private ColorsPane colorsPane;
	private ResizersController resController;
		
	public ColorCombinationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		setBackgroundColor(Color.RED);
		
		resController = new ResizersController();
		colorsPane = new ColorsPane(context, attrs);
//		colorsPane.setBackgroundColor(Color.GREEN);
		
		addView(resController.getLeftPane());
		addView(colorsPane);
		addView(resController.getRightPane());
		
//		leftResizersPane = (ResizersPane) getChildAt(0);
//		rightResizersPane = (ResizersPane) getChildAt(2);
//		colorsPane = (ColorsPane) getChildAt(1);
		
//		leftResizersPane.addView(leftResizersPane.new Resizer(context));
//		leftResizersPane.setBackgroundColor(Color.RED);

	}
	
	public void addColor(int color){
		resController.addPair();
		colorsPane.addColor(new ColorBlock(getContext(), color));
	}
	
	public void removeColor(int idx){
		colorsPane.removeColor(idx);
		resController.removePair(idx+1);
	}
	
	public void setColor(){
		ColorBlock bl = (ColorBlock)colorsPane.getChildAt(1);
		colorsPane.removeViewAt(1);
		colorsPane.addColor(bl);
	}
	

}
