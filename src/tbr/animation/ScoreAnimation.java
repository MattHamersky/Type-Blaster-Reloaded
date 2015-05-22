package tbr.animation;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;
import tbr.main.Config;

public class ScoreAnimation implements Animation {

	public static final int PAUSE_LENGTH = 1250;
	public static final int MOVE_LENGTH = 750;
	private long startTime;
	private UnicodeFont font;
	
	private float x, y;
	private float xStep, yStep;
	
	private String text;
	
	private boolean isFinished = false;
	
	public ScoreAnimation(int score, long startTime) {
		this.text = "+"+score;
		this.startTime = startTime;
		
		this.font = Utility.makeFont(new Font("Verdana", Font.PLAIN, 72), "+"+score);
		
		this.x = Config.WIDTH / 2;
		this.y = Config.HEIGHT / 2 - font.getHeight(text);
		
		this.xStep = (Config.WIDTH / 2) / (float)MOVE_LENGTH;
		this.yStep = (Config.HEIGHT / 2 - font.getHeight(text)) / (float)MOVE_LENGTH;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(container.getTime() - startTime > PAUSE_LENGTH) {
			x -= xStep * delta;
			y -= yStep * delta;
			if(x <= 50 && y <= 20 + font.getHeight(text))
				isFinished = true;
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		FontUtils.drawCenter(font, text, (int)x, (int)y, 0, Color.white);
	}
	
	@Override
	public boolean isFinished() {
		return isFinished;
	}
	
	@Override
	public void destroy() throws SlickException {
		font = null;
	}
	
}
