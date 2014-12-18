import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Contains all the textures we're gonna use, loads them into static space
 *
 */
public class TextureContainer {
	public static Texture StoneTex   = null;
	public static Texture DirtTex    = null;
	public static Texture BedrockTex = null;
	
	/**
	 * Loads textures from disk, exits the program if it can't do it
	 *
	 */
	public static void init() {
		try {
			StoneTex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("images/stone.png"));
			DirtTex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("images/dirt.png"));
			BedrockTex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("images/bedrock.png"));
			System.out.println("Textures loaded");
		} catch (IOException ex) {
			System.exit(0);
		}
	}
	
	/**
	 * Returns a Texture object based on the ID passed
	 *
	 */
	public static Texture getTexByID(int id) {
		switch (id) {
			case StoneBlock.ID:
				return StoneTex;
			case DirtBlock.ID:
				return DirtTex;
			case BedrockBlock.ID:
				return BedrockTex;
			default:
				return null;
		}
	}
	
	/** 
	 * Releases resources, since we won't use them anymore
	 *
	 */
	public static void close() {
		StoneTex.release();
		DirtTex.release();
		BedrockTex.release();
	}
}
