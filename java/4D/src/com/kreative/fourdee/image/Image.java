package com.kreative.fourdee.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Image {
	private ArrayList<BufferedImage> frames;
	private ArrayList<Integer> delays;
	private int x;
	private int y;
	private int width;
	private int height;
	private Color matte;
	private boolean changed;
	
	public Image(int width, int height) {
		this.frames = new ArrayList<BufferedImage>();
		this.delays = new ArrayList<Integer>();
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		this.matte = Color.BLACK;
		this.changed = false;
	}
	
	public void addFrame(BufferedImage frame, int delay) {
		this.frames.add(frame);
		this.delays.add(delay);
		this.changed = true;
	}
	
	public void insertFrame(int index, BufferedImage frame, int delay) {
		this.frames.add(index, frame);
		this.delays.add(index, delay);
		this.changed = true;
	}
	
	public void setFrame(int index, BufferedImage frame, int delay) {
		this.frames.set(index, frame);
		this.delays.set(index, delay);
		this.changed = true;
	}
	
	public void removeFrame(int index) {
		this.frames.remove(index);
		this.delays.remove(index);
		this.changed = true;
	}
	
	public void clearFrames() {
		this.frames.clear();
		this.delays.clear();
		this.changed = true;
	}
	
	public int getFrameCount() {
		return this.frames.size();
	}
	
	public BufferedImage getFrame(int index) {
		return this.frames.get(index);
	}
	
	public int getDelay(int index) {
		return this.delays.get(index);
	}
	
	public void paintFrame(Graphics g, int x, int y, int frame) {
		g.setColor(this.matte);
		g.fillRect(x, y, this.width, this.height);
		if (!this.frames.isEmpty()) {
			BufferedImage f = this.frames.get(frame % this.frames.size());
			g.drawImage(f, x + this.x, y + this.y, null);
		}
	}
	
	public void paintFrame(Graphics2D g, int x, int y, int frame) {
		g.setColor(this.matte);
		g.fillRect(x, y, this.width, this.height);
		if (!this.frames.isEmpty()) {
			BufferedImage f = this.frames.get(frame % this.frames.size());
			g.drawImage(f, null, x + this.x, y + this.y);
		}
	}
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public Point getLocation() {
		return new Point(this.x, this.y);
	}
	public void setX(int x) {
		this.x = x;
		this.changed = true;
	}
	public void setY(int y) {
		this.y = y;
		this.changed = true;
	}
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.changed = true;
	}
	public void setLocation(Point location) {
		this.x = location.x;
		this.y = location.y;
		this.changed = true;
	}
	
	public int getWidth() { return this.width; }
	public int getHeight() { return this.height; }
	public Dimension getSize() {
		return new Dimension(this.width, this.height);
	}
	public void setWidth(int width) {
		this.width = width;
		this.changed = true;
	}
	public void setHeight(int height) {
		this.height = height;
		this.changed = true;
	}
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		this.changed = true;
	}
	public void setSize(Dimension size) {
		this.width = size.width;
		this.height = size.height;
		this.changed = true;
	}
	
	public Color getMatte() { return this.matte; }
	public void setMatte(Color matte) {
		this.matte = matte;
		this.changed = true;
	}
	
	public boolean isChanged() { return this.changed; }
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}