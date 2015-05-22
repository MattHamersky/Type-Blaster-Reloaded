package tbr.game.words;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import tbr.game.effects.FXSystem;
import tbr.states.menus.GameState;

public class CooldownResetWord extends Word {

	public CooldownResetWord(GameState game, String word, int speed, Point position, double delay) {
		super(game, word, speed, position, delay);
		
		untypedColor = Color.cyan;
	}
	
	@Override
	public void fxDestroyed(boolean isNuke) {
		isFinished = true;
		game.getShip().resetCooldown();
		GameState.system.addEmitter(FXSystem.FX_TIMER_RESET, position.getX(), position.getY());
	}

	
	
}
