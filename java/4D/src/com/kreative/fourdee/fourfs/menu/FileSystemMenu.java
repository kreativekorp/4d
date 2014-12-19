package com.kreative.fourdee.fourfs.menu;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import com.kreative.fourdee.common.ui.CloseMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.common.ui.QuitMenuItem;
import com.kreative.fourdee.fourfs.FileSystemFrame;

public class FileSystemMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public FileSystemMenu(FileSystemFrame f) {
		super("FileSystem");
		setMnemonic(KeyEvent.VK_S);
		add(new NewMenuItem());
		add(new OpenMenuItem());
		add(new CloseMenuItem(f));
		addSeparator();
		add(new SaveMenuItem(f));
		add(new SaveAsMenuItem(f));
		if (!OSUtils.isMacOS()) {
			addSeparator();
			add(new QuitMenuItem());
		}
	}
}