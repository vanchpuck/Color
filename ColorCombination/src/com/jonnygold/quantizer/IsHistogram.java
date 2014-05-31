package com.jonnygold.quantizer;

import java.util.Collection;

public interface IsHistogram {
	
	public static interface IsBar {
		
		public RGBColor getColor();
		
		public int getCount();
	}
	
	public Collection<IsBar> getBars();
	
	public IsBar getBar(RGBColor color);
	
	public Collection<RGBColor> getColors();
	
	public int getCount(RGBColor color);
	
	public boolean containsColor(RGBColor color);
	
	public int size();
	
}
