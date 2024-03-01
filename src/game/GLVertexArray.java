package game;

import static org.lwjgl.opengl.GL30.*;

public class GLVertexArray
{

	int name;
	
	public GLVertexArray ()
	{
		name = glGenVertexArrays();
	}

	public void bind ()
	{
		glBindVertexArray(name);
	}

	public void unbind ()
	{
		glBindVertexArray(0);
	}
	
	public void destroy ()
	{
		glDeleteVertexArrays(name);
		name = 0;
	}
	
}
