package com.kreative.fourdee.common;

public class Numbers {
	public static int longToShort(long length) {
		if (length <= 0L) return 0;
		if (length >= 0xFFFFL) return 0xFFFF;
		return (int)length;
	}
	
	public static int longToInt(long length) {
		if (length <= 0L) return 0;
		if (length >= (long)Integer.MAX_VALUE) return Integer.MAX_VALUE;
		return (int)length;
	}
	
	public static int intToClusters(int length) {
		if (length <= 0) return 0;
		int clusters = (length >> 16);
		if ((length & 0xFFFF) != 0) clusters++;
		return clusters;
	}
	
	public static int longToClusters(long length) {
		if (length <= 0L) return 0;
		long clusters = (length >> 16L);
		if ((length & 0xFFFFL) != 0L) clusters++;
		if (clusters >= (long)Integer.MAX_VALUE) return Integer.MAX_VALUE;
		return (int)clusters;
	}
	
	public static int clustersToInt(int clusters) {
		if (clusters <= 0) return 0;
		if (clusters >= 0x8000) return Integer.MAX_VALUE;
		return clusters << 16;
	}
	
	public static long clustersToLong(int clusters) {
		if (clusters <= 0) return 0L;
		return (long)clusters << 16L;
	}
	
	public static int imageSizeToClusterOffset(int imageSize) {
		switch (imageSize) {
		case  2: return 0x0010;
		case  3: return 0x0020;
		case  4: return 0x0040;
		case  6: return 0x0080;
		case  8: return 0x0100;
		case 12: return 0x0200;
		case 16: return 0x0400;
		case 24: return 0x0800;
		case 32: return 0x1000;
		case 48: return 0x2000;
		case 64: return 0x4000;
		case 96: return 0x8000;
		default: return 0;
		}
	}
	
	public static int clusterOffsetToImageSize(int clusterOffset) {
		switch (clusterOffset) {
		case 0x0010: return  2;
		case 0x0020: return  3;
		case 0x0040: return  4;
		case 0x0080: return  6;
		case 0x0100: return  8;
		case 0x0200: return 12;
		case 0x0400: return 16;
		case 0x0800: return 24;
		case 0x1000: return 32;
		case 0x2000: return 48;
		case 0x4000: return 64;
		case 0x8000: return 96;
		default: return 0;
		}
	}
}