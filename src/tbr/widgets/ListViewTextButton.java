package tbr.widgets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import tbr.main.Config;

import Util.Utility;

public class ListViewTextButton extends TextButton {

	private boolean isSelected = false;
	private boolean wasSelected = false;
	
	private boolean enabled = true;
	
	public ListViewTextButton(GUIContext container, String text, int x, int y, int size, int overSize) {
		super(container, text, x, y, size, overSize, false);
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		if(enabled) {
			if(Utility.isInBounds(this.x, this.y, 900, getHeight(), x, y)) {
				notifyListeners();
				isSelected = true;
			}
			else {
				if(isSelected)
					wasSelected = true;
				else
					wasSelected = false;
				isSelected = false;
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(enabled) {
			if(Utility.isInBounds(x, y, 900, getHeight(), newx, newy))
				isOver = true;
			else
				isOver = false;
		}
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if(isOver || isSelected) {
			g.setColor(Color.white);
			g.fillRect(Config.WIDTH / 2 - 450, y-5, 900, 55);
			overFont.drawString(x, y, text, Color.black);
		}
		else {
			font.drawString(x, y, text, Color.white);
		}
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public boolean wasSelected() {
		return wasSelected;
	}
	
	public String getText() {
		return text;
	}

	public void disable() {
		isSelected = false;
		wasSelected = false;
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
