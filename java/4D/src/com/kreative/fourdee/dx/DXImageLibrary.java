package com.kreative.fourdee.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kreative.fourdee.common.IO;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class DXImageLibrary extends DXLibrary<DXImage> {
	private static final Pattern FILE_NAME_PATTERN = Pattern.compile("([0-9]{2})-([A-Za-z0-9]+)\\.([A-Za-z0-9]+)");
	
	public void readFile(File file) throws IOException {
		if (file.isDirectory()) {
			for (File subfile : file.listFiles()) {
				Matcher m = FILE_NAME_PATTERN.matcher(subfile.getName());
				if (m.matches()) readFile(m, subfile);
			}
		} else {
			Matcher m = FILE_NAME_PATTERN.matcher(file.getName());
			if (m.matches()) readFile(m, file);
			else throw new IOException("Invalid magic filename.");
		}
	}
	
	private void readFile(Matcher m, File file) throws IOException {
		int size = Integer.parseInt(m.group(1), 10);
		String name = m.group(2);
		Image image = ImageIO.read(file);
		if (image == null) throw new IOException("Invalid image file.");
		DXImage di = getObject(name);
		if (di == null) add(di = new DXImage(name));
		di.setImage(size, image);
	}
	
	public void writeDirectory(File dir, String format) throws IOException {
		if (!dir.exists()) dir.mkdirs();
		if (!dir.isDirectory()) throw new IOException("Must be a directory.");
		for (DXImage image : this) {
			if (image.getName().startsWith("~~DATA~~")) continue;
			for (int size : image.getSizes()) {
				File out = new File(dir, Strings.zeroPad(size, 2) + "-" + image.getName() + "." + format.toUpperCase());
				ImageIO.write(out, image.getImage(size), format);
			}
		}
	}
	
	public void read4DX(RandomAccessFile in, int count) throws IOException {
		long p = in.getFilePointer();
		for (int i = 0; i < count; i++) {
			DXImage image = new DXImage();
			image.read4DX(in);
			add(image);
			p += 0x10000L;
			in.seek(p);
		}
	}
	
	public int write4DXData(RandomAccessFile out) throws IOException {
		long p = out.getFilePointer();
		for (DXImage image : this) {
			image.write4DX(out);
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