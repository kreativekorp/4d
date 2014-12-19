package test.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import com.kreative.fourdee.dx.DXDatabase;

public class DXDatabaseTest {
	public static void main(String[] args) throws IOException {
		for (String arg : args) {
			// Read Directory Original
			System.out.println(arg);
			File file = new File(arg);
			DXDatabase db1 = new DXDatabase();
			db1.readDirectory(file);
			
			// Write/Read 4DX Original
			File temp = File.createTempFile("dx4dxtest-", ".4dx");
			RandomAccessFile raf = new RandomAccessFile(temp, "rwd");
			db1.write4DX(raf);
			raf.seek(0L);
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read 4DX Database
			raf.seek(0L);
			DXDatabase db2 = new DXDatabase();
			db2.read4DX(raf);
			raf.close();
			temp.delete();
			
			// Write/Read 4DX Copy
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			db2.write4DX(raf);
			raf.seek(0L);
			byte[] copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write 4DX Fragment
			File out = new File(file.getParentFile(), file.getName() + ".4DX");
			raf = new RandomAccessFile(out, "rwd");
			raf.write(copy);
			raf.close();
			System.out.println("WRITTEN");
		}
	}
}