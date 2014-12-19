package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import com.kreative.fourdee.common.IO;

public class DXLayout extends DXNamedObject implements Comparable<DXLayout> {
	private DXLayoutPoint[][] points;
	
	public DXLayout() {
		this.name = "";
		this.points = new DXLayoutPoint[1][];
		this.points[0] = new DXLayoutPoint[0];
	}
	
	public DXLayout(String name) {
		this.name = (name != null) ? name : "";
		this.points = new DXLayoutPoint[1][];
		this.points[0] = new DXLayoutPoint[0];
	}
	
	public DXLayout(String name, int max) {
		this.name = (name != null) ? name : "";
		this.points = new DXLayoutPoint[max + 1][];
		for (int i = 0; i <= max; i++) {
			this.points[i] = new DXLayoutPoint[i];
		}
	}
	
	public void readDLO(String name, RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int max = in.readUnsignedShort();
		int[] offsets = new int[max];
		for (int i = 0; i < max; i++) {
			offsets[i] = in.readUnsignedShort();
		}
		this.name = (name != null) ? name : "";
		this.points = new DXLayoutPoint[max + 1][];
		this.points[0] = new DXLayoutPoint[0];
		for (int i = 1; i <= max; i++) {
			in.seek(origin + (long)offsets[i - 1]);
			int imageSize = in.readUnsignedShort();
			int count = in.readUnsignedShort();
			this.points[i] = new DXLayoutPoint[count];
			for (int j = 0; j < count; j++) {
				DXLayoutPoint point = new DXLayoutPoint();
				point.readDLO(in, imageSize);
				this.points[i][j] = point;
			}
		}
	}
	
	public void read4DX(RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int max = in.readUnsignedShort();
		this.name = IO.readCString(in);
		this.points = new DXLayoutPoint[max + 1][];
		this.points[0] = new DXLayoutPoint[0];
		for (int i = 1; i <= max; i++) {
			in.seek(origin + (long)i * 512L);
			int count = in.readUnsignedShort();
			this.points[i] = new DXLayoutPoint[count];
			for (int j = 0; j < count; j++) {
				DXLayoutPoint point = new DXLayoutPoint();
				point.read4DX(in);
				this.points[i][j] = point;
			}
		}
	}
	
	public void writeDLO(RandomAccessFile out) throws IOException {
		int max = this.points.length - 1;
		out.writeShort(max);
		int offset = 2 + (max * 2);
		for (int i = 1; i <= max; i++) {
			out.writeShort(offset);
			offset += 4 + (this.points[i].length * 4);
		}
		for (int i = 1; i <= max; i++) {
			out.writeShort(this.points[i][0].getSize());
			out.writeShort(this.points[i].length);
			for (DXLayoutPoint point : this.points[i]) {
				point.writeDLO(out);
			}
		}
	}
	
	public void write4DX(RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		int max = this.points.length - 1;
		out.writeShort(max);
		IO.writeCString(out, this.name);
		for (int i = 1; i <= max; i++) {
			out.seek(origin + (long)i * 512L);
			out.writeShort(this.points[i].length);
			for (DXLayoutPoint point : this.points[i]) {
				point.write4DX(out);
			}
		}
	}
	
	public int getMaxCount() { return this.points.length - 1; }
	public int getCount(int count) { return this.points[count].length; }
	public DXLayoutPoint getPoint(int count, int index) { return this.points[count][index]; }
	public void setPoint(int count, int index, DXLayoutPoint point) { this.points[count][index] = point; }
	
	private int sortClass() {
		if (this.name.length() > 0) {
			switch (this.name.charAt(0)) {
				case '$': return -1;
				case '_': return 1;
				case '~': return 2;
			}
		}
		return 0;
	}
	
	@Override
	public int compareTo(DXLayout other) {
		int cmp = this.sortClass() - other.sortClass();
		if (cmp == 0) cmp = this.name.compareToIgnoreCase(other.name);
		return cmp;
	}
}