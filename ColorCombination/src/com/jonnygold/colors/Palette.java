package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Palette implements IsPalette {

	private Collection<BlockBasic> colors;
	
	public Palette(Collection<BlockBasic> colors, int bound) {		
		if(colors == null){
			throw new IllegalArgumentException("Colors collections is not initialized");
		}
		
		this.colors = new ArrayList<BlockBasic>(colors);
		
		if(colors.size() > bound){
			int counter = 0;
			for(Iterator<BlockBasic> iter = colors.iterator(); iter.hasNext(); ){
				if(counter == bound){
					break;
				}
				this.colors.add(iter.next());
			}
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
