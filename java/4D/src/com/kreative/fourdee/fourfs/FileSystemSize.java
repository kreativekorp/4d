package com.kreative.fourdee.fourfs;

public enum FileSystemSize {
	KB256(0x4),
	KB512(0x8),
	KB768(0xC),
	MB1(0x10),
	MB2(0x20),
	MB4(0x40),
	MB8(0x80),
	MB16(0x100),
	MB32(0x200),
	MB64(0x400),
	MB128(0x800),
	MB256(0x1000),
	MB512(0x2000),
	MB768(0x3000),
	GB1(0x4000),
	GB2(0x8000),
	GB3(0xC000);
	
	private final int clusters;
	
	private FileSystemSize(int clusters) {
		this.clusters = clusters;
	}
	
	public int getByteLength() {
		return this.clusters << 16;
	}
	
	public int getClusterLength() {
		return this.clusters;
	}
	
	@Override
	public String toString() {
		long length = (long)this.clusters << 16L;
		if (length >= (1L << 30L)) return (length >> 30L) + " GiB";
		if (length >= (1L << 20L)) return (length >> 20L) + " MiB";
		if (length >= (1L << 10L)) return (length >> 10L) + " KiB";
		return length + " B";
	}
	
	public static FileSystemSize forByteLength(int length) {
		for (FileSystemSize value : values()) {
			if (value.getByteLength() == length) return value;
		}
		return null;
	}
	
	public static FileSystemSize forClusterLength(int length) {
		for (FileSystemSize value : values()) {
			if (value.getClusterLength() == length) return value;
		}
		return null;
	}
	
	public static FileSystemSize forString(String s) {
		for (FileSystemSize value : values()) {
			if (value.name().equalsIgnoreCase(s)) return value;
			if (value.toString().equalsIgnoreCase(s)) return value;
			if (value.toString().replaceAll("([KMG])iB", "$1B").equalsIgnoreCase(s)) return value;
		}
		return null;
	}
}