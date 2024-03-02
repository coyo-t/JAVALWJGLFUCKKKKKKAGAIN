package game.drawshit;

import game.ShaderProgram;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

public class SHaderShit
{

	static int current_shader_id = 0;

	public static ShaderProgram shader_load (String path)
	{
		try
		{
			return ShaderProgram.load(path);
		}
		catch (IOException e)
		{
			throw new RuntimeException(
				"Couldn't load shader at %s, info:\n%s".formatted(path, e.getStackTrace())
			);
		}
	}
	
	public static void shader_set (ShaderProgram sh)
	{
		if (current_shader_id != sh.name)
		{
			current_shader_id = sh.name;
			glUseProgram(sh.name);
		}
	}
	
	public static void shader_reset ()
	{
		glUseProgram(current_shader_id = 0);
	}
	
	public static int shader_get_uniform (ShaderProgram sh, String name)
	{
		return glGetUniformLocation(sh.name, name);
	}
	
	public static int shader_get_uniform_block (ShaderProgram sh, String name)
	{
		return glGetUniformBlockIndex(sh.name, name);
	}
	
	public static void shader_set_uniform_block_binding (ShaderProgram sh, String name, int index)
	{
		glUniformBlockBinding(sh.name, shader_get_uniform_block(sh, name), index);
	}
	
	public static void shader_delete (ShaderProgram sh)
	{
		sh.destroy();
	}
	
	public static int shader_max_vertex_attributes ()
	{
		return glGetInteger(GL_MAX_VERTEX_ATTRIBS);
	}
}
