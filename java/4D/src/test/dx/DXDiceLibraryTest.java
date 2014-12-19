package test.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import com.kreative.fourdee.common.Numbers;
import com.kreative.fourdee.dx.DXDice;
import com.kreative.fourdee.dx.DXDiceLibrary;
import com.kreative.fourdee.dx.DXImageLibrary;

public class DXDiceLibraryTest {
	public static void main(String[] args) throws IOException {
		System.out.println("Read Image Library...");
		DXImageLibrary ilib = new DXImageLibrary();
		for (String arg : args) ilib.readFile(new File(arg));
		
		System.out.println("Read File System Original...");
		DXDiceLibrary lib = new DXDiceLibrary();
		for (String arg : args) lib.readFile(new File(arg));
		lib.sort();
		
		System.out.println("Write/Read 4DX Original...");
		File temp = File.createTempFile("dx4dxtest-", ".4dx");
		RandomAccessFile raf = new RandomAccessFile(temp, "rwd");
		lib.write4DXData(ilib, raf);
		raf.seek(0L);
		byte[] original = new byte[(int)raf.length()];
		raf.readFully(original);
		
		System.out.println("Read 4DX Library...");
		raf.seek(0L);
		DXDiceLibrary lib2 = new DXDiceLibrary();
		lib2.read4DX(ilib, raf, Numbers.longToClusters(raf.length()));
		raf.close();
		temp.delete();
		
		System.out.println("Write/Read 4DX Copy...");
		temp = File.createTempFile("dx4dxtest-", ".4dx");
		raf = new RandomAccessFile(temp, "rwd");
		lib2.write4DXData(ilib, raf);
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
		for (DXDice dice : lib2) {
			File out = new File(root, dice.getName() + ".DIE.PNG");
			DXDiceTest.writePNG(ilib, out, dice);
		}
		
		System.out.println("Write 4DX Fragments...");
		for (DXDice dice : lib2) {
			File out = new File(root, dice.getName() + ".DIE.4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			dice.write4DX(ilib, raf);
			raf.close();
		}
		System.out.println("WRITTEN");
	}
}