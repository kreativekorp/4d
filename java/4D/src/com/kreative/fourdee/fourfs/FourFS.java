package com.kreative.fourdee.fourfs;

import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;
import com.kreative.fourdee.file.FileSystem;

public class FourFS {
	public static void main(String[] args) {
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		if (args.length == 0) {
			FileSystemCreateDialog dlg = new FileSystemCreateDialog(); 
			FileSystem files = dlg.createFileSystem();
			dlg.disposeForReal();
			if (files != null) {
				FileSystemFrame f = new FileSystemFrame(null, files);
				f.setVisible(true);
			}
		} else for (String arg : args) {
			try {
				File in = new File(arg);
				FileSystem files = new FileSystem(in);
				FileSystemFrame f = new FileSystemFrame(in, files);
				f.setVisible(true);
			} catch (IOException ioe) {
				System.err.println("Error reading " + arg + ": " + ioe.getMessage());
			}
		}
		try { Class.forName("com.kreative.fourdee.fourfs.mac.MacOpenHandler").newInstance(); } catch (Exception e) {}
		try { Class.forName("com.kreative.fourdee.fourfs.mac.MacQuitHandler").newInstance(); } catch (Exception e) {}
	}
}