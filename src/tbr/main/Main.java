package tbr.main;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import tbr.game.Profile;
import tbr.states.States;
import tbr.states.menus.*;


public class Main extends StateBasedGame {
	
	public Main(String gamename) {
		super(gamename);
		
		Profile.loadLastProfile(); //load the profile of the last person using the game
		
		//add game states
		addState(new SplashScreenState());
		addState(new MainMenuState());
		addState(new OptionsMenuState());
		addState(new TutorialState());
		addState(new PlayMenuState());
		addState(new UpgradesMenuState());
		addState(new ProfileState());
		addState(new GameState());
		addState(new LevelInfoState());
		addState(new SelectUpgradesState());
		enterState(States.SPLASH_SCREEN);
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		
	}
	
	public static void main(String[] args) {
		try	{
			Config.loadConfig(); //load user settings
			
			AppGameContainer appgc;
			appgc = new AppGameContainer(new ScalableGame(new Main("Type Blaster Reloaded"), 1920, 1080));
			if(Config.FIRST_LAUNCH) {
				Config.G_WIDTH = appgc.getScreenWidth();
				Config.G_HEIGHT = appgc.getScreenHeight();
				Config.FIRST_LAUNCH = false;
				Config.saveConfig();
			}
			appgc.setDisplayMode(Config.G_WIDTH, Config.G_HEIGHT, Config.IS_FULLSCREEN);
			appgc.setVSync(Config.VSYNC);
			appgc.setMinimumLogicUpdateInterval(Config.MIN_LOGIC_UPDATES);
			appgc.setShowFPS(Config.SHOW_FPS);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}