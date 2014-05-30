package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Palette implements IsPalette {

	private Collection<BlockBasic> colors;
	
	public Palette(Collection<BlockBasic> colors, int bound) {		
		if(colors == null){
			throw new IllegalArgumentException("Colors collection is not initialized.");
		}
		if(bound <= 0){
			throw new IllegalArgumentException("Illegal bound value (should be > 0).");
		}
		
		this.colors = new ArrayList<BlockBasic>(bound);
		
		if(colors.size() > bound){
			int counter = 0;
			for(Iterator<BlockBasic> iter = colors.iterator(); iter.hasNext(); ){
				if(counter == bound){
					break;
				}
				this.colors.add(iter.next());
			}
		}
		else {
			this.colors.addAll(colors);
		}
	}
	
	@Override
	public Collection<BlockBasic> getBlocks() {
		return new ArrayList<BlockBasic>(colors);
	}

	@Override
	public int size() {
		return colors.size();
	}
	
}
