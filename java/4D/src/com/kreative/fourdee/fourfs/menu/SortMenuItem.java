package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenuItem;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class SortMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SortMenuItem(final FileSystemTable table) {
		super("Sort", KeyEvent.VK_S);
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				table.sortFiles();
			}
		});
	}
}