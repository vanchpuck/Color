package com.jonnygold.colors;

import com.jonnygold.colors.R;

import de.devmil.common.ui.color.ColorSelectorDialog;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
//import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ColorCombinationView extends LinearLayout implements ColorSelectorDialog.OnColorChangedListener{

	private int duration = getResources().getInteger(android.R.integer.config_shortAnimTime);
	
	private class ResizersController{
		
//		private LinearLayout leftResizersPane;
		private LinearLayout rightResizersPane;
		
		private ResizersController(){
			
//			leftResizersPane = new LinearLayout(getContext());
			rightResizersPane = new LinearLayout(getContext());
			rightResizersPane.setPadding(0, 5, 0, 0);
//			leftResizersPane.setBackgroundColor(Color.BLUE);		
//			leftResizersPane.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.FILL_PARENT, 0.2f));
			
			rightResizersPane.setOrientation(VERTICAL);
//			rightResizersPane.setBackgroundColor(Color.BLUE);		
			rightResizersPane.setLayoutParams(new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.FILL_PARENT, 0.14f));
			
			ResizersPair firstPair = addPair();
			firstPair.rightResizer.setMargin((Resizer.HEIGHT>>1)*-1);
			firstPair.rightResizer.setAlpha(0f);
		}
		
		public ResizersPair addPair(){
			
			Resizer left = new Resizer(getContext());
			Resizer right = new Resizer(getContext());
			
//			leftResizersPane.addView(left);
			rightResizersPane.addView(right);
			
			refresh();
			
			return new ResizersPair(left, right);
		}
		
		public void refresh(){
			int childCount = rightResizersPane.getChildCount();
			if(childCount > 2){
				rightResizersPane.getChildAt(0).setAlpha(0f);
				for(int i=1; i<childCount-1; i++){
					rightResizersPane.getChildAt(i).setAlpha(1f);
				}
				rightResizersPane.getChildAt(childCount-1).setAlpha(0f);
			}
			else{
				for(int i=0; i<childCount; i++){
					rightResizersPane.getChildAt(i).setAlpha(0f);
				}
			}
		}
		
		public ResizersPair addPair(int idx){
			
			Resizer left = new Resizer(getContext());
			Resizer right = new Resizer(getContext());
			
//			leftResizersPane.addView(left, idx);
			rightResizersPane.addView(right, idx);
			
			refresh();
			
			return new ResizersPair(left, right);
		}
		
		public void removePair(int idx){
//			leftResizersPane.removeViewAt(idx);
			rightResizersPane.removeViewAt(idx);
			refresh();
		}
		
		public void removeAllPairs(){
			rightResizersPane.removeAllViews();
			ResizersPair firstPair = resController.addPair();
			firstPair.rightResizer.setMargin((Resizer.HEIGHT>>1)*-1);
			firstPair.rightResizer.setAlpha(0f);
		}
		
		public void hideAllPairs(){
			for(int i=0; i<resController.getResCount(); i++){
				((ImageView)resController.rightResizersPane.getChildAt(i)).animate()
																		.alpha(0f)
																		.setDuration(duration)
																		.setListener(null);
			}
		}
		
		public void showAllPairs(){
			int resCounter = resController.getResCount();
			for(int i=1; i<resCounter-1; i++){
				((ImageView)resController.rightResizersPane.getChildAt(i)).animate()
																		.alpha(1f)
																		.setDuration(duration)
																		.setListener(null);
			}
			resController.getPair(0).rightResizer.setAlpha(0f);
			resController.getPair(resCounter-1).rightResizer.setAlpha(0f);
		}
		
		public void setPair(int idx){
			
		}
				
		public ResizersPair getPair(int idx){
			return new ResizersPair(null, (Resizer)rightResizersPane.getChildAt(idx));
		}
		
//		public LinearLayout getLeftPane(){
//			return leftResizersPane;
//		}
		
		public LinearLayout getRightPane(){
			return rightResizersPane;
		}
		
		public int getResCount(){
			return rightResizersPane.getChildCount();
		}
	}
	
	private class ResizersPair{		
		
//		private Resizer leftResizer;
		private Resizer rightResizer;
		
		public ResizersPair(Resizer left, Resizer right){
//			this.leftResizer = left;
			this.rightResizer = right;
		}
	}
	
	private class Resizer extends ImageView{

		public final static int HEIGHT = 50;
		
		
		public Resizer(Context context) {
			super(context);
//			this.setBackgroundColor(Color.YELLOW);
//			this.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow));
			this.setImageDrawable(getResources().getDrawable(R.drawable.arrow));
			this.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, HEIGHT));
		}
		
		public void setMargin(int margin){
			LayoutParams lParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, HEIGHT);
			lParams.setMargins(0, margin, 0, 0);
			
			this.setLayoutParams(lParams);
		}
		
		int startY = 0;
		@Override
		public boolean onTouchEvent(MotionEvent event){ 
			
			int idx = ((LinearLayout)this.getParent()).indexOfChild(this);
			
//			Log.w("IDXX", idx+"" );
			
			if(idx == 0 || idx == resController.rightResizersPane.getChildCount()-1){
//				Log.w("IDXX", "RETURN!!!" );
				return false;
			}
//			Log.w("IDXX", "NOT RETURN!!!" );
			
			int action = MotionEventCompat.getActionMasked(event);
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
			setBackgroundResource(R.drawable.shadow);
			this.setLayoutParams(new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.FILL_PARENT, 1f));
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
//				Log.w("0","0");
				// Вычислим высоту нового блока		
				newHeight = this.getHeight()/(childCount+1);
				color.setHeight(newHeight);
				
				// Пройдёмся по всем блокам и изменим размеры
				for(int i=0; i<childCount; i++){	
					cBlock = (ColorBlock)this.getChildAt(i);
					cBlock.setHeight(cBlock.getActualHeight() - newHeight/(childCount));
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
			
			resController.removePair(idx+1);
			
		}
		
//		public void setColorBlock(ColorBlock color, int idx){
//			
//		}

		
	}
	
	/**
	 * 
	 * @author Vanchpuck
	 *
	 */
	private class BinPane extends LinearLayout{
		
		public BinPane(Context context, AttributeSet attrs) {
			super(context,attrs);
			this.setLayoutParams(new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.FILL_PARENT, 0.13f));
			this.setOrientation(HORIZONTAL);
			this.setGravity(Gravity.CENTER);
			setAlpha(0f);
			
			ImageView bin = new ImageView(getContext());
			bin.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			bin.setImageResource(R.drawable.content_discard);

			
			this.addView(bin);
			
			this.setOnDragListener(new OnDragListener() {
				
				@Override
				public boolean onDrag(View v, DragEvent event) {

					final int action = event.getAction();

					int idx = 0;
					
					switch(action) {
						case DragEvent.ACTION_DRAG_STARTED:
							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
//							Log.w("DEL_IDX_START", idx+"");
							return true;
						case DragEvent.ACTION_DRAG_ENTERED:
							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
							binPane.animate().scaleY(1.3f).setDuration(duration).setListener(null);
							binPane.animate().scaleX(1.3f).setDuration(duration).setListener(null);
//							binPane.setBackgroundColor(Color.CYAN);
							return true;
						case DragEvent.ACTION_DRAG_EXITED:
							binPane.animate().scaleY(1.0f).setDuration(duration).setListener(null);
							binPane.animate().scaleX(1.0f).setDuration(duration).setListener(null);
							return true;
						case DragEvent.ACTION_DROP:
							View block = (View)event.getLocalState();
							if(block == null){
//								Log.w("DEL_IDX", "Block is null");
							}
							idx = ((LinearLayout)block.getParent()).indexOfChild(block);
//							Log.w("DEL_IDX", idx+"");
							colorsPane.removeColor(idx);
							binPane.animate().alpha(0f).setDuration(duration).setListener(null);
							binPane.animate().scaleY(1.0f).setDuration(duration).setListener(null);
							binPane.animate().scaleX(1.0f).setDuration(duration).setListener(null);
							resController.showAllPairs();
							return true;
						case DragEvent.ACTION_DRAG_ENDED:
							/* REMOVE COLOR FILTER*/
							return true;
					}
					return true;
				}
			});
		}
		Paint p1 = new Paint();
		
		
	}
	

	public class ColorBlock extends View implements ColorSelectorDialog.OnColorChangedListener{
		
		private int height;
		
		private class BlockClickListener implements View.OnClickListener{
			private int initColor;
			protected BlockClickListener(int initColor){
				this.initColor = initColor;
			}
			@Override
			public void onClick(View v) {
//				new ColorPicker(getContext(), ColorBlock.this, initColor).show();
				new ColorSelectorDialog(getContext(), ColorBlock.this, initColor).show();
			}
			public void setInitColor(int color){
				initColor = color;
			}
		}
		
		private BlockClickListener cListener;
		
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
					 		resController.hideAllPairs();
					 		binPane.animate().alpha(1f).setDuration(duration).setListener(null);
					 		return true;
					 		
					 	case DragEvent.ACTION_DRAG_ENTERED :
					 		return true;
					 		
					 	case DragEvent.ACTION_DRAG_LOCATION :
//					 		Log.w("ColorDragDrop", "Y location: "+event.getY()+" border = "+(v.getHeight()/2));
					 		idx = ((LinearLayout)v.getParent()).indexOfChild(v);
							if(event.getY() > v.getHeight()/2){
								resController.getPair(idx+1).rightResizer.setAlpha(1f);
								resController.getPair(idx).rightResizer.setAlpha(0f);
							}
							else{
								resController.getPair(idx+1).rightResizer.setAlpha(0f);	
								resController.getPair(idx).rightResizer.setAlpha(1f);	
							}
							return true;
						
						case DragEvent.ACTION_DRAG_EXITED :
							resController.hideAllPairs();
//							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
//							resController.getPair(idx).rightResizer.setBackgroundColor(Color.YELLOW);
//							resController.getPair(idx+1).rightResizer.setBackgroundColor(Color.YELLOW);
							return true;
							
						case DragEvent.ACTION_DROP:
							idx = ((LinearLayout)v.getParent()).indexOfChild(v);
													
							// Вроде как получаем вьюшку, которую перетаскиваем
							View view = (View) event.getLocalState();
							ColorsPane owner = (ColorsPane) view.getParent();
							
							if(owner.indexOfChild(view) > idx){
								if(event.getY() > v.getHeight()/2){
									idx = ((LinearLayout)v.getParent()).indexOfChild(v)+1;
								}
								else{
									idx = ((LinearLayout)v.getParent()).indexOfChild(v);
								}
							}
							else if(owner.indexOfChild(view) < idx){
								if(event.getY() > v.getHeight()/2){
									idx = ((LinearLayout)v.getParent()).indexOfChild(v);
								}
								else{
									idx = ((LinearLayout)v.getParent()).indexOfChild(v)-1;
								}
							}
							
							
							
//							resController.getPair(idx+1).rightResizer.setBackgroundColor(Color.YELLOW);
//							resController.getPair(idx).rightResizer.setBackgroundColor(Color.YELLOW);
							owner.removeView(view);
							owner.addView(view, idx);
							
							owner.refreshResizers();
							binPane.animate().alpha(0f).setDuration(duration).setListener(null);
							resController.showAllPairs();							
							return true;
							
						case DragEvent.ACTION_DRAG_ENDED :
							return true;
							
						default:
//							Log.w("ColorDragDrop", "Uknown drag action");
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
					
//					Log.w("DRAG", "START");
					
					return true;
				}
			});
			
			cListener = new BlockClickListener(getColor());
			this.setOnClickListener(cListener);
		}
		
		public int getPosition(){
			return ((LinearLayout)this.getParent()).indexOfChild(this);
		}
		
		public int getColor(){
			return ((ColorDrawable)this.getBackground()).getColor();
		}
				
		@Override
		public void onSizeChanged(int w, int h, int oldw, int oldh){
						
			int index = ((LinearLayout)this.getParent()).indexOfChild(this);
			
//			Log.w("onSizeChanged IDX", index+"");
			
			int margin = h-Resizer.HEIGHT;

			ResizersPair pair = resController.getPair(index+1);
			
			pair.rightResizer.setMargin(margin);
		}
		
		public void setHeight(int height){
			this.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, height));
			this.height = height;
		}
		
		public int getActualHeight() {
			return height;
		}
		
		public BlockBasic getBlockBasic(){
			return new BlockBasic(this.getColor(), (float)this.getHeight()/(float)colorsPane.getHeight(), height);
		}

		@Override
		public void colorChanged(int color) {
			this.setBackgroundColor(color);
			cListener.setInitColor(color);
		}
		
	}
		
	public final static class BlockBasic implements Parcelable{

		private int color;
		private float height;
		private int actHeight;
		
		public BlockBasic(int color, float height, int actHeight){
			this.color = color;
			this.height = height;
			this.actHeight = actHeight;
		}
		
		public BlockBasic(Parcel parcel){
			this.color = parcel.readInt();
			this.height = parcel.readFloat();
			this.actHeight = parcel.readInt();
		}
		
		public final static Parcelable.Creator<BlockBasic> CREATOR = new Parcelable.Creator<BlockBasic>() {
		    @Override
			public BlockBasic createFromParcel(Parcel in) {
		        return new BlockBasic(in);
		    }
		
		    @Override
			public BlockBasic[] newArray(int size) {
		        return new BlockBasic[size];
		    }
		};
		
		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(color);
			dest.writeFloat(height);
			dest.writeInt(actHeight);
		}
		
		public int getColor(){
			return this.color;
		}
		
		public float getHeight(){
			return this.height;
		}
		
		public int getActHeight(){
			return this.actHeight;
		}
		
	}
	
	
//	private LinearLayout leftResizersPane;
//	private LinearLayout rightResizersPane;
	private ColorsPane colorsPane;
	private ResizersController resController;
	private BinPane binPane;
		
	public ColorCombinationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		setBackgroundColor(Color.RED);
		
		this.setOrientation(HORIZONTAL);
//		this.setBackgroundColor(0xFFF8F8FF);
		
		LinearLayout workspace = new LinearLayout(context);
		workspace.setOrientation(LinearLayout.HORIZONTAL);
		workspace.setLayoutParams(new LinearLayout.LayoutParams(0, android.view.ViewGroup.LayoutParams.FILL_PARENT, 1f));
		
		resController = new ResizersController();
		colorsPane = new ColorsPane(context, attrs);
		binPane = new BinPane(context, attrs);
		
//		colorsPane.setBackgroundResource(R.drawable.test);
//		colorsPane.setBackgroundColor(Color.GREEN);
		
		workspace.addView(colorsPane);
		workspace.addView(resController.getRightPane());
		this.addView(binPane);
		this.addView(workspace);
		
		
//		leftResizersPane = (ResizersPane) getChildAt(0);
//		rightResizersPane = (ResizersPane) getChildAt(2);
//		colorsPane = (ColorsPane) getChildAt(1);
		
//		leftResizersPane.addView(leftResizersPane.new Resizer(context));
//		leftResizersPane.setBackgroundColor(Color.RED);

	}
	
	
	public void addColor(int color){
		if(colorsPane.getChildCount() < 5){
			resController.addPair();
			colorsPane.addColor(new ColorBlock(getContext(), color));
		}
		else{
			Toast.makeText(getContext(), getResources().getString(R.string.max_color_count), Toast.LENGTH_LONG).show();
		}		
	}
	
	public ColorBlock getColorBlock(int index){
		return (ColorBlock)colorsPane.getChildAt(index);
	}
	
	public int getColorsCount(){
		return colorsPane.getChildCount();
	}
	
	public void removeColor(int idx){
		colorsPane.removeColor(idx);
		resController.removePair(idx+1);
	}
	
	public void removeAllColors(){
//		resController.leftResizersPane.removeAllViews();
		resController.removeAllPairs();
		colorsPane.removeAllViews();
	}
	
	public void setColor(){
		ColorBlock bl = (ColorBlock)colorsPane.getChildAt(1);
		colorsPane.removeViewAt(1);
		colorsPane.addColor(bl);
	}
	
	public BlockBasic[] getBlockBasics(){
		int count = colorsPane.getChildCount();
		
		BlockBasic[] basics = new BlockBasic[count];
		for(int i=0; i<count; i++){
			basics[i] = ((ColorBlock)colorsPane.getChildAt(i)).getBlockBasic();
		}
		return basics;
	}

	@Override
	public void colorChanged(int color) {
		this.addColor(color);
	}
	

}
