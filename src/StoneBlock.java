/**
 * Class representation of a stone block
 *
 */
public class StoneBlock extends Block {
	public static final int ID = 2;
	public static final double[] color = { 0.8, 0.8, 0.8 };
	
	/**
	 * Constructor, takes params for location
	 *
	 */
	public StoneBlock(double x, double y, double z) {
		super(x, y, z);
	}
	
	/**
	 * Renders a stone block by calling a parent method
	 *
	 */
	@Override
	public void render(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six) { 
		texRenderAll(ID, one, two, three, four, five, six);
	}
	
	/**
	 * Returns the unique ID of stone blocks
	 *
	 */
	@Override
	public int getID() {
		return ID;
	}
}