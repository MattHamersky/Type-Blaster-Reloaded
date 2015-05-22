package tbr.widgets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import tbr.game.Profile;
import tbr.game.Upgrades;
import Util.Utility;

public class SelectUpgradeButton extends AbstractComponent {

	private static Color overColor = new Color(255, 255, 255, 100);
	
	private int x, y, width, height;
	private int upgrade;
	private boolean doesUserHaveUpgraded;
	private boolean isOver;
	
	private Image locked, unlocked, overlay;
	
	public SelectUpgradeButton(GUIContext container, int x, int y, int upgrade, Image unlocked, Image locked, Image overlay) {
		super(container);
		this.x = x;
		this.y = y;
		this.upgrade = upgrade;
		doesUserHaveUpgraded = Profile.currentUser.isUpgraded(upgrade);
		
		this.locked = locked;
		this.unlocked = unlocked;
		this.overlay = overlay;
		
		this.width = locked.getWidth();
		this.height = locked.getHeight();
		
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if(Utility.isInBounds(this.x, this.y, getWidth(), getHeight(), x, y) && doesUserHaveUpgraded) {
			notifyListeners();
			Upgrades.setUpgraded(upgrade, !Upgrades.isUpgraded(upgrade));
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(Utility.isInBounds(x, y, getWidth(), getHeight(), newx, newy))
			isOver = true;
		else
			isOver = false;
	}
	
	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
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
	public void render(GUIContext context, Graphics g) throws SlickException {
		g.setColor(overColor);
		if(doesUserHaveUpgraded) {
			unlocked.draw(x, y);
			if(isOver)
				g.fillRect(x, y, getWidth(), getHeight());
			if(Upgrades.isUpgraded(upgrade))
				overlay.draw(x, y);
		}
		else {
			locked.draw(x, y);
		}
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void destroy() throws SlickException {
		locked.destroy(); locked = null;
		unlocked.destroy(); unlocked = null;
		if(overlay != null)
			overlay.destroy();
		overlay = null;
		setAcceptingInput(false);
	}

}
