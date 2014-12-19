package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class MoveDownMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public MoveDownMenuItem(final FileSystemTable table) {
		super("Move Down", KeyEvent.VK_D);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_DOWN, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.moveSelected(+1);
			}
		});
	}
}