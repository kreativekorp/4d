package com.kreative.fourdee.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Unmake4DX {
	public static void main(String[] args) {
		File outputFile = null;
		String imageFormat = "png";
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (arg.startsWith("-")) {
				if (arg.equals("--help")) {
					System.out.println("-o <path> : Set output file.");
					System.out.println("-f <str>  : Set image output format.");
				} else if (arg.equals("-o") && argi < args.length) {
					outputFile = new File(args[argi++]);
				} else if (arg.equals("-f") && argi < args.length) {
					imageFormat = args[argi++];
				} else {
					System.err.println("Invalid option: " + arg);
				}
			} else {
				File inputFile = new File(arg);
				if (outputFile == null) outputFile = new File(
					inputFile.getParentFile(),
					inputFile.getName() + ".d"
				);
				DXDatabase db = new DXDatabase();
				try {
					RandomAccessFile in = new RandomAccessFile(inputFile, "r");
					db.read4DX(in);
					in.close();
					try {
						boolean success = db.writeDirectory(outputFile, imageFormat);
						if (!success) throw new IOException("Not a directory.");
					} catch (IOException ioe2) {
						System.err.println("Error writing " + outputFile.getName() + ": " + ioe2.getMessage());
					}
				} catch (IOException ioe) {
					System.err.println("Error reading " + arg + ": " + ioe.getMessage());
				}
				db = null;
				System.gc();
				outputFile = null;
			}
		}
	}
}