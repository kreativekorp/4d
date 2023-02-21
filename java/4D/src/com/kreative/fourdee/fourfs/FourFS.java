package com.kreative.fourdee.fourfs;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.UIManager;
import com.kreative.fourdee.file.FileSystem;

public class FourFS {
	public static void main(String[] args) {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "FourFS"); } catch (Exception e) {}
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		try {
			Method getModule = Class.class.getMethod("getModule");
			Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
			Object allUnnamed = getModule.invoke(FourFS.class);
			Class<?> module = Class.forName("java.lang.Module");
			Method addOpens = module.getMethod("addOpens", String.class, module);
			addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
		} catch (Exception e) {}
		
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
			aacn.setAccessible(true);
			aacn.set(tk, "FourFS");
		} catch (Exception e) {}
		
		if (args.length == 0) {
			FileSystemCreateDialog dlg = new FileSystemCreateDialog(); 
			FileSystem files = dlg.createFileSystem();
			dlg.disposeForReal();
			if (files != null) {
				FileSystemFrame f = new FileSystemFrame(null, files);
				f.setVisible(true);
			}
		} else for (String arg : args) {
			try {
				File in = new File(arg);
				FileSystem files = new FileSystem(in);
				FileSystemFrame f = new FileSystemFrame(in, files);
				f.setVisible(true);
			} catch (IOException ioe) {
				System.err.println("Error reading " + arg + ": " + ioe);
			}
		}
		
		try { Class.forName("com.kreative.fourdee.fourfs.mac.MyApplicationListener").newInstance(); }
		catch (Exception e) {}
	}
}