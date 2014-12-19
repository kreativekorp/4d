package test.dx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import javax.imageio.ImageIO;
import com.kreative.fourdee.common.Numbers;
import com.kreative.fourdee.dx.DXImage;
import com.kreative.fourdee.dx.DXImageLibrary;
import com.kreative.fourdee.image.Image;

public class DXImageLibraryTest {
	public static void main(String[] args) throws IOException {
		System.out.println("Read File System Original...");
		DXImageLibrary lib = new DXImageLibrary();
		for (String arg : args) lib.readFile(new File(arg));
		lib.sort();
		
		System.out.println("Write/Read 4DX Original...");
		File temp = File.createTempFile("dx4dxtest-", ".4dx");
		RandomAccessFile raf = new RandomAccessFile(temp, "rwd");
		lib.write4DXData(raf);
		raf.seek(0L);
		byte[] original = new byte[(int)raf.length()];
		raf.readFully(original);
		
		System.out.println("Read 4DX Library...");
		raf.seek(0L);
		DXImageLibrary lib2 = new DXImageLibrary();
		lib2.read4DX(raf, Numbers.longToClusters(raf.length()));
		raf.close();
		temp.delete();
		
		System.out.println("Write/Read 4DX Copy...");
		temp = File.createTempFile("dx4dxtest-", ".4dx");
		raf = new RandomAccessFile(temp, "rwd");
		lib2.write4DXData(raf);
		raf.seek(0L);
		byte[] copy = new byte[(int)raf.length()];
		raf.readFully(copy);
		raf.close();
		temp.delete();
		
		System.out.println("Assert Original == Copy...");
		System.out.println((lib.size() == lib2.size()) ? "PASSED" : "FAILED");
		System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
		
		System.out.println("Write PNG V4Vs...");
		File root;
		if (args.length == 1) {
			root = new File(args[0]);
			if (!root.isDirectory()) {
				root = root.getParentFile();
			}
		} else {
			root = new File(".");
		}
		for (DXImage image : lib2) {
			File out = new File(root, "XX-" + image.getName() + ".XXX.PNG");
			int w = 0;
			int h = 0;
			for (Image i : image.getImages()) {
				if (w != 0) w += 8;
				w += i.getWidth();
				if (i.getHeight() > h) h = i.getHeight();
			}
			BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			int x = 0;
			for (Image i : image.getImages()) {
				i.paintFrame(g, x, 0, 0);
				x += i.getWidth() + 8;
			}
			g.dispose();
			ImageIO.write(img, "png", out);
		}
		
		System.out.println("Write 4DX Fragments...");
		for (DXImage image : lib2) {
			File out = new File(root, "XX-" + image.getName() + ".XXX.4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			image.write4DX(raf);
			raf.close();
		}
		System.out.println("WRITTEN");
	}
}