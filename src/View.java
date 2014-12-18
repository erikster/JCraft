import static org.lwjgl.opengl.GL11.*;

/**
 * Helper class for the Player that sets up the translations/rotations needed
 * by OpenGL so we can "see" our world.
 *
 */
public class View {
	public double x, y, z, yaw, pitch;
	
	/**
	 * Constructor, no args, default values.
	 *
	 */
	public View() {
		this.x = 0.0;
		this.y = 0.0;
		this.z = 0.0;
		this.yaw = 0.0;
		this.pitch = 0.0;
	}
	
	/**
	 * Makes OpenGL calls so we have the right perspective of the world.
	 * Assumes ModelView.
	 *
	 */
	public void setup() {
		glRotated(pitch, 1.0, 0.0, 0.0);
		glRotated(yaw, 0.0, 1.0, 0.0);
		glTranslated(-x, -y, -z);
	}
}
