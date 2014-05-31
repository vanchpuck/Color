package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Collection;

import com.jonnygold.quantizer.IsHistogram;
import com.jonnygold.quantizer.IsHistogram.IsBar;

public class BoundedHistogramConverter implements IsHistogramConverter {

	private final int mBound;
	
	public BoundedHistogramConverter(int bound) {
		if(bound <= 0) {
			throw new IllegalArgumentException("Bound should be greater than 0.");
		}
		this.mBound = bound;
	}
	
	public final int getBound() {
		return mBound;
	}
	
	@Override
	public Collection<BlockBasic> convert(IsHistogram histogram) {
		int bound = 0;
		int totalCount = 0;
		if(histogram.size() > mBound){
			bound = mBound;
			totalCount = getTotalCount(histogram, bound);
		} else {
			bound = histogram.size();
			totalCount = histogram.getTotalCount();
		}
		
		Collection<BlockBasic> blocks = new ArrayList<BlockBasic>(bound);
		
		int counter = 0;
		for(IsBar bar : histogram.getBars()){
			if(counter >= bound) {
				break;
			}
			blocks.add(new BlockBasic(bar.getColor().getIntColor(), 1f/(float)totalCount, 0));
			counter++;
		}
		return blocks;
	}
	
	private int getTotalCount(IsHistogram histogram, int bound) {
		int totalCount = 0;
		
		int counter = 0;
		for(IsBar bar : histogram.getBars()){
			if(counter >= bound) {
				break;
			}
			totalCount += bar.getCount();
			counter++;
		}
		
		return totalCount;
	}

}
