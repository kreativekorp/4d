package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kreative.fourdee.common.IO;

public abstract class DXDice extends DXNamedObject implements Comparable<DXDice> {
	/* Ugly stuff here. */
	private static final Map<Integer,Constructor<? extends DXDice>> registry =
		new HashMap<Integer,Constructor<? extends DXDice>>();
	public static final void register(int type, Class<? extends DXDice> cls) {
		try { registry.put(type, cls.getConstructor(String.class, int.class)); }
		catch (Exception e) { e.printStackTrace(); }
	}
	static {
		register(0, DXDiceSimple.class);
		register(1, DXDiceSimple.class);
		register(2, DXDiceNumeric.class);
	}
	
	public static final int TYPE_FIXED = 0;
	public static final int TYPE_VARIABLE = 1;
	public static final int TYPE_NUMERIC = 2;
	
	private int type;
	
	protected DXDice(String name, int type) {
		this.name = (name != null) ? name : "";
		this.type = type;
	}
	
	public final static DXDice readDIE(String name, RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int type = in.readUnsignedShort();
		if (registry.containsKey(type)) {
			DXDice die;
			try { die = registry.get(type).newInstance(name, type); }
			catch (Exception e) { throw new IOException("Failed to instantiate DXDice subclass.", e); }
			die.readDIEImpl(in, origin);
			return die;
		} else {
			throw new IOException("Invalid magic value.");
		}
	}
	protected abstract void readDIEImpl(RandomAccessFile in, long origin) throws IOException;
	
	public final static DXDice read4DX(DXImageLibrary lib, RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int type = in.readUnsignedShort();
		String name = IO.readCString(in);
		if (registry.containsKey(type)) {
			DXDice die;
			try { die = registry.get(type).newInstance(name, type); }
			catch (Exception e) { throw new IOException("Failed to instantiate DXDice subclass.", e); }
			die.read4DXImpl(lib, in, origin);
			return die;
		} else {
			throw new IOException("Invalid magic value.");
		}
	}
	protected abstract void read4DXImpl(DXImageLibrary lib, RandomAccessFile in, long origin) throws IOException;
	
	public final void writeDIE(RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		out.writeShort(this.type);
		writeDIEImpl(out, origin);
	}
	protected abstract void writeDIEImpl(RandomAccessFile out, long origin) throws IOException;
	
	public final void write4DX(DXImageLibrary lib, RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		out.writeShort(this.type);
		IO.writeCString(out, this.name);
		write4DXImpl(lib, out, origin);
	}
	protected abstract void write4DXImpl(DXImageLibrary lib, RandomAccessFile out, long origin) throws IOException;
	
	public final int getType() { return this.type; }
	public abstract int getBaseValue();
	public abstract int getDefaultSides();
	public abstract int getMaximumSides();
	
	private final int majorSortClass() {
		if (this.name.length() > 0) {
			switch (this.name.charAt(0)) {
				case '$': return -1;
				case '_': return 1;
				case '~': return 2;
			}
		}
		return 0;
	}
	
	private static final List<String> SPECIAL_DICE = Arrays.asList(
		"$WESTERN", "$EASTERN",
		"$ANALOG", "$DIGITAL", "$PIMERIC", "$ROMAN",
		"$FUDGE1", "$FUDGE2", "$FUDGE3"
	);
	private final int minorSortClass() {
		int o = SPECIAL_DICE.indexOf(this.name);
		if (o >= 0) return o;
		return SPECIAL_DICE.size();
	}
	
	@Override
	public final int compareTo(DXDice other) {
		int cmp = this.majorSortClass() - other.majorSortClass();
		if (cmp == 0) cmp = this.minorSortClass() - other.minorSortClass();
		if (cmp == 0) cmp = this.name.compareToIgnoreCase(other.name);
		return cmp;
	}
}