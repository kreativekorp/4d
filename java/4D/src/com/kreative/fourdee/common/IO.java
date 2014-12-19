package com.kreative.fourdee.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class IO {
	public static void readFully(InputStream in, byte[] data, int offset, int length) throws IOException {
		while (length > 0) {
			int read = in.read(data, offset, length);
			if (read < 0) break;
			offset += read;
			length -= read;
		}
	}
	
	public static String readCString(DataInput in) throws IOException {
		StringBuffer s = new StringBuffer();
		while (true) {
			int b = in.readUnsignedByte();
			if (b <= 0) break;
			if (b >= 32) s.append((char)b);
		}
		return s.toString();
	}
	
	public static void writeCString(DataOutput out, String s) throws IOException {
		CharacterIterator it = new StringCharacterIterator(s);
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch >= 32 && ch < 256) out.writeByte(ch);
		}
		out.writeByte(0);
	}
	
	public static String readFixedString(DataInput in, int length) throws IOException {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int b = in.readUnsignedByte();
			if (b >= 32) s.append((char)b);
		}
		return s.toString();
	}
	
	public static void writeFixedString(DataOutput out, String s, int length) throws IOException {
		CharacterIterator it = new StringCharacterIterator(s);
		int i = 0; char ch = it.first();
		while (i < length && ch != CharacterIterator.DONE) { 
			if (ch >= 32 && ch < 256) {
				out.writeByte(ch);
				i++;
			}
			ch = it.next();
		}
		while (i < length) {
			out.writeByte(0);
			i++;
		}
	}
	
	public static void writeEmptyBlocks(DataOutput out, int blockSize, int count) throws IOException {
		byte[] block = new byte[blockSize];
		for (int i = 0; i < count; i++) {
			out.write(block);
		}
	}
}