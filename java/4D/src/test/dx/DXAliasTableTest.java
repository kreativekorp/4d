package test.dx;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import com.kreative.fourdee.dx.DXAliasTable;

public class DXAliasTableTest {
	public static void main(String[] args) throws IOException {
		for (String arg : args) {
			System.out.println(arg);
			File file = new File(arg);
			
			// Read TXT Original
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			byte[] original = new byte[(int)raf.length()];
			raf.readFully(original);
			raf.close();
			
			// Read TXT AliasTable
			DXAliasTable aliastable = new DXAliasTable();
			aliastable.readTXT(file);
			
			// Write/Read TXT Copy
			File temp = File.createTempFile("dxtxttest-", ".txt");
			aliastable.writeTXT(temp);
			raf = new RandomAccessFile(temp, "r");
			byte[] copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write/Read 4DX Original
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			aliastable.write4DX(raf);
			raf.seek(0L);
			original = new byte[(int)raf.length()];
			raf.readFully(original);
			
			// Read 4DX AliasTable
			raf.seek(0L);
			aliastable = new DXAliasTable();
			aliastable.read4DX(raf);
			raf.close();
			temp.delete();
			
			// Write/Read 4DX Copy
			temp = File.createTempFile("dx4dxtest-", ".4dx");
			raf = new RandomAccessFile(temp, "rwd");
			aliastable.write4DX(raf);
			raf.seek(0L);
			copy = new byte[(int)raf.length()];
			raf.readFully(copy);
			raf.close();
			temp.delete();
			
			// Assert Original == Copy
			System.out.println(Arrays.equals(original, copy) ? "PASSED" : "FAILED");
			
			// Write 4DX Fragment
			File out = new File(file.getParentFile(), file.getName() + ".4DX-fragment");
			raf = new RandomAccessFile(out, "rwd");
			raf.write(copy);
			raf.close();
			System.out.println("WRITTEN");
		}
	}
}