package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class RemoveMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public RemoveMenuItem(final FileSystemTable table) {
		super("Remove", KeyEvent.VK_R);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_BACK_SPACE, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.removeSelected();
			}
		});
	}
}