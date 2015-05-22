package tbr.states.menus;

import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.Button;
import tbr.widgets.TextButton;


public class TutorialState extends BasicGameState {
	
	private StateBasedGame game;
	
	private Button leftButton, rightButton;
	private TextButton backButton;
	private UnicodeFont indexFont;
	
	private Image[] tutSlides = new Image[14];
	private int currentSlideIndex;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void enter(GameContainer container, final StateBasedGame game) throws SlickException {
		
		currentSlideIndex = 0;
		
		//load fonts
		indexFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 40));
		
		//load buttons
		backButton = new TextButton(container, "Back", Config.WIDTH / 2, 1020, 40, 40, true);
		backButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				game.enterState(States.MAIN_MENU);
			}
		});
		
		//load arrow buttons
		Image rightArrow = new Image("images/menu_buttons/right_arrow.png");
		Image rightArrowArmed = new Image("images/menu_buttons/right_arrow_armed.png");
		Image rightArrowOver = new Image("images/menu_buttons/right_arrow_over.png");
		
		Image leftArrow = new Image("images/menu_buttons/right_arrow.png").getFlippedCopy(true, false);
		Image leftArrowArmed = new Image("images/menu_buttons/right_arrow_armed.png").getFlippedCopy(true, false);
		Image leftArrowOver = new Image("images/menu_buttons/right_arrow_over.png").getFlippedCopy(true, false);
		
		leftButton = new Button(container, Config.WIDTH / 2 - 105, 980, leftArrow, leftArrowArmed, leftArrowOver, null);
		rightButton = new Button(container, Config.WIDTH / 2 + 75, 980, rightArrow, rightArrowArmed, rightArrowOver, null);
		
		//load tutorial images
		try {
			tutSlides[0] = new Image("images/tutorials/1.png");
			tutSlides[1] = new Image("images/tutorials/2.png");
			tutSlides[2] = new Image("images/tutorials/3.png");
			tutSlides[3] = new Image("images/tutorials/4.png");
			tutSlides[4] = new Image("images/tutorials/5.png");
			tutSlides[5] = new Image("images/tutorials/6.png");
			tutSlides[6] = new Image("images/tutorials/7.png");
			tutSlides[7] = new Image("images/tutorials/8.png");
			tutSlides[8] = new Image("images/tutorials/9.png");
			tutSlides[9] = new Image("images/tutorials/10.png");
			tutSlides[10] = new Image("images/tutorials/11.png");
			tutSlides[11] = new Image("images/tutorials/12.png");
			tutSlides[12] = new Image("images/tutorials/13.png");
			tutSlides[13] = new Image("images/tutorials/14.png");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void leave(GameContainer container, final StateBasedGame game) throws SlickException {
		leftButton.destroy(); leftButton = null;
		rightButton.destroy(); rightButton = null;
		backButton.destroy(); backButton = null;
		indexFont = null;
		
		for(int i = 0; i < tutSlides.length; i++) {
			tutSlides[i].destroy();
			tutSlides[i] = null;
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		//render tutorial image
		tutSlides[currentSlideIndex].draw(0, 0);
		
		FontUtils.drawCenter(indexFont, (currentSlideIndex+1)+"/"+tutSlides.length, Config.WIDTH / 2, 980, 0);
		
		//render buttons
		leftButton.render(container, g);
		rightButton.render(container, g);
		backButton.render(container, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(rightButton.isActivated()) {
			rightButton.deactivate();
			currentSlideIndex++;
			if(currentSlideIndex >= tutSlides.length)
				currentSlideIndex = 0;
		}
		if(leftButton.isActivated()) {
			leftButton.deactivate();
			currentSlideIndex--;
			if(currentSlideIndex < 0)
				currentSlideIndex = tutSlides.length - 1;
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(States.MAIN_MENU);
			
	}
	
	@Override
	public int getID() {
		return States.TUTORIAL;
	}
}
