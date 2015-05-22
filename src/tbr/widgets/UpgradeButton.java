package tbr.widgets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import tbr.game.Profile;
import Util.Utility;

public class UpgradeButton extends AbstractComponent {

	private static Color overColor = new Color(255, 255, 255, 100);
	
	private int x, y;
	private int width, height;
	
	private boolean isOver = false;
	private boolean isSelected = false;
	private boolean wasSelected = false;
	
	private int upgrade, prereq, cost;
	private Image unlocked, locked;
	
	public UpgradeButton(GUIContext container, int x, int y, int upgrade, int prereq, int cost, Image unlocked, Image locked) {
		super(container);
		this.x = x;
		this.y = y;
		this.upgrade = upgrade;
		this.prereq = prereq;
		this.cost = cost;
		this.unlocked = unlocked;
		this.locked = locked;
		
		this.width = unlocked.getWidth();
		this.height = unlocked.getHeight();
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		if(Utility.isInBounds(this.x, this.y, getWidth(), getHeight(), x, y)) {
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
		if(isOver || isSelected)
			g.fillRect(x, y, getWidth(), getHeight());
		
		if(Profile.currentUser.isUpgraded(upgrade))
			unlocked.draw(x, y);
		else
			locked.draw(x, y);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getUpgrade() {
		return upgrade;
	}
	
	public int getPrereq() {
		return prereq;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void destroy() throws SlickException {
		unlocked.destroy(); unlocked = null;
		locked.destroy(); locked = null;
		listeners.clear();
		setAcceptingInput(false);
	}
	
	public void setSelected(boolean status) {
		isSelected = status;
	}
	
	public void setWasSelected(boolean status) {
		wasSelected = status;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public boolean wasSelected() {
		return wasSelected;
	}

}
