package game.vertex;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_HALF_FLOAT;
import static org.lwjgl.opengl.GL41.GL_FIXED;

public enum VertexType
{
	VERTEX_TYPE_U8(1, GL_BYTE),
	VERTEX_TYPE_S8(1, GL_UNSIGNED_BYTE),
	VERTEX_TYPE_S16(2, GL_SHORT),
	VERTEX_TYPE_U16(2, GL_UNSIGNED_SHORT),
	VERTEX_TYPE_S32(4, GL_INT),
	VERTEX_TYPE_U32(4, GL_UNSIGNED_INT),
	VERTEX_TYPE_F16(2, GL_HALF_FLOAT),
	VERTEX_TYPE_F32(4, GL_FLOAT),
	VERTEX_TYPE_F64(8, GL_DOUBLE),
	VERTEX_TYPE_FIXED(4, GL_FIXED),
	;
	public final int gl;
	public final int sizeof;
	
	VertexType (int sizeof, int gl)
	{
		this.gl = gl;
		this.sizeof = sizeof;
	}
}
