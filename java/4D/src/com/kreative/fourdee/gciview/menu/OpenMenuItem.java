package com.kreative.fourdee.gciview.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.gciview.GCIFrame;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class OpenMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public OpenMenuItem() {
		super("Open...", KeyEvent.VK_O);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_O, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = Dialogs.openFile("Open Image");
				if (file == null) return;
				try {
					Image image = ImageIO.read(file);
					GCIFrame frame = new GCIFrame(file.getName(), image);
					frame.setVisible(true);
					frame.startAnimation();
				} catch (IOException ioe) {
					Dialogs.errorMessage("Open Image", "Could not read " + file.getName() + ".");
				}
			}
		});
	}
}