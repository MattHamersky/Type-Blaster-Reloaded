package tbr.states.menus;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;
import tbr.game.Profile;
import tbr.game.effects.StardustEmitter;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.MainMenuButton;
import tbr.widgets.TextButton;

public class MainMenuState extends BasicGameState {
	
	private TextButton play, profile, options, tutorial, exit;
	private Image arrow, background;
	
	private TextField console;
	private UnicodeFont consoleFont, titleFont;
	
	private StardustEmitter stardust;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {}
	
	@Override
	public void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
		
		//create particles
		stardust = new StardustEmitter();
		
		//load images
		arrow = new Image("images/menu_buttons/main_menu_arrow.png");
		background = new Image("images/backgrounds/mm_background1.png");
		
		//create buttons
		play = new MainMenuButton(container, "PLAY", arrow, Config.WIDTH / 2, 350, 40, 40, true);
		play.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				game.enterState(States.PLAY_MENU);
			}	
		});
		profile = new MainMenuButton(container, "PROFILE", arrow, Config.WIDTH / 2, 400, 40, 40, true);
		profile.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				game.enterState(States.PROFILE_MENU);
			}	
		});
		tutorial = new MainMenuButton(container, "TUTORIAL", arrow, Config.WIDTH / 2, 450, 40, 40, true);
		tutorial.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				game.enterState(States.TUTORIAL);
			}	
		});
		options = new MainMenuButton(container, "OPTIONS", arrow, Config.WIDTH / 2, 500, 40, 40, true);
		options.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				game.enterState(States.OPTIONS_MENU);
			}	
		});
		exit = new MainMenuButton(container, "EXIT", arrow, Config.WIDTH / 2, 550, 40, 40, true);
		exit.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				Profile.saveCurrentProfile();
				container.exit();
			}	
		});
		
		//load fonts
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60));
		consoleFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 20));
		console = new TextField(container, consoleFont, 0, 0, Config.WIDTH, 30);
		console.setBorderColor(Color.gray);
		console.setFocus(false);
		console.setConsumeEvents(false);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		play.destroy(); play = null;
		profile.destroy(); profile = null;
		options.destroy(); options = null;
		tutorial.destroy(); tutorial = null;
		exit.destroy(); exit = null;
		arrow.destroy(); arrow = null;
		background.destroy(); background = null;
		stardust.destroy(); stardust = null;
		console = null;
		consoleFont = null;
		titleFont = null;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
//		Input input = container.getInput();
//		if(input.isKeyPressed(Input.KEY_SPACE)) {
//		}
		
		stardust.update(container, game, delta);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw(0, 0);
		
		stardust.render(container, game, g);
		
		g.drawString(Profile.currentUser.getName(), 20, Config.HEIGHT - 30);
		
		FontUtils.drawCenter(titleFont, "Type Blaster Reloaded", Config.WIDTH / 2, 250, 0, Color.white);
		
		play.render(container, g);
		profile.render(container, g);
		options.render(container, g);
		tutorial.render(container, g);
		exit.render(container, g);
		
		if(console.hasFocus())
			console.render(container, g);
		
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_GRAVE) {
			console.setFocus(!console.hasFocus());
			console.setText("");
		}
		
		if(key == Input.KEY_ENTER && console.hasFocus()) {
			evaluateConsoleInput();
			console.setFocus(false);
			console.setText("");
		}
	}
	
	private void evaluateConsoleInput() {
		String input = console.getText();
		if(input.equals("reset"))
			Profile.currentUser.resetEverything();
		else if(input.equals("to_the_moon"))
			Profile.currentUser.maxEverything();
		else if(input.equals("unlock_all_levels"))
			Profile.currentUser.unlockAllLevels();
		else if(input.equals("lock_all_upgrades"))
			Profile.currentUser.lockAllUpgrades();
		else if(input.equals("unlock_all_upgrades"))
			Profile.currentUser.unlockAllUpgrades();
	}
	
	@Override
	public int getID() {
		return States.MAIN_MENU;
	}

}
