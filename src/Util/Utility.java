package Util;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

@SuppressWarnings("unchecked")
public class Utility {

	private static Random rand = new Random(System.currentTimeMillis());
	
	/**
	 * returns random number between 0 (inclusive) and i (exclusive)
	 * @param i - upper bound (exclusive)
	 */
	public static int nextInt(int i) {
		return rand.nextInt(i);
	}
	
	/**
	 * returns random number between lower (inclusive) and upper (exclusive)
	 * @param lower - lower bound (inclusive)
	 * @param upper - upper bound (exclusive)
	 */
	public static int nextInt(int lower, int upper) {
		return rand.nextInt(upper-lower)+lower;
	}
	
	public static boolean isInBounds(Image img, int imgx, int imgy, int eventx, int eventy) {
		if(eventx > imgx && eventx < imgx + img.getWidth() &&
		   eventy > imgy && eventy < imgy + img.getHeight())
			return true;
		return false;
	}
	
	public static boolean isInBounds(int x, int y, int width, int height, int eventx, int eventy) {
		if(eventx > x && eventx < x + width &&
		   eventy > y && eventy < y + height)
			return true;
		return false;
	}
	
	public static int speedToPixels(int timeToTravel) {
		return 1760 / timeToTravel;
	}
	
	public static UnicodeFont makeFont(Font font, String text) {
		UnicodeFont newFont = new UnicodeFont(font);
		newFont.addGlyphs(text);
		newFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		newFont.setDisplayListCaching(false);
		try {
			newFont.loadGlyphs();
			return newFont;
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static UnicodeFont makeFont(Font font, String text, Color... colors) {
		UnicodeFont newFont = new UnicodeFont(font);
		newFont.addGlyphs(text);
		newFont.setDisplayListCaching(false);
		for(Color color : colors)
			newFont.getEffects().add(new ColorEffect(color));
		try {
			newFont.loadGlyphs();
			return newFont;
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static UnicodeFont makeFont(Font font) {
		UnicodeFont newFont = new UnicodeFont(font);
		newFont.addAsciiGlyphs();
		newFont.getEffects().add(new ColorEffect(java.awt.Color.white));
		newFont.setDisplayListCaching(false);
		try {
			newFont.loadGlyphs();
			return newFont;
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static UnicodeFont makeFont(Font font, Color... colors) {
		UnicodeFont newFont = new UnicodeFont(font);
		newFont.addAsciiGlyphs();
		newFont.setDisplayListCaching(false);
		for(Color color : colors)
			newFont.getEffects().add(new ColorEffect(color));
		try {
			newFont.loadGlyphs();
			return newFont;
		} catch (SlickException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void addColorToFont(UnicodeFont font, Color... colors) {
		for(Color color : colors)
			font.getEffects().add(new ColorEffect(color));
	}
	
}
