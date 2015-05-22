package tbr.widgets;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;

public class TextButton extends AbstractComponent {

	protected String text;
	protected UnicodeFont font, overFont;
	protected int x, y; //scaled already
	protected boolean center;
	protected boolean rightAlign = false; //overrides center
	
	protected boolean isOver = false;
	protected Color color = Color.white;
	protected Color overColor = Color.gray;
	
	protected boolean enabled = true;
	
	public TextButton(GUIContext container, String text, int x, int y, int size, int overSize, boolean center) {
		super(container);
		this.text = text;
		this.x = x;
		this.y = y;
		this.center = center;
		
		this.font = Utility.makeFont(new Font("Verdana", Font.PLAIN, size), text);
		this.overFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, overSize), text);
	}

	@Override
	public int getHeight() {
		if(isOver)
			return overFont.getHeight(text);
		else
			return font.getHeight(text);
		
	}

	@Override
	public int getWidth() {
		if(isOver)
			return overFont.getWidth(text);
		else
			return font.getWidth(text);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		if(enabled) {
			if(center) {
				if(Utility.isInBounds(this.x-getWidth()/2, this.y, getWidth(), getHeight(), x, y))
					notifyListeners();
			}
			else if(rightAlign) {
				if(Utility.isInBounds(this.x-getWidth(), this.y, getWidth(), getHeight(), x, y))
					notifyListeners();
			}
			else {
				if(Utility.isInBounds(this.x, this.y, getWidth(), getHeight(), x, y))
					notifyListeners();
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(enabled) {
			if(center) {
				if(Utility.isInBounds(x-getWidth()/2, y, getWidth(), getHeight(), newx, newy))
					isOver = true;
				else
					isOver = false;
			}
			else if(rightAlign) {
				if(Utility.isInBounds(x-getWidth(), y, getWidth(), getHeight(), newx, newy))
					isOver = true;
				else
					isOver = false;
			}
			else {
				if(Utility.isInBounds(x, y, getWidth(), getHeight(), newx, newy))
					isOver = true;
				else
					isOver = false;
			}
		}
	}

	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if(center) {
			if(isOver)
				FontUtils.drawCenter(overFont, text, x, y, 0, overColor);
			else
				FontUtils.drawCenter(font, text, x, y, 0, color);
		}
		else if(rightAlign) {
			if(isOver)
				FontUtils.drawRight(overFont, text, x, y, 0, overColor);
			else
				FontUtils.drawRight(font, text, x, y, 0, color);
		}
		else {
			if(isOver)
				overFont.drawString(x, y, text, overColor);
			else
				font.drawString(x, y, text, color);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void rightAlign() {
		rightAlign = true;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setOverColor(Color color) {
		this.overColor = color;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void destroy() {
		listeners.clear();
		setAcceptingInput(false);
	}
}
