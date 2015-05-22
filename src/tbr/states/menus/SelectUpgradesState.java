package tbr.states.menus;

import java.awt.Font;

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
import tbr.game.Profile;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.SelectUpgradeButton;
import tbr.widgets.TextButton;

public class SelectUpgradesState extends BasicGameState {

	private StateBasedGame game;
	
	private static final String titleText = "Select Upgrades";
	private UnicodeFont titleFont;
	
	private SelectUpgradeButton[] upgradeButtons = new SelectUpgradeButton[8];
	private TextButton start;
	
	@Override
	public void enter(GameContainer container, final StateBasedGame game) {
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60), titleText);
		start = new TextButton(container, "Start", Config.WIDTH / 2, 720, 40, 40, true);
		start.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				game.enterState(States.GAME);
			}
		});
		
		try {
			//load buttons
			Image overlay = new Image("graphics/sprites/checkmark.png");
			upgradeButtons[Profile.INCREASED_HEALTH] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 - 460,
					258,
					Profile.INCREASED_HEALTH,
					new Image("graphics/sprites/upgrades/unlocked/health.png"),
					new Image("graphics/sprites/upgrades/grayscale/health.png"),
					overlay
			);
			upgradeButtons[Profile.SHIELD] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 - 220,
					258,
					Profile.SHIELD,
					new Image("graphics/sprites/upgrades/unlocked/shield.png"),
					new Image("graphics/sprites/upgrades/grayscale/shield.png"),
					overlay
			);
			upgradeButtons[Profile.ADDITIONAL_SHIELD_CHARGE] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 + 20,
					258,
					Profile.ADDITIONAL_SHIELD_CHARGE,
					new Image("graphics/sprites/upgrades/unlocked/shield_charge.png"),
					new Image("graphics/sprites/upgrades/grayscale/shield_charge.png"),
					overlay
			);
			upgradeButtons[Profile.DECREASED_SHIELD_COOLDOWN] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 + 260,
					258,
					Profile.DECREASED_SHIELD_COOLDOWN,
					new Image("graphics/sprites/upgrades/unlocked/shield_cooldown.png"),
					new Image("graphics/sprites/upgrades/grayscale/shield_cooldown.png"),
					overlay
			);
			upgradeButtons[Profile.SLOW_LASER] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 - 460,
					491,
					Profile.SLOW_LASER,
					new Image("graphics/sprites/upgrades/unlocked/laser.png"),
					new Image("graphics/sprites/upgrades/grayscale/laser.png"),
					overlay
			);
			upgradeButtons[Profile.MISSILE_UPPER] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 - 220,
					491,
					Profile.MISSILE_UPPER,
					new Image("graphics/sprites/upgrades/unlocked/turret_upper.png"),
					new Image("graphics/sprites/upgrades/grayscale/turret_upper.png"),
					overlay
			);
			upgradeButtons[Profile.MISSILE_LOWER] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 + 20,
					491,
					Profile.MISSILE_LOWER,
					new Image("graphics/sprites/upgrades/unlocked/turret_lower.png"),
					new Image("graphics/sprites/upgrades/grayscale/turret_lower.png"),
					overlay
			);
			upgradeButtons[Profile.INCREASE_SPECIAL_WORD_SPAWN] = new SelectUpgradeButton(
					container,
					Config.WIDTH / 2 + 260,
					491,
					Profile.INCREASE_SPECIAL_WORD_SPAWN,
					new Image("graphics/sprites/upgrades/unlocked/special.png"),
					new Image("graphics/sprites/upgrades/grayscale/special.png"),
					overlay
			);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		for(int i = 0; i < upgradeButtons.length; i++) {
			upgradeButtons[i].destroy();
			upgradeButtons[i] = null;
		}
		titleFont = null;
		start.destroy(); start = null;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//render title
		FontUtils.drawCenter(titleFont, titleText, Config.WIDTH / 2, 120, 0);
		
		//render buttons
		for(int i = 0; i < upgradeButtons.length; i++)
			upgradeButtons[i].render(container, g);
		
		//render start button
		start.render(container, g);
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
		return States.SELECT_UPGRADES;
	}

}
