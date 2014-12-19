package com.kreative.fourdee.gciview.mac;

import java.awt.Window;
import java.awt.event.WindowEvent;
import com.apple.eawt.Application;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import com.apple.eawt.AppEvent.QuitEvent;

public class MacQuitHandler implements QuitHandler {
	public MacQuitHandler() {
		Application a = Application.getApplication();
		a.setQuitHandler(this);
	}
	
	@Override
	public void handleQuitRequestWith(QuitEvent e, QuitResponse r) {
		System.gc();
		for (Window window : Window.getWindows()) {
			if (window.isVisible()) {
				window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
				if (window.isVisible()) {
					r.cancelQuit();
					return;
				}
			}
		}
		r.performQuit();
	}
}