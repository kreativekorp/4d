package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DXDiceSimple extends DXDice implements List<String> {
	private int baseValue;
	private int defaultSides;
	private List<String> imageCodes;
	
	public DXDiceSimple(String name, int type) {
		super(name, type);
		this.baseValue = 0;
		this.defaultSides = 0;
		this.imageCodes = new ArrayList<String>();
	}
	
	@Override
	protected void readDIEImpl(RandomAccessFile in, long origin) throws IOException {
		this.baseValue = in.readUnsignedShort();
		this.defaultSides = in.readUnsignedShort();
		int maximumSides = in.readUnsignedShort();
		this.imageCodes = new ArrayList<String>(maximumSides);
		for (int i = 0; i < maximumSides; i++) {
			StringBuffer s = new StringBuffer();
			s.appendCodePoint(in.readUnsignedByte());
			s.appendCodePoint(in.readUnsignedByte());
			this.imageCodes.add(s.toString());
		}
	}
	
	@Override
	protected void read4DXImpl(DXImageLibrary lib, RandomAccessFile in, long origin) throws IOException {
		in.seek(origin + 0x0200);
		int type = in.readUnsignedShort();
		if (type != this.getType()) throw new IOException("Invalid magic values.");
		this.baseValue = in.readUnsignedShort();
		this.defaultSides = in.readUnsignedShort();
		int maximumSides = in.readUnsignedShort();
		this.imageCodes = new ArrayList<String>(maximumSides);
		in.seek(origin + 0x0400);
		for (int i = 0; i < maximumSides; i++) {
			int imageIndex = in.readUnsignedShort();
			if (imageIndex >= lib.size()) continue;
			String imageName = lib.getName(imageIndex);
			if (imageName == null) continue;
			this.imageCodes.add(imageName);
		}
	}
	
	@Override
	protected void writeDIEImpl(RandomAccessFile out, long origin) throws IOException {
		out.writeShort(this.baseValue);
		out.writeShort(this.defaultSides);
		out.writeShort(this.imageCodes.size());
		for (String imageCode : imageCodes) {
			out.writeByte((imageCode.length() > 0) ? imageCode.charAt(0) : 0);
			out.writeByte((imageCode.length() > 1) ? imageCode.charAt(1) : 0);
		}
	}
	
	@Override
	protected void write4DXImpl(DXImageLibrary lib, RandomAccessFile out, long origin) throws IOException {
		out.seek(origin + 0x0200);
		out.writeShort(this.getType());
		out.writeShort(this.baseValue);
		out.writeShort(this.defaultSides);
		out.writeShort(this.imageCodes.size());
		out.seek(origin + 0x0400);
		for (String imageCode : this.imageCodes) {
			out.writeShort(lib.getIndex(imageCode));
		}
	}
	
	@Override public int getBaseValue() { return this.baseValue; }
	public void setBaseValue(int baseValue) { this.baseValue = baseValue; }
	@Override public int getDefaultSides() { return this.defaultSides; }
	public void setDefaultSides(int defaultSides) { this.defaultSides = defaultSides; }
	@Override public int getMaximumSides() { return this.imageCodes.size(); }
	
	@Override public boolean add(String e) { return this.imageCodes.add(e); }
	@Override public void add(int index, String element) { this.imageCodes.add(index, element); }
	@Override public boolean addAll(Collection<? extends String> c) { return this.imageCodes.addAll(c); }
	@Override public boolean addAll(int index, Collection<? extends String> c) { return this.imageCodes.addAll(index, c); }
	@Override public void clear() { this.imageCodes.clear(); }
	@Override public boolean contains(Object o) { return this.imageCodes.contains(o); }
	@Override public boolean containsAll(Collection<?> c) { return this.imageCodes.containsAll(c); }
	@Override public String get(int index) { return this.imageCodes.get(index); }
	@Override public int indexOf(Object o) { return this.imageCodes.indexOf(o); }
	@Override public boolean isEmpty() { return this.imageCodes.isEmpty(); }
	@Override public Iterator<String> iterator() { return this.imageCodes.iterator(); }
	@Override public int lastIndexOf(Object o) { return this.imageCodes.lastIndexOf(o); }
	@Override public ListIterator<String> listIterator() { return this.imageCodes.listIterator(); }
	@Override public ListIterator<String> listIterator(int index) { return this.imageCodes.listIterator(index); }
	@Override public boolean remove(Object o) { return this.imageCodes.remove(o); }
	@Override public String remove(int index) { return this.imageCodes.remove(index); }
	@Override public boolean removeAll(Collection<?> c) { return this.imageCodes.removeAll(c); }
	@Override public boolean retainAll(Collection<?> c) { return this.imageCodes.retainAll(c); }
	@Override public String set(int index, String element) { return this.imageCodes.set(index, element); }
	@Override public int size() { return this.imageCodes.size(); }
	@Override public List<String> subList(int fromIndex, int toIndex) { return this.imageCodes.subList(fromIndex, toIndex); }
	@Override public Object[] toArray() { return this.imageCodes.toArray(); }
	@Override public <T> T[] toArray(T[] a) { return this.imageCodes.toArray(a); }
}