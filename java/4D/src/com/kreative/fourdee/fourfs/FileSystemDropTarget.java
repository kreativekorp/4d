package com.kreative.fourdee.fourfs;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public class FileSystemDropTarget implements DropTargetListener {
	private FileSystemTable owner;
	private boolean shiftDown;
	
	public FileSystemDropTarget(FileSystemTable owner) {
		this.owner = owner;
		this.shiftDown = false;
		
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent e) {
				FileSystemDropTarget.this.shiftDown = ((KeyEvent)e).isShiftDown();
			}
		}, AWTEvent.KEY_EVENT_MASK);
	}
	
	@Override
	public void drop(DropTargetDropEvent e) {
		try {
			int action = e.getDropAction();
			e.acceptDrop(action);
			Transferable t = e.getTransferable();
			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				List<?> list = (List<?>)t.getTransferData(DataFlavor.javaFileListFlavor);
				for (Object o : list) {
					if (o instanceof File) {
						owner.addFile((File)o, !shiftDown);
					}
				}
				e.dropComplete(true);
			} else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String s = t.getTransferData(DataFlavor.stringFlavor).toString();
				for (String l : s.split("\r\n|\r|\n")) {
					l = l.trim();
					if (l.length() > 0) {
						owner.addFile(new File(l), !shiftDown);
					}
				}
				e.dropComplete(true);
			} else {
				e.dropComplete(false);
			}
		} catch (Exception ex) {
			e.dropComplete(false);
		}
	}
	
	@Override public void dragEnter(DropTargetDragEvent e) {}
	@Override public void dragExit(DropTargetEvent e) {}
	@Override public void dragOver(DropTargetDragEvent e) {}
	@Override public void dropActionChanged(DropTargetDragEvent e) {}
}