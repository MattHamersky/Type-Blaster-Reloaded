package tbr.states.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeOutTransition;

import tbr.states.States;

public class SplashScreenState extends BasicGameState {
	
	private Image logo;
	private long startTime = 0;
	private int duration = 3000; //3 seconds to display splash screen
	
	private StateBasedGame game;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		logo = new Image("images/splash_screen/logo.png");
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		logo.destroy(); logo = null;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		logo.draw(0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(startTime == 0) {
			startTime = container.getTime();
		}
		if((container.getTime() - startTime) > duration)
			transition(2000);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_SPACE || key == Input.KEY_ESCAPE || key == Input.KEY_ENTER)
			transition(0);
	}
	
	private void transition(int time) {
		game.enterState(States.MAIN_MENU, new FadeOutTransition(Color.black, time), null);
	}
	
	@Override
	public int getID() {
		return States.SPLASH_SCREEN;
	}

}
