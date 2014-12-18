import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Holds our main method, maintains the loop and passes along the time 
 * since the last update to JCraft. 
 *
 */
public class Launch {
	private static long time_ms = 0;
	
	/**
	 * Main method of program.
	 *
	 */
	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		}
		
		time_ms = getTime();
		int delta_ms = 0;
		JCraft game = new JCraft();
		
		TextureContainer.init();
		
		game.init();
		
		Mouse.setGrabbed(true);
		
		while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			delta_ms = getDelta();
			
			game.update(delta_ms);
			game.render();
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}
	
	// credit to ninjacave.com for this method
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	// credit to ninjacave.com for this method
	// side-effect: sets time_ms to latest time
	public static int getDelta() {
		long time = getTime();
		int result = (int) (time - time_ms);
		time_ms = time;
		return result;
	}
}