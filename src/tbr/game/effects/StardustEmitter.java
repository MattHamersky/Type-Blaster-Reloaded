package tbr.game.effects;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import Util.Utility;
import tbr.main.Config;

public class StardustEmitter {

	private Image particle;
	private List<Stardust> dustParticles = new ArrayList<Stardust>();
	private static final int MAX_PARTICLES = 40;
	
	private static final int WAIT_TIME = 800; //time between spawning new particles
	private long startTime;
	
	public StardustEmitter() {
		//initialize rng
		
		//load star dust sprite
		try {
			particle = new Image("fx/images/stardust_particle.png");
			placeInitialParticles();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() throws SlickException {
		particle.destroy(); particle = null;
		dustParticles.clear(); dustParticles = null;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) {
		if(container.getTime() - startTime > WAIT_TIME) {
			if(dustParticles.size() < MAX_PARTICLES) {
				//arbitrary number to provide a bit of randomness between spawning particles
				if(Utility.nextInt(100) > 60) {
					startTime = container.getTime();
					spawnParticle(false);
				} //end rand if
			} //end max particles if
		} //end wait time if
		
		//update star dust particles
		for(int i = 0; i < dustParticles.size(); i++) {
			dustParticles.get(i).update(container, game, delta);
			if(dustParticles.get(i).isFinished()) {
				dustParticles.remove(i);
				i--;
			}
		}
		
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		//render star dust particles
		for(int i = 0; i < dustParticles.size(); i++)
			dustParticles.get(i).render(container, game, g);
	}
	
	private void placeInitialParticles() {
		int spawns = Utility.nextInt(MAX_PARTICLES / 2, MAX_PARTICLES);
		for(int i = 0; i < spawns; i++)
			spawnParticle(true);
	}
	
	private void spawnParticle(boolean randomX) {
		if(randomX)
			dustParticles.add(new Stardust(
					Utility.nextInt(Config.WIDTH),
					Utility.nextInt(Config.HEIGHT),
					Utility.speedToPixels(Utility.nextInt(38, 60)),
					Utility.nextInt(255),
					Utility.nextInt(particle.getWidth())
			));
		else
			dustParticles.add(new Stardust(
					Utility.nextInt(Config.HEIGHT),
					Utility.speedToPixels(Utility.nextInt(38, 60)),
					Utility.nextInt(255),
					Utility.nextInt(particle.getWidth())
			));
	}
	
	class Stardust {
		
		private float x, y;
		private int speed;
		private Color color;
		private int width;
		
		private boolean isFinished = false;
		
		public Stardust(int y, int speed, int opacity, int width) {
			this.x = Config.WIDTH;
			this.y = y;
			this.speed = speed;
			this.color = new Color(1.0f, 1.0f, 1.0f, opacity / 255.0f);
			this.width = width;
		}
		
		public Stardust(int x, int y, int speed, int opacity, int width) {
			this.x = x;
			this.y = y;
			this.speed = speed;
			this.color = new Color(1.0f, 1.0f, 1.0f, opacity / 255.0f);
			this.width = width;
		}
		
		public void update(GameContainer container, StateBasedGame game, int delta) {
			x -= (speed/1000.0) * delta;
			if(x < 0)
				isFinished = true;
		}
		
		public void render(GameContainer container, StateBasedGame game, Graphics g) {
			particle.draw(x, y, width, width, color);
		}
		
		public boolean isFinished() {
			return isFinished;
		}
		
	}
	
}
