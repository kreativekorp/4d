package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.file.FileSystem;
import com.kreative.fourdee.fourfs.FileSystemCreateDialog;
import com.kreative.fourdee.fourfs.FileSystemFrame;

public class NewMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public NewMenuItem() {
		super("New...", KeyEvent.VK_N);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_N, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemCreateDialog dlg = new FileSystemCreateDialog(); 
				FileSystem files = dlg.createFileSystem();
				dlg.disposeForReal();
				if (files != null) {
					FileSystemFrame f = new FileSystemFrame(null, files);
					f.setVisible(true);
				}
			}
		});
	}
}