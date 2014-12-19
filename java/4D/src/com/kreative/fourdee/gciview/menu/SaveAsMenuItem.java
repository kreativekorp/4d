package com.kreative.fourdee.gciview.menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JMenuItem;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.gciview.GCIFrame;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class SaveAsMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public SaveAsMenuItem(final GCIFrame f) {
		super("Save As...", KeyEvent.VK_S);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_S, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File file = Dialogs.saveFile("Save Image", f.getTitle());
				if (file == null) return;
				Image image = f.getImage();
				String ext = Strings.getExtension(file.getName(), "gci");
				try {
					if (ext.equalsIgnoreCase("d")) {
						file.mkdirs();
						ext = file.getName();
						ext = ext.substring(0, ext.length() - 2);
						ext = Strings.getExtension(ext, "gci");
						int count = image.getFrameCount();
						int w = image.getWidth();
						int h = image.getHeight();
						int x = image.getX();
						int y = image.getY();
						Color matte = image.getMatte();
						int n = Integer.toString(count - 1).length();
						int m = 0;
						for (int i = 0; i < count; i++) {
							int d = image.getDelay(i);
							if (d > m) m = d;
						}
						m = Integer.toString(m).length();
						for (int i = 0; i < count; i++) {
							Image frame = new Image(w, h);
							frame.setX(x); frame.setY(y);
							frame.setMatte(matte);
							frame.addFrame(image.getFrame(i), 0);
							String fn = "Frame" + Strings.zeroPad(i, n);
							String mn = Strings.zeroPad(image.getDelay(i), m) + "ms";
							File ffile = new File(file, fn + "-" + mn + "." + ext);
							ImageIO.write(ffile, frame, ext);
						}
					} else {
						ImageIO.write(file, image, ext);
					}
				} catch (IOException ioe) {
					Dialogs.errorMessage("Save Image", "Could not write " + file.getName() + ".");
				}
			}
		});
	}
}