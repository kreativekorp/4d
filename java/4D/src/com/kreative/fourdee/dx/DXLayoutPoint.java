package com.kreative.fourdee.dx;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import com.kreative.fourdee.common.Numbers;

public class DXLayoutPoint {
	private int x;
	private int y;
	private int size;
	
	public DXLayoutPoint() {
		this.x = 0;
		this.y = 0;
		this.size = 0;
	}
	
	public DXLayoutPoint(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void readDLO(DataInput in, int size) throws IOException {
		this.x = in.readUnsignedShort();
		this.y = in.readUnsignedShort();
		this.size = size;
	}
	
	public void read4DX(DataInput in) throws IOException {
		this.x = in.readUnsignedShort();
		this.y = in.readUnsignedShort();
		this.size = Numbers.clusterOffsetToImageSize(in.readUnsignedShort());
	}
	
	public void writeDLO(DataOutput out) throws IOException {
		out.writeShort(this.x);
		out.writeShort(this.y);
	}
	
	public void write4DX(DataOutput out) throws IOException {
		out.writeShort(this.x);
		out.writeShort(this.y);
		out.writeShort(Numbers.imageSizeToClusterOffset(this.size));
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getSize() { return this.size; }
}