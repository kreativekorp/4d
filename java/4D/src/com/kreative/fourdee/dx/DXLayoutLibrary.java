package com.kreative.fourdee.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import com.kreative.fourdee.common.IO;

public class DXLayoutLibrary extends DXLibrary<DXLayout> {
	public void readFile(File file) throws IOException {
		if (file.isDirectory()) {
			for (File subfile : file.listFiles()) {
				String name = subfile.getName();
				if (!name.startsWith(".")) {
					if (name.toLowerCase().endsWith(".dlo")) {
						name = name.substring(0, name.length() - 4);
						readFile(name, subfile);
					}
				}
			}
		} else {
			String name = file.getName();
			if (name.toLowerCase().endsWith(".dlo")) {
				name = name.substring(0, name.length() - 4);
			}
			readFile(name, file);
		}
	}
	
	private void readFile(String name, File file) throws IOException {
		RandomAccessFile in = new RandomAccessFile(file, "r");
		DXLayout layout = new DXLayout();
		layout.readDLO(name, in);
		add(layout);
	}
	
	public void writeDirectory(File dir) throws IOException {
		if (!dir.exists()) dir.mkdirs();
		if (!dir.isDirectory()) throw new IOException("Must be a directory.");
		for (DXLayout layout : this) {
			if (layout.getName().startsWith("~~DATA~~")) continue;
			RandomAccessFile out = new RandomAccessFile(new File(dir, layout.getName() + ".DLO"), "rwd");
			layout.writeDLO(out);
			out.close();
		}
	}
	
	public void read4DX(RandomAccessFile in, int count) throws IOException {
		long p = in.getFilePointer();
		for (int i = 0; i < count; i++) {
			DXLayout layout = new DXLayout();
			layout.read4DX(in);
			add(layout);
			p += 0x10000L;
			in.seek(p);
		}
	}
	
	public int write4DXData(RandomAccessFile out) throws IOException {
		long p = out.getFilePointer();
		for (DXLayout layout : this) {
			layout.write4DX(out);
			p += 0x10000L;
			out.seek(p);
		}
		if (out.length() < p) out.setLength(p);
		return this.size();
	}
	
	public int write4DXIndex(DXAliasTable aliases, RandomAccessFile out) throws IOException {
		long op = out.getFilePointer();
		int count = 0;
		for (int i = 0; i < this.size(); i++) {
			String name = this.get(i).getName();
			if (name.startsWith("~~DATA~~")) continue;
			if (aliases.containsKey(name)) name = aliases.get(name);
			out.writeShort(i);
			IO.writeFixedString(out, name, 30);
			count++;
		}
		long p = out.getFilePointer();
		long extents = (p - op) & 0xFFFFL;
		if (extents > 0L) p += (0x10000L - extents);
		if (out.length() < p) out.setLength(p);
		return count;
	}
}