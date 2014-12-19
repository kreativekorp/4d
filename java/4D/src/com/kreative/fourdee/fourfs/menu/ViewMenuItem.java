package com.kreative.fourdee.fourfs.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.fourfs.FileSystemTable;
import com.kreative.fourdee.gciview.GCIFrame;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class ViewMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public ViewMenuItem(final FileSystemTable table) {
		super("View", KeyEvent.VK_V);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_Y, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<File> files = table.getSelected();
				for (File file : files) {
					try {
						Image image = ImageIO.read(file.getData());
						GCIFrame frame = new GCIFrame(file.getName(), image);
						frame.setVisible(true);
						frame.startAnimation();
					} catch (IOException ioe) {}
				}
			}
		});
	}
}