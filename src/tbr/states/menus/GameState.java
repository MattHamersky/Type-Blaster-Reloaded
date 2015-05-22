package tbr.states.menus;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;
import tbr.animation.Animation;
import tbr.animation.CountdownAnimation;
import tbr.animation.ScoreAnimation;
import tbr.animation.StarAnimation;
import tbr.game.Level;
import tbr.game.Profile;
import tbr.game.Score;
import tbr.game.Upgrades;
import tbr.game.effects.FXSystem;
import tbr.game.effects.StardustEmitter;
import tbr.game.ship.Ship;
import tbr.game.words.*;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.TextButton;

public class GameState extends BasicGameState {

	private StateBasedGame game;
	
	private List<Word> words = new ArrayList<Word>();
	private int specialWordProbability;
	
	private Score score;
	
	private long startTime; //start time of match so we know when to start spawning words in based off their day
	private int wordIndex; //words are sorted in order of their delay, wordIndex is the latest index we have reached in terms of delay
	private int counter;  //numbers of words finished
	private boolean resetWordProgress;
	
	private UnicodeFont uniFont;
	
	private Ship ship;
	private Image background;
	
	public static FXSystem system;
	private StardustEmitter stardust;
	
	//starting countdown
	private Animation countdownAnimation;
	
	//score animation
	private Animation scoreAnimation;
	private boolean playScoreAnimation;
	public static final int END_OF_LEVEL_BONUS = 500;
	
	//star animation
	private boolean playStarAnimation;
	private Animation starAnimation;
	
	//game over members
	private boolean gameover;
	private boolean missionSuccess;
	private UnicodeFont gameOverFont, missionStatusFont, finalScoreFont;
	private Image star, silhoutte;
	private boolean hasHighScore; //is the score higher than the previous best
	private int starsGained; //how many new stars were gained this time
	private TextButton restartButton, exitButton;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}
	
	@Override
	public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
		container.setMouseGrabbed(true); //hide the mouse cursor
		
		specialWordProbability = 5;
		if(Upgrades.isUpgraded(Profile.INCREASE_SPECIAL_WORD_SPAWN))
			specialWordProbability *= 2; //double the probability that a special word will spawn
		
		//reset member variables
		words.clear();
		startTime = 0;
		wordIndex = 0;
		playScoreAnimation = false;
		playStarAnimation = false;
		gameover = false;
		missionSuccess = false;
		hasHighScore = false;
		starsGained = 0;
		resetWordProgress = false;
		
		//game over screen buttons
		restartButton = new TextButton(container, "Restart", Config.WIDTH / 2 + 125, 550, 40, 40, true);
		restartButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				restartButton.disable();
				exitButton.disable();
				Profile.saveCurrentProfile();
				game.enterState(States.GAME);
			}
		});
		exitButton = new TextButton(container, "Back", Config.WIDTH / 2 - 125, 550, 40, 40, true);
		exitButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				restartButton.disable();
				exitButton.disable();
				Profile.saveCurrentProfile();
				game.enterState(States.PLAY_MENU);
			}
		});
		restartButton.setColor(Color.black);
		restartButton.disable();
		exitButton.setColor(Color.black);
		exitButton.disable();
		
		//load fonts
		uniFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 30));
		gameOverFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60), "GAME OVER");
		missionStatusFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 50));
		finalScoreFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 40), new java.awt.Color[] {java.awt.Color.BLACK, java.awt.Color.RED});
		
		//setup our scoring algorithm
		score = new Score(container, uniFont);
		
		//load the ship
		this.ship = new Ship(this, uniFont);
		
		//load particles
		system = new FXSystem();
		stardust = new StardustEmitter();
		
		String backgroundString = "star_field"+Utility.nextInt(1, 8);
		
		try {
			background = new Image("images/backgrounds/"+backgroundString+".png");
			//background = new Image("images/backgrounds/star_field8.png");
			star = new Image("graphics/sprites/star.png");
			silhoutte = new Image("graphics/sprites/star_silhoutte.png");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		//load level
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(GameState.class.getResourceAsStream("/levels/"+PlayMenuState.selectedLevelString+PlayMenuState.difficulty+".level")));
			
			String line;
			String[] tokens;
			
			//load level specific word bank
			line = reader.readLine();
			WordFinder.loadWordList(line);
			
			while((line = reader.readLine()) != null) {
				tokens = line.split("-");
				if(Utility.nextInt(0, 100) < specialWordProbability)
					words.add(WordFinder.getSpecialWord(this, WordFinder.getWord(), Integer.parseInt(tokens[2]), new Point(Config.WIDTH, Integer.parseInt(tokens[0])*30+150), Double.parseDouble(tokens[1])));
				else
					words.add(new Word(this, WordFinder.getWord(), Integer.parseInt(tokens[2]), new Point(Config.WIDTH, Integer.parseInt(tokens[0])*30+150), Double.parseDouble(tokens[1])));
			}
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
		
		countdownAnimation = new CountdownAnimation(container.getTime());
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		//show the mouse cursor
		container.setMouseGrabbed(false);
		
		//free resources
		words.clear();
		background.destroy(); background = null;
		star.destroy(); star = null;
		silhoutte.destroy(); silhoutte = null;
		ship.destroy(); ship = null;
		stardust.destroy(); stardust = null;
		system = null;
		score = null;
		restartButton.destroy(); restartButton = null;
		exitButton.destroy(); exitButton = null;
		uniFont = null;
		gameOverFont = null;
		missionStatusFont = null;
		finalScoreFont = null;
		countdownAnimation.destroy(); countdownAnimation = null;
		if(scoreAnimation != null)
			scoreAnimation.destroy(); scoreAnimation = null;
		if(starAnimation != null)
			starAnimation.destroy(); starAnimation = null;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//render background
		background.draw(0, 0);
		
		//render stardust particles
		stardust.render(container, game, g);
		
		//render ship and upgrades
		ship.render(container, game, g);
		
		//render particles
		system.render();
		
		//render words
		for(int i = 0; i < wordIndex; i++) {
			words.get(i).render(container, game, g);
		}
		
		//render text hud
		score.render(container, game, g);
		FontUtils.drawCenter(uniFont, words.size()-counter+" words to advance", Config.WIDTH / 2, 1000, 0);
		//if(countdownAnimation.isFinished())
		//	FontUtils.drawCenter(uniFont, Long.toString((container.getTime()-startTime)/1000), Config.WIDTH/2, 10, 0);
		
		if(!countdownAnimation.isFinished())
			countdownAnimation.render(container, game, g);
		
		if(playScoreAnimation)
			scoreAnimation.render(container, game, g);
		
		if(gameover)
			gameover(container, game, g);
		
		if(playStarAnimation)
			starAnimation.render(container, game, g);
	}
	
	private void gameover(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//redisplay mouse
		container.setMouseGrabbed(false);
		
		exitButton.enable();
		restartButton.enable();
		
		//draw filled rectangle as the dialog box
		g.setColor(Color.white);
		g.fillRect(Config.WIDTH / 2 - 300, 250, 600, 400);
		
		//draw game over text
		FontUtils.drawCenter(gameOverFont, "GAME OVER", Config.WIDTH / 2, 275, 0, Color.black);
		
		if(missionSuccess) {
			
			FontUtils.drawCenter(missionStatusFont, "MISSION SUCCESS", Config.WIDTH / 2, 335, 0, Color.black);
			
			for(int i = 0; i < PlayMenuState.selectedLevel.getStars(); i++)
				star.draw(Config.WIDTH / 2 - 80 - star.getWidth() / 2 + i * 80, 405);
			for(int i = PlayMenuState.selectedLevel.getStars(); i < Level.MAX_STARS; i++)
				silhoutte.draw(Config.WIDTH / 2 - 80 - star.getWidth() / 2 + i * 80, 405);
		}
		else {
			
			FontUtils.drawCenter(missionStatusFont, "MISSION FAILED", Config.WIDTH / 2, 335, 0, Color.black);
			
			//draw the number of stars that were previously earned (can't earn additional stars if the mission is failed)
			for(int i = 0; i < PlayMenuState.selectedLevel.getStars(); i++)
				star.draw(Config.WIDTH / 2 - 80 - star.getWidth() / 2 + i * 80, 405);
			for(int i = PlayMenuState.selectedLevel.getStars(); i < Level.MAX_STARS; i++)
				silhoutte.draw(Config.WIDTH / 2 - 80 - star.getWidth() / 2 + i * 80, 405);
		}
		
		//if there is a new high score, display that in red
		if(hasHighScore)
			FontUtils.drawCenter(finalScoreFont, "NEW HIGH SCORE", Config.WIDTH / 2, 455, 0, Color.red);
		else
			FontUtils.drawCenter(finalScoreFont, "FINAL SCORE", Config.WIDTH / 2, 455, 0, Color.black);
		
		//draw score
		FontUtils.drawCenter(finalScoreFont, Integer.toString(score.getScore()), Config.WIDTH / 2, 495, 0, Color.black);
		
		restartButton.render(container, g);
		exitButton.render(container, g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {		
		//if we finished typing a word, reset all other words
		if(resetWordProgress) {
			resetWordProgress = false;
			resetWordProgress();
		}
		
		ship.update(container, game, delta);
		
		if(!countdownAnimation.isFinished())
			countdownAnimation.update(container, game, delta);
		
		
		if(countdownAnimation.isFinished() && startTime == 0)
			startTime = container.getTime();
		
		//update words whose delay has already passed
		for(int i = 0; i < wordIndex; i++)
			words.get(i).update(container, game, delta);
		
		if(!gameover && countdownAnimation.isFinished()) {
			//check to see which words are ready to be spawned (based on their delay)
			for(int i = wordIndex; i < words.size(); i++) {
				if((container.getTime() - startTime) > words.get(i).getDelay() * 1000)
					wordIndex++;
				else
					break;
			}
		}
		
		score.update(container, game, delta);
		
		system.update(delta);
		stardust.update(container,  game,  delta);
		
		//check game over conditions
		if(!gameover && !playScoreAnimation) {
			
			//ship destroyed, mission failed
			if(ship.isDestroyed()) {
				gameover = true;
				missionSuccess = false;
				checkHighScore(); //check if there is a new high score
			}
			
			//check how many words have passed
			counter = 0;
			for(int i = 0; i < words.size(); i++) {
				if(words.get(i).isFinished())
					counter++;
			}
			
			//all words destroyed, mission success
			if(counter == words.size() && !gameover) {
				playScoreAnimation = true;
				scoreAnimation = new ScoreAnimation(END_OF_LEVEL_BONUS, container.getTime());
				score.resetModifier();
				score.addPoints(END_OF_LEVEL_BONUS);
			}
		}
		
		if(playScoreAnimation) {
			if(scoreAnimation.isFinished()) {
				postGame(container);
				playScoreAnimation = false;
			}
			else {
				scoreAnimation.update(container, game, delta);
			}
		}
		
		if(playStarAnimation) {
			if(starAnimation.isFinished())
				playStarAnimation = false;
			else
				starAnimation.update(container, game, delta);
		}
	}
	
	private void postGame(GameContainer container) {
		gameover = true;
		missionSuccess = true;
		unlockNextLevel();
		
		checkHighScore(); //check if there is a new high score
		checkStarConditions(); //check which stars were earned
		if(starsGained > 0) {
			playStarAnimation = true;
			starAnimation = new StarAnimation(starsGained, container.getTime());
		}
	}
	
	private void unlockNextLevel() {
		Level nextLevel = Profile.currentUser.getLevel(PlayMenuState.selectedLevelIndex+1);
		if(nextLevel != null)
			nextLevel.unlock();
		nextLevel = Profile.currentUser.getLegendaryLevel(PlayMenuState.selectedLevelIndex+1);
		if(nextLevel != null)
			nextLevel.unlock();
	}

	private void checkHighScore() {
		//new high score
		if(score.getScore() > PlayMenuState.selectedLevel.getScore()) {
			hasHighScore = true;
			PlayMenuState.selectedLevel.setScore(score.getScore());
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(States.PLAY_MENU);
		
		if(key == Input.KEY_F1)
			game.enterState(States.GAME);
		
		if(!gameover) {
			if(Character.isLetter(c)) {
				for(int i = 0; i < wordIndex; i++) {
					if(!words.get(i).isFinished())
						words.get(i).update(c);
				}
			}
		}
	}
	
	@Override
	public int getID() {
		return States.GAME;
	}
	
	public void addPoints(int points, boolean resetPoints) {
		score.addPoints(points);
		resetWordProgress = resetPoints;
	}
	
	public void subtractPoints(int points) {
		score.subtractPoints(points);
	}
	
	public void resetWordProgress() {
		for(int i = 0; i < words.size(); i++) {
			words.get(i).resetProgress();
		}
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public List<Word> getWords() {
		return words;
	}
	
	public List<Word> getWordsOnScreen() {
		List<Word> onScreen = new ArrayList<Word>();
		for(int i = 0; i < wordIndex; i++)
			if(!words.get(i).isFinished())
				onScreen.add(words.get(i));
		return onScreen;
	}
	
	public List<Word> getWordsBeingTyped() {
		List<Word> typedWords = new ArrayList<Word>();
		for(int i = 0; i < words.size(); i++)
			if(words.get(i).isBeingTyped())
				typedWords.add(words.get(i));
		return typedWords;
	}
	
	public FXSystem getFXSystem() {
		return system;
	}
	
	public void checkStarConditions() {
		Level level = PlayMenuState.selectedLevel;
		boolean[] conditions = level.getStarConditions();
		starsGained = 0;
		if(!conditions[Level.CONDITION_FINISH]) {
			level.setStarCondition(Level.CONDITION_FINISH, true);
			starsGained++;
		}
		if(!conditions[Level.CONDITION_NO_DAMAGE]) {
			if(!ship.hasBeenHit()) {
				level.setStarCondition(Level.CONDITION_NO_DAMAGE, true);
				starsGained++;
			}
		}
		if(!conditions[Level.CONDITION_POINTS_REACHED]) {
			if(score.getScore() >= level.getMaxScore()) {
				level.setStarCondition(Level.CONDITION_POINTS_REACHED, true);
				starsGained++;
			}
		}
	}

}
