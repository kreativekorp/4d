package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.file.FileSystem;
import com.kreative.fourdee.fourfs.FileSystemFrame;

public class OpenMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public OpenMenuItem() {
		super("Open...", KeyEvent.VK_O);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_O, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = Dialogs.openFile("Open File System");
				if (file == null) return;
				try {
					FileSystem files = new FileSystem(file);
					FileSystemFrame f = new FileSystemFrame(file, files);
					f.setVisible(true);
				} catch (IOException ioe) {
					Dialogs.errorMessage("Open File System", "Could not read file system image from " + file.getName() + ".");
				}
			}
		});
	}
}