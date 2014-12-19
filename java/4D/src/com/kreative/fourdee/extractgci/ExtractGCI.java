package com.kreative.fourdee.extractgci;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import com.kreative.fourdee.common.Strings;

public class ExtractGCI {
	// Find all GCI-format images/videos inside a file.
	public static void main(String[] args) {
		for (String arg : args) {
			System.out.println("From " + arg + "...");
			try {
				File infile = new File(arg);
				RandomAccessFile inraf = new RandomAccessFile(infile, "r");
				File outdir = new File(infile.getParentFile(), infile.getName() + ".d");
				outdir.mkdirs();
				for (long offset = 0; offset < inraf.length(); offset += 0x0200L) {
					inraf.seek(offset);
					int width = inraf.readUnsignedShort();
					int height = inraf.readUnsignedShort();
					int magic = inraf.readUnsignedByte();
					int delay = inraf.readUnsignedByte();
					int frames = inraf.readUnsignedShort();
					if (width > 0 && width <= 640 && height > 0 && height <= 480 && magic == 0x10) {
						long length = (long)width * (long)height * 2L;
						if (delay == 0) {
							length += 6L;
						} else {
							length *= (long)frames;
							length += 8L;
						}
						if (offset + length <= inraf.length()) {
							inraf.seek(offset);
							String name = Strings.zeroPadHex(offset, 8) + ".GCI";
							System.out.println("\tExtracting " + name + "...");
							try {
								File outfile = new File(outdir, name);
								RandomAccessFile outraf = new RandomAccessFile(outfile, "rwd");
								byte[] buffer = new byte[65536];
								while (length > 0L) {
									int toRead = (length < (long)buffer.length) ? (int)length : buffer.length;
									int read = inraf.read(buffer, 0, toRead);
									if (read < 0) break;
									outraf.write(buffer, 0, read);
									length -= (long)read;
								}
								outraf.close();
							} catch (IOException ioe2) {
								System.err.println("Cannot write " + arg + ".d/" + name + ": " + ioe2.getMessage());
							}
						}
					}
				}
				inraf.close();
			} catch (IOException ioe) {
				System.err.println("Cannot read " + arg + ": " + ioe.getMessage());
			}
		}
	}
}