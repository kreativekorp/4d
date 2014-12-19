package com.kreative.fourdee.fourfs.mac;

import java.io.File;
import java.io.IOException;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.AppEvent.OpenFilesEvent;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.file.FileSystem;
import com.kreative.fourdee.fourfs.FileSystemFrame;

public class MacOpenHandler implements OpenFilesHandler {
	public MacOpenHandler() {
		Application a = Application.getApplication();
		a.setOpenFileHandler(this);
	}
	
	@Override
	public void openFiles(OpenFilesEvent e) {
		for (File file : e.getFiles()) {
			try {
				FileSystem files = new FileSystem(file);
				FileSystemFrame f = new FileSystemFrame(file, files);
				f.setVisible(true);
			} catch (IOException ioe) {
				Dialogs.errorMessage("Open File System", "Could not read file system image from " + file.getName() + ".");
			}
		}
	}
}