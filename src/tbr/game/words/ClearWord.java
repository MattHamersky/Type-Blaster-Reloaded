package tbr.game.words;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

import tbr.game.effects.FXSystem;
import tbr.states.menus.GameState;

public class ClearWord extends Word {

	public ClearWord(GameState game, String word, int speed, Point position, double delay) {
		super(game, word, speed, position, delay);
		
		untypedColor = Color.yellow;
	}
	
	@Override
	public void fxDestroyed(boolean isNuke) {
		isFinished = true;
		GameState.system.addEmitter(FXSystem.FX_NUKE, position.getX(), position.getY());
		
		//if what cause this word to be destroyed was a nuke then don't run this code
		if(!isNuke) {
			//clear all the words on screen
			List<Word> words = game.getWordsOnScreen();
			for(int i = 0; i < words.size(); i++) {
				//skip this word otherwise we'll get in an infinite loop
				if(words.get(i) == this)
					continue;
				words.get(i).fxDestroyed(true);
				words.get(i).addPoints(true);
			}
		}
	}
	
	@Override
	public void fxDetonated() {
		isFinished = true;
		GameState.system.addEmitter(FXSystem.FX_NUKE, position.getX(), position.getY());
	}
	
	@Override
	public int getDamage() {
		return word.length() * 2;
	}

}
