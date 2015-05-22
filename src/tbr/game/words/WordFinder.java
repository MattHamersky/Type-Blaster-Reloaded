package tbr.game.words;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.geom.Point;

import Util.Utility;
import tbr.states.menus.GameState;

public class WordFinder {

	private static int counter;
	private static List<String> words = new ArrayList<String>();
	
	public static void loadWordList(String list) {
		words.clear();
		counter = 0;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(WordFinder.class.getResourceAsStream("/levels/word_lists/"+list+".txt")));
			
			String line;
			while((line = reader.readLine()) != null)
				words.add(line);
			
			Collections.shuffle(words);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch(Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	public static String getWord() {
		if(words.size() == 0)
			throw new NullPointerException("No words to return");
		if(counter >= words.size())
			counter = 0;
		
		String word = words.get(counter);
		counter++;
		return word;
	}

	public static Word getSpecialWord(GameState game, String word, int speed, Point position, double delay) {
		switch(Utility.nextInt(4)) {
			case 0:
				return new HealthWord(game, word, speed, position, delay);
			case 1:
				return new BonusPointsWord(game, word, speed, position, delay);
			case 2:
				return new CooldownResetWord(game, word, speed, position, delay);
			case 3:
				return new ClearWord(game, word, speed, position, delay);
			default:
				return new Word(game, word, speed, position, delay);
		}
	}
	
}
