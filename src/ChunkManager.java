import java.util.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Tracks, loads/generates, updates all chunks in the game
 *
 */
public class ChunkManager {
	private Chunk[][][] chunks;
	private List<Triple> inMemory; // tracks existing chunks
	private int time_ms; // time since last check for unwanted chunks
	
	/**
	 * Constructor, no args
	 *
	 */
	public ChunkManager() {
		this.chunks = new Chunk[32][8][32];
		this.inMemory = new ArrayList<>();
		this.time_ms = 0;
	}

	/**
	 * Initializes first chunk for the player to walk around in
	 *
	 */
	public void init() {
		for (int i = 0; i < 8; i++) {
			Triple t = new Triple(0, i, 0);
			inMemory.add(t);
			chunks[0][i][0] = new Chunk(0, i * 16, 0, this);
			chunks[0][i][0].init();
		}
	}

	/**
	 * Every half-second, checks around current player position to decide 
	 * if we want to create more chunks for the player to see
	 *
	 */
	public void update(int delta_ms, double x, double y, double z) {
		if (time_ms > 500) {
			int inx = (int) x, iny = (int) y, inz = (int) z; // points in int form
			for (int i = -3; i < 3; i++) {
				for (int j = -3; j < 3; j++) {
					for (int k = -3; k < 3; k++) {
						Triple tcurr = new Triple(inx / 16, iny / 16, inz / 16);
						Triple tnext = new Triple(inx / 16 + i, iny / 16 + j, inz / 16 + k);
						if (tnext.one <= 31 && tnext.two <= 7 && tnext.thr <= 31 &&
							tnext.one >= 0 && tnext.two >= 0 && tnext.thr >= 0) {
							if (!inMemory.contains(tnext)) {
								chunks[tnext.one][tnext.two][tnext.thr] = new Chunk(16 * tnext.one, 16 * tnext.two, 16 * tnext.thr, this);
								chunks[tnext.one][tnext.two][tnext.thr].init();
								inMemory.add(tnext);
								updateRound(tnext);
							}
						}
					}
				}
			}
			time_ms = 0;
		} else {
			time_ms += delta_ms;
		}
	}
	
	/**
	 * Updates the block at the point given
	 *
	 */
	public void update(double i, double j, double k, int block_id) {
		int cx = (int) i / 16; int cxm = (int) i % 16;
		int cy = (int) j / 16; int cym = (int) j % 16;
		int cz = (int) k / 16; int czm = (int) k % 16;
		chunks[cx][cy][cz].update(cxm, cym, czm, block_id);
	}
	
	/**
	 * Updates neighbor chunks (not diagonals) at the location given by the passed Triple.
	 * (future: check this method and its calls to determine if it's 
	 * causing framerate drops)
	 *
	 */
	public void updateRound(Triple t) {
		if (inMemory.contains(t)) {
			Triple[] testables = { 
				new Triple(t.one - 1, t.two, t.thr),
				new Triple(t.one + 1, t.two, t.thr),
				new Triple(t.one, t.two - 1, t.thr),
				new Triple(t.one, t.two + 1, t.thr),
				new Triple(t.one, t.two, t.thr - 1),
				new Triple(t.one, t.two, t.thr + 1)
			};
			for (int i = 0; i < 6; i++) {
				Triple t1 = testables[i];
				if (inMemory.contains(t1))
					chunks[t1.one][t1.two][t1.thr].redraw();
			}
		} // else no-op
	}
	
	/**
	 * Renders all chunks within the range of the passed coordinates 
	 *
	 */
	public void render(double x, double y, double z) {
		for (int i = 0; i < inMemory.size(); i++) {
			Triple t = inMemory.get(i);
			//System.err.println("Rendering chunk: " + t);
			if (inRange(t, x, y, z))
				chunks[t.one][t.two][t.thr].render();
		}
	}
	
	/**
	 * Returns true if location of the passed chunk is within 64 "units" of the player
	 * Excuse the magic numbers
	 */
	public boolean inRange(Triple t, double x, double y, double z) {
		double x1 = (double) t.one * 16 + 8;
		double y1 = (double) t.two * 16 + 8;
		double z1 = (double) t.thr * 16 + 8;
		
		//System.err.println("At location: " + x + ", " + y + ", " + z);
		
		return ((x - x1) * (x - x1) + (y - y1) * (y - y1) + (z - z1) * (z - z1)) < 64 * 64; // this means 64 is our current render distance, but we can fix this all up later
	}
	
	/**
	 * Returns block at location given (rounded down as per double -> int conversion)
	 *
	 */
	public Block getBlockAt(double x, double y, double z) {
		int inx = (int) x;
		int iny = (int) y;
		int inz = (int) z;
		if (inx < 0 || inx > 511 || iny < 0 || iny > 127 || inz < 0 || inz > 511)
			return null;
		if (!inMemory.contains(new Triple(inx / 16, iny / 16, inz / 16)))
			return null;
		return chunks[inx / 16][iny / 16][inz / 16].getBlock(inx % 16, iny % 16, inz % 16);
	}
	
	/**
	 * Checks for a block at the passed location (0.1 subtracted from y coord), 
	 * returns true if one is found, false otherwise
	 *
	 */
	public boolean blockUnderneath(double x, double y, double z) {
		Block b = getBlockAt(x, y - 0.1, z);
		if (b != null) {
			return (b.getID() != 0);
		}
		return true;
	}
}