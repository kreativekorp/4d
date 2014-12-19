package com.kreative.fourdee.make4fs;

import java.io.IOException;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.file.FileSystem;

public class Unmake4FS {
	public static void main(String[] args) {
		String outputDirectory = null;
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (arg.startsWith("-")) {
				if (arg.equals("--help")) {
					System.out.println("-o <path> : Set output directory.");
				} else if (arg.equals("-o") && argi < args.length) {
					outputDirectory = args[argi++];
				} else {
					System.err.println("Invalid option: " + arg);
				}
			} else {
				try {
					java.io.File in = new java.io.File(arg);
					FileSystem files = new FileSystem(in);
					String name = files.getName();
					if (name == null || name.length() < 1) {
						name = in.getName() + ".d";
					}
					java.io.File root;
					if (outputDirectory != null) {
						root = new java.io.File(outputDirectory.replaceAll("\\{}", name));
					} else {
						root = new java.io.File(in.getParentFile(), name);
					}
					root.mkdirs();
					for (File file : files) {
						try {
							file.writeFile(new java.io.File(root, file.getName()));
						} catch (IOException ioe) {
							System.err.println("Error writing " + file.getName() + ": " + ioe.getMessage());
						}
					}
				} catch (IOException ioe) {
					System.err.println("Error reading " + arg + ": " + ioe.getMessage());
				}
			}
		}
	}
}