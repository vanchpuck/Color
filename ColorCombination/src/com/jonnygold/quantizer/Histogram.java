package com.jonnygold.quantizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Histogram implements IsHistogram {
	
	public static final class Bar implements IsBar {
		
		private final RGBColor color;
		
		private int count;
		
		public Bar(RGBColor color, int count) {
			this.color = color;
			this.count = count;
		}
		
		@Override
		public RGBColor getColor() {
			return color;
		}
		
		@Override
		public int getCount() {
			return count;
		}
		
		public void setCount(int count) {
			if(count <= 0){
				throw new IllegalArgumentException("Colors count should be greater than 0");
			}
			this.count = count;
		}
				
	}
	
	public static class Builder{
		
		private Map<RGBColor, Bar> builderData = new HashMap<RGBColor, Bar>();
		
		public Builder addColor(RGBColor color){
			return addColor(color, 1);
		}
		
		public Builder addColor(RGBColor color, int count){
			if(builderData.containsKey(color)) {
				builderData.get(color).setCount(builderData.get(color).getCount()+count);
			} else {
				builderData.put(color, new Bar(color, count));
			}
			return this;
		}
		
		public Histogram build(){
			return new Histogram(new HashMap<RGBColor, IsBar>(builderData));
		}
		
		public Histogram build(int bound) {
			Map<RGBColor, IsBar> data = new HashMap<RGBColor, IsBar>(builderData);
			
			RGBColor color = null;
			for(Iterator<RGBColor> iter = data.keySet().iterator(); iter.hasNext(); ){
				color = iter.next();
				if(data.get(color).getCount() <= bound){
					iter.remove();;
				}
			}
			return new Histogram(data);
		}
		
		public Histogram build(double bound) {
			Map<RGBColor, IsBar> data = new HashMap<RGBColor, IsBar>(builderData);
			
			int sum = 0;
			for(RGBColor color : data.keySet()){
				sum+=data.get(color).getCount();
			}
			RGBColor color = null;			
			for(Iterator<RGBColor> iter = data.keySet().iterator(); iter.hasNext(); ){
				color = iter.next();
				if((double)data.get(color).getCount()/(double)sum <= bound){
					iter.remove();;
				}
			}
			return new Histogram(data);
		}
	}
	
	
	private final Map<RGBColor, IsBar> data;
	
	
	private Histogram(Map<RGBColor, IsBar> builderData){
		data = builderData;
	}
	
	@Override
	public Collection<RGBColor> getColors() {
		return data.keySet();
	}
	
	@Override
	public int getCount(RGBColor color) {
		return data.get(color).getCount();
	}

	@Override
	public boolean containsColor(RGBColor color) {
		return data.containsKey(color);
	}

	@Override
	public int size() {
		return data.size();
	}
	
	@Override
	public String toString() {
		return getColors().toString();
	}

	@Override
	public Collection<IsBar> getBars() {
		return new ArrayList<IsBar>(data.values());
	}

	@Override
	public IsBar getBar(RGBColor color) {
		return data.get(color);
	}

	@Override
	public int getTotalCount() {
		int totalCount = 0;
		for(IsBar bar : data.values()){
			totalCount += bar.getCount();
		}
		return totalCount;
	}
}
