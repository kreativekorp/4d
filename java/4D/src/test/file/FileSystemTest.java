package test.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemTest {
	public static void main(String[] args) throws IOException {
		for (String arg : args) {
			System.out.print(arg + "... ");
			
			// Read Original
			File in = new File(arg);
			RandomAccessFile raf = new RandomAccessFile(in, "r");
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			raf.seek(0L);
			FileSystem fs = new FileSystem(raf);
			raf.close();
			
			// Write/Read Copy
			File temp = File.createTempFile("FileSystemTest-", ".4fs");
			raf = new RandomAccessFile(temp, "rwd");
			fs.writeFileSystem(raf, false);
			raf.seek(0L);
			byte[] copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
		}
	}
}