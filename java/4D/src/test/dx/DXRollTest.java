package test.dx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.dx.DXDice;
import com.kreative.fourdee.dx.DXDiceLibrary;
import com.kreative.fourdee.dx.DXDiceNumeric;
import com.kreative.fourdee.dx.DXDiceSimple;
import com.kreative.fourdee.dx.DXImageLibrary;
import com.kreative.fourdee.dx.DXRoll;
import com.kreative.fourdee.dx.DXRollDice;
import com.kreative.fourdee.image.Image;

public class DXRollTest {
	public static void main(String[] args) throws IOException {
		// Read Image Library
		DXImageLibrary ilib = new DXImageLibrary();
		ilib.readFile(new File(args[0]));
		
		// Read Dice Library
		DXDiceLibrary dlib = new DXDiceLibrary();
		dlib.readFile(new File(args[0]));
		
		for (int argi = 1; argi < args.length; argi++) {
			String arg = args[argi];
			System.out.println(arg);
			File file = new File(arg);
			String name = Strings.stripExtension(file.getName());
			
			// Read ROL Original
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read ROL Roll
			raf.seek(0L);
			DXRoll roll = new DXRoll();
			roll.readROL(dlib, name, raf);
			raf.close();
			
			// Write/Read ROL Copy
			File temp = File.createTempFile("dxroltest-", ".rol");
			raf = new RandomAccessFile(temp, "rwd");
			roll.writeROL(dlib, raf);
			raf.seek(0L);
			byte[] copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write/Read 4DX Original
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			roll.write4DX(dlib, raf);
			raf.seek(0L);
			original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read 4DX Roll
			raf.seek(0L);
			roll = new DXRoll();
			roll.read4DX(dlib, raf);
			raf.close();
			temp.delete();
			
			// Write/Read 4DX Copy
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			roll.write4DX(dlib, raf);
			raf.seek(0L);
			copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write PNG V4V
			File out = new File(file.getParentFile(), file.getName() + ".PNG");
			writePNG(ilib, dlib, out, roll);
			
			// Write 4DX Fragment
			out = new File(file.getParentFile(), file.getName() + ".4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			raf.write(copy);
			raf.close();
			System.out.println("WRITTEN");
		}
	}
	
	public static void writePNG(DXImageLibrary ilib, DXDiceLibrary dlib, File out, DXRoll roll) throws IOException {
		List<List<Image>> imageses = new ArrayList<List<Image>>();
		int maxImages = 0;
		for (DXRollDice rd : roll) {
			List<Image> images = new ArrayList<Image>();
			DXDice dice = dlib.getObject(rd.getDataFile());
			if (dice instanceof DXDiceSimple) {
				DXDiceSimple ds = ((DXDiceSimple)dice);
				switch (ds.getType()) {
				case 0:
					for (String imageCode : ds) {
						images.add(ilib.getObject(imageCode).getImage(32));
					}
					break;
				case 1:
					for (int i = 0; i < rd.getSides(); i++) {
						String imageCode = ds.get(i + rd.getMinimumValue() - ds.getBaseValue());
						images.add(ilib.getObject(imageCode).getImage(32));
					}
					break;
				}
			} else if (dice instanceof DXDiceNumeric) {
				DXDiceNumeric dn = ((DXDiceNumeric)dice);
				String imageCode = dn.getImageCode();
				Image b = ilib.getObject(imageCode).getImage(32);
				int[] xca = dn.getXCoords(32);
				for (int i = 0, v = rd.getMinimumValue(); i < rd.getSides(); i++, v++) {
					BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
					Graphics2D g = img.createGraphics();
					b.paintFrame(g, 0, 0, 0);
					if (v < 10) {
						ilib.getObject(dn.getImageCode(0, v)).getImage(32).paintFrame(g, xca[0], 0, 0);
					} else if (v < 100) {
						ilib.getObject(dn.getImageCode(1, v/10)).getImage(32).paintFrame(g, xca[1], 0, 0);
						ilib.getObject(dn.getImageCode(2, v%10)).getImage(32).paintFrame(g, xca[2], 0, 0);
					} else if (v < 1000) {
						ilib.getObject(dn.getImageCode(3, v/10/10)).getImage(32).paintFrame(g, xca[3], 0, 0);
						ilib.getObject(dn.getImageCode(4, v/10%10)).getImage(32).paintFrame(g, xca[4], 0, 0);
						ilib.getObject(dn.getImageCode(5, v%10%10)).getImage(32).paintFrame(g, xca[5], 0, 0);
					} else if (v < 10000) {
						ilib.getObject(dn.getImageCode(6, v/10/10/10)).getImage(32).paintFrame(g, xca[6], 0, 0);
						ilib.getObject(dn.getImageCode(7, v/10/10%10)).getImage(32).paintFrame(g, xca[7], 0, 0);
						ilib.getObject(dn.getImageCode(8, v/10%10%10)).getImage(32).paintFrame(g, xca[8], 0, 0);
						ilib.getObject(dn.getImageCode(9, v%10%10%10)).getImage(32).paintFrame(g, xca[9], 0, 0);
					}
					g.dispose();
					Image image = new Image(32, 32);
					image.addFrame(img, 0);
					images.add(image);
				}
			}
			imageses.add(images);
			if (images.size() > maxImages) maxImages = images.size();
		}
		int w = maxImages * 40 - 8;
		int h = imageses.size() * 40 - 8;
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		for (int yi = 0, y = 0; yi < imageses.size(); yi++, y += 40) {
			List<Image> images = imageses.get(yi);
			for (int xi = 0, x = 0; xi < images.size(); xi++, x += 40) {
				Image image = images.get(xi);
				image.paintFrame(g, x, y, 0);
			}
		}
		g.dispose();
		ImageIO.write(img, "png", out);
	}
}