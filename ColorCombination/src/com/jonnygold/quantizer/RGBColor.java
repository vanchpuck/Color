package com.jonnygold.quantizer;

import android.graphics.Color;

public final class RGBColor implements IsRGBColor {

	private final int red;
	
	private final int green;
	
	private final int blue;
	
	public RGBColor(int r, int g, int b){
		red = r;
		green = g;
		blue = b;
	}
	
	public int getRed(){
		return red;
	}
	
	public int getGreen(){
		return green;
	}

	public int getBlue(){
		return blue;
	}
	
	@Override
	public String toString() {
		return "RGBColor {R:"+getRed()+" G:"+getGreen()+" B:"+getBlue()+"}";
	}
	
	public int getIntColor() {
		return Color.rgb(getRed(), getGreen(), getBlue());
//		return ((255 & 0xFF) << 24) | //alpha
//	            ((getRed() & 0xFF) << 16) | //red
//	            ((getGreen() & 0xFF) << 8)  | //green
//	            ((getBlue() & 0xFF) << 0); //blue
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + getRed();
		result = 31 * result + getGreen();
		result = 31 * result + getBlue();		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){
			return false;
		}
		if(!(obj instanceof RGBColor)){
			return false;
		}
		RGBColor color = (RGBColor)obj;
		if(color.getRed() == getRed() && color.getGreen() == getGreen() && color.getBlue() == getBlue()){
			return true;
		}
		return false;
	}
	
}
