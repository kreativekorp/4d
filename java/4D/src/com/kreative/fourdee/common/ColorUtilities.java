package com.kreative.fourdee.common;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtilities {
	private static final String CM = "\\s*,\\s*";
	private static final String CL = "\\s*:\\s*";
	private static final String OP = "\\s*\\(\\s*";
	private static final String CP = "\\s*\\)\\s*";
	private static final String OB = "\\s*\\[\\s*";
	private static final String CB = "\\s*\\]\\s*";
	private static final String INT = "([0-9]+)";
	private static final String FP = "([0-9]+\\.[0-9]+)";
	private static final String OX = "(0[Xx]|#|\\$)?";
	private static final String LX = "([0-9A-Fa-f]{2})";
	private static final String SX = "([0-9A-Fa-f])";
	private static final String CSS = "[Cc][Ss][Ss]";
	private static final String XKCD = "[Xx][Kk][Cc][Dd]";
	private static final String STR = "(.*?)";
	private static final Pattern RGBA_INTEGER_PATTERN = Pattern.compile("^"+INT+CM+INT+CM+INT+CM+INT+"$");
	private static final Pattern RGB_INTEGER_PATTERN = Pattern.compile("^"+INT+CM+INT+CM+INT+"$");
	private static final Pattern RGBA_DECIMAL_PATTERN = Pattern.compile("^"+FP+CM+FP+CM+FP+CM+FP+"$");
	private static final Pattern RGB_DECIMAL_PATTERN = Pattern.compile("^"+FP+CM+FP+CM+FP+"$");
	private static final Pattern ARGB_LONG_HEX_PATTERN = Pattern.compile("^"+OX+LX+LX+LX+LX+"$");
	private static final Pattern RGB_LONG_HEX_PATTERN = Pattern.compile("^"+OX+LX+LX+LX+"$");
	private static final Pattern ARGB_SHORT_HEX_PATTERN = Pattern.compile("^"+OX+SX+SX+SX+SX+"$");
	private static final Pattern RGB_SHORT_HEX_PATTERN = Pattern.compile("^"+OX+SX+SX+SX+"$");
	private static final Pattern CSS_COLOR_PATTERN_PAREN = Pattern.compile("^"+CSS+OP+STR+CP+"$");
	private static final Pattern CSS_COLOR_PATTERN_COLON = Pattern.compile("^"+CSS+CL+STR+"$");
	private static final Pattern XKCD_COLOR_PATTERN_PAREN = Pattern.compile("^"+XKCD+OP+STR+CP+"$");
	private static final Pattern XKCD_COLOR_PATTERN_COLON = Pattern.compile("^"+XKCD+CL+STR+"$");
	private static final Pattern MIXED_COLOR_PATTERN = Pattern.compile("^"+OB+STR+CB+"$");
	
	public static int parseColor(String s, Integer def) {
		s = s.replaceAll("\\s+", " ").trim();
		Matcher m;
		if ((m = RGBA_INTEGER_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(1)); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = Integer.parseInt(m.group(2)); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = Integer.parseInt(m.group(3)); if (b < 0) b = 0; if (b > 255) b = 255;
			int a = Integer.parseInt(m.group(4)); if (a < 0) a = 0; if (a > 255) a = 255;
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = RGB_INTEGER_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(1)); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = Integer.parseInt(m.group(2)); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = Integer.parseInt(m.group(3)); if (b < 0) b = 0; if (b > 255) b = 255;
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = RGBA_DECIMAL_PATTERN.matcher(s)).matches()) {
			int r = (int)Math.round(Double.parseDouble(m.group(1)) * 255); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = (int)Math.round(Double.parseDouble(m.group(2)) * 255); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = (int)Math.round(Double.parseDouble(m.group(3)) * 255); if (b < 0) b = 0; if (b > 255) b = 255;
			int a = (int)Math.round(Double.parseDouble(m.group(4)) * 255); if (a < 0) a = 0; if (a > 255) a = 255;
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = RGB_DECIMAL_PATTERN.matcher(s)).matches()) {
			int r = (int)Math.round(Double.parseDouble(m.group(1)) * 255); if (r < 0) r = 0; if (r > 255) r = 255;
			int g = (int)Math.round(Double.parseDouble(m.group(2)) * 255); if (g < 0) g = 0; if (g > 255) g = 255;
			int b = (int)Math.round(Double.parseDouble(m.group(3)) * 255); if (b < 0) b = 0; if (b > 255) b = 255;
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = ARGB_LONG_HEX_PATTERN.matcher(s)).matches()) {
			int a = Integer.parseInt(m.group(2), 16);
			int r = Integer.parseInt(m.group(3), 16);
			int g = Integer.parseInt(m.group(4), 16);
			int b = Integer.parseInt(m.group(5), 16);
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = RGB_LONG_HEX_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(2), 16);
			int g = Integer.parseInt(m.group(3), 16);
			int b = Integer.parseInt(m.group(4), 16);
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = ARGB_SHORT_HEX_PATTERN.matcher(s)).matches()) {
			int a = Integer.parseInt(m.group(2), 16) * 17;
			int r = Integer.parseInt(m.group(3), 16) * 17;
			int g = Integer.parseInt(m.group(4), 16) * 17;
			int b = Integer.parseInt(m.group(5), 16) * 17;
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = RGB_SHORT_HEX_PATTERN.matcher(s)).matches()) {
			int r = Integer.parseInt(m.group(2), 16) * 17;
			int g = Integer.parseInt(m.group(3), 16) * 17;
			int b = Integer.parseInt(m.group(4), 16) * 17;
			return 0xFF000000 | (r << 16) | (g << 8) | (b << 0);
		} else if ((m = CSS_COLOR_PATTERN_PAREN.matcher(s)).matches()) {
			return CSSColor.parse(m.group(1));
		} else if ((m = CSS_COLOR_PATTERN_COLON.matcher(s)).matches()) {
			return CSSColor.parse(m.group(1));
		} else if ((m = XKCD_COLOR_PATTERN_PAREN.matcher(s)).matches()) {
			return XKCDColor.parse(m.group(1));
		} else if ((m = XKCD_COLOR_PATTERN_COLON.matcher(s)).matches()) {
			return XKCDColor.parse(m.group(1));
		} else if ((m = MIXED_COLOR_PATTERN.matcher(s)).matches()) {
			int a = 0, r = 0, g = 0, b = 0, ad = 0, cd = 0;
			CharacterIterator ci = new StringCharacterIterator(m.group(1));
			char ch = ci.first();
			while (ch != CharacterIterator.DONE) {
				if (Character.isLetter(ch)) {
					String name = Character.toString(ch);
					ch = ci.next();
					int d = 0;
					while (Character.isDigit(ch)) {
						d = d * 10 + Character.getNumericValue(ch);
						ch = ci.next();
					}
					if (d < 1) d = 1;
					if (name.equalsIgnoreCase("t")) {
						ad += d;
					} else if (name.equalsIgnoreCase("q")) {
						a += 255 * d;
						ad += d;
					} else {
						int color = ColorConstants.parse(name);
						a += ((color >> 24) & 0xFF) * d;
						r += ((color >> 16) & 0xFF) * d;
						g += ((color >>  8) & 0xFF) * d;
						b += ((color >>  0) & 0xFF) * d;
						ad += d;
						cd += d;
					}
				} else {
					ch = ci.next();
				}
			}
			if (ad > 0) a /= ad;
			if (cd > 0) { r /= cd; g /= cd; b /= cd; }
			return (a << 24) | (r << 16) | (g << 8) | (b << 0);
		} else {
			int color = ColorConstants.parse(s);
			if (color != 0) return color;
			else if (def != null) return def;
			else throw new NumberFormatException("Invalid color string: " + s);
		}
	}
}