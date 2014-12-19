package com.kreative.fourdee.dx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import com.kreative.fourdee.common.IO;

public class DXAliasTable implements Map<String,String> {
	private TreeMap<String,String> aliases;
	
	public DXAliasTable() {
		this.aliases = new TreeMap<String,String>();
	}
	
	public void readTXT(File in) throws IOException {
		FileInputStream fis = new FileInputStream(in);
		Scanner scan = new Scanner(fis, "UTF-8");
		while (scan.hasNextLine()) {
			String line = scan.nextLine().trim();
			int o = line.indexOf('\t');
			if (o >= 0) {
				String key = line.substring(0, o).trim();
				String value = line.substring(o).trim();
				this.aliases.put(key, value);
			}
		}
		scan.close();
		fis.close();
	}
	
	public void writeTXT(File out) throws IOException {
		FileOutputStream fos = new FileOutputStream(out);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
		PrintWriter pw = new PrintWriter(osw, true);
		for (Map.Entry<String,String> e : this.aliases.entrySet()) {
			pw.print(e.getKey());
			pw.print("\t");
			pw.print(e.getValue());
			pw.println();
		}
		pw.flush(); osw.flush(); fos.flush();
		pw.close(); osw.close(); fos.close();
	}
	
	public void read4DX(RandomAccessFile in) throws IOException {
		long origin = in.getFilePointer();
		int count = in.readUnsignedShort();
		in.seek(origin + 0x0200L);
		while (count-- > 0) {
			String key = IO.readFixedString(in, 32);
			String value = IO.readFixedString(in, 32);
			this.aliases.put(key, value);
		}
	}
	
	public void write4DX(RandomAccessFile out) throws IOException {
		long origin = out.getFilePointer();
		out.writeShort(this.aliases.size());
		IO.writeCString(out, "ALIASES");
		out.seek(origin + 0x0200L);
		for (Map.Entry<String,String> e : this.aliases.entrySet()) {
			IO.writeFixedString(out, e.getKey(), 32);
			IO.writeFixedString(out, e.getValue(), 32);
		}
	}
	
	@Override public void clear() { this.aliases.clear(); }
	@Override public boolean containsKey(Object key) { return this.aliases.containsKey(key); }
	@Override public boolean containsValue(Object value) { return this.aliases.containsValue(value); }
	@Override public Set<Map.Entry<String,String>> entrySet() { return this.aliases.entrySet(); }
	@Override public String get(Object key) { return this.aliases.get(key); }
	@Override public boolean isEmpty() { return this.aliases.isEmpty(); }
	@Override public Set<String> keySet() { return this.aliases.keySet(); }
	@Override public String put(String key, String value) { return this.aliases.put(key, value); }
	@Override public void putAll(Map<? extends String,? extends String> map) { this.aliases.putAll(map); }
	@Override public String remove(Object key) { return this.aliases.remove(key); }
	@Override public int size() { return this.aliases.size(); }
	@Override public Collection<String> values() { return this.aliases.values(); }
}