package com.kreative.fourdee.makegci;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtilities {
	private static final Pattern RGBA_INTEGER_PATTERN = Pattern.compile("([0-9]+),([0-9]+),([0-9]+),([0-9]+)");
	private static final Pattern RGB_INTEGER_PATTERN = Pattern.compile("([0-9]+),([0-9]+),([0-9]+)");
	private static final Pattern RGBA_DECIMAL_PATTERN = Pattern.compile("([0-9]+\\.[0-9]+),([0-9]+\\.[0-9]+),([0-9]+\\.[0-9]+),([0-9]+\\.[0-9]+)");
	private static final Pattern RGB_DECIMAL_PATTERN = Pattern.compile("([0-9]+\\.[0-9]+),([0-9]+\\.[0-9]+),([0-9]+\\.[0-9]+)");
	private static final Pattern ARGB_LONG_HEX_PATTERN = Pattern.compile("(0x|#|\\$|)([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})");
	private static final Pattern RGB_LONG_HEX_PATTERN = Pattern.compile("(0x|#|\\$|)([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})([0-9A-Fa-f]{2})");
	private static final Pattern ARGB_SHORT_HEX_PATTERN = Pattern.compile("(0x|#|\\$|)([0-9A-Fa-f])([0-9A-Fa-f])([0-9A-Fa-f])([0-9A-Fa-f])");
	private static final Pattern RGB_SHORT_HEX_PATTERN = Pattern.compile("(0x|#|\\$|)([0-9A-Fa-f])([0-9A-Fa-f])([0-9A-Fa-f])");
	
	public static int parseColor(String s, Integer def) {
		s = s.trim().replaceAll("\\s+", "");
		Matcher m;
		if ((m = RGBA_INTEGER_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(1)); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = Integer.parseInt(m.group(2)); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = Integer.parseInt(m.group(3)); if (b < 0) b = 0; if (b > 255) b = 255;
			int a = Integer.parseInt(m.group(4)); if (a < 0) a = 0; if (a > 255) a = 255;
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = RGB_INTEGER_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(1)); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = Integer.parseInt(m.group(2)); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = Integer.parseInt(m.group(3)); if (b < 0) b = 0; if (b > 255) b = 255;
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = RGBA_DECIMAL_PATTERN.matcher(s)).matches()) {
			int r = (int)Math.round(Double.parseDouble(m.group(1)) * 255); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = (int)Math.round(Double.parseDouble(m.group(2)) * 255); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = (int)Math.round(Double.parseDouble(m.group(3)) * 255); if (b < 0) b = 0; if (b > 255) b = 255;
			int a = (int)Math.round(Double.parseDouble(m.group(4)) * 255); if (a < 0) a = 0; if (a > 255) a = 255;
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = RGB_DECIMAL_PATTERN.matcher(s)).matches()) {
			int r = (int)Math.round(Double.parseDouble(m.group(1)) * 255); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = (int)Math.round(Double.parseDouble(m.group(2)) * 255); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = (int)Math.round(Double.parseDouble(m.group(3)) * 255); if (b < 0) b = 0; if (b > 255) b = 255;
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = ARGB_LONG_HEX_PATTERN.matcher(s)).matches()) {
			int a = Integer.parseInt(m.group(2), 16);
			int r = Integer.parseInt(m.group(3), 16);
			int g = Integer.parseInt(m.group(4), 16);
			int b = Integer.parseInt(m.group(5), 16);
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = RGB_LONG_HEX_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(2), 16);
			int g = Integer.parseInt(m.group(3), 16);
			int b = Integer.parseInt(m.group(4), 16);
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = ARGB_SHORT_HEX_PATTERN.matcher(s)).matches()) {
			int a = Integer.parseInt(m.group(2), 16) * 17;
			int r = Integer.parseInt(m.group(3), 16) * 17;
			int g = Integer.parseInt(m.group(4), 16) * 17;
			int b = Integer.parseInt(m.group(5), 16) * 17;
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		}
		else if ((m = RGB_SHORT_HEX_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(2), 16) * 17;
			int g = Integer.parseInt(m.group(3), 16) * 17;
			int b = Integer.parseInt(m.group(4), 16) * 17;
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		}
		else if (s.equalsIgnoreCase("k")) return ColorConstants.BLACK;
		else if (s.equalsIgnoreCase("black")) return ColorConstants.BLACK;
		else if (s.equalsIgnoreCase("dx")) return ColorConstants.DARK_GRAY;
		else if (s.equalsIgnoreCase("darkgray")) return ColorConstants.DARK_GRAY;
		else if (s.equalsIgnoreCase("darkgrey")) return ColorConstants.DARK_GREY;
		else if (s.equalsIgnoreCase("iron")) return ColorConstants.IRON;
		else if (s.equalsIgnoreCase("n")) return ColorConstants.BROWN;
		else if (s.equalsIgnoreCase("brown")) return ColorConstants.BROWN;
		else if (s.equalsIgnoreCase("x")) return ColorConstants.GRAY;
		else if (s.equalsIgnoreCase("gray")) return ColorConstants.GRAY;
		else if (s.equalsIgnoreCase("grey")) return ColorConstants.GREY;
		else if (s.equalsIgnoreCase("silver")) return ColorConstants.SILVER;
		else if (s.equalsIgnoreCase("lx")) return ColorConstants.LIGHT_GRAY;
		else if (s.equalsIgnoreCase("lightgray")) return ColorConstants.LIGHT_GRAY;
		else if (s.equalsIgnoreCase("lightgrey")) return ColorConstants.LIGHT_GREY;
		else if (s.equalsIgnoreCase("w")) return ColorConstants.WHITE;
		else if (s.equalsIgnoreCase("white")) return ColorConstants.WHITE;
		else if (s.equalsIgnoreCase("dr")) return ColorConstants.MAROON;
		else if (s.equalsIgnoreCase("maroon")) return ColorConstants.MAROON;
		else if (s.equalsIgnoreCase("darkred")) return ColorConstants.MAROON;
		else if (s.equalsIgnoreCase("do")) return ColorConstants.UMBER;
		else if (s.equalsIgnoreCase("umber")) return ColorConstants.UMBER;
		else if (s.equalsIgnoreCase("darkorange")) return ColorConstants.UMBER;
		else if (s.equalsIgnoreCase("dy")) return ColorConstants.OLIVE;
		else if (s.equalsIgnoreCase("olive")) return ColorConstants.OLIVE;
		else if (s.equalsIgnoreCase("darkyellow")) return ColorConstants.OLIVE;
		else if (s.equalsIgnoreCase("dg")) return ColorConstants.PINE;
		else if (s.equalsIgnoreCase("pine")) return ColorConstants.PINE;
		else if (s.equalsIgnoreCase("forest")) return ColorConstants.PINE;
		else if (s.equalsIgnoreCase("darkgreen")) return ColorConstants.PINE;
		else if (s.equalsIgnoreCase("dc")) return ColorConstants.TEAL;
		else if (s.equalsIgnoreCase("dt")) return ColorConstants.TEAL;
		else if (s.equalsIgnoreCase("teal")) return ColorConstants.TEAL;
		else if (s.equalsIgnoreCase("darkcyan")) return ColorConstants.TEAL;
		else if (s.equalsIgnoreCase("darkturquoise")) return ColorConstants.TEAL;
		else if (s.equalsIgnoreCase("darkterquoise")) return ColorConstants.TEAL;
		else if (s.equalsIgnoreCase("db")) return ColorConstants.NAVY;
		else if (s.equalsIgnoreCase("du")) return ColorConstants.NAVY;
		else if (s.equalsIgnoreCase("navy")) return ColorConstants.NAVY;
		else if (s.equalsIgnoreCase("darkblue")) return ColorConstants.NAVY;
		else if (s.equalsIgnoreCase("dv")) return ColorConstants.EGGPLANT;
		else if (s.equalsIgnoreCase("eggplant")) return ColorConstants.EGGPLANT;
		else if (s.equalsIgnoreCase("darkviolet")) return ColorConstants.EGGPLANT;
		else if (s.equalsIgnoreCase("dm")) return ColorConstants.PLUM;
		else if (s.equalsIgnoreCase("plum")) return ColorConstants.PLUM;
		else if (s.equalsIgnoreCase("darkmagenta")) return ColorConstants.PLUM;
		else if (s.equalsIgnoreCase("r")) return ColorConstants.RED;
		else if (s.equalsIgnoreCase("red")) return ColorConstants.RED;
		else if (s.equalsIgnoreCase("l")) return ColorConstants.SCARLET;
		else if (s.equalsIgnoreCase("f")) return ColorConstants.SCARLET;
		else if (s.equalsIgnoreCase("scarlet")) return ColorConstants.SCARLET;
		else if (s.equalsIgnoreCase("scarlett")) return ColorConstants.SCARLET;
		else if (s.equalsIgnoreCase("fire")) return ColorConstants.SCARLET;
		else if (s.equalsIgnoreCase("o")) return ColorConstants.ORANGE;
		else if (s.equalsIgnoreCase("orange")) return ColorConstants.ORANGE;
		else if (s.equalsIgnoreCase("e")) return ColorConstants.GOLD;
		else if (s.equalsIgnoreCase("j")) return ColorConstants.GOLD;
		else if (s.equalsIgnoreCase("gold")) return ColorConstants.GOLD;
		else if (s.equalsIgnoreCase("blond")) return ColorConstants.GOLD;
		else if (s.equalsIgnoreCase("blonde")) return ColorConstants.GOLD;
		else if (s.equalsIgnoreCase("y")) return ColorConstants.YELLOW;
		else if (s.equalsIgnoreCase("yellow")) return ColorConstants.YELLOW;
		else if (s.equalsIgnoreCase("h")) return ColorConstants.CHARTREUSE;
		else if (s.equalsIgnoreCase("chartreuse")) return ColorConstants.CHARTREUSE;
		else if (s.equalsIgnoreCase("chartruese")) return ColorConstants.CHARTREUSE;
		else if (s.equalsIgnoreCase("chartruse")) return ColorConstants.CHARTREUSE;
		else if (s.equalsIgnoreCase("g")) return ColorConstants.GREEN;
		else if (s.equalsIgnoreCase("green")) return ColorConstants.GREEN;
		else if (s.equalsIgnoreCase("a")) return ColorConstants.AQUAMARINE;
		else if (s.equalsIgnoreCase("q")) return ColorConstants.AQUAMARINE;
		else if (s.equalsIgnoreCase("aquamarine")) return ColorConstants.AQUAMARINE;
		else if (s.equalsIgnoreCase("c")) return ColorConstants.CYAN;
		else if (s.equalsIgnoreCase("t")) return ColorConstants.CYAN;
		else if (s.equalsIgnoreCase("cyan")) return ColorConstants.CYAN;
		else if (s.equalsIgnoreCase("aqua")) return ColorConstants.CYAN;
		else if (s.equalsIgnoreCase("turquoise")) return ColorConstants.CYAN;
		else if (s.equalsIgnoreCase("terquoise")) return ColorConstants.CYAN;
		else if (s.equalsIgnoreCase("z")) return ColorConstants.AZURE;
		else if (s.equalsIgnoreCase("d")) return ColorConstants.AZURE;
		else if (s.equalsIgnoreCase("azure")) return ColorConstants.AZURE;
		else if (s.equalsIgnoreCase("b")) return ColorConstants.BLUE;
		else if (s.equalsIgnoreCase("u")) return ColorConstants.BLUE;
		else if (s.equalsIgnoreCase("blue")) return ColorConstants.BLUE;
		else if (s.equalsIgnoreCase("i")) return ColorConstants.INDIGO;
		else if (s.equalsIgnoreCase("indigo")) return ColorConstants.INDIGO;
		else if (s.equalsIgnoreCase("v")) return ColorConstants.VIOLET;
		else if (s.equalsIgnoreCase("violet")) return ColorConstants.VIOLET;
		else if (s.equalsIgnoreCase("p")) return ColorConstants.PURPLE;
		else if (s.equalsIgnoreCase("purple")) return ColorConstants.PURPLE;
		else if (s.equalsIgnoreCase("m")) return ColorConstants.MAGENTA;
		else if (s.equalsIgnoreCase("magenta")) return ColorConstants.MAGENTA;
		else if (s.equalsIgnoreCase("s")) return ColorConstants.ROSE;
		else if (s.equalsIgnoreCase("rose")) return ColorConstants.ROSE;
		else if (s.equalsIgnoreCase("lr")) return ColorConstants.CORAL;
		else if (s.equalsIgnoreCase("coral")) return ColorConstants.CORAL;
		else if (s.equalsIgnoreCase("lightred")) return ColorConstants.CORAL;
		else if (s.equalsIgnoreCase("lo")) return ColorConstants.CORANGE;
		else if (s.equalsIgnoreCase("corange")) return ColorConstants.CORANGE;
		else if (s.equalsIgnoreCase("lightorange")) return ColorConstants.CORANGE;
		else if (s.equalsIgnoreCase("ly")) return ColorConstants.LEMON;
		else if (s.equalsIgnoreCase("lemon")) return ColorConstants.LEMON;
		else if (s.equalsIgnoreCase("lightyellow")) return ColorConstants.LEMON;
		else if (s.equalsIgnoreCase("lg")) return ColorConstants.LIME;
		else if (s.equalsIgnoreCase("lime")) return ColorConstants.LIME;
		else if (s.equalsIgnoreCase("lightgreen")) return ColorConstants.LIME;
		else if (s.equalsIgnoreCase("lc")) return ColorConstants.SKY;
		else if (s.equalsIgnoreCase("lt")) return ColorConstants.SKY;
		else if (s.equalsIgnoreCase("sky")) return ColorConstants.SKY;
		else if (s.equalsIgnoreCase("lightcyan")) return ColorConstants.SKY;
		else if (s.equalsIgnoreCase("lightturquoise")) return ColorConstants.SKY;
		else if (s.equalsIgnoreCase("lightterquoise")) return ColorConstants.SKY;
		else if (s.equalsIgnoreCase("lb")) return ColorConstants.FROST;
		else if (s.equalsIgnoreCase("lu")) return ColorConstants.FROST;
		else if (s.equalsIgnoreCase("frost")) return ColorConstants.FROST;
		else if (s.equalsIgnoreCase("lightblue")) return ColorConstants.FROST;
		else if (s.equalsIgnoreCase("lv")) return ColorConstants.LAVENDER;
		else if (s.equalsIgnoreCase("lavender")) return ColorConstants.LAVENDER;
		else if (s.equalsIgnoreCase("lavendar")) return ColorConstants.LAVENDER;
		else if (s.equalsIgnoreCase("lightviolet")) return ColorConstants.LAVENDER;
		else if (s.equalsIgnoreCase("lm")) return ColorConstants.PINK;
		else if (s.equalsIgnoreCase("pink")) return ColorConstants.PINK;
		else if (s.equalsIgnoreCase("lightmagenta")) return ColorConstants.PINK;
		else if (def != null) return def;
		else throw new NumberFormatException("Invalid color string: " + s);
	}
}