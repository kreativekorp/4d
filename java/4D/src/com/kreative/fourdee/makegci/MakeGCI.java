package com.kreative.fourdee.makegci;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import com.kreative.fourdee.common.ColorConstants;
import com.kreative.fourdee.common.ColorUtilities;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.image.Image;
import com.kreative.fourdee.image.ImageIO;

public class MakeGCI {
	public static void main(String[] args) {
		String outputFile = null;
		String outputDirectory = null;
		int width = 0, height = 0;
		Anchor anchor = Anchor.CENTER;
		int x = 0, y = 0;
		Color matte = Color.black;
		CropType crop = CropType.NO_CROP;
		int argi = 0;
		while (argi < args.length) {
			String arg = args[argi++];
			if (arg.startsWith("-")) {
				if (arg.equals("--help")) {
					System.out.println("-o <path> : Set output file.");
					System.out.println("-d <path> : Set output directory.");
					System.out.println("-w <num>  : Set canvas width.");
					System.out.println("-h <num>  : Set canvas height.");
					System.out.println("-a <str>  : Set position of image on canvas.");
					System.out.println("-x <num>  : Set X offset of image on canvas.");
					System.out.println("-y <num>  : Set Y offset of image on canvas.");
					System.out.println("-m <str>  : Set matte color.");
					System.out.println("-c <str>  : Set image cropping: none, max, rect, or square.");
				} else if (arg.equals("-o") && argi < args.length) {
					outputFile = args[argi++];
				} else if (arg.equals("-d") && argi < args.length) {
					outputDirectory = args[argi++];
				} else if (arg.equals("-w") && argi < args.length) {
					width = Strings.parseInt(args[argi++], 0);
				} else if (arg.equals("-h") && argi < args.length) {
					height = Strings.parseInt(args[argi++], 0);
				} else if (arg.equals("-a") && argi < args.length) {
					anchor = Anchor.parseAnchor(args[argi++]);
				} else if (arg.equals("-x") && argi < args.length) {
					x = Strings.parseInt(args[argi++], 0);
				} else if (arg.equals("-y") && argi < args.length) {
					y = Strings.parseInt(args[argi++], 0);
				} else if (arg.equals("-m") && argi < args.length) {
					matte = new Color(ColorUtilities.parseColor(args[argi++], ColorConstants.BLACK));
				} else if (arg.equals("-c") && argi < args.length) {
					crop = CropType.parseCropType(args[argi++]);
				} else {
					System.err.println("Invalid option: " + arg);
				}
			} else {
				File in = new File(arg);
				File out;
				if (outputFile != null) {
					out = new File(outputFile);
					outputFile = null;
				} else if (outputDirectory != null) {
					out = new File(outputDirectory, in.getName() + ".GCI");
				} else {
					out = new File(in.getParentFile(), in.getName() + ".GCI");
				}
				try {
					Image image = ImageIO.read(in);
					if (width > 0) image.setWidth(width);
					if (height > 0) image.setHeight(height);
					image.setX(anchor.getX(image.getWidth(), image.getFrame(0).getWidth()) + x);
					image.setY(anchor.getY(image.getHeight(), image.getFrame(0).getHeight()) + y);
					image.setMatte(matte);
					image = crop.cropImage(image);
					ImageIO.write(out, image, "gci");
				} catch (IOException ioe) {
					System.err.println("Error converting " + arg + ": " + ioe.getMessage());
				}
			}
		}
	}
}