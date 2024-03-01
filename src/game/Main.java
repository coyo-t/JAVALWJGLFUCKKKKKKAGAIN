package game;

import game.vertex.VertexFormat;
import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;

import static game.Mth.clamp_symd;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main
{
	static final String SHADER_ROOT = "assets/shader";

	long window;
	
	static final int
	INIT_W = 640,
	INIT_H = 480;
	
	Tex tex;
	public Mouser mouser = new Mouser();
	
	int win_wide = INIT_W, win_tall = INIT_H;
	
	void init ()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		window = glfwCreateWindow(win_wide, win_tall, "Hello World!", NULL, NULL);
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
				glfwSetWindowShouldClose(window, true);
		});

		var vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
			window,
			(vidmode.width() - win_wide) / 2,
			(vidmode.height() - win_tall) / 2
		);
		glfwSetWindowSizeCallback(window, this::win_size_changed);
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		tex = new Tex("sprite/tileset.png");
		System.out.println(tex.hasData() ? "Loaded" : "Fuck");
		
		glClearColor(0.5f, 0.0f, 0.5f, 0.0f);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.5f);
	}
	
	void win_size_changed (long window, int wide, int tall)
	{
		win_wide = wide;
		win_tall = tall;
		glViewport(0, 0, wide, tall);
		mouser.update(window);
		draw();
		glfwSwapBuffers(window);
	}
	
	public void run ()
	{
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		
		init();
		win_size_changed(window, win_wide, win_tall);
		
		try
		{
			run_loop();
		}
		catch (Exception e)
		{
			System.err.println("Uh oh, something fucked up in tha run loop!");
			e.printStackTrace(System.err);
		}
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	void run_loop () throws IOException
	{
		final var sh_program = ShaderProgram.load("test");
		
		var vbo = new GLBuffer(GL_ARRAY_BUFFER);
		var vao = new GLVertexArray();
		var ebo = new GLBuffer(GL_ELEMENT_ARRAY_BUFFER);
		var vf = new VertexFormat();
		{
			vf
			.begin()
			.add_position_3d()
			.add_colour4x8()
			.add_texco()
			.end();
			
			final float xsz = tex.wide * 0.5f;
			final float ysz = tex.tall * 0.5f;
			
			try (final var stack = stackPush())
			{
				float[] coords = {
					-xsz, -ysz, 0,
					+xsz, -ysz, 0,
					+xsz, +ysz, 0,
					-xsz, +ysz, 0,
				};
				
				float[] textures = {
					0, 0,
					1, 0,
					1, 1,
					0, 1,
				};
				
				int[] colours = {
					0xFF_FFFF00,
					0xFF_FFFF00,
					0xFF_FFFFFF,
					0xFF_FFFFFF,
				};
				
				
				final var to_send = stack.malloc(vf.get_size_bytes() * colours.length);
				
				for (int i = 0, cp = 0, up = 0; i < colours.length; i++)
				{
					var col = colours[i];
					to_send
					.putFloat(coords[cp++])
					.putFloat(coords[cp++])
					.putFloat(coords[cp++])
					.put((byte)((col >> 16) & 0xFF))
					.put((byte)((col >> 8) & 0xFF))
					.put((byte)(col & 0xFF))
					.put((byte)((col >>> 24) & 0xFF))
					.putFloat(textures[up++])
					.putFloat(textures[up++])
					;
				}
				
				int[] indicies = {
					0, 1, 3,
					1, 2, 3
				};
				ebo.bind();
				glBufferData(ebo.target, indicies, GL_STATIC_DRAW);
				
				vao.bind();
				
				vbo.bind();
				glBufferData(vbo.target, to_send.flip(), GL_STATIC_DRAW);
				
				vf.enable_attrib_array();
				
			}
		}
		
		final var MATRIX_SIZE = 4*4;
		final var matrix_buffer = new GLBuffer(GL_UNIFORM_BUFFER);
		final var MATRIX_TEMP_SIZE = MATRIX_SIZE*3;
		final var MATRIX_TEMP_BYTES = MATRIX_TEMP_SIZE * Float.BYTES;
		final var MATRIX_TEMP = MemoryUtil.memAllocFloat(MATRIX_TEMP_SIZE);
		{
			sh_program.set_uniform_block_binding("MATRIX", 0);
			matrix_buffer.bind();
			glBufferData(matrix_buffer.target, MATRIX_TEMP_BYTES, GL_DYNAMIC_DRAW);
			glBindBufferRange(matrix_buffer.target, 0, matrix_buffer.name, 0, MATRIX_TEMP_BYTES);
			matrix_buffer.unbind();
		}
		final var m_world = new Matrix4f();
		final var m_view  = new Matrix4f();
		final var m_projection = new Matrix4f();
		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();
			step();
			
			{
				final var t = glfwGetTime();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				m_projection
				.identity()
				.setOrthoSymmetric(win_wide,win_tall,0, 100)
				;
				
				m_view
				.identity()
				.translate(
					(float)clamp_symd(mouser.x - win_wide*0.5, win_wide * 0.5),
					(float)clamp_symd(win_tall*0.5-mouser.y, win_tall * 0.5),
					0
				)
				;
				
				m_world
				.identity()
				.rotate((float)(Math.sin(t * Math.PI) * Math.PI * 0.01), 0, 0, 1)
				;

				m_world.get(0, MATRIX_TEMP);
				m_view.get(MATRIX_SIZE, MATRIX_TEMP);
				m_projection.get(MATRIX_SIZE*2, MATRIX_TEMP);
				matrix_buffer.bind();
				glBufferSubData(matrix_buffer.target, 0, MATRIX_TEMP);
				matrix_buffer.unbind();
				
//				draw();
				
				tex.bind();
				glPushAttrib(GL_ALL_ATTRIB_BITS);
				sh_program.use();
				ebo.bind();
				glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
				sh_program.unuse();
				glPopAttrib();
				
			}
			
			glfwSwapBuffers(window);
		}
		
		MemoryUtil.memFree(MATRIX_TEMP);
		vbo.destroy();
		ebo.destroy();
		vao.destroy();
		sh_program.destroy();
		matrix_buffer.destroy();
	}
	
	public void step ()
	{
		mouser.update(window);
	}

	public void draw ()
	{

	}

}



