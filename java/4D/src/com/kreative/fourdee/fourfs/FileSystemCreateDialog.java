package com.kreative.fourdee.fourfs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemCreateDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	
	private JTextField nameField;
	private FileSystemSizePopup sizePopup;
	private JButton okButton;
	private JButton cancelButton;
	private boolean accepted;
	
	public FileSystemCreateDialog() {
		super(new Frame(), "Create File System", true);
		
		JPanel labelPanel = new JPanel(new GridLayout(0,1,8,8));
		labelPanel.add(new JLabel("Name:"));
		labelPanel.add(new JLabel("Size:"));
		
		JPanel controlPanel = new JPanel(new GridLayout(0,1,8,8));
		controlPanel.add(nameField = new JTextField("Goldelox HD", 24));
		controlPanel.add(sizePopup = new FileSystemSizePopup(8192));
		
		JPanel formPanel = new JPanel(new BorderLayout(8,8));
		formPanel.add(labelPanel, BorderLayout.LINE_START);
		formPanel.add(controlPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(okButton = new JButton("Create"));
		buttonPanel.add(Box.createHorizontalStrut(4));
		buttonPanel.add(cancelButton = new JButton("Cancel"));
		buttonPanel.add(Box.createHorizontalGlue());
		
		JPanel contentPanel = new JPanel(new BorderLayout(8,8));
		contentPanel.add(formPanel, BorderLayout.CENTER);
		contentPanel.add(buttonPanel, BorderLayout.PAGE_END);
		contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		setContentPane(contentPanel);
		getRootPane().setDefaultButton(okButton);
		setCancelButton(getRootPane(), cancelButton);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				accepted = true;
				setVisible(false);
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				accepted = false;
				setVisible(false);
			}
		});
	}
	
	public FileSystem createFileSystem() {
		setVisible(true);
		if (accepted) {
			return new FileSystem(
				nameField.getText(),
				sizePopup.getSelectedClusterLength()
			);
		} else {
			return null;
		}
	}
	
	public void disposeForReal() {
		this.dispose();
		this.getOwner().dispose();
	}
	
	private static void setCancelButton(JComponent c, AbstractButton cancel) {
		c.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
		c.getActionMap().put("cancel", new CancelAction(cancel));
	}
	private static class CancelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private AbstractButton cancel;
		public CancelAction(AbstractButton cancel) {
			this.cancel = cancel;
		}
		public void actionPerformed(ActionEvent ev) {
			cancel.doClick();
		}
	}
}