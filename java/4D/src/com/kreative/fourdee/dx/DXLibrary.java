package com.kreative.fourdee.dx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DXLibrary<T extends DXNamedObject & Comparable<T>> implements List<T> {
	private final List<String> keys;
	private final List<T> values;
	
	public DXLibrary() {
		this.keys = new ArrayList<String>();
		this.values = new ArrayList<T>();
	}
	
	private DXLibrary(List<String> keys, List<T> values) {
		assert(keys.size() == values.size());
		this.keys = keys;
		this.values = values;
	}
	
	public int getIndex(String name) {
		return this.keys.indexOf(name);
	}
	public int getIndex(T t) {
		return this.values.indexOf(t);
	}
	
	public String getName(int index) {
		return this.keys.get(index);
	}
	public String getName(T t) {
		int index = this.values.indexOf(t);
		return (index >= 0) ? this.keys.get(index) : null;
	}
	
	public T getObject(int index) {
		return this.values.get(index);
	}
	public T getObject(String name) {
		int index = this.keys.indexOf(name);
		return (index >= 0) ? this.values.get(index) : null;
	}
	
	public void rekey() {
		this.keys.clear();
		for (T t : this.values) {
			this.keys.add(t.getName());
		}
		assert(this.keys.size() == this.values.size());
	}
	
	public void sort() {
		Collections.sort(this.values);
		rekey();
	}
	
	public void sort(Comparator<? super T> cmp) {
		Collections.sort(this.values, cmp);
		rekey();
	}
	
	@Override
	public boolean add(T t) {
		return this.keys.add(t.getName()) && this.values.add(t);
	}
	
	@Override
	public void add(int index, T t) {
		this.keys.add(index, t.getName());
		this.values.add(index, t);
	}
	
	@Override
	public boolean addAll(Collection<? extends T> ts) {
		boolean changed = false;
		for (T t : ts) {
			changed |= add(t);
		}
		return changed;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends T> ts) {
		boolean changed = false;
		for (T t : ts) {
			add(index++, t);
			changed |= true;
		}
		return changed;
	}
	
	@Override
	public void clear() {
		this.keys.clear();
		this.values.clear();
	}
	
	@Override
	public boolean contains(Object o) {
		return this.keys.contains(o) || this.values.contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c)
			if (!contains(o))
				return false;
		return true;
	}
	
	@Override
	public T get(int index) {
		return this.values.get(index);
	}
	
	@Override
	public int indexOf(Object o) {
		int o1 = this.keys.indexOf(o);
		int o2 = this.values.indexOf(o);
		if (o1 >= 0 && o2 >= 0) return Math.min(o1, o2);
		else if (o1 >= 0) return o1;
		else if (o2 >= 0) return o2;
		else return -1;
	}
	
	@Override
	public boolean isEmpty() {
		return this.keys.isEmpty() || this.values.isEmpty();
	}
	
	@Override
	public Iterator<T> iterator() {
		return Collections.unmodifiableList(this.values).iterator();
	}
	
	@Override
	public int lastIndexOf(Object o) {
		int o1 = this.keys.lastIndexOf(o);
		int o2 = this.values.lastIndexOf(o);
		if (o1 >= 0 && o2 >= 0) return Math.max(o1, o2);
		else if (o1 >= 0) return o1;
		else if (o2 >= 0) return o2;
		else return -1;
	}
	
	@Override
	public ListIterator<T> listIterator() {
		return Collections.unmodifiableList(this.values).listIterator();
	}
	
	@Override
	public ListIterator<T> listIterator(int index) {
		return Collections.unmodifiableList(this.values).listIterator(index);
	}
	
	@Override
	public boolean remove(Object o) {
		int index = indexOf(o);
		if (index >= 0) {
			remove(index);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public T remove(int index) {
		this.keys.remove(index);
		return this.values.remove(index);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			changed |= remove(o);
		}
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		for (int i = size() - 1; i >= 0; i--) {
			boolean ck = c.contains(this.keys.get(i));
			boolean cv = c.contains(this.values.get(i));
			if (!(ck || cv)) {
				remove(i);
				changed |= true;
			}
		}
		return changed;
	}
	
	@Override
	public T set(int index, T t) {
		this.keys.set(index, t.getName());
		return this.values.set(index, t);
	}
	
	@Override
	public int size() {
		return Math.min(this.keys.size(), this.values.size());
	}
	
	@Override
	public List<T> subList(int start, int end) {
		return new DXLibrary<T>(
			this.keys.subList(start, end),
			this.values.subList(start, end)
		);
	}
	
	@Override
	public Object[] toArray() {
		return this.values.toArray();
	}
	
	@Override
	public <A> A[] toArray(A[] a) {
		if (a instanceof String[]) {
			return this.keys.toArray(a);
		} else {
			return this.values.toArray(a);
		}
	}
}