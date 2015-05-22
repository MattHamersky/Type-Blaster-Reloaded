package tbr.widgets;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import tbr.game.Level;

import Util.Utility;

public class LevelButton extends AbstractComponent {

	public static UnicodeFont uniFont;
	
	static {
		uniFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 32));
	}
	
	private String levelName;
	private boolean isUnlocked;
	private int numStars;
	
	private Image star;
	private Image silhouette;
	
	private int x, y; //already scaled
	private boolean isSelected = false;
	private boolean isOver = false;
	
	public LevelButton(GUIContext container, int x, int y, String levelName, boolean isUnlocked, int numStars, Image star, Image silhouette) {
		super(container);
		this.x = x;
		this.y = y;
		this.levelName = levelName;
		this.isUnlocked = isUnlocked;
		this.numStars = numStars;
		this.star = star;
		this.silhouette = silhouette;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
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
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(isUnlocked) {
				if(Utility.isInBounds(this.x, this.y, 780, 50, x, y))
					isSelected = true;
				else
					isSelected = false;
			}
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(Utility.isInBounds(x, y, 780, 50, newx, newy))
			isOver = true;
		else
			isOver = false;
	}

	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if(isUnlocked) {
			if(isOver) {
				g.setColor(Color.white);
				g.fillRect(x - 15, y, 700, 50);
				uniFont.drawString(x, y, levelName, Color.black);
			}
			else {
				uniFont.drawString(x, y, levelName, Color.white);
			}
			for(int i = 0; i < numStars; i++)
				star.draw(x+500+i*60, y);
			for(int i = numStars; i < Level.MAX_STARS; i++)
				silhouette.draw(x+500+i*60, y);
		}
		else {
			uniFont.drawString(x, y, "???");
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isUnlocked() {
		return isUnlocked;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public String getLevelName() {
		return levelName;
	}

	public void destroy() throws SlickException {
		if(star != null && !star.isDestroyed())
			star.destroy();
		if(silhouette != null && !silhouette.isDestroyed())
			silhouette.destroy();
		
		listeners.clear();
		setAcceptingInput(false);
	}
	
}
