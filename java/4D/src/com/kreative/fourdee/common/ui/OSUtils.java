package com.kreative.fourdee.common.ui;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;

public class OSUtils {
	private static String osName = null;
	
	public static String getOSName() {
		if (osName != null) {
			return osName;
		} else try {
			return osName = System.getProperty("os.name");
		} catch (Exception e) {
			return "";
		}
	}
	
	public static boolean isMacOS() {
		return getOSName().toUpperCase().contains("MAC OS");
	}
	
	public static boolean isWindows() {
		return getOSName().toUpperCase().contains("WINDOWS");
	}
	
	public static KeyStroke createShortcut(int vk, boolean shift) {
		int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		if (shift) mask |= KeyEvent.SHIFT_MASK;
		return KeyStroke.getKeyStroke(vk, mask);
	}
}