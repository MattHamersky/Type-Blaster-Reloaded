package tbr.states.menus;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
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
import tbr.main.DisplayModeWrapper;
import tbr.states.States;
import tbr.widgets.Button;
import tbr.widgets.TextButton;

public class OptionsMenuState extends BasicGameState {

	private StateBasedGame game;
	
	private UnicodeFont titleFont, optionsFont;
	private TextButton backButton, applyButton;
	
	private Button resolutionRight, resolutionLeft;
	private List<DisplayModeWrapper> displayModes = new ArrayList<DisplayModeWrapper>();
	private int currentResolutionIndex;
	
	private String[] noyes = {"No", "Yes"};
	
	private Button fullscreenRight, fullscreenLeft;
	private int currentFullscreenIndex;
	
	private Button vsyncRight, vsyncLeft;
	private int currentVsyncIndex;
	
	private Button showFPSRight, showFPSLeft;
	private int currentShowFPSIndex;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}

	@Override
	public void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
		try {
			//get list of all display modes
			DisplayMode[] dm = Display.getAvailableDisplayModes();
			Set<DisplayModeWrapper> tempSet = new HashSet<DisplayModeWrapper>();
			//add display modes to hash set to prevent duplicates (we don't care about the bit depth)
			for(int i = 0; i < dm.length; i++)
				tempSet.add(new DisplayModeWrapper(dm[i].getWidth(), dm[i].getHeight()));
			
			displayModes.addAll(tempSet); //add set to list
			Collections.sort(displayModes); //sort list in ascending order
			
			//default to showing the currently selected resolution
			for(int i = 0; i < displayModes.size(); i++) {
				if(Config.G_WIDTH == displayModes.get(i).width && Config.G_HEIGHT == displayModes.get(i).height)
					currentResolutionIndex = i;
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		//initialize current options indices
		currentFullscreenIndex = Config.IS_FULLSCREEN ? 1 : 0;
		currentVsyncIndex = Config.VSYNC ? 1 : 0;
		currentShowFPSIndex = Config.SHOW_FPS ? 1 : 0;
		
		//load fonts
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60));
		optionsFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 40));
		
		//load buttons
		backButton = new TextButton(container, "Back", Config.WIDTH / 2 - 400, 760, 40, 40, false);
		backButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				game.enterState(States.MAIN_MENU);
			}
		});
		applyButton = new TextButton(container, "Apply", Config.WIDTH / 2 + 400, 760, 40, 40, false);
		applyButton.rightAlign();
		applyButton.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent component) {
				saveConfig();
				try {
					AppGameContainer appgc = (AppGameContainer) container;
					appgc.setDisplayMode(Config.G_WIDTH, Config.G_HEIGHT, Config.IS_FULLSCREEN);
					appgc.setVSync(Config.VSYNC);
					appgc.setShowFPS(Config.SHOW_FPS);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			private void saveConfig() {
				Config.G_WIDTH = displayModes.get(currentResolutionIndex).width;
				Config.G_HEIGHT = displayModes.get(currentResolutionIndex).height;
				Config.IS_FULLSCREEN = (currentFullscreenIndex != 0);
				Config.VSYNC =  (currentVsyncIndex != 0);
				Config.SHOW_FPS = (currentShowFPSIndex != 0);
				Config.saveConfig();
			}
		});
		
		//load arrow buttons
		Image rightArrow = new Image("images/menu_buttons/right_arrow.png");
		Image rightArrowArmed = new Image("images/menu_buttons/right_arrow_armed.png");
		Image rightArrowOver = new Image("images/menu_buttons/right_arrow_over.png");
		
		Image leftArrow = new Image("images/menu_buttons/right_arrow.png").getFlippedCopy(true, false);
		Image leftArrowArmed = new Image("images/menu_buttons/right_arrow_armed.png").getFlippedCopy(true, false);
		Image leftArrowOver = new Image("images/menu_buttons/right_arrow_over.png").getFlippedCopy(true, false);
		
		resolutionRight = new Button(container, Config.WIDTH / 2 + 335, 270, rightArrow, rightArrowArmed, rightArrowOver, null);
		resolutionLeft = new Button(container, Config.WIDTH / 2 + 33, 270, leftArrow, leftArrowArmed, leftArrowOver, null);
		
		fullscreenRight = new Button(container, Config.WIDTH / 2 + 260, 350, rightArrow, rightArrowArmed, rightArrowOver, null);
		fullscreenLeft = new Button(container, Config.WIDTH / 2 + 108, 350, leftArrow, leftArrowArmed, leftArrowOver, null);
		
		vsyncRight = new Button(container, Config.WIDTH / 2 + 260, 430, rightArrow, rightArrowArmed, rightArrowOver, null);
		vsyncLeft = new Button(container, Config.WIDTH / 2 + 108, 430, leftArrow, leftArrowArmed, leftArrowOver, null);
		
		showFPSRight = new Button(container, Config.WIDTH / 2 + 260, 510, rightArrow, rightArrowArmed, rightArrowOver, null);
		showFPSLeft = new Button(container, Config.WIDTH / 2 + 108, 510, leftArrow, leftArrowArmed, leftArrowOver, null);
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		titleFont = null;
		optionsFont = null;
		backButton.destroy(); backButton = null;
		applyButton.destroy(); applyButton = null;
		resolutionRight.destroy(); resolutionRight = null;
		resolutionLeft.destroy(); resolutionLeft = null;
		fullscreenRight.destroy(); fullscreenRight = null;
		fullscreenLeft.destroy(); fullscreenLeft = null;
		vsyncRight.destroy(); vsyncRight = null;
		vsyncLeft.destroy(); vsyncLeft = null;
		showFPSRight.destroy(); showFPSRight = null;
		showFPSLeft.destroy(); showFPSLeft = null;
		displayModes.clear();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//draw title
		FontUtils.drawCenter(titleFont, "Options", Config.WIDTH / 2, 120, 0);
		
		//draw rectangle that encloses the options
		g.setColor(Color.white);
		g.drawRect(Config.WIDTH / 2 - 400, 250, 800, 500);
		
		optionsFont.drawString(Config.WIDTH / 2 - 375, 270, "Resolution:");
		g.drawRect(Config.WIDTH / 2 + 75, 270, 250, 50);
		FontUtils.drawCenter(optionsFont, displayModes.get(currentResolutionIndex).toString(), Config.WIDTH / 2 + 200, 270, 0);
		resolutionRight.render(container, g);
		resolutionLeft.render(container, g);
		
		optionsFont.drawString(Config.WIDTH / 2 - 375, 350, "Fullscreen:");
		g.drawRect(Config.WIDTH / 2 + 150, 350, 100, 50);
		FontUtils.drawCenter(optionsFont, noyes[currentFullscreenIndex], Config.WIDTH / 2 + 200, 350, 0);
		fullscreenRight.render(container, g);
		fullscreenLeft.render(container, g);
		
		optionsFont.drawString(Config.WIDTH / 2 - 375, 430, "Vertical Sync:");
		g.drawRect(Config.WIDTH / 2 + 150, 430, 100, 50);
		FontUtils.drawCenter(optionsFont, noyes[currentVsyncIndex], Config.WIDTH / 2 + 200, 430, 0);
		vsyncRight.render(container, g);
		vsyncLeft.render(container, g);
		
		optionsFont.drawString(Config.WIDTH / 2 - 375, 510, "Show FPS:");
		g.drawRect(Config.WIDTH / 2 + 150, 510, 100, 50);
		FontUtils.drawCenter(optionsFont, noyes[currentShowFPSIndex], Config.WIDTH / 2 + 200, 510, 0);
		showFPSRight.render(container, g);
		showFPSLeft.render(container, g);
		
		backButton.render(container, g);
		applyButton.render(container, g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(resolutionRight.isActivated()) {
			resolutionRight.deactivate();
			currentResolutionIndex++;
			if(currentResolutionIndex >= displayModes.size())
				currentResolutionIndex = 0;
		}
		if(resolutionLeft.isActivated()) {
			resolutionLeft.deactivate();
			currentResolutionIndex--;
			if(currentResolutionIndex < 0)
				currentResolutionIndex = displayModes.size()-1;
		}
		
		if(fullscreenRight.isActivated()) {
			fullscreenRight.deactivate();
			currentFullscreenIndex++;
			if(currentFullscreenIndex >= noyes.length)
				currentFullscreenIndex = 0;
		}
		if(fullscreenLeft.isActivated()) {
			fullscreenLeft.deactivate();
			currentFullscreenIndex--;
			if(currentFullscreenIndex < 0)
				currentFullscreenIndex = noyes.length-1;
		}
		
		if(vsyncRight.isActivated()) {
			vsyncRight.deactivate();
			currentVsyncIndex++;
			if(currentVsyncIndex >= noyes.length)
				currentVsyncIndex = 0;
		}
		if(vsyncLeft.isActivated()) {
			vsyncLeft.deactivate();
			currentVsyncIndex--;
			if(currentVsyncIndex < 0)
				currentVsyncIndex = noyes.length-1;
		}
		
		if(showFPSRight.isActivated()) {
			showFPSRight.deactivate();
			currentShowFPSIndex++;
			if(currentShowFPSIndex >= noyes.length)
				currentShowFPSIndex = 0;
		}
		if(showFPSLeft.isActivated()) {
			showFPSLeft.deactivate();
			currentShowFPSIndex--;
			if(currentShowFPSIndex < 0)
				currentShowFPSIndex = noyes.length-1;
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE)
			game.enterState(States.MAIN_MENU);
	}
	
	@Override
	public int getID() {
		return States.OPTIONS_MENU;
	}

}
