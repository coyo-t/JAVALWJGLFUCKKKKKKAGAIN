package game;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import static org.lwjgl.stb.STBImage.*;

public class Img
{
	private static int[]
	TEMP_W = new int[1],
	TEMP_H = new int[1],
	TEMP_F = new int[1];
	
	public int wide, tall;
	public ByteBuffer bitmap;
	
	public Img (String path)
	{
		final var p = Paths.get("./assets", path);
		stbi_set_flip_vertically_on_load(true);
		bitmap = stbi_load(p.toString(), TEMP_W, TEMP_H, TEMP_F, 4);
		wide = TEMP_W[0];
		tall = TEMP_H[0];
	}
	
	public void destroy ()
	{
		stbi_image_free(bitmap);
	}
	
	public boolean hasData ()
	{
		return bitmap != null;
	}
	
}
