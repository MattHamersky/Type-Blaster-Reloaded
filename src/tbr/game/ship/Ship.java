package tbr.game.ship;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import tbr.game.Profile;
import tbr.game.Upgrades;
import tbr.game.words.Word;
import tbr.states.menus.GameState;

public class Ship {

	private GameState game;
	
	private int maxHealth = 25;
	private int health = 25;
	
	private Image shipSprite;
	
	private UnicodeFont uniFont;
	
	//upgrades
	private Shield shield;
	private LaserBattery laserBattery;
	private MissileBattery missileBatteryUpper;
	private MissileBattery missileBatteryLower;
	
	private boolean hasBeenHit = false;
	
	public Ship(GameState game, UnicodeFont uniFont) {
		this.game = game;
		this.uniFont = uniFont;
		try {
			shipSprite = new Image("graphics/sprites/ship.png");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		shield = new Shield(this, 75, 970);
		laserBattery = new LaserBattery(this);
		missileBatteryUpper = new MissileBattery(this, 200, 970, true);
		missileBatteryLower = new MissileBattery(this, 325, 970, false);
		
		if(Upgrades.isUpgraded(Profile.INCREASED_HEALTH)) {
			maxHealth = 40;
			health = 40;
		}
	}
	
	public void destroy() throws SlickException {
		shipSprite.destroy(); shipSprite = null;
		shield.destroy(); shield = null;
		laserBattery.destroy(); laserBattery = null;
		missileBatteryUpper.destroy(); missileBatteryUpper = null;
		missileBatteryLower.destroy(); missileBatteryLower = null;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		shield.update(container, game, delta);
		laserBattery.update(container, game, delta);
		missileBatteryUpper.update(container, game, delta);
		missileBatteryLower.update(container, game, delta);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		shield.render(container, game, g);
		shipSprite.draw(-80, 0);
		laserBattery.render(container, game, g);
		missileBatteryUpper.render(container, game, g);
		missileBatteryLower.render(container, game, g);
		
		uniFont.drawString(30, 50, health+"/"+maxHealth, Color.red);
	}
	
	public GameState getGame() {
		return game;
	}
	
	public void collide(Word word) {
		if(shield.isActive()) {
			word.fxDestroyed(false);
			word.addPoints(false);
			shield.deactivate();
		}
		else {
			hasBeenHit = true;
			word.fxDetonated();
			loseHealth(word.getDamage());
		}
	}
	
	public void loseHealth(int damage) {
		health -= damage;
		if(health < 0)
			health = 0;
	}
	
	public void addHealth(int heal) {
		health += heal;
		if(health > maxHealth)
			health = maxHealth;
	}
	
	public boolean isDestroyed() {
		return health == 0;
	}
	
	public boolean hasBeenHit() {
		return hasBeenHit;
	}
	
	public void resetCooldown() {
		shield.activate();
		missileBatteryUpper.resetCooldown();
		missileBatteryLower.resetCooldown();
	}
	
}
