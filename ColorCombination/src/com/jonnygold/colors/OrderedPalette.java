package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class OrderedPalette implements IsPalette {

	private static final class DefaultComparator implements Comparator<BlockBasic> {

		@Override
		public int compare(BlockBasic lhs, BlockBasic rhs) {
			return Double.compare(lhs.getHeight(), rhs.getHeight());
		}
	}
	
	
	private Collection<BlockBasic> colors;
	
	public OrderedPalette(Collection<BlockBasic> colors, int bound) {
		this(colors, new DefaultComparator(), bound);
	}
	
	public OrderedPalette(Collection<BlockBasic> colors, Comparator<BlockBasic> comparator, int bound) {		
		if(colors == null){
			throw new IllegalArgumentException("Colors collections is not initialized");
		}
		
		this.colors = new TreeSet<BlockBasic>(comparator);
		
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
