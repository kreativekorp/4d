package com.kreative.fourdee.fourfs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.common.ui.SaveChangesDialog;
import com.kreative.fourdee.file.FileSystem;
import com.kreative.fourdee.fourfs.menu.MenuBar;

public class FileSystemFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private File file;
	private FileSystemPanel panel;
	
	public FileSystemFrame(File file, FileSystem fs) {
		super((file != null) ? file.getName() : "Untitled");
		this.file = file;
		this.panel = new FileSystemPanel(fs);
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		setJMenuBar(new MenuBar(this, panel.getFileSystemTable()));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		int wx = this.getWidth() - panel.getWidth();
		int hx = this.getHeight() - panel.getHeight();
		Dimension ms = panel.getMinimumSize();
		setMinimumSize(new Dimension(ms.width + wx, ms.height + hx));
		Dimension ps = panel.getPreferredSize();
		setPreferredSize(new Dimension(ps.width + wx, ps.height + hx));
		setLocationRelativeTo(null);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (isChanged()) {
					SaveChangesDialog sc = new SaveChangesDialog(FileSystemFrame.this, getTitle());
					switch (sc.showDialog()) {
					case SAVE: if (doSave()) dispose(); break;
					case DONT_SAVE: dispose(); break;
					}
				} else {
					dispose();
				}
			}
		});
	}
	
	public boolean isChanged() {
		FileSystem fs = panel.getFileSystem();
		if (fs == null) return false;
		return fs.isChanged();
	}
	
	public boolean doSave() {
		if (this.file == null) return doSaveAs();
		else return doSave(this.file);
	}
	
	public boolean doSaveAs() {
		String filename;
		if (this.file != null) {
			filename = this.file.getName();
		} else {
			FileSystem fs = panel.getFileSystem();
			if (fs != null && fs.getName().length() > 0) {
				filename = fs.getName() + ".4fs";
			} else {
				filename = "Untitled.4fs";
			}
		}
		File file = Dialogs.saveFile("Save File System", filename);
		if (file == null) return false;
		else return doSave(file);
	}
	
	public boolean doSave(File out) {
		if (out == null) return false;
		FileSystem fs = panel.getFileSystem();
		if (fs == null) return false;
		try {
			fs.writeFileSystem(out, false);
			this.setTitle(out.getName());
			this.file = out;
			return true;
		} catch (IOException ioe) {
			Dialogs.errorMessage("Save File System", "Could not write file system image to " + out.getName() + ".");
			return false;
		}
	}
}