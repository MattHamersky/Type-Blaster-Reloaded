package tbr.states.menus;

import java.awt.Font;
import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.FontUtils;

import Util.Utility;

import tbr.game.Profile;
import tbr.main.Config;
import tbr.states.States;
import tbr.widgets.ListViewTextButton;
import tbr.widgets.TextButton;

public class ProfileState extends BasicGameState {
	
	private StateBasedGame game;
	
	private File[] profiles;
	private ListViewTextButton[] profileButtons;
	
	private UnicodeFont titleFont;
	private final String titleText = "Profiles";
	
	private TextButton newProfile, deleteProfile, selectProfile;
	
	private boolean showNewDialog = false;
	private TextField newUserTB;
	private UnicodeFont newUserTBFont;
	private TextButton ok, cancel;
	
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
	}
	
	@Override
	public void enter(final GameContainer container, final StateBasedGame game) throws SlickException {
		refreshProfilesList(container);
		titleFont = Utility.makeFont(new Font("Verdana", Font.BOLD, 60), titleText);
		
		newUserTBFont = Utility.makeFont(new Font("Verdana", Font.PLAIN, 36));
		newUserTB = new TextField(container, newUserTBFont, Config.WIDTH / 2 - 250, 475, 500, 50);
		newUserTB.setBackgroundColor(Color.black);
		newUserTB.setMaxLength(16);
		ok = new TextButton(container, "OK", Config.WIDTH / 2 - 250, 525, 40, 40, false);
		ok.setColor(Color.black);
		ok.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				//make sure the profile name isn't all whitespace
				if(newUserTB.getText().trim().length() == 0) {
					newUserTB.setText("");
					return;
				}
				
				//make sure the profile name hasn't already been taken
				for(int i = 0; i < profileButtons.length; i++) {
					if(profileButtons[i].getText().equals(newUserTB.getText())) {
						newUserTB.setText("");
						return;
					}
				}
				
				ok.disable();
				cancel.disable();
				Profile.currentUser = new Profile(newUserTB.getText());
				cancelDialog();
				//refreshProfilesList(container);
				game.enterState(States.MAIN_MENU);
			}
		});
		ok.disable();
		cancel = new TextButton(container, "Cancel", Config.WIDTH / 2 + 150, 525, 40, 40, true);
		cancel.setColor(Color.black);
		cancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				ok.disable();
				cancel.disable();
				cancelDialog();
			}
		});
		cancel.disable();
		
		newProfile = new TextButton(container, "New...", Config.WIDTH / 2 - 450, 905, 40, 40, false);
		newProfile.setOverColor(Color.gray);
		newProfile.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				ok.enable();
				cancel.enable();
				showNewDialog = true;
				newUserTB.setText("");
				newProfile.disable();
				selectProfile.disable();
				deleteProfile.disable();
				for(int i = 0; i < profileButtons.length; i++)
					profileButtons[i].disable();
			}
		});
		selectProfile = new TextButton(container, "Select", Config.WIDTH / 2, 905, 40, 40, true);
		selectProfile.setOverColor(Color.gray);
		selectProfile.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				boolean selection = false;
				for(int i = 0; i < profileButtons.length; i++)
					if(profileButtons[i].isSelected()) {
						Profile.loadProfile(profiles[i]);
						game.enterState(States.MAIN_MENU);
						selection = true;
					}
				if(!selection)
					for(int i = 0; i < profileButtons.length; i++) 
						if(profileButtons[i].wasSelected()) {
							Profile.loadProfile(profiles[i]);
							game.enterState(States.MAIN_MENU);
							selection = true;
						}
			}
		});
		deleteProfile = new TextButton(container, "Delete", Config.WIDTH / 2 + 450, 905, 40, 40, false);
		deleteProfile.setOverColor(Color.gray);
		deleteProfile.rightAlign();
		deleteProfile.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent ac) {
				boolean selection = false;
				for(int i = 0; i < profileButtons.length; i++)
					if(profileButtons[i].isSelected() && !profileButtons[i].getText().equals(Profile.currentUser.getName())) {
						profiles[i].delete();
						refreshProfilesList(container);
						selection = true;
					}
				if(!selection)
					for(int i = 0; i < profileButtons.length; i++) 
						if(profileButtons[i].wasSelected() && !profileButtons[i].getText().equals(Profile.currentUser.getName())) {
							profiles[i].delete();
							refreshProfilesList(container);
							selection = true;
						}
			}
		});
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		profiles = null;
		for(int i = 0; i < profileButtons.length; i++) {
			profileButtons[i].destroy(); profileButtons[i] = null;
		}
		profileButtons = null;
		newProfile.destroy(); newProfile = null;
		deleteProfile.destroy(); deleteProfile = null;
		selectProfile.destroy(); selectProfile = null;
		newUserTB = null;
		ok.destroy(); ok = null;
		cancel.destroy(); cancel = null;
		titleFont = null;
		newUserTBFont = null;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//draw title
		FontUtils.drawCenter(titleFont, titleText, Config.WIDTH / 2, 120, 10);
		
		//draw profiles rectangle
		g.drawRect(Config.WIDTH / 2 - 450, 200, 900, 700);
		
		//draw buttons
		newProfile.render(container, g);
		selectProfile.render(container, g);
		deleteProfile.render(container, g);
		
		for(int i = 0; i < profileButtons.length; i++)
			profileButtons[i].render(container, g);
		
		if(showNewDialog) {
			g.fillRect(Config.WIDTH / 2 - 300, 400, 600, 200);
			newUserTBFont.drawString(Config.WIDTH / 2 - 250, 425, "Username:", Color.black);
			newUserTB.render(container, g);
			
			ok.render(container, g);
			cancel.render(container, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(showNewDialog)
			newUserTB.setFocus(true);
	}
	
	public void cancelDialog() {
		showNewDialog = false;
		newUserTB.setFocus(false);
		newProfile.enable();
		selectProfile.enable();
		deleteProfile.enable();
		
		for(int i = 0; i < profileButtons.length; i++)
			profileButtons[i].enable();
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_ESCAPE) {
			if(showNewDialog)
				cancelDialog();
			else
				game.enterState(States.MAIN_MENU);
		}
	}
	
	@Override
	public int getID() {
		return States.PROFILE_MENU;
	}
	
	public void refreshProfilesList(GameContainer container) {
		File file = new File("data/profiles");
		profiles = file.listFiles();
		profileButtons = new ListViewTextButton[profiles.length];
		for(int i = 0; i < profiles.length; i++)
			profileButtons[i] = new ListViewTextButton(container, profiles[i].getName().substring(0, profiles[i].getName().length()-8), Config.WIDTH / 2 - 445, 205+i*50, 40, 40);
		
	}

}
