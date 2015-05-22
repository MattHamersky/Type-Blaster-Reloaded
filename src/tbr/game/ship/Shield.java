package tbr.game.ship;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import tbr.game.Profile;
import tbr.game.Upgrades;

public class Shield extends Upgrade {
	
	private Image shieldSprite;
	
	private Image shieldHUD;
	private Image shieldHUDdisabled;
	private int xHUD, yHUD;
	
	private boolean isActive = true;
	private int currentCharge = 1; //how many words can hit the ship before initiating a cooldown
	private int maxCharge = 1;
	private int cooldown = 20000; //milliseconds before we can reuse the shield
	private int warmup = 3000; //milliseconds before the cooldown ends where the shield stars flashing back up
	private int flashDuration = 250; // milliseconds between flashes during the warmup
	private boolean isFlashing = false; //are we displaying the shield during a flash
	private long startCooldownTime = 0; //initial cooldown start time
	private long flashStartTime = 0; //start time of a single flash animation
	
	public Shield(Ship ship, int x, int y) {
		super(ship);
		xHUD = x;
		yHUD = y;
		
		if(Upgrades.isUpgraded(Profile.ADDITIONAL_SHIELD_CHARGE)) {
			currentCharge = 2;
			maxCharge = 2;
		}
		
		if(Upgrades.isUpgraded(Profile.DECREASED_SHIELD_COOLDOWN))
			cooldown = 10000;
		
		try {
			shieldSprite = new Image("graphics/sprites/shield.png");
			shieldHUD = new Image("graphics/sprites/hud/shield.png");
			shieldHUDdisabled = new Image("graphics/sprites/hud/shield_disabled.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() throws SlickException {
		shieldSprite.destroy(); shieldSprite = null;
		shieldHUD.destroy(); shieldHUD = null;
		shieldHUDdisabled.destroy(); shieldHUDdisabled = null;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!Upgrades.isUpgraded(Profile.SHIELD))
			return;
		if(!isActive) {
			if(startCooldownTime == 0)
				startCooldownTime = container.getTime();
			if((container.getTime() - startCooldownTime) > cooldown)
				activate();
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(!Upgrades.isUpgraded(Profile.SHIELD)) {
			shieldHUDdisabled.draw(xHUD, yHUD);
			return;
		}
		
		drawShieldHUD(g, container.getTime());
		
		if(isActive)
			drawShield(g);
		else {
			if((container.getTime() - startCooldownTime) > cooldown - warmup) {
				if(isFlashing) {
					drawShield(g);
					if((container.getTime() - flashStartTime) > flashDuration) {
						isFlashing = false;
						flashStartTime = 0;
					}
				}
				else {
					if(flashStartTime == 0)
						flashStartTime = container.getTime();
					if((container.getTime() - flashStartTime) > flashDuration) {
						flashStartTime = container.getTime();
						isFlashing = !isFlashing;
					}
				}
			}
		}
	}
	
	public void drawShield(Graphics g) {
		shieldSprite.draw(-80, 0);
	}
	
	public void drawShieldHUD(Graphics g, long time) {
		if(isActive) {
			shieldHUD.draw(xHUD, yHUD);
		}
		else {
			shieldHUDdisabled.draw(xHUD, yHUD);
			centerHUDText(g, xHUD, yHUD, shieldHUD.getWidth(), ""+(cooldown-(time-startCooldownTime))/1000);
		}
	}
	
	public boolean isActive() {
		if(Upgrades.isUpgraded(Profile.SHIELD))
			return isActive;
		else
			return false;
	}
	
	public void deactivate() {
		currentCharge--;
		if(currentCharge == 0)
			isActive = false;
	}
	
	public void activate() {
		currentCharge = maxCharge;
		isActive = true;
		startCooldownTime = 0;
		flashStartTime = 0;
	}
	
}
