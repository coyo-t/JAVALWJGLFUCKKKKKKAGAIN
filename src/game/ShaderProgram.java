package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;

public class ShaderProgram
{
	static final String SHADER_ROOT = "assets/shader";
	
	static final String
	SHADER_V_PREAMBLE = "#version 400 core\n",
	SHADER_F_PREAMBLE = "#version 400 core\n";
	
	static final String
	COMPILE_ERROR_PROBLEM_STR = "Shader \"%s\" had an error compiling at %s stage with status %d; Info:\n%s";
	
	public int sh_program;
	
	private ShaderProgram ()
	{
	}

	public void use ()
	{
		glUseProgram(sh_program);
	}

	public void unuse ()
	{
		glUseProgram(0);
	}

	public void destroy ()
	{
		glDeleteProgram(sh_program);
		sh_program = 0;
	}

	public int get_uniform_location (CharSequence name)
	{
		return glGetUniformLocation(sh_program, name);
	}
	
	public int get_uniform_block_index (CharSequence name)
	{
		return glGetUniformBlockIndex(sh_program, name);
	}
	
	public void set_uniform_block_binding (CharSequence name, int index)
	{
		glUniformBlockBinding(sh_program, get_uniform_block_index(name), 0);
	}
	
	static int create_and_compile (String source, int shader_type, String asset_name, String stage_name)
	{
		final var sh_id = glCreateShader(shader_type);
		glShaderSource(sh_id, source);
		glCompileShader(sh_id);

		final var sh_status = glGetShaderi(sh_id, GL_COMPILE_STATUS);
		if (sh_status != 1)
		{
			throw new RuntimeException(COMPILE_ERROR_PROBLEM_STR.formatted(asset_name, stage_name, sh_status, glGetShaderInfoLog(sh_id)));
		}
		return sh_id;
	}

	public static ShaderProgram load (String assetName)
	throws IOException
	{
		final var outs = new ShaderProgram();
		outs.sh_program = glCreateProgram();
		
		final var sh_v_source = SHADER_V_PREAMBLE+Files.readString(Path.of(SHADER_ROOT, assetName+".vertex.glsl"));
		final var sh_v_id = create_and_compile(sh_v_source, GL_VERTEX_SHADER, assetName, "vertex");
		
		final var sh_f_source = SHADER_F_PREAMBLE+Files.readString(Path.of(SHADER_ROOT, assetName+".fragment.glsl"));
		final var sh_f_id = create_and_compile(sh_f_source, GL_FRAGMENT_SHADER, assetName, "fragment");

		glAttachShader(outs.sh_program, sh_v_id);
		glAttachShader(outs.sh_program, sh_f_id);
		glLinkProgram(outs.sh_program);
		
		glDeleteShader(sh_v_id);
		glDeleteShader(sh_f_id);
		return outs;
	}

	public static int getMaxVertexAttributes ()
	{
		return glGetInteger(GL_MAX_VERTEX_ATTRIBS);
	}

}
