package com.kreative.fourdee.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class DXDatabase {
	private final DXImageLibrary ilib;
	private final DXDiceLibrary dlib;
	private final DXRollLibrary rlib;
	private final DXLayoutLibrary llib;
	private final DXAliasTable atab;
	
	public DXDatabase() {
		this.ilib = new DXImageLibrary();
		this.dlib = new DXDiceLibrary();
		this.rlib = new DXRollLibrary();
		this.llib = new DXLayoutLibrary();
		this.atab = new DXAliasTable();
	}
	
	public boolean readDirectory(File dir) throws IOException {
		if (!dir.isDirectory()) return false;
		ilib.readFile(dir); ilib.sort();
		dlib.readFile(dir); dlib.sort();
		rlib.readFile(dlib, dir); rlib.sort();
		llib.readFile(dir); llib.sort();
		File aliases = new File(dir, "ALIASES.TXT");
		if (aliases.exists()) atab.readTXT(aliases);
		return true;
	}
	
	public boolean writeDirectory(File dir, String imageFormat) throws IOException {
		if (!dir.exists()) dir.mkdirs();
		if (!dir.isDirectory()) return false;
		ilib.writeDirectory(dir, imageFormat);
		dlib.writeDirectory(dir);
		rlib.writeDirectory(dlib, dir);
		llib.writeDirectory(dir);
		atab.writeTXT(new File(dir, "ALIASES.TXT"));
		return true;
	}
	
	public void read4DX(RandomAccessFile in) throws IOException {
		ilib.clear(); dlib.clear(); rlib.clear(); llib.clear(); atab.clear();
		long origin = in.getFilePointer();
		/* int imageIndexCluster = */ in.readUnsignedShort();
		/* int imageIndexCount = */ in.readUnsignedShort();
		int imageDataCluster = in.readUnsignedShort();
		int imageDataCount = in.readUnsignedShort();
		/* int diceIndexCluster = */ in.readUnsignedShort();
		/* int diceIndexCount = */ in.readUnsignedShort();
		int diceDataCluster = in.readUnsignedShort();
		int diceDataCount = in.readUnsignedShort();
		/* int rollIndexCluster = */ in.readUnsignedShort();
		/* int rollIndexCount = */ in.readUnsignedShort();
		int rollDataCluster = in.readUnsignedShort();
		int rollDataCount = in.readUnsignedShort();
		/* int layoutIndexCluster = */ in.readUnsignedShort();
		/* int layoutIndexCount = */ in.readUnsignedShort();
		int layoutDataCluster = in.readUnsignedShort();
		int layoutDataCount = in.readUnsignedShort();
		/* int saveFileCluster = */ in.readUnsignedShort();
		/* int saveFileCount = */ in.readUnsignedShort();
		int aliasTableCluster = in.readUnsignedShort();
		int aliasTableCount = in.readUnsignedShort();
		if (in.readInt() != 0x64580EDB) throw new IOException("Invalid magic number.");
		if (in.readInt() != 0x64580EDB) throw new IOException("Invalid magic number.");
		in.seek(origin + ((long)imageDataCluster << 16L));
		ilib.read4DX(in, imageDataCount);
		in.seek(origin + ((long)diceDataCluster << 16L));
		dlib.read4DX(ilib, in, diceDataCount);
		in.seek(origin + ((long)rollDataCluster << 16L));
		rlib.read4DX(dlib, in, rollDataCount);
		in.seek(origin + ((long)layoutDataCluster << 16L));
		llib.read4DX(in, layoutDataCount);
		for (int i = 0; i < aliasTableCount; i++) {
			in.seek(origin + (((long)aliasTableCluster + (long)i) << 16L));
			atab.read4DX(in);
		}
	}
	
	public void write4DX(RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		
		int imageIndexCluster = 1;
		out.seek(origin + ((long)imageIndexCluster << 16L));
		int imageIndexCount = ilib.write4DXIndex(atab, out);
		
		int diceIndexCluster = 2;
		out.seek(origin + ((long)diceIndexCluster << 16L));
		int diceIndexCount = dlib.write4DXIndex(atab, out);
		
		int rollIndexCluster = 3;
		out.seek(origin + ((long)rollIndexCluster << 16L));
		int rollIndexCount = rlib.write4DXIndex(atab, out);
		
		int layoutIndexCluster = 4;
		out.seek(origin + ((long)layoutIndexCluster << 16L));
		int layoutIndexCount = llib.write4DXIndex(atab, out);
		
		int imageDataCluster = 5;
		out.seek(origin + ((long)imageDataCluster << 16L));
		int imageDataCount = ilib.write4DXData(out);
		
		int diceDataCluster = imageDataCluster + imageDataCount;
		out.seek(origin + ((long)diceDataCluster << 16L));
		int diceDataCount = dlib.write4DXData(ilib, out);
		
		int rollDataCluster = diceDataCluster + diceDataCount;
		out.seek(origin + ((long)rollDataCluster << 16L));
		int rollDataCount = rlib.write4DXData(dlib, out);
		
		int layoutDataCluster = rollDataCluster + rollDataCount;
		out.seek(origin + ((long)layoutDataCluster << 16L));
		int layoutDataCount = llib.write4DXData(out);
		
		int aliasTableCluster = layoutDataCluster + layoutDataCount;
		out.seek(origin + ((long)aliasTableCluster << 16L));
		atab.write4DX(out);
		
		int saveFileCluster = aliasTableCluster + 1;
		out.seek(origin + ((long)saveFileCluster << 16L));
		DXRoll roll = rlib.getObject("01D06");
		if (roll == null) roll = rlib.getObject(0);
		roll.write4DZ(dlib, out);
		
		long end = out.getFilePointer();
		long p = 1L; while (p < end) p <<= 1L;
		
		out.seek(origin);
		out.writeShort(imageIndexCluster);
		out.writeShort(imageIndexCount);
		out.writeShort(imageDataCluster);
		out.writeShort(imageDataCount);
		out.writeShort(diceIndexCluster);
		out.writeShort(diceIndexCount);
		out.writeShort(diceDataCluster);
		out.writeShort(diceDataCount);
		out.writeShort(rollIndexCluster);
		out.writeShort(rollIndexCount);
		out.writeShort(rollDataCluster);
		out.writeShort(rollDataCount);
		out.writeShort(layoutIndexCluster);
		out.writeShort(layoutIndexCount);
		out.writeShort(layoutDataCluster);
		out.writeShort(layoutDataCount);
		out.writeShort(saveFileCluster);
		out.writeShort(1);
		out.writeShort(aliasTableCluster);
		out.writeShort(1);
		out.writeInt(0x64580EDB);
		out.writeInt(0x64580EDB);
		out.seek(end);
		out.setLength(p);
		out.seek(p);
	}
	
	public DXImageLibrary images() { return this.ilib; }
	public DXDiceLibrary dice() { return this.dlib; }
	public DXRollLibrary rolls() { return this.rlib; }
	public DXLayoutLibrary layouts() { return this.llib; }
	public DXAliasTable aliases() { return this.atab; }
}