import java.io.*;

import static org.lwjgl.opengl.GL11.*;

public class Chunk {
	private Block[][][] blocks;
	
	private boolean loaded;
	private boolean carded;     // either loaded to GPU (true) or needs to be (false)
	private int dispListHandle = -1;
	
	private int x;
	private int y;
	private int z;
	
	private ChunkManager cm;
	
	public Chunk(int x, int y, int z, ChunkManager cm) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.blocks = new Block[16][16][16]; // x, y, z axis
		this.loaded = false;
		this.carded = false;
		this.dispListHandle = glGenLists(1);
		this.cm = cm;
	}
	
	public boolean init() {
		return generate();
	}
	
	// set/remove block
	public void update(int i, int j, int k, int block_id) {
		switch (block_id) {
			case 0:
				blocks[i][j][k] = new AirBlock(x + i, y + j, z + k);
				break;
			case 1:
				blocks[i][j][k] = new BedrockBlock(x + i, y + j, z + k);
				break;
			case 2:
				blocks[i][j][k] = new StoneBlock(x + i, y + j, z + k);
				break;
			case 3:
				blocks[i][j][k] = new DirtBlock(x + i, y + j, z + k);
				break;
			default:
				break;
		}
		cm.updateRound(new Triple((x + i) / 16, (y + j) / 16, (z + k) / 16));
		carded = false;
	}
	
	// place-holder for loading from disk
	private boolean load() {
		return false;
	}
	
	private boolean generate() {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 16; k++) {
					if (j + y == 0) {
						blocks[i][j][k] = new BedrockBlock(x + i, j + y, k + z);
					} else if (j + y < 35 + 4 * Math.sin(0.04 * (x)) + 4 * Math.cos(0.08 * (z))) {
						blocks[i][j][k] = new StoneBlock(x + i, j + y, k + z);
					} else if (j + y < 45 + 4 * Math.sin(0.04 * (x)) + 4 * Math.cos(0.08 * (z))) {
						blocks[i][j][k] = new DirtBlock(x + i, j + y, k + z);
					} else {
						blocks[i][j][k] = new AirBlock(x + i, j + y, k + z);
					}
				}
			}
		}
		loaded = true;
		// System.err.println("Chunk at " + x + ", " + y + ", " + z + " was loaded");
		return true;
	}
	
	public void render() {
		if (!carded) { // needs to be put on the card...
			if (loaded) {
				glNewList(dispListHandle, GL_COMPILE_AND_EXECUTE);
				for (int i = 0; i < 16; i++) {
					for (int j = 0; j < 16; j++) {
						for (int k = 0; k < 16; k++) {
							boolean one = false, two = false, three = false, four = false, five = false, six = false;
							// use short-circuit to prevent outofarraybound exceptions
							Block curr = blocks[i][j][k];
							Block xdec = cm.getBlockAt(curr.getX() - 1, curr.getY(), curr.getZ());
							Block xinc = cm.getBlockAt(curr.getX() + 1, curr.getY(), curr.getZ());
							Block ydec = cm.getBlockAt(curr.getX(), curr.getY() - 1, curr.getZ());
							Block yinc = cm.getBlockAt(curr.getX(), curr.getY() + 1, curr.getZ());
							Block zdec = cm.getBlockAt(curr.getX(), curr.getY(), curr.getZ() - 1);
							Block zinc = cm.getBlockAt(curr.getX(), curr.getY(), curr.getZ() + 1);
							if (zdec != null && zdec.invisible())
								one = true;
							if (zinc != null && zinc.invisible())
								two = true;
							if (ydec != null && ydec.invisible())
								three = true;
							if (yinc != null && yinc.invisible())
								four = true;
							if (xdec != null && xdec.invisible())
								five = true;
							if (xinc != null && xinc.invisible())
								six = true;
							blocks[i][j][k].render(one, two, three, four, five, six);
						}
					}
				}
				glEndList();
			}
			carded = true;
		} else {
			glCallList(dispListHandle);
		}
	}
	
	public void redraw() {
		carded = false;
	}
	
	public Block getBlock(int i, int j, int k) {
		if (loaded) {
			return blocks[i][j][k];
		}
		return null;
	}
}