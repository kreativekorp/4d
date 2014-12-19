package com.kreative.fourdee.dx;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import com.kreative.fourdee.common.Numbers;
import com.kreative.fourdee.common.Strings;

public class DXRollDice {
	private String dataFile;
	private int value;
	private int minimumValue;
	private int sides;
	
	public DXRollDice() {
		this.dataFile = "";
		this.value = 0;
		this.minimumValue = 0;
		this.sides = 0;
	}
	
	public DXRollDice(String dataFile, int value, int minimumValue, int sides) {
		this.dataFile = (dataFile != null) ? dataFile : "";
		this.value = value;
		this.minimumValue = minimumValue;
		this.sides = sides;
	}
	
	public void readROL(DXDiceLibrary lib, RandomAccessFile in) throws IOException {
		byte[] b = new byte[14];
		in.readFully(b);
		this.value = in.readUnsignedShort();
		this.minimumValue = in.readUnsignedShort();
		this.sides = in.readUnsignedShort();
		if (b[0] == 0 && b[1] == 0 && b[2] == 'D' && b[3] == 'A' && b[4] == 'T' && b[5] == 'A') {
			long localOrigin = in.getFilePointer();
			int dataLength = ((b[6] & 0xFF) << 8) | (b[7] & 0xFF);
			this.dataFile = "~~DATA~~" + Strings.zeroPad(lib.size(), 4);
			lib.add(DXDice.readDIE(this.dataFile, in));
			in.seek(localOrigin + (long)dataLength);
		} else {
			StringBuffer s = new StringBuffer();
			for (byte bb : b) {
				if ((bb & 0xFF) >= 32) {
					s.append((char)(bb & 0xFF));
				}
			}
			this.dataFile = Strings.stripExtension(s.toString());
		}
	}
	
	public void read4DX(DXDiceLibrary lib, RandomAccessFile in) throws IOException {
		int diceIndex = in.readUnsignedShort();
		if (diceIndex < lib.size()) {
			String diceName = lib.getName(diceIndex);
			if (diceName != null) {
				this.dataFile = diceName;
			} else {
				this.dataFile = "";
			}
		} else {
			this.dataFile = "";
		}
		/* int type = */ in.readUnsignedShort();
		this.value = in.readUnsignedShort();
		this.minimumValue = in.readUnsignedShort();
		this.sides = in.readUnsignedShort();
		/* int baseValue = */ in.readUnsignedShort();
		/* int defaultSides = */ in.readUnsignedShort();
		/* int maximumSides = */ in.readUnsignedShort();
	}
	
	public void writeROL(DXDiceLibrary lib, RandomAccessFile out) throws IOException {
		if (this.dataFile.startsWith("~~DATA~~")) {
			out.writeShort(0);
			out.writeByte('D');
			out.writeByte('A');
			out.writeByte('T');
			out.writeByte('A');
			long lengthMarker = out.getFilePointer();
			out.writeShort(-1);
			out.writeShort(0);
			out.writeShort(0);
			out.writeShort(0);
			out.writeShort(this.value);
			out.writeShort(this.minimumValue);
			out.writeShort(this.sides);
			long dataStart = out.getFilePointer();
			DXDice dice = lib.getObject(this.dataFile);
			if (dice != null) dice.writeDIE(out);
			long dataEnd = out.getFilePointer();
			int dataLength = Numbers.longToShort(dataEnd - dataStart);
			out.seek(lengthMarker);
			out.writeShort(dataLength);
			out.seek(dataStart + (long)dataLength);
		} else {
			byte[] b = new byte[14];
			CharacterIterator iter = new StringCharacterIterator(this.dataFile + ".DIE");
			int i = 0; char ch = iter.first();
			while (i < b.length && ch != CharacterIterator.DONE) {
				if (ch >= 32 && ch < 256) b[i++] = (byte)ch;
				ch = iter.next();
			}
			out.write(b);
			out.writeShort(this.value);
			out.writeShort(this.minimumValue);
			out.writeShort(this.sides);
		}
	}
	
	public void write4DX(DXDiceLibrary lib, RandomAccessFile out) throws IOException {
		DXDice dice = lib.getObject(this.dataFile);
		out.writeShort(lib.getIndex(this.dataFile));
		out.writeShort((dice != null) ? dice.getType() : 0);
		out.writeShort(this.value);
		out.writeShort(this.minimumValue);
		out.writeShort(this.sides);
		out.writeShort((dice != null) ? dice.getBaseValue() : 0);
		out.writeShort((dice != null) ? dice.getDefaultSides() : 0);
		out.writeShort((dice != null) ? dice.getMaximumSides() : 0);
	}
	
	public String getDataFile() { return this.dataFile; }
	public int getValue() { return this.value; }
	public int getMinimumValue() { return this.minimumValue; }
	public int getSides() { return this.sides; }
}