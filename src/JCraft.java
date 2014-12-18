import org.lwjgl.*;
import org.lwjgl.input.*;
import org.lwjgl.util.glu.*;
import static org.lwjgl.opengl.GL11.*; // static import makes it easier to read...
import static org.lwjgl.util.glu.GLU.*;

/**
 * Game class, contains the main player and the chunks. 
 *
 */
public class JCraft {
	private Player player   = null;
	private ChunkManager cm = null;
	
	/**
	 * Sets up all the needed OpenGL stuff, initializes the player and 
	 * chunk manager that the game needs to update/render.
	 *
	 */
	public void init() { 
		this.cm = new ChunkManager();
		this.player = new Player(8.0, 70.0, 8.0, cm);
		cm.init();
		System.err.println("Game loaded");
		// GL setup stuff
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(45.0f, 800.0f / 600.0f, 0.1f, 100.0f);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_FRONT); // future: fix this to GL_BACK, requires fixing rendering methods by changing order of glVertex calls for (counter?) clockwise order
		System.err.println("GL initialized");
	}
	
	/**
	 * Updates the chunk manager and player objects, passes along
	 * the time since update was last called
	 *
	 */
	public void update(int delta_ms) { 
		player.update(delta_ms);
		cm.update(delta_ms, player.getX(), player.getY(), player.getZ());
	}
	
	/**
	 * Clears the buffers, changes the perspective, then renders the 
	 * contents of the chunk manager.
	 *
	 */
	public void render() { 
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		player.setup();
		cm.render(player.getX(), player.getY(), player.getZ());
	}
}