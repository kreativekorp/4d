package com.kreative.fourdee.gciview;

import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class GCIView {
	public static void main(String[] args) {
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		for (String arg : args) {
			try {
				File in = new File(arg);
				Image image = ImageIO.read(in);
				GCIFrame frame = new GCIFrame(in.getName(), image);
				frame.setVisible(true);
				frame.startAnimation();
			} catch (IOException ioe) {
				System.err.println("Error viewing " + arg + ": " + ioe.getMessage());
			}
		}
		try { Class.forName("com.kreative.fourdee.gciview.mac.MacOpenHandler").newInstance(); } catch (Exception e) {}
		try { Class.forName("com.kreative.fourdee.gciview.mac.MacQuitHandler").newInstance(); } catch (Exception e) {}
	}
}