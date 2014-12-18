import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*; 
import static org.lwjgl.util.glu.GLU.*;

/**
 * Abstract representation of all blocks in game, holds location data, contains 
 * several methods needed for rendering/collisions. 
 * 
 */
public abstract class Block {
	protected double x;
	protected double y;
	protected double z;
	protected double[] color;
	
	/**
	 * Constructor, takes location as parameters
	 *
	 */
	public Block(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Returns x-coord
	 *
	 */
	public final double getX() { return x; }
	
	/**
	 * Returns y-coord
	 *
	 */
	public final double getY() { return y; }
	
	/**
	 * Returns z-coord
	 *
	 */
	public final double getZ() { return z; }
	
	/**
	 * Abstract method to render blocks, takes parameters to determine which
	 * faces of the block we're gonna draw
	 *
	 */
	public abstract void render(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six);
	
	/**
	 * Returns the color of this block (future: use to implement level of detail)
	 *
	 */
	public double[] getColor() {
		return color;
	}
	
	/**
	 * Used to determine if neighbor blocks are drawn, most blocks will be 
	 * opaque so we just have a default false returned
	 *
	 */
	public boolean invisible() { return false; }
	
	/**
	 * Returns ID of block
	 *
	 */
	public abstract int getID();
	
	/**
	 * implementation of the following method is based on "Fast Ray-Box Intersection" 
	 * by Andrew Woo in "Graphics Gems", Academic Press, 1990
	 * pt gets mutated, but pt is only useful if we return true
	 *
	 */
	private final int RIGHT = 0;
	private final int LEFT  = 1;
	private final int MID   = 2;
	public final boolean intersect(double[] pt, Ray r) {
		boolean inside = true;
		int[] quad = new int[3];
		int whichPlane = 0;
		double[] minB = new double[3]; minB[0] = x; minB[1] = y; minB[2] = z;
		double[] maxB = new double[3]; minB[0] = x + 1; minB[1] = y + 1; minB[2] = z + 1;
		double[] maxT = new double[3];
		double[] candidatePlane = new double[3];
		
		// get candidate planes...
		for (int i = 0; i < 3; i++) {
			if (r.ori[i] < minB[i]) {
				quad[i] = LEFT;
				candidatePlane[i] = minB[i];
				inside = false;
			} else if (r.ori[i] > maxB[i]) {
				quad[i] = RIGHT;
				candidatePlane[i] = maxB[i];
				inside= false;
			} else {
				quad[i] = MID;
			}
		}
		
		// ray originates inside the block
		if (inside) {
			pt[0] = r.ori[0];
			pt[1] = r.ori[1];
			pt[2] = r.ori[2];
			return true;
		}
		
		// calc T distances to candidate planes
		for (int i = 0; i < 3; i++) {
			if (quad[i] != MID && r.dir[i] != 0.0)
				maxT[i] = (candidatePlane[i] - r.ori[i]) / r.dir[i];
			else
				maxT[i] = -1.0;
		}
		
		// get largest of maxT to choose intersect
		for (int i = 1; i < 3; i++) {
			if (maxT[whichPlane] < maxT[i])
				whichPlane = i;
		}
		
		// check if the final pt is inside the box
		for (int i = 0; i < 3; i++) {
			if (whichPlane != i) {
				pt[i] = r.ori[i] + maxT[whichPlane] * r.dir[i];
				if (pt[i] < minB[i] || pt[i] > maxB[i])
					return false;
			} else {
				pt[i] = candidatePlane[i];
			}
		}
		return true; // ray hit the box, pt has the coords of the intersection
	}
	
	/**
	 * when called, draw an identifying cube around this block (white in color)
	 * ( future: determine why this breaks the rendering )
	 *
	 */
	public final void highlight(boolean draw) {
		if (draw) {
			glColor3f(0.0f, 0.0f, 0.0f);
			glBegin(GL_LINES);
				glVertex3d(1 + x, 1 + y, 1 + z);
				glVertex3d(1 + x, 1 + y, z);
				glVertex3d(1 + x, 1 + y, 1 + z);
				glVertex3d(1 + x, y, 1 + z);
				glVertex3d(1 + x, 1 + y, 1 + z);
				glVertex3d(x, 1 + y, 1 + z);
				glVertex3d(x, y, z);
				glVertex3d(x, y, 1 + z);
				glVertex3d(x, y, z);
				glVertex3d(x, 1 + y, z);
				glVertex3d(x, y, z);
				glVertex3d(1 + x, y, z);
				glVertex3d(1 + x, y, 1 + z);
				glVertex3d(1 + x, y, z);
				glVertex3d(1 + x, y, z);
				glVertex3d(1 + x, 1 + y, z);
				glVertex3d(1 + x, 1 + y, z);
				glVertex3d(x, 1 + y, z);
				glVertex3d(x, 1 + y, 1 + z);
				glVertex3d(x, y, 1 + z);
				glVertex3d(x, 1 + y, 1 + z);
				glVertex3d(x, 1 + y, z);
				glVertex3d(1 + x, y, 1 + z);
				glVertex3d(x, y, 1 + z);
			glEnd();
		}
	}
	
	/**
	 * Renders the faces of a block based on passed booleans, uses texture 
	 * of the passed block_id, all faces drawn the same
	 * (future: another method for drawing when faces aren't the same, like
	 * a tree stump)
	 *
	 */
	protected final void texRenderAll(int block_id, boolean one, boolean two, boolean three, boolean four, boolean five, boolean six) {
		glEnable(GL_TEXTURE_2D);
		Texture texture = TextureContainer.getTexByID(block_id);
		glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
		glBegin(GL_QUADS);
			// xy
			if (one)
				drawxy();
			// xy, 1+z
			if (two)
				drawxyone();
			// xz
			if (three)
				drawxz();
			// xz, 1+y
			if (four)
				drawxzone();
			//yz
			if (five)
				drawyz();
			//yz, 1+x
			if (six)
				drawyzone();
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
	
	/**
	 * Draws face at z = 0 plane
	 *
	 */
	private void drawxy() {
		glTexCoord2f(0, 0); glVertex3d(x      , y      , z);
		glTexCoord2f(1, 0); glVertex3d(x + 1.0, y      , z);
		glTexCoord2f(1, 1); glVertex3d(x + 1.0, y + 1.0, z);
		glTexCoord2f(0, 1); glVertex3d(x      , y + 1.0, z);
	}
	
	/**
	 * Draws face at z = 1 plane
	 *
	 */
	private void drawxyone() {
		glTexCoord2f(0, 1); glVertex3d(x      , y + 1.0, z + 1.0);
		glTexCoord2f(1, 1); glVertex3d(x + 1.0, y + 1.0, z + 1.0);
		glTexCoord2f(1, 0); glVertex3d(x + 1.0, y      , z + 1.0);
		glTexCoord2f(0, 0); glVertex3d(x      , y      , z + 1.0);
	}
	
	/**
	 * Draws face at y = 0 plane
	 *
	 */
	private void drawxz() {
		glTexCoord2f(0, 1); glVertex3d(x      , y, z + 1.0);
		glTexCoord2f(1, 1); glVertex3d(x + 1.0, y, z + 1.0);
		glTexCoord2f(1, 0); glVertex3d(x + 1.0, y, z);
		glTexCoord2f(0, 0); glVertex3d(x      , y, z);
	}
	
	/**
	 * Draws face at y = 1 plane
	 *
	 */
	private void drawxzone() {
		glTexCoord2f(0, 0); glVertex3d(x      , y + 1.0, z);
		glTexCoord2f(1, 0); glVertex3d(x + 1.0, y + 1.0, z);
		glTexCoord2f(1, 1); glVertex3d(x + 1.0, y + 1.0, z + 1.0);
		glTexCoord2f(0, 1); glVertex3d(x      , y + 1.0, z + 1.0);
	}
	
	/**
	 * Draws face at x = 0 plane
	 *
	 */
	private void drawyz() {
		glTexCoord2f(0, 0); glVertex3d(x, y      , z);
		glTexCoord2f(1, 0); glVertex3d(x, y + 1.0, z);
		glTexCoord2f(1, 1); glVertex3d(x, y + 1.0, z + 1.0);
		glTexCoord2f(0, 1); glVertex3d(x, y      , z + 1.0);
	}
	
	/**
	 * Draws face at x = 1 plane
	 *
	 */
	private void drawyzone() {
		glTexCoord2f(0, 1); glVertex3d(x + 1.0, y      , z + 1.0);
		glTexCoord2f(1, 1); glVertex3d(x + 1.0, y + 1.0, z + 1.0);
		glTexCoord2f(1, 0); glVertex3d(x + 1.0, y + 1.0, z);
		glTexCoord2f(0, 0); glVertex3d(x + 1.0, y      , z);
	}
}