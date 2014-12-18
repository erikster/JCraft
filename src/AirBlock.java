/**
 * Class representation of blocks of air
 *
 */

public class AirBlock extends Block {
	public static final int ID = 0;
	public static final double[] color = new double[] { 1.0f, 1.0f, 1.0f };
	
	/**
	 * Constructor, takes params for location
	 *
	 */
	public AirBlock(double x, double y, double z) {
		super(x, y, z);
	}
	
	/**
	 * "renders" this block, but there's nothing to do since it's just air
	 *
	 */
	@Override
	public void render(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six) { 
		// no-op
	}
	
	/**
	 * Override of parent's "invisible" method, returns true. So, if there's 
	 * a block next to it, that face should be visible
	 *
	 */
	@Override
	public boolean invisible() {
		return true;
	}
	
	/**
	 * Returns the unique id of air blocks
	 *
	 */
	@Override
	public int getID() {
		return ID;
	}
}