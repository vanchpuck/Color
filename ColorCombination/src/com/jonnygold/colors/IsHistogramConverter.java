package com.jonnygold.colors;

import java.util.Collection;

import com.jonnygold.quantizer.IsHistogram;

public interface IsHistogramConverter {

	public Collection<BlockBasic> convert(IsHistogram palete);
	
}
