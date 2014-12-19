package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import com.kreative.fourdee.common.IO;
import com.kreative.fourdee.common.Numbers;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class DXImage extends DXNamedObject implements Comparable<DXImage> {
	private TreeMap<Integer,Image> images;
	
	public DXImage() {
		this.name = "";
		this.images = new TreeMap<Integer,Image>();
	}
	
	public DXImage(String name) {
		this.name = (name != null) ? name : "";
		this.images = new TreeMap<Integer,Image>();
	}
	
	public void read4DX(RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int map = in.readUnsignedShort();
		this.name = IO.readCString(in);
		this.images = new TreeMap<Integer,Image>();
		for (int m = 0x0010; m <= 0x8000; m <<= 1) {
			if ((map & m) != 0) {
				in.seek(origin + (long)m);
				byte[] data = new byte[m];
				in.readFully(data);
				Image image = ImageIO.read(data);
				if (image != null) {
					int size = Numbers.clusterOffsetToImageSize(m);
					this.images.put(size, image);
				}
			}
		}
	}
	
	public void write4DX(RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		int map = 0;
		for (int size : this.images.keySet()) {
			map |= Numbers.imageSizeToClusterOffset(size);
		}
		out.writeShort(map);
		IO.writeCString(out, this.name);
		for (Map.Entry<Integer,Image> e : this.images.entrySet()) {
			int m = Numbers.imageSizeToClusterOffset(e.getKey());
			if (m > 0) {
				byte[] b = ImageIO.write(e.getValue(), "gci");
				if (b != null) {
					out.seek(origin + (long)m);
					out.write(b);
				}
			}
		}
	}
	
	public Collection<Integer> getSizes() { return this.images.keySet(); }
	public boolean hasImage(int size) { return this.images.containsKey(size); }
	public Image getImage(int size) { return this.images.get(size); }
	public Collection<Image> getImages() { return this.images.values(); }
	public void setImage(int size, Image image) { this.images.put(size, image); }
	public void removeImage(int size) { this.images.remove(size); }
	public void clearImages() { this.images.clear(); }
	
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
	public int compareTo(DXImage other) {
		int cmp = this.sortClass() - other.sortClass();
		if (cmp == 0) cmp = this.name.compareToIgnoreCase(other.name);
		return cmp;
	}
}