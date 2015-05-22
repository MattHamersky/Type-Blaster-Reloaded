package tbr.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

public class Score {

	private GameContainer container;
	
	private int score;
	private int modifier;
	private static final int MODIFIER_TIME = 2500;
	
	private long lastTimePointsAdded;
	
	private UnicodeFont font;
	
	private int x = 30;
	private int y = 20;
	
	public Score(GameContainer container, UnicodeFont font) {
		this.container = container;
		this.score = 0;
		this.modifier = 0;
		this.font = font;
		this.lastTimePointsAdded = 0;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) {
		long time = container.getTime();
		if((time - lastTimePointsAdded) > MODIFIER_TIME)
			modifier = 0;
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(modifier > 0) {
			String temp = score + "  x" + modifier;
			String modifierString = "x"+modifier;
			font.drawString(x, y, temp, Color.white, 0, temp.length() - modifierString.length());
			font.drawString(x, y, temp, getColor(), temp.length() - modifierString.length(), temp.length());
		}
		else {
			font.drawString(x, y, Integer.toString(score), Color.white);
		}
	}
	
	public void addPoints(int points) {
		lastTimePointsAdded = container.getTime();
		modifier++;
		score += points * modifier;
	}
	
	public void subtractPoints(int points) {
		score -= points;
		modifier = 0;
	}
	
	public void resetModifier() {
		modifier = 0;
		lastTimePointsAdded = 0;
	}
	
	public int getScore() {
		return score;
	}
	
	private Color getColor() {
		if(modifier < 2)
			return Color.white;
		else if(modifier < 6)
			return Color.yellow;
		else if(modifier < 12)
			return new Color(255, 102, 0); //orange, because the built in orange is horrific
		else if(modifier < 17)
			return Color.red;
		else
			return Color.magenta;
	}
	
}
