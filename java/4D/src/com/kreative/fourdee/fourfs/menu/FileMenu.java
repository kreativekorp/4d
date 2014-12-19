package com.kreative.fourdee.fourfs.menu;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import com.kreative.fourdee.fourfs.FileSystemTable;

public class FileMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public FileMenu(FileSystemTable table) {
		super("File");
		setMnemonic(KeyEvent.VK_F);
		add(new AddMenuItem(table));
		add(new RemoveMenuItem(table));
		addSeparator();
		add(new MoveUpMenuItem(table));
		add(new MoveDownMenuItem(table));
		add(new SortMenuItem(table));
		addSeparator();
		add(new ViewMenuItem(table));
		add(new ExtractMenuItem(table));
	}
}