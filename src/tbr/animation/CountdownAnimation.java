package tbr.animation;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import tbr.main.Config;
import Util.Utility;

public class CountdownAnimation implements Animation {

	private boolean isFinished = false;
	private long startTime;
	
	private int index = 0;
	private String[] text = {"3", "2", "1", "Start"};
	private UnicodeFont font;
	
	public CountdownAnimation(long startTime) {
		this.startTime = startTime;
		this.font = Utility.makeFont(new Font("Verdana", Font.PLAIN, 90));
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		index = (int)(container.getTime() - startTime) / 1000;
		if(index >= text.length)
			isFinished = true;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		FontUtils.drawCenter(font, text[index], Config.WIDTH / 2, Config.HEIGHT / 2 - font.getHeight(text[index]), 0);
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
