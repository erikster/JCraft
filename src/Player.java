import org.lwjgl.input.*;
import java.util.*;

/**
 * Class representation of the main player, used for camera + movement + interactions
 *
 */
public class Player {
	private double x, y, z, yaw, pitch; // pos of feet + direction faced
	private View eyes;
	private double[][] interestPts;
	private ChunkManager cm;
	private double vy;
	private Block lastSeen;
	private double[] intersection;
	
	private double mouse_dx = 0.0;
	private double mouse_dy = 0.0;
	private double sensitivity = 0.05;
	private double movespeed = 0.8;
	private double gravity = 0.05;
	
	/**
	 * Constructor, takes a spawning location and reference to the chunkmanager
	 *
	 */
	public Player(double x, double y, double z, ChunkManager cm) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 135.0;
		this.pitch = 90.0;
		this.eyes = new View();
		this.interestPts = new double[10][3];
		this.cm = cm;
		this.lastSeen = null;
		this.intersection = new double[3];
	}
	
	/**
	 * Updates location/actions/camera, takes a time in milliseconds since the
	 * last call to update.
	 *
	 */
	public void update(int delta_ms) {
		mouse_dx = Mouse.getDX();
		mouse_dy = -1.0 * Mouse.getDY();
		yaw(mouse_dx * sensitivity);
		pitch(mouse_dy * sensitivity);
		
		// gravity 
		if (!cm.blockUnderneath(x, y, z)) {
			if (vy > -0.4)
				vy -= gravity;
		} else {
			vy = 0;
		}
		
		// unstick from ground
		if (cm.getBlockAt(x, y, z).getID() != 0) {
			y += 0.04;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			mvFwd(movespeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			mvBack(movespeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			mvLeft(movespeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			mvRight(movespeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			if (cm.blockUnderneath(x, y, z)) {
				jump();
			}
		}
		if (Mouse.isButtonDown(0)) { // left, destroy
			if (lastSeen != null && lastSeen.getID() != 1) {
				cm.update(intersection[0], intersection[1], intersection[2], 0);
				//System.err.println("trying to delete: " + Arrays.toString(intersection));
				//System.err.println("while at location: " + eyes.x + ", " + eyes.y + ", " + eyes.z);
				//System.err.println("w/ viewangles: " + pitch + ", " + yaw);
			} else {
				//System.err.println("deletion failed");
			}
		} else if (Mouse.isButtonDown(1)) { // right, place
			// future: implement
		}
		
		y += vy;
		
		eyes.x = x;
		eyes.y = y + 1.5;
		eyes.z = z;
		eyes.yaw = yaw;
		eyes.pitch = pitch;
		
		// "un-highlight" last block
		//if (lastSeen != null)
			//lastSeen.highlight(false);
		
		double dx = Math.cos(Math.toRadians(yaw + 90)) * - Math.cos(Math.toRadians(pitch));
		double dy = - Math.sin(Math.toRadians(pitch));
		double dz = Math.sin(Math.toRadians(yaw + 90)) * - Math.cos(Math.toRadians(pitch));
		Ray r = new Ray(eyes.x, eyes.y, eyes.z, dx, dy, dz);
		// perform ray-checks
		for (int i = 0; i < 10; i++) { // for each interest point...
			interestPts[i][0] = r.ori[0] + (i / 2) * r.dir[0];
			interestPts[i][1] = r.ori[1] + (i / 2) * r.dir[1];
			interestPts[i][2] = r.ori[2] + (i / 2) * r.dir[2];
			Block curr = cm.getBlockAt(interestPts[i][0], interestPts[i][1], interestPts[i][2]);
			if (curr != null && curr.getID() != 0) { // can't highlight air
				//if (curr.intersect(intersection, r)) {
					lastSeen = curr;
				//}
				intersection = new double[] { interestPts[i][0], interestPts[i][1], interestPts[i][2] };
				//System.err.println("FROM: " + x + ", " + y + ", " + z);
				//System.err.println("AT:" + Arrays.toString(intersection));
				//System.err.println("w/RAY: " + r);
				break; // nothing more to do, get out of loop
			}
		}
		
		// highlight new block
		//if (lastSeen != null)
			//lastSeen.highlight(true);
	}
	
	/**
	 * Returns x-coord of player's feet
	 *
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns y-coord of player's feet
	 *
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns z-coord of player's feet
	 *
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Changes y-velocity to 0.6 to simulate a jump
	 * future: less magic numbers
	 *
	 */
	public void jump() {
		vy = 0.6;
	}
	
	/**
	 * Updates the contained view
	 *
	 */
	public void setup() {
		eyes.setup();
	}
	
	/**
	 * Changes "yaw" of camera (left/right rotation, y=0 plane) by amount passed
	 *
	 */
	public void yaw(double dyaw) {
		yaw += dyaw;
	}
	
	/**
	 * Changes "pitch" of camera (up/down rotation, z or x = 0) by amount passed
	 *
	 */
	public void pitch(double dpitch) {
		double val = pitch += dpitch;
		if (val > 87.0)
			pitch = 87.0;
		else if (val < -87.0)
			pitch = -87.0;
		else
			pitch = val;
	}
	
	/**
	 * Moves this player forward by given distance if not hitting boundaries/blocks
	 *
	 */
	public void mvFwd(double dist) {
		double val1 = x + dist * Math.sin(Math.toRadians(yaw));
		if (val1 < 0.0)
			x = 0.0;
		else if (val1 > 511.0)
			x = 511.0;
		else if (cm.getBlockAt(val1, y + 0.3, z) == null || cm.getBlockAt(val1, y + 0.3, z).getID() != 0)
			x = x;
		else x = val1;
		
		double val2 = z - dist * Math.cos(Math.toRadians(yaw));
		if (val2 < 0.0)
			z = 0.0;
		else if (val2 > 511.0)
			z = 511.0;
		else if (cm.getBlockAt(x, y + 0.3, val2) == null || cm.getBlockAt(x, y + 0.3, val2).getID() != 0)
			z = z;
		else z = val2;
	}
	
	/**
	 * Moves this player backwards by given distance if not hitting boundaries/blocks
	 *
	 */
	public void mvBack(double dist) {
		double val1 = x - dist * Math.sin(Math.toRadians(yaw));
		if (val1 < 0.0)
			x = 0.0;
		else if (val1 > 511.0)
			x = 511.0;
		else if (cm.getBlockAt(val1, y + 0.3, z) == null || cm.getBlockAt(val1, y + 0.3, z).getID() != 0)
			x = x;
		else x = val1;
		
		double val2 = z + dist * Math.cos(Math.toRadians(yaw));
		if (val2 < 0.0)
			z = 0.0;
		else if (val2 > 511.0)
			z = 511.0;
		else if (cm.getBlockAt(x, y + 0.3, val2) == null || cm.getBlockAt(x, y + 0.3, val2).getID() != 0)
			z = z;
		else z = val2;		
	}
	
	/**
	 * Strafes this player left by given distance if not hitting boundaries/blocks
	 *
	 */
	public void mvLeft(double dist) {
		double val1 = x + dist * Math.sin(Math.toRadians(yaw - 90));
		if (val1 < 0.0)
			x = 0.0;
		else if (val1 > 511.0)
			x = 511.0;
		else if (cm.getBlockAt(val1, y + 0.3, z) == null || cm.getBlockAt(val1, y + 0.3, z).getID() != 0)
			x = x;
		else x = val1;
		
		double val2 = z - dist * Math.cos(Math.toRadians(yaw - 90));
		if (val2 < 0.0)
			z = 0.0;
		else if (val2 > 511.0)
			z = 511.0;
		else if (cm.getBlockAt(x, y + 0.3, val2) == null || cm.getBlockAt(x, y + 0.3, val2).getID() != 0)
			z = z;
		else z = val2;
	}
	
	/**
	 * Strafes this player right by given distance if not hitting boundaries/blocks
	 *
	 */
	public void mvRight(double dist) {
		double val1 = x + dist * Math.sin(Math.toRadians(yaw + 90));
		if (val1 < 0.0)
			x = 0.0;
		else if (val1 > 511.0)
			x = 511.0;
		else if (cm.getBlockAt(val1, y + 0.3, z) == null || cm.getBlockAt(val1, y + 0.3, z).getID() != 0)
			x = x;
		else x = val1;
		
		double val2 = z - dist * Math.cos(Math.toRadians(yaw + 90));
		if (val2 < 0.0)
			z = 0.0;
		else if (val2 > 511.0)
			z = 511.0;
		else if (cm.getBlockAt(x, y + 0.3, val2) == null || cm.getBlockAt(x, y + 0.3, val2).getID() != 0)
			z = z;
		else z = val2;
	}
}