package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class MoveUpMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public MoveUpMenuItem(final FileSystemTable table) {
		super("Move Up", KeyEvent.VK_U);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_UP, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.moveSelected(-1);
			}
		});
	}
}