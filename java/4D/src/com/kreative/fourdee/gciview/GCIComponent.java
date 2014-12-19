package com.kreative.fourdee.gciview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import com.kreative.fourdee.image.Image;

public class GCIComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	
	private Thread animationThread;
	private Image image;
	private int frame;
	
	public GCIComponent(Image image) {
		this.animationThread = null;
		this.image = image;
		this.frame = 0;
		
		setFocusable(true);
		setRequestFocusEnabled(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER: startAnimation(); break;
				case KeyEvent.VK_ESCAPE: stopAnimation(); break;
				case KeyEvent.VK_SPACE:
					if (isAnimating()) stopAnimation();
					else startAnimation();
					break;
				case KeyEvent.VK_BACK_SPACE: setFrame(0); break;
				case KeyEvent.VK_LEFT: advanceFrame(-1); break;
				case KeyEvent.VK_RIGHT: advanceFrame(+1); break;
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				requestFocusInWindow();
				if (isAnimating()) stopAnimation();
				else startAnimation();
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				requestFocusInWindow();
				advanceFrame(e.getWheelRotation());
			}
		});
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public void setImage(Image image) {
		this.stopAnimation();
		this.image = image;
		this.frame = 0;
		this.repaint();
	}
	
	public int getFrame() {
		return this.frame;
	}
	
	public void setFrame(int frame) {
		if (image == null) {
			this.frame = 0;
		} else {
			int count = image.getFrameCount();
			if (count < 1) {
				this.frame = 0;
			} else {
				this.frame = frame % count;
			}
		}
		this.repaint();
	}
	
	public void advanceFrame(int advance) {
		if (image == null) {
			this.frame = 0;
		} else {
			int count = image.getFrameCount();
			if (count < 1) {
				this.frame = 0;
			} else {
				this.frame += advance;
				while (this.frame < 0) this.frame += count;
				this.frame %= count;
			}
		}
		this.repaint();
	}
	
	public void startAnimation() {
		stopAnimation();
		if (image == null) return;
		final int count = image.getFrameCount();
		if (count <= 1) return;
		animationThread = new Thread() {
			public void run() {
				while (!Thread.interrupted()) {
					GCIComponent.this.repaint();
					int delay = image.getDelay(frame % count);
					try { Thread.sleep(delay); }
					catch (InterruptedException ie) { break; }
					frame = (frame + 1) % count;
				}
			}
		};
		animationThread.start();
	}
	
	public void stopAnimation() {
		if (animationThread != null) {
			animationThread.interrupt();
			animationThread = null;
		}
	}
	
	public boolean isAnimating() {
		return (animationThread != null);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) return;
		Insets insets = getInsets();
		int cw = getWidth() - insets.left - insets.right;
		int ch = getHeight() - insets.top - insets.bottom;
		int iw = image.getWidth();
		int ih = image.getHeight();
		int ix = insets.left + (cw - iw) / 2;
		int iy = insets.top + (ch - ih) / 2;
		image.paintFrame(g, ix, iy, frame);
	}
	
	@Override
	public Dimension getMinimumSize() {
		if (image == null) return super.getMinimumSize();
		Insets insets = getInsets();
		int width = insets.left + insets.right + image.getWidth();
		int height = insets.top + insets.bottom + image.getHeight();
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (image == null) return super.getPreferredSize();
		Insets insets = getInsets();
		int width = insets.left + insets.right + image.getWidth();
		int height = insets.top + insets.bottom + image.getHeight();
		return new Dimension(width, height);
	}
	
	@Override
	public Dimension getMaximumSize() {
		if (image == null) return super.getMaximumSize();
		Insets insets = getInsets();
		int width = insets.left + insets.right + image.getWidth();
		int height = insets.top + insets.bottom + image.getHeight();
		return new Dimension(width, height);
	}
}