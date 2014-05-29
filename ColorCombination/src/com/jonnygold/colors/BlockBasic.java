package com.jonnygold.colors;

import android.os.Parcel;
import android.os.Parcelable;

public final class BlockBasic implements Parcelable{

	private final int color;
	private final float height;
	private final int actHeight;
	
	public BlockBasic(int color, float height, int actHeight) {
		this.color = color;
		this.height = height;
		this.actHeight = actHeight;
	}
	
	public BlockBasic(Parcel parcel){
		this.color = parcel.readInt();
		this.height = parcel.readFloat();
		this.actHeight = parcel.readInt();
	}
	
	public final static Parcelable.Creator<BlockBasic> CREATOR = new Parcelable.Creator<BlockBasic>() {
	    @Override
		public BlockBasic createFromParcel(Parcel in) {
	        return new BlockBasic(in);
	    }
	
	    @Override
		public BlockBasic[] newArray(int size) {
	        return new BlockBasic[size];
	    }
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(color);
		dest.writeFloat(height);
		dest.writeInt(actHeight);
	}
	
	public int getColor(){
		return this.color;
	}
	
	public float getHeight(){
		return this.height;
	}
	
	public int getActHeight(){
		return this.actHeight;
	}
	
}
