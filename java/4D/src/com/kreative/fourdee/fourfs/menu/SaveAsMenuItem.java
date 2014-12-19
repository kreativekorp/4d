package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.fourfs.FileSystemFrame;

public class SaveAsMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SaveAsMenuItem(final FileSystemFrame f) {
		super("Save As...", KeyEvent.VK_A);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_S, true));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				f.doSaveAs();
			}
		});
	}
}