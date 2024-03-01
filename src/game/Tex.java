package game;

import static org.lwjgl.opengl.GL11.*;


public class Tex
{
	public int glName = 0;
	public int wide, tall;
	
	public Tex (String path)
	{
		final var pic = new Img(path);
		init(pic);
		pic.destroy();
	}
	
	public Tex (Img img)
	{
		init(img);
	}

	public void init (Img img)
	{
		wide = img.wide;
		tall = img.tall;
		
		glName = glGenTextures();
		
		final var tgt = GL_TEXTURE_2D;
		glPushAttrib(GL_ENABLE_BIT);
		glEnable(tgt);
		glBindTexture(tgt, glName);
		glTexParameteri(tgt, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(tgt, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(tgt, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(tgt, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
		glTexImage2D(
			tgt,
			0,
			GL_RGBA,
			wide,
			tall,
			0,
			GL_RGBA,
			GL_UNSIGNED_BYTE,
			img.bitmap
		);
		
//		glBindTexture(GL_TEXTURE_2D, 0);
		glPopAttrib();
	}

	public void bind ()
	{
		glBindTexture(GL_TEXTURE_2D, glName);
	}

	public boolean hasData ()
	{
		return glName != 0;
	}

}
