package com.kreative.fourdee.fourfs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.kreative.fourdee.common.ui.Dialogs;
import com.kreative.fourdee.file.File;
import com.kreative.fourdee.fourfs.res.Resources;
import com.kreative.fourdee.gciview.GCIFrame;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class FileSystemButtonPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private FileSystemTable table;
	
	public FileSystemButtonPanel(FileSystemTable table) {
		this.table = table;
		JButton addButton = iconButton("icon-add.png", "Add");
		JButton removeButton = iconButton("icon-remove.png", "Remove");
		JButton upButton = iconButton("icon-up.png", "Move Up");
		JButton downButton = iconButton("icon-down.png", "Move Down");
		JButton sortButton = iconButton("icon-sort.png", "Sort");
		JButton viewButton = iconButton("icon-view.png", "View");
		JButton extractButton = iconButton("icon-extract.png", "Extract");
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(Box.createHorizontalGlue());
		add(addButton);
		add(removeButton);
		add(upButton);
		add(downButton);
		add(sortButton);
		add(viewButton);
		add(extractButton);
		add(Box.createHorizontalGlue());
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.io.File file = Dialogs.openFile("Add File");
				if (file == null) return;
				boolean stripExtension = ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 0);
				FileSystemButtonPanel.this.table.addFile(file, stripExtension);
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemButtonPanel.this.table.removeSelected();
			}
		});
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemButtonPanel.this.table.moveSelected(-1);
			}
		});
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemButtonPanel.this.table.moveSelected(+1);
			}
		});
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemButtonPanel.this.table.sortFiles();
			}
		});
		viewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemTable table = FileSystemButtonPanel.this.table;
				List<File> files = table.getSelected();
				for (File file : files) {
					try {
						Image image = ImageIO.read(file.getData());
						GCIFrame frame = new GCIFrame(file.getName(), image);
						frame.setVisible(true);
						frame.startAnimation();
					} catch (IOException ioe) {}
				}
			}
		});
		extractButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileSystemTable table = FileSystemButtonPanel.this.table;
				List<File> files = table.getSelected();
				for (File file : files) {
					java.io.File out = Dialogs.saveFile("Extract File", file.getName());
					if (out == null) continue;
					try {
						file.writeFile(out);
					} catch (IOException ioe) {
						Dialogs.errorMessage("Extract File", "Could not save " + out.getName() + ".");
					}
				}
			}
		});
	}
	
	private static JButton iconButton(String iconName, String tooltip) {
		JButton button = new JButton(new ImageIcon(Resources.getImage(iconName)));
		button.setToolTipText(tooltip);
		Dimension d = new Dimension(32, 32);
		button.setMinimumSize(d);
		button.setPreferredSize(d);
		button.setMaximumSize(d);
		return button;
	}
}