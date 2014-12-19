package com.kreative.fourdee.gciview.menu;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import com.kreative.fourdee.common.ui.CloseMenuItem;
import com.kreative.fourdee.common.ui.OSUtils;
import com.kreative.fourdee.common.ui.QuitMenuItem;
import com.kreative.fourdee.gciview.GCIFrame;

public class ImageMenu extends JMenu {
	private static final long serialVersionUID = 1L;
	
	public ImageMenu(GCIFrame f) {
		super("Image");
		setMnemonic(KeyEvent.VK_I);
		add(new OpenMenuItem());
		add(new CloseMenuItem(f));
		addSeparator();
		add(new SaveAsMenuItem(f));
		if (!OSUtils.isMacOS()) {
			addSeparator();
			add(new QuitMenuItem());
		}
	}
}