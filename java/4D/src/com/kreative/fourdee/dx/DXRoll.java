package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import com.kreative.fourdee.common.IO;

public class DXRoll extends DXNamedObject implements List<DXRollDice>, Comparable<DXRoll> {
	public static final int ORDER_TYPE_IN_ORDER = 0;
	public static final int ORDER_TYPE_REVERSED = 1;
	public static final int ORDER_TYPE_RANDOMIZE = 2;
	
	private int orderType;
	private List<DXRollDice> dice;
	
	public DXRoll() {
		this.name = "";
		this.orderType = 0;
		this.dice = new ArrayList<DXRollDice>();
	}
	
	public DXRoll(String name) {
		this.name = (name != null) ? name : "";
		this.orderType = 0;
		this.dice = new ArrayList<DXRollDice>();
	}
	
	public void readROL(DXDiceLibrary lib, String name, RandomAccessFile in) throws IOException {
		this.name = (name != null) ? name : "";
		this.orderType = in.readUnsignedShort();
		int count = in.readUnsignedShort();
		this.dice = new ArrayList<DXRollDice>(count);
		for (int i = 0; i < count; i++) {
			DXRollDice rd = new DXRollDice();
			rd.readROL(lib, in);
			this.dice.add(rd);
		}
	}
	
	public void read4DX(DXDiceLibrary lib, RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int count = in.readUnsignedShort();
		this.dice = new ArrayList<DXRollDice>(count);
		this.orderType = in.readUnsignedShort();
		this.name = IO.readCString(in);
		in.seek(origin + 0x0200L);
		for (int i = 0; i < count; i++) {
			DXRollDice rd = new DXRollDice();
			rd.read4DX(lib, in);
			this.dice.add(rd);
		}
	}
	
	public void read4DZ(DXDiceLibrary lib, RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int count = in.readUnsignedShort();
		this.dice = new ArrayList<DXRollDice>(count);
		this.orderType = in.readUnsignedShort();
		this.name = "~~SAVE~~";
		long p = origin;
		for (int i = 0; i < count; i++) {
			p += 0x0200L;
			in.seek(p);
			DXRollDice rd = new DXRollDice();
			rd.read4DX(lib, in);
			this.dice.add(rd);
		}
	}
	
	public void writeROL(DXDiceLibrary lib, RandomAccessFile out) throws IOException {
		out.writeShort(this.orderType);
		out.writeShort(this.dice.size());
		for (DXRollDice rd : this.dice) {
			rd.writeROL(lib, out);
		}
	}
	
	public void write4DX(DXDiceLibrary lib, RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		out.writeShort(this.dice.size());
		out.writeShort(this.orderType);
		IO.writeCString(out, this.name);
		out.seek(origin + 0x0200L);
		for (DXRollDice rd : this.dice) {
			rd.write4DX(lib, out);
		}
	}
	
	private static final Random RANDOM = new Random();
	public void write4DZ(DXDiceLibrary lib, RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		out.writeShort(this.dice.size());
		out.writeShort(this.orderType);
		out.writeLong(0x7E7E534156457E7EL); // ~~SAVE~~
		out.writeShort(0); // current layout
		out.writeShort(RANDOM.nextInt(0x10000)); // PRNG seed
		long p = origin;
		for (DXRollDice rd : this.dice) {
			p += 0x0200L;
			out.seek(p);
			rd.write4DX(lib, out);
		}
		long end = origin + 0x10000L;
		if (out.length() < end) out.setLength(end);
	}
	
	public int getOrderType() { return this.orderType; }
	public void setOrderType(int orderType) { this.orderType = orderType; }
	
	@Override public boolean add(DXRollDice e) { return this.dice.add(e); }
	@Override public void add(int index, DXRollDice element) { this.dice.add(index, element); }
	@Override public boolean addAll(Collection<? extends DXRollDice> c) { return this.dice.addAll(c); }
	@Override public boolean addAll(int index, Collection<? extends DXRollDice> c) { return this.dice.addAll(index, c); }
	@Override public void clear() { this.dice.clear(); }
	@Override public boolean contains(Object o) { return this.dice.contains(o); }
	@Override public boolean containsAll(Collection<?> c) { return this.dice.containsAll(c); }
	@Override public DXRollDice get(int index) { return this.dice.get(index); }
	@Override public int indexOf(Object o) { return this.dice.indexOf(o); }
	@Override public boolean isEmpty() { return this.dice.isEmpty(); }
	@Override public Iterator<DXRollDice> iterator() { return this.dice.iterator(); }
	@Override public int lastIndexOf(Object o) { return this.dice.lastIndexOf(o); }
	@Override public ListIterator<DXRollDice> listIterator() { return this.dice.listIterator(); }
	@Override public ListIterator<DXRollDice> listIterator(int index) { return this.dice.listIterator(index); }
	@Override public boolean remove(Object o) { return this.dice.remove(o); }
	@Override public DXRollDice remove(int index) { return this.dice.remove(index); }
	@Override public boolean removeAll(Collection<?> c) { return this.dice.removeAll(c); }
	@Override public boolean retainAll(Collection<?> c) { return this.dice.retainAll(c); }
	@Override public DXRollDice set(int index, DXRollDice element) { return this.dice.set(index, element); }
	@Override public int size() { return this.dice.size(); }
	@Override public List<DXRollDice> subList(int fromIndex, int toIndex) { return this.dice.subList(fromIndex, toIndex); }
	@Override public Object[] toArray() { return this.dice.toArray(); }
	@Override public <T> T[] toArray(T[] a) { return this.dice.toArray(a); }
	
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
	public int compareTo(DXRoll other) {
		int cmp = this.sortClass() - other.sortClass();
		if (cmp == 0) cmp = this.name.compareToIgnoreCase(other.name);
		return cmp;
	}
}