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
import tbr.game.Profile;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.Button;
import tbr.widgets.LevelButton;
import tbr.widgets.TextButton;

public class PlayMenuState extends BasicGameState {

	private StateBasedGame game;
	
	public static Level selectedLevel;
	public static String selectedLevelString;
	public static int selectedLevelIndex;
	public static int difficulty = 0; //0 = normal, 1 = legendary
	
	private final String titleText = "Play Now";
	private final String[] difficultyText = {"Normal", "Legendary"};
	private UnicodeFont titleFont, difficultyFont;
	private Button rightArrow, leftArrow;
	
	private LevelButton[] levels = new LevelButton[10];
	private LevelButton[] legendaryLevels = new LevelButton[10];
	
	private TextButton upgradeButton, backButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
		upgradeButton = new TextButton(container, "Upgrades", Config.WIDTH / 2 + 360, 805, 40, 40, false);
		upgradeButton.rightAlign();
		upgradeButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				game.enterState(States.UPGRADES_MENU);
			}
		});
		backButton = new TextButton(container, "Back", Config.WIDTH / 2 - 340, 805, 40, 40, false);
		backButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				game.enterState(States.MAIN_MENU);
			}
		});
		
		
		//load right and left difficulty arrows
		rightArrow = new Button(container,
								Config.WIDTH / 2 + 160,
								200,
								new Image("images/menu_buttons/right_arrow.png"),
								new Image("images/menu_buttons/right_arrow_armed.png"),
								new Image("images/menu_buttons/right_arrow_over.png"),
								null
		);
		leftArrow = new Button(container,
							   Config.WIDTH / 2 - 192,
							   200,
							   new Image("images/menu_buttons/right_arrow.png").getFlippedCopy(true, false),
							   new Image("images/menu_buttons/right_arrow_armed.png").getFlippedCopy(true, false),
							   new Image("images/menu_buttons/right_arrow_over.png").getFlippedCopy(true, false),
							   null
		);
		
		//load level lists
		Image star = new Image("graphics/sprites/star.png");
		Image silhoutte = new Image("graphics/sprites/star_silhoutte.png");
		Level[] tempLevels = Profile.currentUser.getAllLevels();
		Level[] tempLegendaryLevels = Profile.currentUser.getAllLegendaryLevels();
		
		for(int i = 0; i < levels.length; i++) {
			levels[i] = new LevelButton(container, Config.WIDTH / 2 - 325, 300 + i * 50, tempLevels[i].getName(), tempLevels[i].isUnlocked(), tempLevels[i].getStars(), star, silhoutte);
			legendaryLevels[i] = new LevelButton(container, Config.WIDTH / 2 - 325, 300 + i * 50, tempLegendaryLevels[i].getName(), tempLegendaryLevels[i].isUnlocked(), tempLegendaryLevels[i].getStars(), star, silhoutte);
		}
		
		//load fonts
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60), titleText);
		difficultyFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 40));
	}
	
	@Override
	public void leave(GameContainer container, final StateBasedGame game) throws SlickException {
		 titleFont = null;
		 difficultyFont = null;
		 leftArrow.destroy(); leftArrow = null;
		 rightArrow.destroy(); rightArrow = null;
		 upgradeButton.destroy(); upgradeButton = null;
		 backButton.destroy(); backButton = null;
		 
		 for(int i = 0; i < levels.length; i++) {
			 levels[i].destroy(); levels[i] = null;
			 legendaryLevels[i].destroy(); legendaryLevels[i] = null;
		 }
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		
		//draw title
		FontUtils.drawCenter(titleFont, titleText, Config.WIDTH / 2, 120, 0);
		
		//draw difficulty rectangle
		g.drawRect(Config.WIDTH / 2 - 150, 200, 300, 50);
		
		//draw difficulty
		FontUtils.drawCenter(difficultyFont, difficultyText[difficulty], Config.WIDTH / 2, 198, 0);
		
		//draw difficulty arrows
		rightArrow.render(container, g);
		leftArrow.render(container, g);
		
		//draw rectangle enclosing levels
		g.drawRect(Config.WIDTH / 2 - 340, 300, 700, 500);
		
		//draw level lists
		if(difficulty == 0)
			for(int i = 0; i < levels.length; i++)
				levels[i].render(container, g);
		else
			for(int i = 0; i < legendaryLevels.length; i++)
				legendaryLevels[i].render(container, g);
		
		//draw buttons
		upgradeButton.render(container, g);
		backButton.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//check if one of the difficulty arrows has been pressed
		if(rightArrow.isActivated()) {
			rightArrow.deactivate();
			difficulty++;
			if(difficulty > 1)
				difficulty = 0;
		}
		if(leftArrow.isActivated()) {
			leftArrow.deactivate();
			difficulty--;
			if(difficulty < 0)
				difficulty = 1;
		}
		
		//check if a level has been pressed
		LevelButton[] temp;
		if(difficulty == 0)
			temp = levels;
		else
			temp = legendaryLevels;
		for(int i = 0; i < temp.length; i++)
			if(temp[i].isSelected()) {
				if(difficulty == 0)
					selectedLevel = Profile.currentUser.getLevel(i);
				else
					selectedLevel = Profile.currentUser.getLegendaryLevel(i);
				selectedLevelIndex = i;
				selectedLevelString = temp[i].getLevelName();
				game.enterState(States.LEVEL_INFO);
			}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(States.MAIN_MENU);
	}

	@Override
	public int getID() {
		return States.PLAY_MENU;
	}
}
