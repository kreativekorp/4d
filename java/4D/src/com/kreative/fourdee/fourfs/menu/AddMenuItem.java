package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class AddMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public AddMenuItem(final FileSystemTable table) {
		super("Add...", KeyEvent.VK_A);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_D, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = Dialogs.openFile("Add File");
				if (file == null) return;
				boolean stripExtension = ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 0);
				table.addFile(file, stripExtension);
			}
		});
	}
}