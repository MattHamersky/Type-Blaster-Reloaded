package tbr.animation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import tbr.main.Config;

public class StarAnimation implements Animation {

	private boolean isFinished = false;
	
	public static final int WAIT_TIME = 750; //time to wait between being asked to run and actually running
	private boolean isWaitTimeOver = false;
	
	public static final int MAX_DIMENSIONS = 400; //largest the star should get
	public static final int GROWTH_TIME = 500; //how long it takes the star to reach its MAX_DIMENSIONS
	private float step, current; //how many pixels to grow each each step, what the current dimensions of the star is
	
	public static final int PAUSE_TIME = 750; //how long to pause before starting the next star's animation or finishing
	private boolean isPaused = false;	
	
	private int stars, currentIndex = 0;
	private Image star;
	
	private long startTime;
	
	public StarAnimation(int stars, long startTime) {
		this.stars = stars;
		this.startTime = startTime;
		
		try {
			star = new Image("graphics/sprites/star.png");
			current = star.getWidth();
			
			step = (MAX_DIMENSIONS - current) / (float)GROWTH_TIME;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(isWaitTimeOver) {
			if(!isPaused) {
				current += step * delta;
				if(current > MAX_DIMENSIONS) {
					isPaused = true;
					startTime = container.getTime();
				}
			}
			else {
				if(container.getTime() - startTime > PAUSE_TIME) {
					isPaused = false;
					currentIndex++;
					current = star.getWidth();
					if(currentIndex >= stars)
						isFinished = true;
				}
			}
		}
		else {
			if(container.getTime() - startTime > WAIT_TIME)
				isWaitTimeOver = true;
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if(isWaitTimeOver)
			star.draw((int) (Config.WIDTH / 2 - (current / 2.0)), (int)(Config.HEIGHT / 2 - (current / 2.0)), current, current);
	}
	
	@Override
	public boolean isFinished() {
		return isFinished;
	}
	
	@Override
	public void destroy() throws SlickException {
		star.destroy(); star = null;
	}

}
