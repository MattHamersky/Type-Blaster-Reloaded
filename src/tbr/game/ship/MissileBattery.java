package tbr.game.ship;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import tbr.game.Profile;
import tbr.game.Upgrades;
import tbr.game.words.Word;
import tbr.main.Config;

public class MissileBattery extends Upgrade {

	private Image missileBattery;
	private double rotation = 0.0;
	private Word trackingWord = null;
	
	private Image missileHUD, missileHUDdisabled;
	private int xHUD, yHUD;
	private int xBattery, yBattery;
	private boolean isUpper;
	
	private boolean isCoolingDown = false;
	private int COOLDOWN = 15000;
	private long startCooldownTime;
	
	private List<Missile> missiles = new ArrayList<Missile>();
	private Image missileSprite;
	
	private boolean autoRotationDown = true;
	
	public MissileBattery(Ship ship, int x, int y, boolean upperBattery) {
		super(ship);
		xHUD = x;
		yHUD = y;		
		isUpper = upperBattery;
		
		if(isUpper) {
			xBattery = 80;
			yBattery = 340;
		}
		else {
			xBattery = 80;
			yBattery = 690;
		}
		
		try {
			missileBattery = new Image("graphics/sprites/missile_battery2.png");
			missileHUD = new Image("graphics/sprites/hud/missile.png");
			missileHUDdisabled = new Image("graphics/sprites/hud/missile_disabled.png");
			missileSprite = new Image("graphics/sprites/missile.png");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() throws SlickException {
		missileBattery.destroy(); missileBattery = null;
		missileHUD.destroy(); missileHUD = null;
		missileHUDdisabled.destroy(); missileHUDdisabled = null;
		missileSprite.destroy(); missileSprite = null;
		missiles.clear(); missiles = null;
		trackingWord = null;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(isUpper) {
			if(!Upgrades.isUpgraded(Profile.MISSILE_UPPER))
				return;
			else
				upgradedUpdate(container, game, delta);
		}
		else {
			if(!Upgrades.isUpgraded(Profile.MISSILE_LOWER))
				return;
			else
				upgradedUpdate(container, game, delta);
		}
	}
	
	private void upgradedUpdate(GameContainer container, StateBasedGame game, int delta) {
		//auto rotate the turret if it isn't tracking a word
		if(autoRotationDown)
			rotation += 0.1;
		else
			rotation -= 0.1;
		if(rotation > 360)
			rotation = 0;
		if(rotation < 0)
			rotation = 360;
		if(rotation > 45.0 && rotation < 50)
			autoRotationDown = false;
		if(rotation < 315 && rotation > 310)
			autoRotationDown = true;
		
		//update missiles
		for(int i = 0; i < missiles.size(); i++) {
			missiles.get(i).update(container, game, delta);
			if(missiles.get(i).isFinished()) {
				missiles.remove(i);
				trackingWord = null;
				i--;
			}
		}
		
		//check if cooldown is finished
		if(isCoolingDown) {
			//update rotation of missile battery
			if(trackingWord != null)
				calculateRotation();
			//check if the battery is done cooling down
			if(container.getTime() - startCooldownTime >= COOLDOWN)
				isCoolingDown = false;
		}
		else {
			Word word = getAvailableWord(true);
			if(word != null) {
				word.targeted();
				fireMissile(word, container.getTime());
				calculateRotation();
			}
		}
	}
	
	public Word getAvailableWord(boolean isStrict) {
		List<Word> wordsOnScreen = ship.getGame().getWordsOnScreen();
		for(Word word : wordsOnScreen) {
			//return the first word not being typed and not targeted
			if(isStrict && !word.isTargeted() && !word.isBeingTyped())
				return word;
			else if(!isStrict && !word.isTargeted())
				return word;
		}
		return null;
	}
	
	private void fireMissile(Word word, long time) {
		missiles.add(new Missile(this, xBattery + missileBattery.getWidth() / 2, yBattery + missileBattery.getHeight() / 2, word, missileSprite));
		isCoolingDown = true;
		startCooldownTime = time;
		trackingWord = word;
	}
	
	private void calculateRotation() {
		float lookatX = Config.WIDTH - xBattery;
		float lookatY = yBattery;
		
//		float orientToX = trackingWord.getX() - xBattery;
//		float orientToY = trackingWord.getY() - yBattery;
		
		float orientToX = trackingWord.getX();
		float orientToY = trackingWord.getY();
		
		float dot = lookatX * orientToX + lookatY * orientToY;
		double magLookat = Math.sqrt(lookatX * lookatX + lookatY * lookatY);
		double magOrientTo = Math.sqrt(orientToX * orientToX + orientToY * orientToY);
		
		if(lookatY > trackingWord.getY())
			rotation = 360.0 - Math.toDegrees(Math.acos(dot/(magLookat * magOrientTo)));
		else
			rotation = Math.toDegrees(Math.acos(dot/(magLookat * magOrientTo)));
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(isUpper) {
			if(Upgrades.isUpgraded(Profile.MISSILE_UPPER)) {
				drawMissileBattery(g);
				drawHUD(g, container.getTime());
				for(int i = 0; i < missiles.size(); i++)
					missiles.get(i).render(container, game, g);
			}
			else {
				missileHUDdisabled.draw(xHUD, yHUD);
			}
		}
		else {
			if(Upgrades.isUpgraded(Profile.MISSILE_LOWER)) {
				drawMissileBattery(g);
				drawHUD(g, container.getTime());
				for(int i = 0; i < missiles.size(); i++)
					missiles.get(i).render(container, game, g);
			}
			else {
				missileHUDdisabled.draw(xHUD, yHUD);
			}
		}
	}
	
	private void drawMissileBattery(Graphics g) {
		missileBattery.setCenterOfRotation(missileBattery.getWidth() / 2, missileBattery.getHeight() / 2);
		missileBattery.setRotation((float) rotation);
		missileBattery.draw(xBattery, yBattery);
	}
	
	private void drawHUD(Graphics g, long time) {
		if(isCoolingDown) {
			missileHUDdisabled.draw(xHUD, yHUD);
			centerHUDText(g, xHUD, yHUD, missileHUD.getWidth(), ""+(COOLDOWN-(time-startCooldownTime))/1000);
		}
		else {
			missileHUD.draw(xHUD, yHUD);
		}
	}
	
	public void resetCooldown() {
		if(isCoolingDown)
			isCoolingDown = false;
	}
}
