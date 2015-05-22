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
import tbr.game.Profile;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.TextButton;
import tbr.widgets.UpgradeButton;

public class UpgradesMenuState extends BasicGameState {

	private StateBasedGame game;
	
	private String titleText = "Upgrades";
	private UnicodeFont titleFont, starFont, descriptionFont;
	
	private UpgradeButton[] upgradeButtons = new UpgradeButton[8];
	private TextButton backButton, purchaseButton;
	
	private Image star;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
		//load fonts
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60), titleText);
		starFont = Utility.makeFont(new Font("verdana", Font.PLAIN, 42), java.awt.Color.WHITE);
		descriptionFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 28), java.awt.Color.WHITE);
		
		//create buttons
		backButton = new TextButton(container, "Back", Config.WIDTH / 2 - 500, 925, 40, 40, false);
		backButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				Profile.saveCurrentProfile();
				game.enterState(States.PLAY_MENU);
			}
		});
		purchaseButton = new TextButton(container, "Unlock", Config.WIDTH / 2 + 500, 925, 40, 40, false);
		purchaseButton.rightAlign();
		purchaseButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				for(int i = 0; i < upgradeButtons.length; i++) {
					if(upgradeButtons[i].isSelected()) {
						resetSelections(i);
						if(!Profile.currentUser.isUpgraded(upgradeButtons[i].getUpgrade()) && Profile.currentUser.getStars() >= upgradeButtons[i].getCost()) {
							if(upgradeButtons[i].getPrereq() == -1) {
								Profile.currentUser.unlockUpgrade(upgradeButtons[i].getUpgrade());
								Profile.currentUser.subtractStars(upgradeButtons[i].getCost());
								return;
							}
							else {
								if(Profile.currentUser.isUpgraded(upgradeButtons[i].getPrereq())) {
									Profile.currentUser.unlockUpgrade(upgradeButtons[i].getUpgrade());
									Profile.currentUser.subtractStars(upgradeButtons[i].getCost());
									return;
								}
							}
						}
					}
				}
				for(int i = 0; i < upgradeButtons.length; i++) {
					if(upgradeButtons[i].wasSelected()) {
						resetSelections(i);
						if(!Profile.currentUser.isUpgraded(upgradeButtons[i].getUpgrade()) && Profile.currentUser.getStars() >= upgradeButtons[i].getCost()) {
							if(!Profile.currentUser.isUpgraded(upgradeButtons[i].getUpgrade()) && Profile.currentUser.getStars() >= upgradeButtons[i].getCost()) {
								if(upgradeButtons[i].getPrereq() == -1) {
									Profile.currentUser.unlockUpgrade(upgradeButtons[i].getUpgrade());
									Profile.currentUser.subtractStars(upgradeButtons[i].getCost());
									return;
								}
								else {
									if(Profile.currentUser.isUpgraded(upgradeButtons[i].getPrereq())) {
										Profile.currentUser.unlockUpgrade(upgradeButtons[i].getUpgrade());
										Profile.currentUser.subtractStars(upgradeButtons[i].getCost());
										return;
									}
								}
							}
						}
					}
				}
			}
			public void resetSelections(int index) {
				for(int i = 0; i < upgradeButtons.length; i++) {
					if(index == i)
						upgradeButtons[i].setSelected(true);
					else
						upgradeButtons[i].setSelected(false);
					upgradeButtons[i].setWasSelected(false);
				}
			}
		});
		
		
		try {
			star = new Image("graphics/sprites/star.png");
			
			upgradeButtons[Profile.INCREASED_HEALTH] = new UpgradeButton(
					container,
					Config.WIDTH / 2 - 460,
					258,
					Profile.INCREASED_HEALTH,
					-1,
					5,
					new Image("graphics/sprites/upgrades/unlocked/health.png"),
					new Image("graphics/sprites/upgrades/locked/health.png")
			);
			upgradeButtons[Profile.SHIELD] = new UpgradeButton(
					container,
					Config.WIDTH / 2 - 220,
					258,
					Profile.SHIELD,
					-1,
					6,
					new Image("graphics/sprites/upgrades/unlocked/shield.png"),
					new Image("graphics/sprites/upgrades/locked/shield.png")
			);
			upgradeButtons[Profile.ADDITIONAL_SHIELD_CHARGE] = new UpgradeButton(
					container,
					Config.WIDTH / 2 + 20,
					258,
					Profile.ADDITIONAL_SHIELD_CHARGE,
					Profile.SHIELD,
					4,
					new Image("graphics/sprites/upgrades/unlocked/shield_charge.png"),
					new Image("graphics/sprites/upgrades/locked/shield_charge.png")
			);
			upgradeButtons[Profile.DECREASED_SHIELD_COOLDOWN] = new UpgradeButton(
					container,
					Config.WIDTH / 2 + 260,
					258,
					Profile.DECREASED_SHIELD_COOLDOWN,
					Profile.SHIELD,
					4,
					new Image("graphics/sprites/upgrades/unlocked/shield_cooldown.png"),
					new Image("graphics/sprites/upgrades/locked/shield_cooldown.png")
			);
			upgradeButtons[Profile.SLOW_LASER] = new UpgradeButton(
					container,
					Config.WIDTH / 2 - 460,
					491,
					Profile.SLOW_LASER,
					-1,
					9,
					new Image("graphics/sprites/upgrades/unlocked/laser.png"),
					new Image("graphics/sprites/upgrades/locked/laser.png")
			);
			upgradeButtons[Profile.MISSILE_UPPER] = new UpgradeButton(
					container,
					Config.WIDTH / 2 - 220,
					491,
					Profile.MISSILE_UPPER,
					-1,
					6,
					new Image("graphics/sprites/upgrades/unlocked/turret_upper.png"),
					new Image("graphics/sprites/upgrades/locked/turret_upper.png")
			);
			upgradeButtons[Profile.MISSILE_LOWER] = new UpgradeButton(
					container,
					Config.WIDTH / 2 + 20,
					491,
					Profile.MISSILE_LOWER,
					-1,
					6,
					new Image("graphics/sprites/upgrades/unlocked/turret_lower.png"),
					new Image("graphics/sprites/upgrades/locked/turret_lower.png")
			);
			upgradeButtons[Profile.INCREASE_SPECIAL_WORD_SPAWN] = new UpgradeButton(
					container,
					Config.WIDTH / 2 + 260,
					491,
					Profile.INCREASE_SPECIAL_WORD_SPAWN,
					-1,
					5,
					new Image("graphics/sprites/upgrades/unlocked/special.png"),
					new Image("graphics/sprites/upgrades/locked/special.png")
			);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		titleFont = null;
		starFont = null;
		descriptionFont = null;
		backButton.destroy(); backButton = null;
		purchaseButton.destroy(); purchaseButton = null;
		for(int i = 0; i < upgradeButtons.length; i++) {
			upgradeButtons[i].destroy();
			upgradeButtons[i] = null;
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		
		//draw title
		FontUtils.drawCenter(titleFont, titleText, Config.WIDTH / 2, 120, 0);
		
		//draw section devisions
		//g.drawRect(Config.WIDTH / 2 - 350, 225, 1000, 700);
		//g.drawRect(Config.WIDTH / 2 - 350, 725, 1000, 200);
		g.drawRect(Config.WIDTH / 2 - 500, 225, 1000, 700);
		g.drawRect(Config.WIDTH / 2 - 500, 725, 1000, 200);
		
		//draw upgrades
		for(int i = 0; i < upgradeButtons.length; i++)
			upgradeButtons[i].render(container, g);
		
		//draw description if an upgrade is selected
		for(int i = 0; i < upgradeButtons.length; i++) {
			if(upgradeButtons[i].isSelected())
				drawDescription(g, i);
		}
		
		//draw buttons
		backButton.render(container, g);
		purchaseButton.render(container, g);
		
		String stars = Integer.toString(Profile.currentUser.getStars());
		star.draw(Config.WIDTH / 2 - (star.getWidth() *.70f) - starFont.getWidth(stars) / 2, 935, star.getWidth() * .70f, star.getHeight() * .70f);
		FontUtils.drawCenter(starFont, stars, Config.WIDTH / 2, 925, 0);
	}
	
	public void drawDescription(Graphics g, int index) {
		switch(index) {
			case Profile.INCREASED_HEALTH:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Increases Health", Color.white);
				break;
			case Profile.SHIELD:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Shield that absorbs a word. Recharges after each hit.", Color.white);
				break;
			case Profile.ADDITIONAL_SHIELD_CHARGE:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Allows the shield to absorb an additional word before recharging.", Color.white);
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 755, "Prerequisite: Shield", Color.red);
				break;
			case Profile.DECREASED_SHIELD_COOLDOWN:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Decreases the cooldown of the shield.", Color.white);
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 755, "Prerequisite: Shield", Color.red);
				break;
			case Profile.SLOW_LASER:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Slows down any word that is currently being typed.", Color.white);
				break;
			case Profile.MISSILE_UPPER:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Adds a missile turret to the top of the ship which shoots down", Color.white);
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 755, "incoming words. Reloads after every shot.", Color.white);
				break;
			case Profile.MISSILE_LOWER:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Adds a missile turret to the bottom of the ship which shoots down", Color.white);
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 755, "incoming words. Reloads after every shot.", Color.white);
				break;
			case Profile.INCREASE_SPECIAL_WORD_SPAWN:
				descriptionFont.drawString(Config.WIDTH / 2 - 475, 725, "Doubles the chance that a special word will spawn in.", Color.white);
				break;
			default:
				throw new IllegalArgumentException("Error: not an upgrade");
		}
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
		return States.UPGRADES_MENU;
	}

}
