package tbr.animation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public interface Animation {

	public void update(GameContainer container, StateBasedGame game, int delta);
	public void render(GameContainer container, StateBasedGame game, Graphics g);
	public boolean isFinished();
	public void destroy() throws SlickException;
	
}
