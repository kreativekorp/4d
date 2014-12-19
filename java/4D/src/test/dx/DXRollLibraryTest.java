package test.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import com.kreative.fourdee.common.Numbers;
import com.kreative.fourdee.dx.DXDiceLibrary;
import com.kreative.fourdee.dx.DXImageLibrary;
import com.kreative.fourdee.dx.DXRoll;
import com.kreative.fourdee.dx.DXRollLibrary;

public class DXRollLibraryTest {
	public static void main(String[] args) throws IOException {
		System.out.println("Read Image Library...");
		DXImageLibrary ilib = new DXImageLibrary();
		for (String arg : args) ilib.readFile(new File(arg));
		
		System.out.println("Read Dice Library...");
		DXDiceLibrary dlib = new DXDiceLibrary();
		for (String arg : args) dlib.readFile(new File(arg));
		
		System.out.println("Read File System Original...");
		DXRollLibrary lib = new DXRollLibrary();
		for (String arg : args) lib.readFile(dlib, new File(arg));
		lib.sort();
		
		System.out.println("Write/Read 4DX Original...");
		File temp = File.createTempFile("dx4dxtest-", ".4dx");
		RandomAccessFile raf = new RandomAccessFile(temp, "rwd");
		lib.write4DXData(dlib, raf);
		raf.seek(0L);
		byte[] original = new byte[(int)raf.length()];
		raf.readFully(original);
		
		System.out.println("Read 4DX Library...");
		raf.seek(0L);
		DXRollLibrary lib2 = new DXRollLibrary();
		lib2.read4DX(dlib, raf, Numbers.longToClusters(raf.length()));
		raf.close();
		temp.delete();
		
		System.out.println("Write/Read 4DX Copy...");
		temp = File.createTempFile("dx4dxtest-", ".4dx");
		raf = new RandomAccessFile(temp, "rwd");
		lib2.write4DXData(dlib, raf);
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
		for (DXRoll roll : lib2) {
			File out = new File(root, roll.getName() + ".ROL.PNG");
			DXRollTest.writePNG(ilib, dlib, out, roll);
		}
		
		System.out.println("Write 4DX Fragments...");
		for (DXRoll roll : lib2) {
			File out = new File(root, roll.getName() + ".ROL.4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			roll.write4DX(dlib, raf);
			raf.close();
		}
		System.out.println("WRITTEN");
	}
}