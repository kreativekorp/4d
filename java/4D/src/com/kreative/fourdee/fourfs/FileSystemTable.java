package com.kreative.fourdee.fourfs;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemTable extends JTable {
	private static final long serialVersionUID = 1L;
	
	public FileSystemTable(FileSystem fs) {
		super(new FileSystemTableModel(fs));
		getColumnModel().getColumn(0).setMinWidth(30);
		getColumnModel().getColumn(0).setMaxWidth(30);
		getColumnModel().getColumn(1).setMinWidth(30);
		getColumnModel().getColumn(1).setMaxWidth(30);
		getColumnModel().setColumnMargin(0);
		setRowHeight(18);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					if (!isEditing()) {
						int row = getSelectedRow();
						if (row >= 0) {
							editCellAt(row, 2, null);
							getEditorComponent().requestFocusInWindow();
						}
						e.consume();
					}
					break;
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					if (!isEditing()) e.consume();
					break;
				}
			}
			@Override
			public void keyTyped(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '\n': case '\r':
					if (!isEditing()) e.consume();
					break;
				}
			}
		});
	}
	
	@Override
    public boolean editCellAt(int row, int col, EventObject e) {
		if (e instanceof KeyEvent) {
			KeyEvent ke = (KeyEvent)e;
			if (ke.isMetaDown()) return false;
			if (ke.isAltDown()) return false;
			if (ke.isControlDown()) return false;
			switch (ke.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				FileSystemTableModel fsmodel = getFileSystemTableModel();
				if (fsmodel != null) {
					if (ke.isShiftDown()) {
						fsmodel.setValueAt(Boolean.TRUE, row, 0);
						fsmodel.fireTableCellUpdated(row, 0);
					} else {
						boolean active = ((Boolean)fsmodel.getValueAt(row, 1)).booleanValue();
						fsmodel.setValueAt(Boolean.valueOf(!active), row, 1);
						fsmodel.fireTableCellUpdated(row, 1);
					}
				}
				return false;
			case KeyEvent.VK_ESCAPE:
				editCellAt(row, 2, null);
				Component editor = getEditorComponent();
				editor.requestFocusInWindow();
				((JTextComponent)editor).setText("");
				return false;
			}
		}
		return super.editCellAt(row, col, e);
	}
	
	public FileSystemTableModel getFileSystemTableModel() {
		TableModel model = getModel();
		if (model instanceof FileSystemTableModel) {
			return (FileSystemTableModel)model;
		} else {
			return null;
		}
	}
	
	public void setFileSystemTableModel(FileSystemTableModel model) {
		setModel(model);
	}
	
	public FileSystem getFileSystem() {
		TableModel model = getModel();
		if (model instanceof FileSystemTableModel) {
			return ((FileSystemTableModel)model).getFileSystem();
		} else {
			return null;
		}
	}
	
	public void setFileSystem(FileSystem fs) {
		TableModel model = getModel();
		if (model instanceof FileSystemTableModel) {
			((FileSystemTableModel)model).setFileSystem(fs);
		} else {
			setModel(new FileSystemTableModel(fs));
		}
	}
	
	public boolean addFile(java.io.File in, boolean stripExtension) {
		if (in == null) return false;
		TableModel model = getModel();
		if (!(model instanceof FileSystemTableModel)) return false;
		FileSystemTableModel fsmodel = (FileSystemTableModel)model;
		FileSystem fs = fsmodel.getFileSystem();
		try {
			fs.add(new File(in, stripExtension));
			int index = fs.size() - 1;
			fsmodel.fireTableRowsInserted(index, index);
			return true;
		} catch (IOException ioe) {
			Dialogs.errorMessage("Add File", "Could not open " + in.getName() + ".");
			return false;
		}
	}
	
	public List<File> getSelected() {
		TableModel model = getModel();
		if (!(model instanceof FileSystemTableModel)) return null;
		FileSystemTableModel fsmodel = (FileSystemTableModel)model;
		FileSystem fs = fsmodel.getFileSystem();
		List<File> files = new ArrayList<File>();
		for (int index : getSelectedRows()) {
			files.add(fs.get(index));
		}
		return files;
	}
	
	public void setSelected(List<File> files) {
		clearSelection();
		if (files == null || files.isEmpty()) return;
		TableModel model = getModel();
		if (!(model instanceof FileSystemTableModel)) return;
		FileSystemTableModel fsmodel = (FileSystemTableModel)model;
		FileSystem fs = fsmodel.getFileSystem();
		for (File file : files) {
			int index = fs.indexOf(file);
			if (index >= 0) addRowSelectionInterval(index, index);
		}
	}
	
	public boolean removeSelected() {
		TableModel model = getModel();
		if (!(model instanceof FileSystemTableModel)) return false;
		FileSystemTableModel fsmodel = (FileSystemTableModel)model;
		FileSystem fs = fsmodel.getFileSystem();
		List<File> files = new ArrayList<File>();
		for (int index : getSelectedRows()) {
			files.add(fs.get(index));
		}
		fs.removeAll(files);
		clearSelection();
		fsmodel.fireTableDataChanged();
		return true;
	}
	
	public boolean moveSelected(int direction) {
		TableModel model = getModel();
		if (!(model instanceof FileSystemTableModel)) return false;
		FileSystemTableModel fsmodel = (FileSystemTableModel)model;
		FileSystem fs = fsmodel.getFileSystem();
		int index = getSelectedRow();
		if (index < 0 || index >= fs.size()) return false;
		File defaultFile = fs.getDefaultFile();
		File file = fs.remove(index);
		index += direction;
		if (index > fs.size()) index = fs.size();
		if (index < 0) index = 0;
		fs.add(index, file);
		fs.setDefaultFile(defaultFile);
		fs.setChanged(true);
		clearSelection();
		fsmodel.fireTableDataChanged();
		setRowSelectionInterval(index, index);
		return true;
	}
	
	public boolean sortFiles() {
		TableModel model = getModel();
		if (!(model instanceof FileSystemTableModel)) return false;
		FileSystemTableModel fsmodel = (FileSystemTableModel)model;
		FileSystem fs = fsmodel.getFileSystem();
		List<File> selectedFiles = getSelected();
		File defaultFile = fs.getDefaultFile();
		Collections.sort(fs);
		fs.setDefaultFile(defaultFile);
		fs.setChanged(true);
		clearSelection();
		fsmodel.fireTableDataChanged();
		setSelected(selectedFiles);
		return true;
	}
}