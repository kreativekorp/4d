package com.kreative.fourdee.fourfs.menu;

import javax.swing.JMenuBar;
import com.kreative.fourdee.fourfs.FileSystemFrame;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public MenuBar(FileSystemFrame f, FileSystemTable t) {
		add(new FileSystemMenu(f));
		add(new FileMenu(t));
	}
}