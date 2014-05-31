package com.jonnygold.colors;

import java.util.ArrayList;
import java.util.Collection;

import com.jonnygold.quantizer.IsHistogram;
import com.jonnygold.quantizer.IsHistogram.IsBar;

public class HistogramConverter implements IsHistogramConverter {

	@Override
	public Collection<BlockBasic> convert(IsHistogram histogram) {
		Collection<BlockBasic> blocks = new ArrayList<BlockBasic>(histogram.size());
		
		int totalCount = histogram.getTotalCount();
		for(IsBar bar : histogram.getBars()) {
			blocks.add(new BlockBasic(bar.getColor().getIntColor(), 1f/(float)totalCount, 0));
		}
		
		return blocks;
	}

}
