package tbr.game.ship;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import tbr.game.effects.FXSystem;
import tbr.game.words.Word;
import tbr.main.Config;
import tbr.states.menus.GameState;

public class Missile {

	private Word word;
	private float currentX, currentY;
	private int speed = 850;
	private static final int ERROR_MARGIN = 25;
	
	private boolean isFinished = false;
	
	private Image missile;
	private double rotation;
	
	private MissileBattery base;
	
	public Missile(MissileBattery base, int startingX, int startingY, Word word, Image missile) {
		this.base = base;
		this.currentX = startingX;
		this.currentY = startingY;
		this.word = word;
		this.missile = missile;
		getRotation();
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) {
		//the word we were going for was destroyed, try to find a replacement
		if(word.isFinished()) {
			System.out.println("finding new word");
			Word word = base.getAvailableWord(true);
			if(word != null) {
				word.targeted();
				this.word = word;
				getRotation();
			}
			//no replacement could be found, destroy this missile and give the points that the word gave to them again
			else {
				System.out.println("no replacement word found");
				this.word.addPoints(false);
				GameState.system.addEmitter(FXSystem.FX_SMALL_EXPLOSION, currentX, currentY);
			}
		}
		
		float newX = word.getX() - currentX;
		float newY = word.getY() - currentY;
		double magnitude = Math.sqrt((newX*newX)+(newY*newY));
		newX /= magnitude;
		newY /= magnitude;
		
		currentX += newX * ((speed/1000.0) * delta);
		currentY += newY * ((speed/1000.0) * delta);
		
		float wx = word.getX();
		float wy = word.getY();
		if(currentX + ERROR_MARGIN > wx && currentY + ERROR_MARGIN > wy) {
			isFinished = true;
			word.fxDestroyed(false);
			word.addPoints(false);
			GameState.system.addEmitter(FXSystem.FX_SMALL_EXPLOSION, currentX, currentY);
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		missile.setCenterOfRotation(missile.getWidth() / 2, missile.getHeight() / 2);
		missile.setRotation((float)rotation);
		
		missile.draw(currentX, currentY);
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void getRotation() {
		float lookatX = Config.WIDTH - currentX;
		float lookatY = currentY;
		
//		float orientToX = word.getX() - currentX;
//		float orientToY = word.getY() - currentY;
		
		float orientToX = word.getX();
		float orientToY = word.getY();
		
		float dot = lookatX * orientToX + lookatY * orientToY;
		double magLookat = Math.sqrt(lookatX * lookatX + lookatY * lookatY);
		double magOrientTo = Math.sqrt(orientToX * orientToX + orientToY * orientToY);
		
		if(lookatY > word.getY())
			rotation = 360.0 - Math.toDegrees(Math.acos(dot/(magLookat * magOrientTo)));
		else
			rotation = Math.toDegrees(Math.acos(dot/(magLookat * magOrientTo)));
	}

	
}
