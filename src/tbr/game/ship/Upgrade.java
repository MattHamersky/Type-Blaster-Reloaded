package tbr.game.ship;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;

public abstract class Upgrade {

	protected Ship ship;
	protected static UnicodeFont hudFont;
	
	static {
		hudFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 60));
	}
	
	public Upgrade(Ship ship) {
		this.ship = ship;
	}
	
	public void centerHUDText(Graphics g, int x, int y, int width, String text) {
		FontUtils.drawCenter(hudFont, text, x, y + 10, width, Color.blue);
	}
	
}
