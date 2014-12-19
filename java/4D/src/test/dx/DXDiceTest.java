package test.dx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import javax.imageio.ImageIO;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.dx.DXDice;
import com.kreative.fourdee.dx.DXDiceNumeric;
import com.kreative.fourdee.dx.DXDiceSimple;
import com.kreative.fourdee.dx.DXImage;
import com.kreative.fourdee.dx.DXImageLibrary;
import com.kreative.fourdee.image.Image;

public class DXDiceTest {
	public static void main(String[] args) throws IOException {
		// Read Image Library
		DXImageLibrary ilib = new DXImageLibrary();
		ilib.readFile(new File(args[0]));
		
		for (int argi = 1; argi < args.length; argi++) {
			String arg = args[argi];
			System.out.println(arg);
			File file = new File(arg);
			String name = Strings.stripExtension(file.getName());
			
			// Read DIE Original
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read DIE Dice
			raf.seek(0L);
			DXDice dice = DXDice.readDIE(name, raf);
			raf.close();
			
			// Write/Read DIE Copy
			File temp = File.createTempFile("dxdietest-", ".die");
			raf = new RandomAccessFile(temp, "rwd");
			dice.writeDIE(raf);
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
			dice.write4DX(ilib, raf);
			raf.seek(0L);
			original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read 4DX Dice
			raf.seek(0L);
			dice = DXDice.read4DX(ilib, raf);
			raf.close();
			temp.delete();
			
			// Write/Read 4DX Copy
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			dice.write4DX(ilib, raf);
			raf.seek(0L);
			copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write PNG V4V
			File out = new File(file.getParentFile(), file.getName() + ".PNG");
			writePNG(ilib, out, dice);
			
			// Write 4DX Fragment
			out = new File(file.getParentFile(), file.getName() + ".4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			raf.write(copy);
			raf.close();
			System.out.println("WRITTEN");
		}
	}
	
	public static void writePNG(DXImageLibrary ilib, File out, DXDice dice) throws IOException {
		if (dice instanceof DXDiceSimple) {
			DXDiceSimple ds = ((DXDiceSimple)dice);
			int w = ds.size() * 72 - 8;
			BufferedImage img = new BufferedImage(w, 64, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			int x = 0;
			for (String imageCode : ds) {
				DXImage dxi = ilib.getObject(imageCode);
				if (dxi == null) continue;
				Image i = dxi.getImage(64);
				if (i == null) continue;
				i.paintFrame(g, x, 0, 0);
				x += i.getWidth() + 8;
			}
			g.dispose();
			ImageIO.write(img, "png", out);
		} else if (dice instanceof DXDiceNumeric) {
			DXDiceNumeric dn = ((DXDiceNumeric)dice);
			int[] xca = dn.getXCoords(64);
			int h = xca.length * 72 - 8;
			BufferedImage img = new BufferedImage(712, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			Image b = ilib.getObject(dn.getImageCode()).getImage(64);
			for (int yi = 0, y = 0; yi < xca.length; yi++, y += 72) {
				for (int xi = 0, x = 0; xi < 10; xi++, x += 72) {
					b.paintFrame(g, x, y, 0);
					String imageCode = dn.getImageCode(yi, xi);
					DXImage dxi = ilib.getObject(imageCode);
					if (dxi == null) continue;
					Image i = dxi.getImage(64);
					if (i == null) continue;
					i.paintFrame(g, x + xca[yi], y, 0);
				}
			}
			g.dispose();
			ImageIO.write(img, "png", out);
		}
	}
}