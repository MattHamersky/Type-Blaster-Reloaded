package tbr.widgets;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.FontUtils;

public class MainMenuButton extends TextButton {

	private Image rightFacingArrow, leftFacingArrow;
	private int tWidth;
	
	public MainMenuButton(GUIContext container, String text, Image arrow, int x, int y, int size, int overSize, boolean center) {
		super(container, text, x, y, size, overSize, center);
		
		this.rightFacingArrow = arrow;
		this.leftFacingArrow = rightFacingArrow.getFlippedCopy(true, false);
		this.overColor = Color.white;
		this.tWidth = overFont.getWidth(text);
	}
	
	@Override
	public void render(GUIContext context, Graphics g) throws SlickException {
		if(isOver) {
			rightFacingArrow.draw(x - 20 - rightFacingArrow.getWidth() - tWidth / 2, y + 5);
			FontUtils.drawCenter(overFont, text, x, y, 0, overColor);
			leftFacingArrow.draw(x + 20 + tWidth / 2, y + 5);
		}
		else {
			FontUtils.drawCenter(font, text, x, y, 0, color);
		}
	}

}
