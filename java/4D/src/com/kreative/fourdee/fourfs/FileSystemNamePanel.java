package com.kreative.fourdee.fourfs;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemNamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private FileSystem fs;
	private JTextField input;
	
	public FileSystemNamePanel(FileSystem fs) {
		this.fs = fs;
		this.input = new JTextField(fs.getName());
		setLayout(new BorderLayout());
		add(input, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		input.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { changed(); }
			@Override public void insertUpdate(DocumentEvent e) { changed(); }
			@Override public void removeUpdate(DocumentEvent e) { changed(); }
			private void changed() {
				String oldName = FileSystemNamePanel.this.fs.getName();
				String newName = FileSystemNamePanel.this.input.getText();
				if (!newName.equals(oldName)) {
					FileSystemNamePanel.this.fs.setName(newName);
				}
			}
		});
	}
	
	public FileSystem getFileSystem() {
		return this.fs;
	}
	
	public void setFileSystem(FileSystem fs) {
		this.fs = fs;
		this.input.setText(fs.getName());
	}
}