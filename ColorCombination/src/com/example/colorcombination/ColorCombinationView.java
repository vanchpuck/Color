package com.example.colorcombination;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorCombinationView extends LinearLayout implements ColorPicker.OnColorChangedListener{

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
		        	
		        	int y = (int)(event.getY()-startY);
		        
		        	ColorBlock block1 = (ColorBlock)(colorsPane.getChildAt(idx-1));
		        	ColorBlock block2 = (ColorBlock)(colorsPane.getChildAt(idx));
		        	int minH = Resizer.HEIGHT*2;
		        	
		        	
		        	if(event.getY() <= startY && block1.getHeight()+y <= minH){
		        		break;
		        	}
		        	else if(event.getY() >= startY && block2.getHeight()-y <= minH){
		        		break;
		        	}
		        	block1.setHeight((block1.getHeight()+y));
		        	
		        	block2.setHeight((block2.getHeight()-y));
		        	
		        	break;
		        default :
		        	super.onTouchEvent(event);
		    }
			return true;
		}
		
	}
	
	/**
	 * 
	 * @author Vanchpuck
	 *
	 */
	private class ColorsPane extends LinearLayout{
		
		public ColorsPane(Context context, AttributeSet attrs) {
			super(context,attrs);
			this.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 1f));
			this.setOrientation(VERTICAL);
		}
		
		public void refreshResizers(){
			ColorBlock block = null;
			for(int i=0; i<this.getChildCount(); i++){
				block = (ColorBlock) this.getChildAt(i);
				block.onSizeChanged(block.getWidth(), block.getHeight(), block.getWidth(), block.getHeight());
			}
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
	
	/**
	 * 
	 * @author Vanchpuck
	 *
	 */
	private class BinPane extends LinearLayout{
		
		public BinPane(Context context, AttributeSet attrs) {
			super(context,attrs);
			this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 50));
			this.setOrientation(HORIZONTAL);
			this.setGravity(Gravity.CENTER_HORIZONTAL);
			
			ImageView bin = new ImageView(getContext());
			bin.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			bin.setImageResource(R.drawable.ic_launcher);
			
			TextView text = new TextView(getContext());
			text.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(getContext().getResources().getString (R.string.bin_text));
			text.setTextSize(20);
			text.setBackgroundColor(Color.LTGRAY);
			
			this.addView(bin);
			this.addView(text);
			
			this.setOnDragListener(new OnDragListener() {
				
				public boolean onDrag(View v, DragEvent event) {

					final int action = event.getAction();

					int idx = 0;
					
					switch(action) {
						case DragEvent.ACTION_DRAG_STARTED:
							return true;
						case DragEvent.ACTION_DRAG_ENTERED:
//							binPane.setBackgroundColor(Color.CYAN);
							return true;
						case DragEvent.ACTION_DRAG_EXITED:
//							binPane.setBackgroundColor(Color.LTGRAY);
							return true;
						case DragEvent.ACTION_DROP:
							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
							Log.w("DEL_IDX", idx+"");
							colorsPane.removeColor(idx);
							return true;
						case DragEvent.ACTION_DRAG_ENDED:
							/* REMOVE COLOR FILTER*/
							return true;
					}
					return true;
				}
			});
		}
		
	}
	
	/**
	 * 
	 * @author Vanchpuck
	 *
	 */
	private class ColorBlock extends View{
		
		public ColorBlock(Context context, int color) {
			super(context);
			this.setBackgroundColor(color);
			this.setOnDragListener(new OnDragListener() {
				
				@Override
				public boolean onDrag(View v, DragEvent event) {
					final int action = event.getAction();
					int idx = 0;
					// Handles each of the expected events
					switch(action) {
					 	case DragEvent.ACTION_DRAG_STARTED :
					 		return true;
					 		
					 	case DragEvent.ACTION_DRAG_ENTERED :
					 		return true;
					 		
					 	case DragEvent.ACTION_DRAG_LOCATION :
					 		Log.w("ColorDragDrop", "Y location: "+event.getY()+" border = "+(v.getHeight()/2));
					 		idx = ((LinearLayout)v.getParent()).indexOfChild(v);
							if(event.getY() > v.getHeight()/2){
								resController.getPair(idx+1).rightResizer.setBackgroundColor(Color.BLACK);
								resController.getPair(idx).rightResizer.setBackgroundColor(Color.YELLOW);
							}
							else{
								resController.getPair(idx+1).rightResizer.setBackgroundColor(Color.YELLOW);
								resController.getPair(idx).rightResizer.setBackgroundColor(Color.BLACK);
							}
							return true;
						
						case DragEvent.ACTION_DRAG_EXITED :
							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
							resController.getPair(idx).rightResizer.setBackgroundColor(Color.YELLOW);
							resController.getPair(idx+1).rightResizer.setBackgroundColor(Color.YELLOW);
							return true;
							
						case DragEvent.ACTION_DROP:
							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
							// Вроде как получаем вьюшку, которую перетаскиваем
							View view = (View) event.getLocalState();
							ColorsPane owner = (ColorsPane) view.getParent();
							
							resController.getPair(idx+1).rightResizer.setBackgroundColor(Color.YELLOW);
							resController.getPair(idx).rightResizer.setBackgroundColor(Color.YELLOW);
							owner.removeView(view);
							owner.addView(view, idx);
							
							owner.refreshResizers();
														
							return true;
							
						case DragEvent.ACTION_DRAG_ENDED :
							return true;
							
						default:
							Log.w("ColorDragDrop", "Uknown drag action");
						 		return false;
					}
				}
			});
			
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
		
		public int getPosition(){
			return ((LinearLayout)this.getParent()).indexOfChild(this);
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
	private BinPane binPane;
		
	public ColorCombinationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		setBackgroundColor(Color.RED);
		
		LinearLayout workspace = new LinearLayout(context);
		workspace.setOrientation(LinearLayout.HORIZONTAL);
		workspace.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0, 1f));
		
		resController = new ResizersController();
		colorsPane = new ColorsPane(context, attrs);
		binPane = new BinPane(context, attrs);
		
//		colorsPane.setBackgroundColor(Color.GREEN);
		
		workspace.addView(resController.getLeftPane());
		workspace.addView(colorsPane);
		workspace.addView(resController.getRightPane());
		this.addView(workspace);
		this.addView(binPane);
		
		
		
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
	
	public void removeAllColors(){
		resController.leftResizersPane.removeAllViews();
		resController.rightResizersPane.removeAllViews();
		colorsPane.removeAllViews();
		
		ResizersPair firstPair = resController.addPair();
		firstPair.leftResizer.setMargin((Resizer.HEIGHT>>1)*-1);
		firstPair.rightResizer.setMargin((Resizer.HEIGHT>>1)*-1);
		firstPair.rightResizer.setBackgroundColor(Color.LTGRAY);
	}
	
	public void setColor(){
		ColorBlock bl = (ColorBlock)colorsPane.getChildAt(1);
		colorsPane.removeViewAt(1);
		colorsPane.addColor(bl);
	}

	@Override
	public void colorChanged(int color) {
		this.addColor(color);
	}
	

}
