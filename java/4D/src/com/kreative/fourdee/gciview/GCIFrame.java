package com.kreative.fourdee.gciview;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import com.kreative.fourdee.gciview.menu.MenuBar;
import com.kreative.fourdee.image.Image;

public class GCIFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final GCIComponent gci;
	
	public GCIFrame(String title, Image image) {
		super(title);
		setLayout(new BorderLayout());
		gci = new GCIComponent(image);
		add(gci, BorderLayout.CENTER);
		setJMenuBar(new MenuBar(this));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gci.stopAnimation();
				dispose();
			}
		});
	}
	
	public Image getImage() { return gci.getImage(); }
	public void setImage(Image image) { gci.setImage(image); pack(); }
	public void startAnimation() { gci.startAnimation(); }
	public void stopAnimation() { gci.stopAnimation(); }
	public boolean isAnimating() { return gci.isAnimating(); }
}