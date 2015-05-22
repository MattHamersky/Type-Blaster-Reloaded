package tbr.game.words;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import tbr.game.effects.FXSystem;
import tbr.states.menus.GameState;

public class BonusPointsWord extends Word {

	public BonusPointsWord(GameState game, String word, int speed, Point position, double delay) {
		super(game, word, speed, position, delay);
		
		untypedColor = Color.green;
	}
	
	@Override
	public void fxDestroyed(boolean isNuke) {
		isFinished = true;
		GameState.system.addEmitter(FXSystem.FX_POINTS, position.getX(), position.getY());
	}
	
	@Override
	public void addPoints(boolean resetProgress) {
		game.addPoints(word.length() * 8, resetProgress);
	}

}
