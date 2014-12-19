package com.kreative.fourdee.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import com.kreative.fourdee.common.IO;

public class DXDiceLibrary extends DXLibrary<DXDice> {
	public void readFile(File file) throws IOException {
		if (file.isDirectory()) {
			for (File subfile : file.listFiles()) {
				String name = subfile.getName();
				if (!name.startsWith(".")) {
					if (name.toLowerCase().endsWith(".die")) {
						name = name.substring(0, name.length() - 4);
						readFile(name, subfile);
					}
				}
			}
		} else {
			String name = file.getName();
			if (name.toLowerCase().endsWith(".die")) {
				name = name.substring(0, name.length() - 4);
			}
			readFile(name, file);
		}
	}
	
	private void readFile(String name, File file) throws IOException {
		RandomAccessFile in = new RandomAccessFile(file, "r");
		DXDice dice = DXDice.readDIE(name, in);
		add(dice);
	}
	
	public void writeDirectory(File dir) throws IOException {
		if (!dir.exists()) dir.mkdirs();
		if (!dir.isDirectory()) throw new IOException("Must be a directory.");
		for (DXDice dice : this) {
			if (dice.getName().startsWith("~~DATA~~")) continue;
			RandomAccessFile out = new RandomAccessFile(new File(dir, dice.getName() + ".DIE"), "rwd");
			dice.writeDIE(out);
			out.close();
		}
	}
	
	public void read4DX(DXImageLibrary lib, RandomAccessFile in, int count) throws IOException {
		long p = in.getFilePointer();
		for (int i = 0; i < count; i++) {
			DXDice dice = DXDice.read4DX(lib, in);
			add(dice);
			p += 0x10000L;
			in.seek(p);
		}
	}
	
	public int write4DXData(DXImageLibrary lib, RandomAccessFile out) throws IOException {
		long p = out.getFilePointer();
		for (DXDice dice : this) {
			dice.write4DX(lib, out);
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