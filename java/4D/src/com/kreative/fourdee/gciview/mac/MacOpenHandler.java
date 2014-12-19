package com.kreative.fourdee.gciview.mac;

import java.io.File;
import java.io.IOException;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.AppEvent.OpenFilesEvent;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.gciview.GCIFrame;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class MacOpenHandler implements OpenFilesHandler {
	public MacOpenHandler() {
		Application a = Application.getApplication();
		a.setOpenFileHandler(this);
	}
	
	@Override
	public void openFiles(OpenFilesEvent e) {
		for (File file : e.getFiles()) {
			try {
				Image image = ImageIO.read(file);
				GCIFrame frame = new GCIFrame(file.getName(), image);
				frame.setVisible(true);
				frame.startAnimation();
			} catch (IOException ioe) {
				Dialogs.errorMessage("Open Image", "Could not read " + file.getName() + ".");
			}
		}
	}
}