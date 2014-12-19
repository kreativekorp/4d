package com.kreative.fourdee.image;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

public class ImageIO {
	public static Image read(byte[] in) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(in);
		Image image = read(bin);
		bin.close();
		return image;
	}
	
	public static Image read(File in) throws IOException {
		FileInputStream fin = new FileInputStream(in);
		Image image = read(fin);
		fin.close();
		return image;
	}
	
	public static Image read(InputStream in) throws IOException {
		BufferedInputStream bin = new BufferedInputStream(in);
		DataInputStream din = new DataInputStream(bin);
		din.mark(8);
		int width = din.readUnsignedShort();
		int height = din.readUnsignedShort();
		int magic = din.readUnsignedByte();
		int delay = din.readUnsignedByte();
		din.reset();
		if (width == 0x4749 && height == 0x4638 && magic == 0x39 && delay == 0x61) {
			return readGIF(bin);
		} else if (width > 640 || height > 480 || magic != 0x10) {
			return readImageIO(bin);
		} else {
			return readGCI(din);
		}
	}
	
	private static Image readImageIO(InputStream in) throws IOException {
		BufferedImage frame = javax.imageio.ImageIO.read(in);
		if (frame == null) {
			return null;
		} else {
			Image image = new Image(frame.getWidth(), frame.getHeight());
			image.addFrame(frame, 0);
			return image;
		}
	}
	
	private static Image readGIF(BufferedInputStream in) throws IOException {
		GifDecoder gif = new GifDecoder();
		int status = gif.read(in);
		if (status != GifDecoder.STATUS_OK) {
			return null;
		} else {
			Dimension size = gif.getFrameSize();
			Image image = new Image(size.width, size.height);
			int count = gif.getFrameCount();
			for (int i = 0; i < count; i++) {
				BufferedImage frame = gif.getFrame(i);
				int delay = gif.getDelay(i);
				image.addFrame(frame, delay);
			}
			return image;
		}
	}
	
	private static Image readGCI(DataInputStream in) throws IOException {
		int width = in.readUnsignedShort();
		int height = in.readUnsignedShort();
		int magic = in.readUnsignedByte();
		int delay = in.readUnsignedByte();
		if (width > 640 || height > 480 || magic != 0x10) {
			return null;
		} else if (delay == 0) {
			Image image = new Image(width, height);
			BufferedImage frame = readGCIFrame(in, width, height);
			image.addFrame(frame, delay);
			image.setChanged(false);
			return image;
		} else {
			Image image = new Image(width, height);
			int count = in.readUnsignedShort();
			for (int i = 0; i < count; i++) {
				BufferedImage frame = readGCIFrame(in, width, height);
				image.addFrame(frame, delay);
			}
			image.setChanged(false);
			return image;
		}
	}
	
	private static BufferedImage readGCIFrame(DataInputStream in, int width, int height) throws IOException {
		BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int c = in.readUnsignedShort();
				int r = (c >> 11) & 0x1F;
				int g = (c >> 5) & 0x3F;
				int b = c & 0x1F;
				r = (r << 3) | (r >> 2);
				g = (g << 2) | (g >> 4);
				b = (b << 3) | (b >> 2);
				c = 0xFF000000 | (r << 16) | (g << 8) | b;
				frame.setRGB(x, y, c);
			}
		}
		return frame;
	}
	
	public static byte[] write(Image image, String format) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		boolean status = write(out, image, format);
		out.flush();
		out.close();
		return status ? out.toByteArray() : null;
	}
	
	public static boolean write(File out, Image image, String format) throws IOException {
		FileOutputStream fout = new FileOutputStream(out);
		boolean status = write(fout, image, format);
		fout.flush();
		fout.close();
		return status;
	}
	
	public static boolean write(OutputStream out, Image image, String format) throws IOException {
		if (format.equalsIgnoreCase("gci") || format.equalsIgnoreCase(".gci")) {
			return writeGCI(new DataOutputStream(out), image);
		} else if (format.equalsIgnoreCase("gif") || format.equalsIgnoreCase(".gif")) {
			return writeGIF(out, image);
		} else {
			return writeImageIO(out, image, format);
		}
	}
	
	private static boolean writeImageIO(OutputStream out, Image image, String format) throws IOException {
		int count = image.getFrameCount();
		if (count < 1) return false;
		int x = image.getX();
		int y = image.getY();
		int width = image.getWidth();
		int height = image.getHeight();
		Color matte = image.getMatte();
		BufferedImage frame = image.getFrame(0);
		frame = matteFrame(frame, x, y, width, height, matte);
		return javax.imageio.ImageIO.write(frame, format, out);
	}
	
	private static boolean writeGIF(OutputStream out, Image image) throws IOException {
		int count = image.getFrameCount();
		if (count < 1) return false;
		int x = image.getX();
		int y = image.getY();
		int width = image.getWidth();
		int height = image.getHeight();
		Color matte = image.getMatte();
		if (count < 2) {
			BufferedImage frame = image.getFrame(0);
			frame = matteFrame(frame, x, y, width, height, matte);
			return javax.imageio.ImageIO.write(frame, "gif", out);
		}
		AnimatedGifEncoder gif = new AnimatedGifEncoder();
		if (!gif.start(out)) return false;
		gif.setRepeat(0);
		for (int i = 0; i < count; i++) {
			gif.setDelay(image.getDelay(i));
			BufferedImage frame = image.getFrame(i);
			frame = matteFrame(frame, x, y, width, height, matte);
			if (!gif.addFrame(frame)) return false;
		}
		return gif.finish();
	}
	
	private static boolean writeGCI(DataOutputStream out, Image image) throws IOException {
		int count = image.getFrameCount();
		if (count < 1) return false;
		int x = image.getX();
		int y = image.getY();
		int width = image.getWidth();
		int height = image.getHeight();
		Color matte = image.getMatte();
		if (count < 2) {
			out.writeShort(width);
			out.writeShort(height);
			out.writeByte(0x10);
			out.writeByte(0);
			BufferedImage frame = image.getFrame(0);
			frame = matteFrame(frame, x, y, width, height, matte);
			writeGCIFrame(out, width, height, frame);
			return true;
		}
		int[] delays = new int[count];
		delays[0] = image.getDelay(0);
		BigInteger bigDelayBase = BigInteger.valueOf(delays[0]);
		for (int i = 1; i < count; i++) {
			delays[i] = image.getDelay(i);
			bigDelayBase = bigDelayBase.gcd(BigInteger.valueOf(delays[i]));
		}
		int delayBase = bigDelayBase.intValue();
		int frameCount = 0;
		for (int i = 0; i < count; i++) {
			delays[i] /= delayBase;
			frameCount += delays[i];
		}
		out.writeShort(width);
		out.writeShort(height);
		out.writeByte(0x10);
		out.writeByte(delayBase);
		out.writeShort(frameCount);
		for (int i = 0; i < count; i++) {
			BufferedImage frame = image.getFrame(i);
			frame = matteFrame(frame, x, y, width, height, matte);
			for (int j = 0; j < delays[i]; j++) {
				writeGCIFrame(out, width, height, frame);
			}
		}
		return true;
	}
	
	private static void writeGCIFrame(DataOutputStream out, int width, int height, BufferedImage frame) throws IOException {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int c = frame.getRGB(x, y);
				int a = (c >> 24) & 0xFF;
				int r = ((c >> 16) & 0xFF) * a / 255;
				int g = ((c >> 8) & 0xFF) * a / 255;
				int b = (c & 0xFF) * a / 255;
				c = ((r >> 3) << 11) | ((g >> 2) << 5) | (b >> 3);
				out.writeShort(c);
			}
		}
	}
	
	private static BufferedImage matteFrame(BufferedImage frame, int x, int y, int width, int height, Color matte) {
		BufferedImage newFrame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newFrame.createGraphics();
		g.setColor(matte);
		g.fillRect(0, 0, width, height);
		g.drawImage(frame, null, x, y);
		g.dispose();
		return newFrame;
	}
}