package game;

import static org.lwjgl.opengl.GL15.*;

public class GLBuffer
{
	
	int name;
	int target;
	
	public GLBuffer (int target)
	{
		name = glGenBuffers();
		this.target = target;
	}
	
	public GLBuffer ()
	{
		this(GL_ARRAY_BUFFER);
	}

	public void destroy ()
	{
		glDeleteBuffers(name);
		name = 0;
		target = 0;
	}
	
	public void bind ()
	{
		bind(target);
	}
	
	public void bind (int target)
	{
		glBindBuffer(target, name);
	}
	
	public void unbind ()
	{
		glBindBuffer(target, 0);
	}
}
