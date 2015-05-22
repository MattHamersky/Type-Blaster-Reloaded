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
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;
import tbr.game.Level;
import tbr.game.Upgrades;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.TextButton;

public class LevelInfoState extends BasicGameState {

	private StateBasedGame game;
	
	private UnicodeFont titleFont, scoreFont, descriptionFont;
	
	private Image checkbox, xbox;
	private Image star, silhoutte;
	
	private TextButton backButton, startButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
		//load fonts
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60));
		scoreFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 40));
		descriptionFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 32));
		
		//load buttons
		backButton = new TextButton(container, "Back", Config.WIDTH / 2 - 400, 890, 40, 40, false);
		backButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				game.enterState(States.PLAY_MENU);
			}
		});
		startButton = new TextButton(container, "Start", Config.WIDTH / 2 + 400, 890, 40, 40, false);
		startButton.rightAlign();
		startButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				Upgrades.loadUpgrades();
				game.enterState(States.SELECT_UPGRADES);
			}
		});
		
		//load images
		try {
			checkbox = new Image("graphics/sprites/checkbox.png");
			xbox = new Image("graphics/sprites/xbox.png");
			
			star = new Image("graphics/sprites/star.png");
			silhoutte = new Image("graphics/sprites/star_silhoutte.png");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		checkbox.destroy(); checkbox = null;
		xbox.destroy(); xbox = null;
		star.destroy(); star = null;
		silhoutte.destroy(); silhoutte = null;
		backButton.destroy(); backButton = null;
		startButton.destroy(); startButton = null;
		titleFont = null;
		scoreFont = null;
		descriptionFont = null;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Level level = PlayMenuState.selectedLevel;
		
		//draw level name
		String title = PlayMenuState.selectedLevelString;
		if(level.isLegendary())
			title += " (Legendary)";
		FontUtils.drawCenter(titleFont, title, Config.WIDTH / 2, 120, 0);
		
		//draw high scores
		String dHighScore = "Matt's High Score: " + level.getDevHighScore();
		String uHighScore = "Your High Score:   " + level.getScore();
		scoreFont.drawString(Config.WIDTH / 2 - scoreFont.getWidth(dHighScore) / 2, 200, uHighScore);
		scoreFont.drawString(Config.WIDTH / 2 - scoreFont.getWidth(dHighScore) / 2, 250, dHighScore);
		
		//draw stars
		for(int i = 0; i < level.getStars(); i++)
			star.draw(Config.WIDTH / 2 - 80 - star.getWidth() / 2 + i * 80, 320);
		for(int i = level.getStars(); i < Level.MAX_STARS; i++)
			silhoutte.draw(Config.WIDTH / 2 - 80 - star.getWidth() / 2 + i * 80, 320);
		
		//draw rectangle
		g.setColor(Color.white);
		g.drawRect(Config.WIDTH / 2 - 400, 390, 800, 500);
		
		
		//draw descriptions of stars
		descriptionFont.drawString(Config.WIDTH / 2 - 280, 430, "Finish the level");
		if(level.getStarCondition(Level.CONDITION_FINISH))
			checkbox.draw(Config.WIDTH / 2 - 350, 420);
		else
			xbox.draw(Config.WIDTH / 2 - 350, 420);
		
		descriptionFont.drawString(Config.WIDTH / 2 - 280, 510, "Take no damage during the level");
		if(level.getStarCondition(Level.CONDITION_NO_DAMAGE))
			checkbox.draw(Config.WIDTH / 2 - 350, 500);
		else
			xbox.draw(Config.WIDTH / 2 - 350, 500);
		
		descriptionFont.drawString(Config.WIDTH / 2 - 280, 585, "Achieve "+level.getMaxScore()+" points or higher");
		if(level.getStarCondition(Level.CONDITION_POINTS_REACHED))
			checkbox.draw(Config.WIDTH / 2 - 350, 575);
		else
			xbox.draw(Config.WIDTH / 2 - 350, 575);
		
		backButton.render(container, g);
		startButton.render(container, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(States.PLAY_MENU);
	}
	
	@Override
	public int getID() {
		return States.LEVEL_INFO;
	}

}
