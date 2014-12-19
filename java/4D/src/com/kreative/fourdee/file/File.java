package com.kreative.fourdee.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import com.kreative.fourdee.common.IO;
import com.kreative.fourdee.common.Numbers;

public class File implements Comparable<File> {
	private int cluster;
	private String name;
	private boolean active;
	private byte[] data;
	private boolean changed;
	
	public File(String name, byte[] data) {
		this.cluster = 0;
		this.name = (name != null) ? name : "";
		this.active = true;
		this.data = (data != null) ? data : new byte[0];
		this.changed = false;
	}
	
	public File(java.io.File file) throws IOException {
		this.cluster = 0;
		this.name = file.getName();
		this.active = true;
		int length = Numbers.longToInt(file.length());
		this.data = new byte[length];
		if (length > 0) {
			FileInputStream in = new FileInputStream(file);
			IO.readFully(in, this.data, 0, length);
			in.close();
		}
		this.changed = false;
	}
	
	public File(RandomAccessFile raf) throws IOException {
		this.cluster = raf.readUnsignedShort();
		this.name = IO.readFixedString(raf, 24);
		this.active = (raf.readUnsignedShort() != 0);
		int length = raf.readInt();
		if (length < 0) length = 0;
		this.data = new byte[length];
		long filePointer = raf.getFilePointer();
		raf.seek((long)this.cluster << 16L);
		raf.readFully(this.data);
		raf.seek(filePointer);
		this.changed = false;
	}
	
	public void writeFile(java.io.File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		out.write(this.data);
		out.flush();
		out.close();
	}
	
	public void writeFileSystemHeader(RandomAccessFile raf) throws IOException {
		raf.writeShort(this.cluster);
		IO.writeFixedString(raf, this.name, 24);
		raf.writeShort(this.active ? 1 : 0);
		raf.writeInt(this.data.length);
	}
	
	public void writeFileSystemData(RandomAccessFile raf) throws IOException {
		raf.seek((long)this.cluster << 16L);
		raf.write(this.data);
		int extents = (this.data.length & 0xFFFF);
		if (extents > 0) raf.write(new byte[0x10000 - extents]);
	}
	
	public int getCluster() { return this.cluster; }
	public void setCluster(int cluster) {
		this.cluster = cluster & 0xFFFF;
		this.changed = true;
	}
	
	public String getName() { return this.name; }
	public void setName(String name) {
		this.name = (name != null) ? name : "";
		this.changed = true;
	}
	
	public boolean isActive() { return this.active; }
	public void setActive(boolean active) {
		this.active = active;
		this.changed = true;
	}
	
	public int getLength() { return this.data.length; }
	public void setLength(int length) {
		if (length < 0) length = 0;
		this.data = resizeBytes(this.data, length);
		this.changed = true;
	}
	
	public int getClusterLength() {
		return Numbers.intToClusters(this.data.length);
	}
	public void setClusterLength(int length) {
		length = Numbers.clustersToInt(length);
		this.data = resizeBytes(this.data, length);
		this.changed = true;
	}
	
	public byte[] getData() { return this.data; }
	public void setData(byte[] data) {
		this.data = (data != null) ? data : new byte[0];
		this.changed = true;
	}
	
	public boolean isChanged() { return this.changed; }
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	@Override
	public int compareTo(File other) {
		return this.name.compareToIgnoreCase(other.name);
	}
	
	private static byte[] resizeBytes(byte[] data, int length) {
		if (length <= 0) return new byte[0];
		byte[] newData = new byte[length];
		for (int i = 0; i < newData.length && i < data.length; i++) {
			newData[i] = data[i];
		}
		return newData;
	}
}