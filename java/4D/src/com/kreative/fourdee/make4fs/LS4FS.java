package com.kreative.fourdee.make4fs;

import java.io.IOException;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.file.FileSystem;

public class LS4FS {
	public static void main(String[] args) {
		for (String arg : args) {
			try {
				FileSystem files = new FileSystem(new java.io.File(arg));
				File def = files.getDefaultFile();
				System.out.print("    ");
				System.out.print(Strings.zeroPadHex(files.getClusterLength(), 4));
				System.out.print("  ");
				System.out.print(Strings.spacePad(files.size(), 10));
				System.out.print("  ");
				System.out.println(files.getName());
				for (File file : files) {
					System.out.print((file == def) ? "d" : "-");
					System.out.print(file.isActive() ? "a" : "-");
					System.out.print("  ");
					System.out.print(Strings.zeroPadHex(file.getCluster(), 4));
					System.out.print("  ");
					System.out.print(Strings.spacePad(file.getLength(), 10));
					System.out.print("  ");
					System.out.println(file.getName());
				}
			} catch (IOException ioe) {
				System.err.println("Error reading " + arg + ": " + ioe.getMessage());
			}
		}
	}
}