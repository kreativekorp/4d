package com.kreative.fourdee.file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import com.kreative.fourdee.common.IO;
import com.kreative.fourdee.common.Numbers;

public class FileSystem implements List<File>, Comparable<FileSystem> {
	private ArrayList<File> files;
	private int defaultIndex;
	private String name;
	private int clusters;
	private boolean changed;
	
	public FileSystem(String name, int clusters) {
		this.files = new ArrayList<File>();
		this.defaultIndex = 0;
		this.name = (name != null) ? name : "";
		this.clusters = (clusters < 4) ? 4 : (clusters > 0xFFFF) ? 0xFFFF : clusters;
		this.changed = false;
	}
	
	public FileSystem(java.io.File file) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		read(raf);
		raf.close();
	}
	
	public FileSystem(RandomAccessFile raf) throws IOException {
		read(raf);
	}
	
	private void read(RandomAccessFile raf) throws IOException {
		/* Read First Directory Header */
		raf.seek(0L);
		int activeCount = raf.readUnsignedShort();
		/* int activeDefaultIndex = */ raf.readUnsignedShort();
		/* String activeName = */ IO.readFixedString(raf, 24);
		int activeClusters = raf.readUnsignedShort();
		int activeMagic = raf.readUnsignedShort();
		if (activeCount > 2031 || activeClusters < 4 || activeMagic != 0x04F5) {
			throw new IOException("Invalid magic values.");
		}
		
		/* Read Second Directory Header */
		raf.seek(0x10000L);
		int count = raf.readUnsignedShort();
		int defaultIndex = raf.readUnsignedShort();
		if (defaultIndex >= count) defaultIndex = count - 1;
		if (defaultIndex < 0) defaultIndex = 0;
		String name = IO.readFixedString(raf, 24);
		int clusters = raf.readUnsignedShort();
		int magic = raf.readUnsignedShort();
		if (count < activeCount || count > 2047 || clusters != activeClusters || magic != 0x04F5) {
			throw new IOException("Invalid magic values.");
		}
		
		/* Read Files */
		ArrayList<File> files = new ArrayList<File>();
		for (int i = 0; i < count; i++) files.add(new File(raf));
		
		/* Initialize FileSystem */
		this.files = files;
		this.defaultIndex = defaultIndex;
		this.name = name;
		this.clusters = clusters;
		this.changed = false;
	}
	
	public void writeFileSystem(java.io.File file, boolean eraseUnused) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "rwd");
		writeFileSystem(raf, eraseUnused);
		raf.close();
	}
	
	public void writeFileSystem(RandomAccessFile raf, boolean eraseUnused) throws IOException {
		/* Calculate Second Directory Header */
		int count = this.files.size();
		int defaultIndex = this.defaultIndex;
		if (defaultIndex >= count) defaultIndex = count - 1;
		if (defaultIndex < 0) defaultIndex = 0;
		String name = this.name;
		int clusters = this.clusters;
		if (count > 2047 || clusters < 4 || clusters > 0xFFFF) {
			throw new IOException("Invalid magic values.");
		}
		
		/* Calculate First Directory Header */
		int activeCount = 0;
		int activeDefaultIndex = 0;
		int clusterCount = 2;
		for (int i = 0; i < this.files.size(); i++) {
			File file = this.files.get(i);
			if (file.isActive()) {
				if (defaultIndex == i) activeDefaultIndex = activeCount;
				activeCount++;
			}
			file.setCluster(clusterCount);
			clusterCount += file.getClusterLength();
		}
		if (activeCount > 2031 || clusterCount > clusters) {
			throw new IOException("Invalid magic values.");
		}
		
		/* Write First Directory */
		raf.seek(0L);
		raf.writeShort(activeCount);
		raf.writeShort(activeDefaultIndex);
		IO.writeFixedString(raf, name, 24);
		raf.writeShort(clusters);
		raf.writeShort(0x04F5);
		for (File file : files)
			if (file.isActive())
				file.writeFileSystemHeader(raf);
		IO.writeEmptyBlocks(raf, 32, 2047 - activeCount);
		assert(raf.getFilePointer() == 0x10000L);
		
		/* Write Second Directory */
		raf.seek(0x10000L);
		raf.writeShort(count);
		raf.writeShort(defaultIndex);
		IO.writeFixedString(raf, name, 24);
		raf.writeShort(clusters);
		raf.writeShort(0x04F5);
		for (File file : files) file.writeFileSystemHeader(raf);
		IO.writeEmptyBlocks(raf, 32, 2047 - count);
		assert(raf.getFilePointer() == 0x20000L);
		
		/* Write Files */
		for (File file : files) file.writeFileSystemData(raf);
		
		/* Write Empty Space */
		if (eraseUnused || raf.length() < ((long)clusters << 16L)) {
			raf.seek((long)clusterCount << 16L);
			IO.writeEmptyBlocks(raf, 0x10000, clusters - clusterCount);
			assert(raf.getFilePointer() == ((long)clusters << 16L));
		}
		setChanged(false);
	}
	
	public int activeSize() {
		int activeCount = 0;
		for (File file : files)
			if (file.isActive())
				activeCount++;
		return activeCount;
	}
	
	public int getUsedClusterLength() {
		int clusterCount = 2;
		for (File file : files)
			clusterCount += file.getClusterLength();
		return clusterCount;
	}
	
	@Override
	public boolean add(File file) {
		if (this.files.add(file)) {
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void add(int index, File file) {
		this.files.add(index, file);
		if (defaultIndex >= index)
			setDefaultIndex(defaultIndex + 1);
		this.changed = true;
	}
	
	@Override
	public boolean addAll(Collection<? extends File> files) {
		if (this.files.addAll(files)) {
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends File> files) {
		if (this.files.addAll(index, files)) {
			if (defaultIndex >= index)
				setDefaultIndex(defaultIndex + files.size());
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void clear() {
		this.files.clear();
		this.defaultIndex = 0;
		this.changed = true;
	}
	
	@Override
	public boolean contains(Object file) {
		return this.files.contains(file);
	}
	@Override
	public boolean containsAll(Collection<?> files) {
		return this.files.containsAll(files);
	}
	@Override
	public File get(int index) {
		return this.files.get(index);
	}
	@Override
	public int indexOf(Object file) {
		return this.files.indexOf(file);
	}
	@Override
	public boolean isEmpty() {
		return this.files.isEmpty();
	}
	@Override
	public Iterator<File> iterator() {
		return this.files.iterator();
	}
	@Override
	public int lastIndexOf(Object file) {
		return this.files.lastIndexOf(file);
	}
	@Override
	public ListIterator<File> listIterator() {
		return this.files.listIterator();
	}
	@Override
	public ListIterator<File> listIterator(int index) {
		return this.files.listIterator(index);
	}
	
	@Override
	public boolean remove(Object file) {
		int index = this.files.indexOf(file);
		if (this.files.remove(file)) {
			if (defaultIndex >= index)
				setDefaultIndex(defaultIndex - 1);
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public File remove(int index) {
		File removed = this.files.remove(index);
		if (defaultIndex >= index)
			setDefaultIndex(defaultIndex - 1);
		this.changed = true;
		return removed;
	}
	
	@Override
	public boolean removeAll(Collection<?> files) {
		File def = getDefaultFile();
		if (this.files.removeAll(files)) {
			setDefaultFile(def);
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean retainAll(Collection<?> files) {
		File def = getDefaultFile();
		if (this.files.retainAll(files)) {
			setDefaultFile(def);
			this.changed = true;
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public File set(int index, File file) {
		File set = this.files.set(index, file);
		this.changed = true;
		return set;
	}
	
	@Override
	public int size() {
		return this.files.size();
	}
	@Override
	public List<File> subList(int start, int end) {
		return this.files.subList(start, end);
	}
	@Override
	public Object[] toArray() {
		return this.files.toArray();
	}
	@Override
	public <T> T[] toArray(T[] array) {
		return this.files.toArray(array);
	}
	
	public int getDefaultIndex() { return this.defaultIndex; }
	public void setDefaultIndex(int defaultIndex) {
		int max = files.size() - 1;
		if (defaultIndex > max) defaultIndex = max;
		if (defaultIndex < 0) defaultIndex = 0;
		this.defaultIndex = defaultIndex;
		this.changed = true;
	}
	
	public File getDefaultFile() {
		if (defaultIndex < 0) return null;
		if (defaultIndex >= files.size()) return null;
		return files.get(defaultIndex);
	}
	public void setDefaultFile(File file) {
		int defaultIndex = files.indexOf(file);
		if (defaultIndex < 0) defaultIndex = 0;
		this.defaultIndex = defaultIndex;
		this.changed = true;
	}
	
	public String getName() { return this.name; }
	public void setName(String name) {
		if (name == null) name = "";
		this.name = name;
		this.changed = true;
	}
	
	public int getLength() {
		return Numbers.clustersToInt(this.clusters);
	}
	public void setLength(int length) {
		length = Numbers.intToClusters(length);
		if (length < 4) length = 4;
		this.clusters = length;
		this.changed = true;
	}
	
	public int getClusterLength() { return this.clusters; }
	public void setClusterLength(int length) {
		if (length > 0xFFFF) length = 0xFFFF;
		if (length < 4) length = 4;
		this.clusters = length;
		this.changed = true;
	}
	
	public boolean isChanged() {
		if (this.changed) return true;
		for (File file : files)
			if (file.isChanged())
				return true;
		return false;
	}
	public void setChanged(boolean changed) {
		this.changed = changed;
		if (!changed)
			for (File file : files)
				file.setChanged(false);
	}
	
	@Override
	public int compareTo(FileSystem other) {
		return this.name.compareToIgnoreCase(other.name);
	}
}