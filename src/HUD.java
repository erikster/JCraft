import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import static org.lwjgl.opengl.GL11.*;

public class HUD {
	public int delta;
	private Font awtfont;
	private TrueTypeFont drawablefont;
	
	public HUD() {
		awtfont = new Font("Times New Roman", Font.PLAIN, 18);
		drawablefont = new TrueTypeFont(awtfont, false);
	}
	
	void renderFPS() {
		float num = 1000 / (delta + 1);
		
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_DEPTH_TEST);
		drawablefont.drawString(100, 50, "FPS: " + delta, Color.white);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_TEXTURE_2D);
	}
}