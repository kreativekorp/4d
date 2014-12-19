package test.dx;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import javax.imageio.ImageIO;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.dx.DXLayout;
import com.kreative.fourdee.dx.DXLayoutPoint;

public class DXLayoutTest {
	public static void main(String[] args) throws IOException {
		for (String arg : args) {
			System.out.println(arg);
			File file = new File(arg);
			String name = Strings.stripExtension(file.getName());
			
			// Read DLO Original
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read DLO Layout
			raf.seek(0L);
			DXLayout layout = new DXLayout();
			layout.readDLO(name, raf);
			raf.close();
			
			// Write/Read DLO Copy
			File temp = File.createTempFile("dxdlotest-", ".dlo");
			raf = new RandomAccessFile(temp, "rwd");
			layout.writeDLO(raf);
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
			layout.write4DX(raf);
			raf.seek(0L);
			original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read 4DX Layout
			raf.seek(0L);
			layout = new DXLayout();
			layout.read4DX(raf);
			raf.close();
			temp.delete();
			
			// Write/Read 4DX Copy
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			layout.write4DX(raf);
			raf.seek(0L);
			copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write PNG V4V
			File out = new File(file.getParentFile(), file.getName() + ".PNG");
			writePNG(out, layout);
			
			// Write 4DX Fragment
			out = new File(file.getParentFile(), file.getName() + ".4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			raf.write(copy);
			raf.close();
			System.out.println("WRITTEN");
		}
	}
	
	public static void writePNG(File out, DXLayout layout) throws IOException {
		BufferedImage img = new BufferedImage((layout.getMaxCount() + 1) * 128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		Color[] bg = new Color[]{ Color.black, Color.blue };
		Color[] fg = new Color[]{ Color.red, Color.orange, Color.yellow, Color.white, Color.pink };
		for (int i = 0; i <= layout.getMaxCount(); i++) {
			g.setColor(bg[i % bg.length]);
			g.fillRect(i * 128, 0, 128, 128);
			for (int j = 0; j < layout.getCount(i); j++) {
				DXLayoutPoint p = layout.getPoint(i, j);
				g.setColor(fg[j % fg.length]);
				g.fillRect(i * 128 + p.getX(), p.getY(), p.getSize(), p.getSize());
			}
		}
		g.dispose();
		ImageIO.write(img, "png", out);
	}
}