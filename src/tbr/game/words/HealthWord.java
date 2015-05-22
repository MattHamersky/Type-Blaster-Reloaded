package tbr.game.words;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import tbr.game.effects.FXSystem;
import tbr.states.menus.GameState;

public class HealthWord extends Word {

	public HealthWord(GameState game, String word, int speed, Point position, double delay) {
		super(game, word, speed, position, delay);
		
		untypedColor = Color.red;
	}
	
	@Override
	public void fxDestroyed(boolean isNuke) {
		isFinished = true;
		game.getShip().addHealth(word.length());
		GameState.system.addEmitter(FXSystem.FX_HEAL, position.getX(), position.getY());
	}

}
