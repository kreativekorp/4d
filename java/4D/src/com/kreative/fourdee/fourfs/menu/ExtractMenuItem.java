package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class ExtractMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public ExtractMenuItem(final FileSystemTable table) {
		super("Extract...", KeyEvent.VK_E);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_E, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> files = table.getSelected();
				for (File file : files) {
					java.io.File out = Dialogs.saveFile("Extract File", file.getName());
					if (out == null) continue;
					try {
						file.writeFile(out);
					} catch (IOException ioe) {
						Dialogs.errorMessage("Extract File", "Could not save " + out.getName() + ".");
					}
				}
			}
		});
	}
}