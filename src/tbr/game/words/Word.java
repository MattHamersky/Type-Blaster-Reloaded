package tbr.game.words;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.state.StateBasedGame;

import Util.Utility;

import tbr.game.effects.FXSystem;
import tbr.states.menus.GameState;

public class Word {

	public static UnicodeFont uniFont;
	//public static FONT_SIZE = Utility.
	
	public static final int LEEWAY = 5; //space between the end of one word and the beginning of another before they collide
	
	static {
		uniFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 32), Color.RED, Color.WHITE);
	}
	
	protected GameState game;
	
	protected String word;
	protected int correctLetters = 0;
	protected org.newdawn.slick.Color untypedColor = org.newdawn.slick.Color.white;
	protected org.newdawn.slick.Color typedColor = org.newdawn.slick.Color.gray;
	
	protected Point position; //current position on the screen
	protected int speed;
	protected int tempSpeed; //if the word has to alter speed for any reason, the altered speed is set here
	protected double delay;
	
	protected int width;
	protected int height;
	
	protected boolean isColliding = false; //are we currently colliding with another word
	protected Word collidingWord = null; // the word we are colliding with
	
	protected boolean isFinished = false; //the word has completed its task and should no longer be updated/rendered
	
	protected boolean isTargeted = false; //is a missile battery currently targeting this word
	
	public Word(GameState game, String word, int speed, Point position, double delay) {
		this.game = game;
		this.word = word;
		this.speed = Utility.speedToPixels(speed);
		this.tempSpeed = this.speed;
		this.position = position;
		this.delay = delay;
		
		this.width = uniFont.getWidth(word);
		this.height = uniFont.getHeight(word);
	}
	
	public void changeSpeedTo(int speed) {
		this.tempSpeed = speed;
	}
	
	public void resumeOriginalSpeed() {
		isColliding = false;
		collidingWord = null;
		tempSpeed = speed;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) {
		//no need to update, this word is done
		if(isFinished)
			return;
		
		//update position
		if(!isFinished) {
			int move = speed;
			if(move != tempSpeed)
				move = tempSpeed;
			position.setX((float)(position.getX() - ((move/1000.0) * delta)));
		}
		
		//check for collision with ship
		if(!isFinished) {
			if(position.getX() < 160) {
				this.game.getShip().collide(this);
			}
		}
		
		//check for collision with other words if we aren't already colliding
		if(isColliding) {
			changeSpeedTo(collidingWord.getSpeed());
			if(collidingWord.isFinished() || collidingWord.getSpeed() > speed) {
				resumeOriginalSpeed();
			}
		}
		else {
			List<Word> words = this.game.getWords();
			for(int i = 0; i < words.size(); i++) {
				Word word = words.get(i);
				//we only check words that come before us
				if(word.getDelay() >= delay)
					break;
				if(!word.isFinished())
					if(word.getRow() == getRow())
						if(getX() < word.getX() + word.getWidth() + LEEWAY) {
							isColliding = true;
							collidingWord = word;
							changeSpeedTo(collidingWord.getSpeed());
						}
			}
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(!isFinished) {
			uniFont.drawString(position.getX(), position.getY(), word, typedColor, 0, correctLetters);
			uniFont.drawString(position.getX(), position.getY(), word, untypedColor, correctLetters, word.length());
		}
	}
	
	public void update(char c) {		
		//they typed the correct letter
		if(word.charAt(correctLetters) == c) {
			correctLetters++;
			if(correctLetters == word.length()) {
				isFinished = true;
				fxDestroyed(false);
				addPoints(true);
			}
		}
		//incorrect letter so reset the word
		else {
			correctLetters = 0;
		}
	}
	
	public void fxDestroyed(boolean isNuke) {
		isFinished = true;
		GameState.system.addEmitter(FXSystem.FX_FIZZLE, position.getX(), position.getY());
	}
	
	public void fxDetonated() {
		isFinished = true;
		GameState.system.addEmitter(FXSystem.FX_SMALL_EXPLOSION, position.getX(), position.getY());
	}
	
	public void addPoints(boolean resetProgress) {
		game.addPoints(word.length(), resetProgress);
	}
	
	public double getDelay() {
		return delay;
	}
	
	public int getSpeed() {
		if(speed == tempSpeed)
			return speed;
		else
			return tempSpeed;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public int getDamage() {
		return word.length();
	}
	
	public int getRow() {
		return (int) position.getY();
	}
	
	public int getX() {
		return (int) position.getX();
	}
	
	public int getY() {
		return (int) position.getY();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void resetProgress() {
		correctLetters = 0;
	}
	
	public int getCorrectLetters() {
		return correctLetters;
	}
	
	public boolean isBeingTyped() {
		if(!isFinished && correctLetters > 0)
			return true;
		else
			return false;
	}
	
	public boolean isNormalSpeed() {
		if(speed == tempSpeed)
			return true;
		else
			return false;
	}
	
	public boolean isTargeted() {
		return isTargeted;
	}
	
	public void targeted() {
		isTargeted = true;
	}
	
}
