/**
 * Class representation of "bedrock" which is unbreakable
 *
 */
public class BedrockBlock extends Block {
	public static final int ID = 1;
	public static final double[] color = { 0.9f, 0.9f, 0.9f };
	
	/**
	 * Constructor, takes params for location
	 *
	 */
	public BedrockBlock(double x, double y, double z) {
		super(x, y, z);
	}
	
	/**
	 * Renders a bedrock block by calling a parent method
	 *
	 */
	@Override
	public void render(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six) { 
		texRenderAll(ID, one, two, three, four, five, six);
	}
	
	/**
	 * Returns the unique id of bedrock blocks
	 *
	 */
	@Override
	public int getID() {
		return ID;
	}
}