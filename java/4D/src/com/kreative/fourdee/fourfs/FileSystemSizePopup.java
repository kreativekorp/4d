package com.kreative.fourdee.fourfs;

import javax.swing.JComboBox;

public class FileSystemSizePopup extends JComboBox {
	private static final long serialVersionUID = 1L;
	
	public FileSystemSizePopup(int clusters) {
		super(FileSystemSize.values());
		this.setEditable(false);
		this.setMaximumRowCount(this.getItemCount());
		this.setSelectedClusterLength(clusters);
	}
	
	public int getSelectedByteLength() {
		Object o = this.getSelectedItem();
		if (o instanceof FileSystemSize) {
			return ((FileSystemSize)o).getByteLength();
		} else {
			return 8192 << 16;
		}
	}
	
	public int getSelectedClusterLength() {
		Object o = this.getSelectedItem();
		if (o instanceof FileSystemSize) {
			return ((FileSystemSize)o).getClusterLength();
		} else {
			return 8192;
		}
	}
	
	public void setSelectedByteLength(int length) {
		this.setSelectedItem(FileSystemSize.forByteLength(length));
	}
	
	public void setSelectedClusterLength(int length) {
		this.setSelectedItem(FileSystemSize.forClusterLength(length));
	}
}