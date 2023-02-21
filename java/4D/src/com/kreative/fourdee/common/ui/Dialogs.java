package com.kreative.fourdee.common.ui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import javax.swing.JOptionPane;

public class Dialogs {
	private static String lastOpenDirectory = null;
	public static File openFile(String title) {
		Frame owner = new Frame();
		FileDialog fd = new FileDialog(owner, title, FileDialog.LOAD);
		if (lastOpenDirectory != null) fd.setDirectory(lastOpenDirectory);
		fd.setVisible(true);
		String directory = fd.getDirectory();
		String file = fd.getFile();
		fd.dispose();
		owner.dispose();
		if (directory == null || file == null) return null;
		return new File((lastOpenDirectory = directory), file);
	}
	
	private static String lastSaveDirectory = null;
	public static File saveFile(String title, String filename) {
		Frame owner = new Frame();
		FileDialog fd = new FileDialog(owner, title, FileDialog.SAVE);
		if (lastSaveDirectory != null) fd.setDirectory(lastSaveDirectory);
		if (filename != null) fd.setFile(filename);
		fd.setVisible(true);
		String directory = fd.getDirectory();
		String file = fd.getFile();
		fd.dispose();
		owner.dispose();
		if (directory == null || file == null) return null;
		return new File((lastSaveDirectory = directory), file);
	}
	
	public static void errorMessage(String title, String message) {
		Frame owner = new Frame();
		JOptionPane.showMessageDialog(owner, message, title, JOptionPane.ERROR_MESSAGE);
		owner.dispose();
	}
}