package com.kreative.fourdee.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Make4DX {
	public static void main(String[] args) {
		File outputFile = null;
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (arg.startsWith("-")) {
				if (arg.equals("--help")) {
					System.out.println("-o <path> : Set output file.");
				} else if (arg.equals("-o") && argi < args.length) {
					outputFile = new File(args[argi++]);
				} else {
					System.err.println("Invalid option: " + arg);
				}
			} else {
				File inputFile = new File(arg);
				if (outputFile == null) outputFile = new File(
					inputFile.getParentFile(),
					inputFile.getName() + ".4DX"
				);
				DXDatabase db = new DXDatabase();
				try {
					boolean success = db.readDirectory(inputFile);
					if (!success) throw new IOException("Not a directory.");
					try {
						RandomAccessFile out = new RandomAccessFile(outputFile, "rwd");
						out.setLength(0L);
						db.write4DX(out);
						out.close();
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