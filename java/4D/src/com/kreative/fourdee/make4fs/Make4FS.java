package com.kreative.fourdee.make4fs;

import java.io.IOException;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.file.FileSystem;

public class Make4FS {
	public static void main(String[] args) {
		java.io.File outputFile = null;
		FileSystem files = new FileSystem("Goldelox HD", 8192);
		boolean autoSize = false;
		boolean nextDefault = false;
		boolean nextInactive = false;
		boolean stripExtension = false;
		String nextFilename = null;
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (arg.startsWith("-")) {
				if (arg.equals("--help")) {
					System.out.println("-o <path> : Set output file.");
					System.out.println("-n <str>  : Set name of image.");
					System.out.println("-c <num>  : Set size of image in clusters.");
					System.out.println("-d        : Set next file as default.");
					System.out.println("-i        : Set next file as inactive.");
					System.out.println("-s        : Strip file extensions.");
					System.out.println("-e        : Include file extensions.");
					System.out.println("-r <str>  : Set name of next file.");
				} else if (arg.equals("-o") && argi < args.length) {
					outputFile = new java.io.File(args[argi++]);
				} else if (arg.equals("-n") && argi < args.length) {
					files.setName(args[argi++]);
				} else if (arg.equals("-c") && argi < args.length) {
					String s = args[argi++];
					autoSize = s.equalsIgnoreCase("auto");
					if (!autoSize) files.setClusterLength(Strings.parseInt(s, 0));
				} else if (arg.equals("-d")) {
					nextDefault = true;
				} else if (arg.equals("-i")) {
					nextInactive = true;
				} else if (arg.equals("-s")) {
					stripExtension = true;
				} else if (arg.equals("-e")) {
					stripExtension = false;
				} else if (arg.equals("-r") && argi < args.length) {
					nextFilename = args[argi++];
				} else {
					System.err.println("Invalid option: " + arg);
				}
			} else if (outputFile == null) {
				outputFile = new java.io.File(arg);
			} else {
				try {
					File file = new File(new java.io.File(arg), stripExtension);
					files.add(file);
					if (nextDefault) {
						files.setDefaultFile(file);
						nextDefault = false;
					}
					if (nextInactive) {
						file.setActive(false);
						nextInactive = false;
					}
					if (nextFilename != null) {
						file.setName(nextFilename);
						nextFilename = null;
					}
				} catch (IOException ioe) {
					System.err.println("Error reading " + arg + ": " + ioe.getMessage());
				}
			}
		}
		if (autoSize) files.setClusterLength(files.getUsedClusterLength());
		if (outputFile != null) {
			try {
				files.writeFileSystem(outputFile, true);
			} catch (IOException ioe) {
				System.err.println("Error writing " + outputFile.getPath() + ": " + ioe.getMessage());
			}
		}
	}
}