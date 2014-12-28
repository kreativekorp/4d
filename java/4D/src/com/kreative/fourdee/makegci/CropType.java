package com.kreative.fourdee.makegci;

import java.awt.Insets;
import java.awt.image.BufferedImage;
import com.kreative.fourdee.image.Image;

public enum CropType {
	NO_CROP,
	MAXIMUM_CROP,
	RECTANGLE_CROP,
	SQUARE_CROP;
	
	private static boolean rowEmpty(BufferedImage image, int row, int matte) {
		matte &= 0x00FFFFFF;
		for (int column = 0, width = image.getWidth(); column < width; column++) {
			int color = image.getRGB(column, row);
			if (!(
				((color & 0xFF000000) == 0) ||
				((color & 0x00FFFFFF) == matte)
			)) return false;
		}
		return true;
	}
	
	private static boolean columnEmpty(BufferedImage image, int column, int matte) {
		matte &= 0x00FFFFFF;
		for (int row = 0, height = image.getHeight(); row < height; row++) {
			int color = image.getRGB(column, row);
			if (!(
				((color & 0xFF000000) == 0) ||
				((color & 0x00FFFFFF) == matte)
			)) return false;
		}
		return true;
	}
	
	private static Insets calculateInsets(BufferedImage image, int matte) {
		int w = image.getWidth(); int w1 = w - 1;
		int h = image.getHeight(); int h1 = h - 1;
		Insets i = new Insets(0, 0, 0, 0);
		while (i.top < h && rowEmpty(image, i.top, matte)) i.top++;
		while (i.bottom < h && rowEmpty(image, h1-i.bottom, matte)) i.bottom++;
		while (i.left < w && columnEmpty(image, i.left, matte)) i.left++;
		while (i.right < w && columnEmpty(image, w1-i.right, matte)) i.right++;
		return i;
	}
	
	private static Insets minInsets(Insets a, Insets b) {
		if (a == null) return b;
		if (b == null) return a;
		return new Insets(
			Math.min(a.top, b.top),
			Math.min(a.left, b.left),
			Math.min(a.bottom, b.bottom),
			Math.min(a.right, b.right)
		);
	}
	
	private static Insets calculateInsets(Image image) {
		int x = image.getX(), y = image.getY();
		int w = image.getWidth(), h = image.getHeight();
		int matte = image.getMatte().getRGB();
		Insets i = new Insets(h, w, h, w);
		for (int f = 0, fc = image.getFrameCount(); f < fc; f++) {
			BufferedImage fb = image.getFrame(f);
			Insets fi = calculateInsets(fb, matte);
			fi.left += x; fi.top += y;
			fi.right += w - x - fb.getWidth();
			fi.bottom += h - y - fb.getHeight();
			i = minInsets(i, fi);
		}
		return i;
	}
	
	private static Insets rectangleInsets(Insets i) {
		int tb = Math.min(i.top, i.bottom);
		int lr = Math.min(i.left, i.right);
		return new Insets(tb, lr, tb, lr);
	}
	
	private static Insets squareInsets(Insets i) {
		int tb = Math.min(i.top, i.bottom);
		int lr = Math.min(i.left, i.right);
		int s = Math.min(tb, lr);
		return new Insets(s, s, s, s);
	}
	
	private static Image cropImage(Image image, Insets i) {
		image.setX(image.getX() - i.left);
		image.setY(image.getY() - i.top);
		image.setWidth(image.getWidth() - i.left - i.right);
		image.setHeight(image.getHeight() - i.top - i.bottom);
		return image;
	}
	
	public Image cropImage(Image image) {
		switch (this) {
		default: return image;
		case MAXIMUM_CROP: return cropImage(image, calculateInsets(image));
		case RECTANGLE_CROP: return cropImage(image, rectangleInsets(calculateInsets(image)));
		case SQUARE_CROP: return cropImage(image, squareInsets(calculateInsets(image)));
		}
	}
	
	public static CropType parseCropType(String string) {
		string = string.replaceAll("[^A-Za-z0-9]+", "").toUpperCase();
		string = string.replaceAll("(CLIP|CROP)(PING)?$", "");
		if (string.equals("N") || string.equals("NORM") || string.equals("NORMAL")) return CropType.MAXIMUM_CROP;
		if (string.equals("M") || string.equals("MAX") || string.equals("MAXIMUM")) return CropType.MAXIMUM_CROP;
		if (string.equals("R") || string.equals("RECT") || string.equals("RECTANGLE")) return CropType.RECTANGLE_CROP;
		if (string.equals("S") || string.equals("SQ") || string.equals("SQUARE")) return CropType.SQUARE_CROP;
		return CropType.NO_CROP;
	}
}