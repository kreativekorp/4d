package com.kreative.fourdee.fourfs.res;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

public class Resources {
	public static Image getImage(String name) {
		URL resource = Resources.class.getResource(name);
		Image image = Toolkit.getDefaultToolkit().createImage(resource);
		return image;
	}
}