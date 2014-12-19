package com.kreative.fourdee.common;

public class Strings {
	public static int parseInt(String s, int def) {
		try { return Integer.parseInt(s); }
		catch (NumberFormatException nfe) { return def; }
	}
	
	public static String spacePad(int i, int n) {
		String s = Integer.toString(i);
		while (s.length() < n) s = " " + s;
		return s;
	}
	
	public static String zeroPad(int i, int n) {
		String s = Integer.toString(i);
		while (s.length() < n) s = "0" + s;
		return s;
	}
	
	public static String zeroPadHex(int i, int n) {
		String s = Integer.toHexString(i).toUpperCase();
		while (s.length() < n) s = "0" + s;
		return s;
	}
	
	public static String zeroPadHex(long i, int n) {
		String s = Long.toHexString(i).toUpperCase();
		while (s.length() < n) s = "0" + s;
		return s;
	}
	
	public static String getExtension(String name, String def) {
		int o = name.lastIndexOf('.');
		if (o > 0 && o < (name.length() - 1)) {
			return name.substring(o + 1);
		} else {
			return def;
		}
	}
	
	public static String stripExtension(String name) {
		int o = name.lastIndexOf('.');
		if (o > 0) return name.substring(0, o);
		else return name;
	}
}