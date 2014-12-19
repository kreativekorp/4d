package com.kreative.fourdee.common.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenuItem;

public class QuitMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public QuitMenuItem() {
		super(
			(OSUtils.isMacOS() ? "Quit" : "Exit"),
			(OSUtils.isMacOS() ? KeyEvent.VK_Q : KeyEvent.VK_X)
		);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_Q, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.gc();
				for (Window window : Window.getWindows()) {
					if (window.isVisible()) {
						window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
						if (window.isVisible()) {
							return;
						}
					}
				}
				System.exit(0);
			}
		});
	}
}