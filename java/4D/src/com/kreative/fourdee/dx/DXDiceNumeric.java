package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.kreative.fourdee.common.Numbers;

public class DXDiceNumeric extends DXDice {
	private int baseValue;
	private int defaultSides;
	private int maximumSides;
	private String imageCode;
	private TreeMap<Integer,int[]> imageXCoords;
	
	public DXDiceNumeric(String name, int type) {
		super(name, type);
		this.baseValue = 0;
		this.defaultSides = 0;
		this.maximumSides = 0;
		this.imageCode = "NA";
		this.imageXCoords = new TreeMap<Integer,int[]>();
	}
	
	@Override
	protected void readDIEImpl(RandomAccessFile in, long origin) throws IOException {
		this.baseValue = in.readUnsignedShort();
		this.defaultSides = in.readUnsignedShort();
		this.maximumSides = in.readUnsignedShort();
		StringBuffer s = new StringBuffer();
		s.appendCodePoint(in.readUnsignedByte());
		s.appendCodePoint(in.readUnsignedByte());
		this.imageCode = s.toString();
		this.imageXCoords = new TreeMap<Integer,int[]>();
		int sizeCount = in.readUnsignedShort();
		for (int i = 0; i < sizeCount; i++) {
			int size = in.readUnsignedShort();
			int digitCount = in.readUnsignedShort();
			int[] xCoords = new int[digitCount];
			for (int j = 0; j < digitCount; j++) {
				xCoords[j] = in.readUnsignedShort();
			}
			this.imageXCoords.put(size, xCoords);
		}
	}
	
	@Override
	protected void read4DXImpl(DXImageLibrary lib, RandomAccessFile in, long origin) throws IOException {
		in.seek(origin + 0x0200);
		int type = in.readUnsignedShort();
		if (type != this.getType()) throw new IOException("Invalid magic values.");
		this.baseValue = in.readUnsignedShort();
		this.defaultSides = in.readUnsignedShort();
		this.maximumSides = in.readUnsignedShort();
		int imageIndex = in.readUnsignedShort();
		if (imageIndex < lib.size()) {
			String imageName = lib.getName(imageIndex);
			this.imageCode = (imageName != null) ? imageName : "NA";
		} else {
			this.imageCode = "NA";
		}
		this.imageXCoords = new TreeMap<Integer,int[]>();
		int map = in.readUnsignedShort();
		for (int m = 0x0010; m <= 0x8000; m <<= 1) {
			if ((map & m) != 0) {
				in.seek(origin + (long)m);
				int xCoordCount = in.readUnsignedShort();
				int[] xCoords = new int[xCoordCount];
				for (int i = 0, j = 64; i < xCoords.length; i++, j += 64) {
					in.seek(origin + (long)m + (long)j);
					xCoords[i] = in.readUnsignedShort();
				}
				int size = Numbers.clusterOffsetToImageSize(m);
				this.imageXCoords.put(size, xCoords);
			}
		}
	}
	
	@Override
	protected void writeDIEImpl(RandomAccessFile out, long origin) throws IOException {
		out.writeShort(this.baseValue);
		out.writeShort(this.defaultSides);
		out.writeShort(this.maximumSides);
		out.writeByte((this.imageCode.length() > 0) ? this.imageCode.charAt(0) : 0);
		out.writeByte((this.imageCode.length() > 1) ? this.imageCode.charAt(1) : 0);
		out.writeShort(this.imageXCoords.size());
		List<Integer> imageSizes = new ArrayList<Integer>(this.imageXCoords.keySet());
		for (int i = imageSizes.size() - 1; i >= 0; i--) {
			int imageSize = imageSizes.get(i);
			int[] xCoords = this.imageXCoords.get(imageSize);
			out.writeShort(imageSize);
			out.writeShort(xCoords.length);
			for (int x : xCoords) out.writeShort(x);
		}
	}
	
	@Override
	protected void write4DXImpl(DXImageLibrary lib, RandomAccessFile out, long origin) throws IOException {
		out.seek(origin + 0x0200);
		out.writeShort(this.getType());
		out.writeShort(this.baseValue);
		out.writeShort(this.defaultSides);
		out.writeShort(this.maximumSides);
		out.writeShort(lib.getIndex(this.imageCode));
		int map = 0;
		for (int size : this.imageXCoords.keySet()) {
			map |= Numbers.imageSizeToClusterOffset(size);
		}
		out.writeShort(map);
		for (Map.Entry<Integer,int[]> e : this.imageXCoords.entrySet()) {
			int m = Numbers.imageSizeToClusterOffset(e.getKey());
			if (m > 0) {
				int[] xCoords = e.getValue();
				out.seek(origin + (long)m);
				out.writeShort(xCoords.length);
				for (int i = 0, j = 64; i < xCoords.length; i++, j += 64) {
					out.seek(origin + (long)m + (long)j);
					for (int digit = 0; digit < 10; digit++) {
						out.writeShort(xCoords[i]);
						String imageCode = getImageCode(i, digit);
						out.writeShort(lib.getIndex(imageCode));
					}
				}
			}
		}
	}
	
	@Override public int getBaseValue() { return this.baseValue; }
	public void setBaseValue(int baseValue) { this.baseValue = baseValue; }
	@Override public int getDefaultSides() { return this.defaultSides; }
	public void setDefaultSides(int defaultSides) { this.defaultSides = defaultSides; }
	@Override public int getMaximumSides() { return this.maximumSides; }
	public void setMaximumSides(int maximumSides) { this.maximumSides = maximumSides; }
	
	public String getImageCode() {
		return this.imageCode;
	}
	public String getImageCode(int xCoordIndex, int digit) {
		return this.imageCode + trinv(xCoordIndex) + "D" + digit;
	}
	public void setImageCode(String imageCode) {
		this.imageCode = (imageCode != null) ? imageCode : "NA";
	}
	
	public boolean hasXCoords(int size) { return this.imageXCoords.containsKey(size); }
	public int[] getXCoords(int size) { return this.imageXCoords.get(size); }
	public void setXCoords(int size, int[] xCoords) { this.imageXCoords.put(size, xCoords); }
	public void removeXCoords(int size) { this.imageXCoords.remove(size); }
	public void clearXCoords() { this.imageXCoords.clear(); }
	
	private static int trinv(int index) {
		int t = 1, i = 1;
		while (true) {
			if (index < t) return i;
			t += ++i;
		}
	}
}