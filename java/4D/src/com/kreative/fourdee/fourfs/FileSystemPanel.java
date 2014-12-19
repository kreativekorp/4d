package com.kreative.fourdee.fourfs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.dnd.DropTarget;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private FileSystem fs;
	private FileSystemNamePanel namePanel;
	private FileSystemTable table;
	private FileSystemButtonPanel buttonPanel;
	private FileSystemUsagePanel usagePanel;
	
	public FileSystemPanel(FileSystem fs) {
		this.fs = fs;
		this.namePanel = new FileSystemNamePanel(fs);
		this.table = new FileSystemTable(fs);
		this.buttonPanel = new FileSystemButtonPanel(table);
		this.usagePanel = new FileSystemUsagePanel(fs);
		table.getFileSystemTableModel().addTableModelListener(usagePanel);
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(buttonPanel, BorderLayout.PAGE_START);
		bottomPanel.add(usagePanel, BorderLayout.CENTER);
		JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		new DropTarget(scrollPane, new FileSystemDropTarget(table));
		setLayout(new BorderLayout());
		add(namePanel, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);
		setMinimumSize(new Dimension(160, 180));
		setPreferredSize(new Dimension(320, 380));
	}
	
	public FileSystem getFileSystem() {
		return this.fs;
	}
	
	public void setFileSystem(FileSystem fs) {
		this.fs = fs;
		this.namePanel.setFileSystem(fs);
		this.table.setFileSystem(fs);
		this.usagePanel.setFileSystem(fs);
	}
	
	public FileSystemTable getFileSystemTable() {
		return this.table;
	}
}