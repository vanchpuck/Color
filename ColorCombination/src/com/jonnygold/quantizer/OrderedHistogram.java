package com.jonnygold.quantizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class OrderedHistogram implements IsHistogram{
	
	private IsHistogram histogram;
	
	private Comparator<IsBar> comparator;
	
	public OrderedHistogram(IsHistogram histogram, Comparator<IsBar> comparator) {
		this.histogram = histogram;
		this.comparator = comparator;
	}
	
	@Override
	public Collection<IsBar> getBars() {
		TreeSet<IsBar> bars = new TreeSet<IsBar>(comparator);
		bars.addAll(histogram.getBars());
		return bars;
	}

	@Override
	public IsBar getBar(RGBColor color) {
		return histogram.getBar(color);
	}
	
	@Override
	public Collection<RGBColor> getColors() {
		TreeSet<IsBar> bars = new TreeSet<IsBar>(comparator);
		bars.addAll(histogram.getBars());
		
		Collection<RGBColor> colors = new ArrayList<RGBColor>(bars.size());
		for(Iterator<IsBar> iter = bars.iterator(); iter.hasNext(); ){
			colors.add(iter.next().getColor());
		}
		return colors;
	}

	@Override
	public int getCount(RGBColor color) {
		return histogram.getCount(color);
	}

	@Override
	public boolean containsColor(RGBColor color) {
		return histogram.containsColor(color);
	}

	@Override
	public int size() {
		return histogram.size();
	}
}
