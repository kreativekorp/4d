package com.kreative.fourdee.common.ui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenuItem;

public class CloseMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public CloseMenuItem(final Window window) {
		super("Close Window", KeyEvent.VK_W);
		setAccelerator(OSUtils.createShortcut(KeyEvent.VK_W, false));
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
			}
		});
	}
}