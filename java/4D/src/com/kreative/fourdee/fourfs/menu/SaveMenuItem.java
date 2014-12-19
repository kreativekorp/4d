package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.fourfs.FileSystemFrame;

public class SaveMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SaveMenuItem(final FileSystemFrame f) {
		super("Save", KeyEvent.VK_S);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_S, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.doSave();
			}
		});
	}
}