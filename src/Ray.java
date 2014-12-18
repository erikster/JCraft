/**
 * Utility class used for determining ray/cube collisions
 *
 */
public class Ray {
	public double[] ori; // origin - point
	public double[] dir; // direction - vector
	
	/**
	 * Constructor, takes 6 args for origin + direction. First three args are 
	 * for origin, last three are for direction.
	 *
	 */
	public Ray(double ox, double oy, double oz, double dx, double dy, double dz) {
		ori = new double[3]; dir = new double[3];
		this.ori[0] = ox;
		this.ori[1] = oy;
		this.ori[2] = oz;
		this.dir[0] = dx;
		this.dir[1] = dy;
		this.dir[2] = dz;
	}
	
	/**
	 * Returns string representation of a Ray (origin + direction)
	 *
	 */
	@Override
	public String toString() {
		return ori[0] + ", " + ori[1] + ", " + ori[2] + " + " + dir[0] + ", " + dir[1] + ", " + dir[2];
	}
}