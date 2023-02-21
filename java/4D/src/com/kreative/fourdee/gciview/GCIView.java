package com.kreative.fourdee.gciview;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.UIManager;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class GCIView {
	public static void main(String[] args) {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "GCIView"); } catch (Exception e) {}
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		try {
			Method getModule = Class.class.getMethod("getModule");
			Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
			Object allUnnamed = getModule.invoke(GCIView.class);
			Class<?> module = Class.forName("java.lang.Module");
			Method addOpens = module.getMethod("addOpens", String.class, module);
			addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
		} catch (Exception e) {}
		
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
			aacn.setAccessible(true);
			aacn.set(tk, "GCIView");
		} catch (Exception e) {}
		
		if (args.length == 0) {
			File file = Dialogs.openFile("Open Image");
			if (file != null) {
				try {
					Image image = ImageIO.read(file);
					GCIFrame frame = new GCIFrame(file.getName(), image);
					frame.setVisible(true);
					frame.startAnimation();
				} catch (IOException ioe) {
					Dialogs.errorMessage("Open Image", "Could not read " + file.getName() + ".");
				}
			}
		} else for (String arg : args) {
			try {
				File in = new File(arg);
				Image image = ImageIO.read(in);
				GCIFrame frame = new GCIFrame(in.getName(), image);
				frame.setVisible(true);
				frame.startAnimation();
			} catch (IOException ioe) {
				System.err.println("Error viewing " + arg + ": " + ioe);
			}
		}
		
		try { Class.forName("com.kreative.fourdee.gciview.mac.MyApplicationListener").newInstance(); }
		catch (Exception e) {}
	}
}