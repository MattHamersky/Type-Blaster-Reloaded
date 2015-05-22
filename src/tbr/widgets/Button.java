package tbr.widgets;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

import Util.Utility;

public class Button extends AbstractComponent {

	public static final int DISARMED = 0;
	public static final int ARMED = 1;
	public static final int OVER = 2;
	public static final int DISABLED = 3;
	private int currentState = 0;
	
	private boolean activated = false;
	
	Image[] states = new Image[4];
	
	int x, y;
	
	public Button(GUIContext container, int x, int y, Image disarmed, Image armed, Image over, Image disabled) {
		super(container);
		if(disarmed == null)
			throw new IllegalArgumentException("Disarmed image can not be null");
		
		this.x = x;
		this.y = y;
		
		states[DISARMED] = disarmed;
		if(armed == null)
			states[ARMED] = disarmed;
		else
			states[ARMED] = armed;
		
		if(over == null)
			states[OVER] = disarmed;
		else
			states[OVER] = over;
		
		if(disabled == null)
			//states[DISABLED] = Utility.toGrayScale(disarmed);
			;
		else
			states[DISABLED] = disabled;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if(currentState == DISABLED)
			return;
		
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(Utility.isInBounds(states[currentState], this.x, this.y, x, y))
				currentState = ARMED;
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		if(currentState == DISABLED)
			return;
		
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(Utility.isInBounds(states[currentState], this.x, this.y, x, y) && currentState == ARMED)
				activated = true;
			else
				currentState = DISARMED;
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		if(currentState == DISABLED)
			return;
		
		if(Utility.isInBounds(states[currentState], x, y, newx, newy) && currentState != ARMED)
			currentState = OVER;
		else
			currentState = DISARMED;
	}
	
	@Override
	public int getHeight() {
		return states[currentState].getHeight();
	}

	@Override
	public int getWidth() {
		return states[currentState].getWidth();
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
	public void render(GUIContext container, Graphics g) throws SlickException {
		states[currentState].draw(x, y);
	}

	@Override
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	public void deactivate() {
		activated = false;
		currentState = DISARMED;
	}
	
	public void destroy() throws SlickException {
		for(int i = 0; i < states.length; i++)
			if(states[i] != null && !states[i].isDestroyed())
				states[i].destroy();
		listeners.clear();
		setAcceptingInput(false);
	}
	
	public void enable() {
		currentState = DISARMED;
	}
	
	public void disable() {
		currentState = DISABLED;
	}
	
}
