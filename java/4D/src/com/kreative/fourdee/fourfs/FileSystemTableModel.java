package com.kreative.fourdee.fourfs;

import javax.swing.table.AbstractTableModel;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private FileSystem fs;
	
	public FileSystemTableModel(FileSystem fs) {
		this.fs = fs;
	}
	
	public FileSystem getFileSystem() {
		return this.fs;
	}
	
	public void setFileSystem(FileSystem fs) {
		this.fs = fs;
		this.fireTableDataChanged();
	}
	
	@Override
	public Class<?> getColumnClass(int col) {
		switch (col) {
		case 0: return Boolean.class;
		case 1: return Boolean.class;
		case 2: return String.class;
		default: return null;
		}
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public String getColumnName(int col) {
		switch (col) {
		case 0: return "DEF";
		case 1: return "ON";
		case 2: return "Name";
		default: return null;
		}
	}
	
	@Override
	public int getRowCount() {
		return fs.size();
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		File file = fs.get(row);
		if (file == null) return null;
		switch (col) {
		case 0: return Boolean.valueOf(fs.getDefaultFile() == file);
		case 1: return Boolean.valueOf(file.isActive());
		case 2: return file.getName();
		default: return null;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	@Override
	public void setValueAt(Object val, int row, int col) {
		File file = fs.get(row);
		if (file == null) return;
		switch (col) {
		case 0:
			if (((Boolean)val).booleanValue()) {
				int last = fs.getDefaultIndex();
				fs.setDefaultFile(file);
				if (last >= 0 && last < fs.size()) {
					fireTableCellUpdated(last, 0);
				}
			}
			break;
		case 1:
			file.setActive(((Boolean)val).booleanValue());
			break;
		case 2:
			file.setName(val.toString());
			break;
		}
	}
}