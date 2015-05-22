package tbr.game.ship;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Util.Utility;
import tbr.game.Profile;
import tbr.game.Upgrades;
import tbr.game.words.Word;

public class LaserBattery extends Upgrade {
	
	private Image laserSprite;
	private int x, y;
	private int centerX, centerY;
	
	private List<Word> slowedWords;
	public static final int SLOWED_TIME = 35;
	private int slowedSpeedInPixels;
	
	public LaserBattery(Ship ship) {
		super(ship);
		try {
			this.laserSprite = new Image("graphics/sprites/laser.png");
			this.x = 80;
			this.y = 500;
			this.centerX = 117;
			this.centerY = 537;
			
			this.slowedSpeedInPixels = Utility.speedToPixels(SLOWED_TIME);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() throws SlickException {
		laserSprite.destroy(); laserSprite = null;
		slowedWords = null;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!Upgrades.isUpgraded(Profile.SLOW_LASER))
			return;
		
		if(slowedWords != null)
			//return the previous words to their original speeds
			for(int i = 0; i < slowedWords.size(); i++)
				slowedWords.get(i).resumeOriginalSpeed();
		
		//get the new list of words that we are typing
		slowedWords = ship.getGame().getWordsBeingTyped();
		
		//change their speed
		for(int i = 0; i < slowedWords.size(); i++)
			slowedWords.get(i).changeSpeedTo(slowedSpeedInPixels);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(!Upgrades.isUpgraded(Profile.SLOW_LASER))
			return;
		
		laserSprite.draw(x, y);
		
		g.setColor(Color.blue);
		for(int i = 0; i < slowedWords.size(); i++)
			g.drawLine(centerX, centerY, slowedWords.get(i).getX(), slowedWords.get(i).getY()+slowedWords.get(i).getHeight()/2);
	}
}
