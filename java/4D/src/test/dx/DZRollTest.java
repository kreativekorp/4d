package test.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import com.kreative.fourdee.common.Strings;
import com.kreative.fourdee.dx.DXDiceLibrary;
import com.kreative.fourdee.dx.DXImageLibrary;
import com.kreative.fourdee.dx.DXRoll;

public class DZRollTest {
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
			DXRoll roll = new DXRoll();
			roll.readROL(dlib, name, raf);
			raf.close();
			
			// Write/Read 4DZ Original
			File temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			roll.write4DZ(dlib, raf);
			raf.seek(0L);
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read 4DZ Roll
			raf.seek(0L);
			roll = new DXRoll();
			roll.read4DZ(dlib, raf);
			raf.close();
			temp.delete();
			
			// Write/Read 4DZ Copy
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			roll.write4DZ(dlib, raf);
			raf.seek(0L);
			byte[] copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write 4DZ
			File out = new File(file.getParentFile(), file.getName() + ".4DZ");
			raf = new RandomAccessFile(out, "rwd");
			raf.write(copy);
			raf.close();
			System.out.println("WRITTEN");
		}
	}
}