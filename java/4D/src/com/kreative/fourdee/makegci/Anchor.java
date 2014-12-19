package com.kreative.fourdee.makegci;

public enum Anchor {
	CENTER, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;
	
	public int getX(int outerWidth, int innerWidth) {
		switch (this) {
		case NORTH_WEST: case WEST: case SOUTH_WEST: return 0;
		case NORTH_EAST: case EAST: case SOUTH_EAST: return outerWidth - innerWidth;
		default: return (outerWidth - innerWidth) / 2;
		}
	}
	
	public int getY(int outerHeight, int innerHeight) {
		switch (this) {
		case NORTH_WEST: case NORTH: case NORTH_EAST: return 0;
		case SOUTH_WEST: case SOUTH: case SOUTH_EAST: return outerHeight - innerHeight;
		default: return (outerHeight - innerHeight) / 2;
		}
	}
	
	public static Anchor parseAnchor(String string) {
		string = string.replaceAll("[^A-Za-z0-9]+", "").toUpperCase();
		if (string.equals("N") || string.equals("NORTH")) return Anchor.NORTH;
		if (string.equals("NE") || string.equals("NORTHEAST")) return Anchor.NORTH_EAST;
		if (string.equals("E") || string.equals("EAST")) return Anchor.EAST;
		if (string.equals("SE") || string.equals("SOUTHEAST")) return Anchor.SOUTH_EAST;
		if (string.equals("S") || string.equals("SOUTH")) return Anchor.SOUTH;
		if (string.equals("SW") || string.equals("SOUTHWEST")) return Anchor.SOUTH_WEST;
		if (string.equals("W") || string.equals("WEST")) return Anchor.WEST;
		if (string.equals("NW") || string.equals("NORTHWEST")) return Anchor.NORTH_WEST;
		if (string.equals("T") || string.equals("TOP")) return Anchor.NORTH;
		if (string.equals("R") || string.equals("RIGHT")) return Anchor.EAST;
		if (string.equals("B") || string.equals("BOTTOM")) return Anchor.SOUTH;
		if (string.equals("L") || string.equals("LEFT")) return Anchor.WEST;
		if (string.equals("TL") || string.equals("TOPLEFT")) return Anchor.NORTH_EAST;
		if (string.equals("TR") || string.equals("TOPRIGHT")) return Anchor.NORTH_WEST;
		if (string.equals("BR") || string.equals("BOTRIGHT") || string.equals("BOTTOMRIGHT")) return Anchor.SOUTH_WEST;
		if (string.equals("BL") || string.equals("BOTLEFT") || string.equals("BOTTOMLEFT")) return Anchor.SOUTH_EAST;
		return Anchor.CENTER;
	}
}