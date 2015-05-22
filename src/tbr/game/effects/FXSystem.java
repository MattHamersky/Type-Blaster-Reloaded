package tbr.game.effects;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Image;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

public class FXSystem {

	public static final int FX_HEAL = 0;
	public static final int FX_SMALL_EXPLOSION = 1;
	public static final int FX_FIZZLE = 2;
	public static final int FX_POINTS = 3;
	public static final int FX_TIMER_RESET = 4;
	public static final int FX_NUKE = 5;
	
	
	private ParticleSystem[] systems = new ParticleSystem[6];
	private File[] xml = new File[6];
	
	public FXSystem() {
		
		try {
			//load healing fx
			Image heal = new Image("fx/images/heal.png");
			systems[FX_HEAL] = new ParticleSystem(heal, 1500);
			xml[FX_HEAL] = new File("fx/emitters/heal.xml");
			
			//load small explosion fx
			Image smallExplosion = new Image("fx/images/particle.png");
			systems[FX_SMALL_EXPLOSION] = new ParticleSystem(smallExplosion, 1500);
			xml[FX_SMALL_EXPLOSION] = new File("fx/emitters/small_explosion.xml");
			
			//load small explosion fx
			Image fizzle = new Image("fx/images/particle.png");
			systems[FX_FIZZLE] = new ParticleSystem(fizzle, 1500);
			xml[FX_FIZZLE] = new File("fx/emitters/fizzle.xml");
			
			//load small explosion fx
			Image points = new Image("fx/images/dollar_bill.png");
			systems[FX_POINTS] = new ParticleSystem(points, 1500);
			xml[FX_POINTS] = new File("fx/emitters/points_explosion.xml");
			
			//load small explosion fx
			Image timerReset = new Image("fx/images/clock.png");
			systems[FX_TIMER_RESET] = new ParticleSystem(timerReset, 1500);
			xml[FX_TIMER_RESET] = new File("fx/emitters/clocks_explosion.xml");
			
			//load nuclear explosion
			Image nuke = new Image("fx/images/particle.png");
			systems[FX_NUKE] = new ParticleSystem(nuke, 3000);
			xml[FX_NUKE] = new File("fx/emitters/nuclear_explosion.xml");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
 
		for(int i = 0; i < systems.length; i++)
			systems[i].setBlendingMode(ParticleSystem.BLEND_ADDITIVE);	
	}
	
	public void addEmitter(int FX, int x, int y) {
		try {
			ConfigurableEmitter emitter = ParticleIO.loadEmitter(xml[FX]);
			emitter.setPosition(x, y, false);
			systems[FX].addEmitter(emitter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addEmitter(int FX, float x, float y) {
		addEmitter(FX, (int)x, (int) y);
	}
	
	public void update(int delta) {
		for(int i = 0; i < systems.length; i++)
			systems[i].update(delta);
	}
	
	public void render() {
		for(int i = 0; i < systems.length; i++)
			systems[i].render();
	}
}
