package game;

import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.nglfwGetCursorPos;
import static org.lwjgl.system.MemoryUtil.*;

public class Mouser
{
	private static final int ITEM_SIZE = Double.BYTES;
	private final long ptr = nmemCalloc(2, ITEM_SIZE);
	public double x, y;
	public double pev_x, pev_y;
	
	void updatePev ()
	{
		pev_x = x;
		pev_y = y;
	}
	
	public void setCo (double mx, double my)
	{
		x = mx;
		y = my;
	}
	
	public Vector2d getCo (Vector2d dest)
	{
		return dest.set(x, y);
	}
	
	public Vector2f getCo (Vector2f dest)
	{
		return dest.set((float)x, (float)y);
	}
	
	public void setAndUpdatePev (double mx, double my)
	{
		updatePev();
		setCo(mx, my);
	}
	
	public void update (long window)
	{
		final var yptr = ptr+ITEM_SIZE;
		nglfwGetCursorPos(window, ptr, yptr);
		setAndUpdatePev(memGetDouble(ptr), memGetDouble(yptr));
		updatePev();
	}
	
	public void destroy ()
	{
		nmemFree(ptr);
	}
	
}
