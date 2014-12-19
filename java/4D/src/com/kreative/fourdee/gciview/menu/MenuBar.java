package com.kreative.fourdee.gciview.menu;

import javax.swing.JMenuBar;
import com.kreative.fourdee.gciview.GCIFrame;

public class MenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;
	
	public MenuBar(GCIFrame f) {
		add(new ImageMenu(f));
	}
}