/**
 * Class representation of a dirt block
 *
 */
public class DirtBlock extends Block {
	public static final int ID = 3;
	public static final double[] color = { 0.55f, 0.25f, 0.1f };
	
	/**
	 * Constructor, takes params for location
	 *
	 */
	public DirtBlock(double x, double y, double z) {
		super(x, y, z);
	}
	
	/**
	 * Renders a dirt block by calling a parent method
	 *
	 */
	@Override
	public void render(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six) { 
		texRenderAll(ID, one, two, three, four, five, six);
	}
	
	/**
	 * Returns the unique id for dirt blocks
	 *
	 */
	@Override
	public int getID() {
		return ID;
	}
}