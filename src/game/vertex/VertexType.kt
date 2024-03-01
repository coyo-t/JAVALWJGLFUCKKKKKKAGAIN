package game.vertex

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL41
import org.lwjgl.opengl.GL41.*

enum class VertexType(private val sizeof: Int, private val gl: Int)
{
	VERTEX_TYPE_U8    (1, GL_BYTE),
	VERTEX_TYPE_S8    (1, GL_UNSIGNED_BYTE),
	VERTEX_TYPE_S16   (2, GL_SHORT),
	VERTEX_TYPE_U16   (2, GL_UNSIGNED_SHORT),
	VERTEX_TYPE_S32   (4, GL_INT),
	VERTEX_TYPE_U32   (4, GL_UNSIGNED_INT),
	VERTEX_TYPE_F16   (2, GL_HALF_FLOAT),
	VERTEX_TYPE_F32   (4, GL_FLOAT),
	VERTEX_TYPE_F64   (8, GL_DOUBLE),
	VERTEX_TYPE_FIXED (4, GL_FIXED),
	;

	fun get_size_bytes () = sizeof

	fun get_gl_type () = gl
}
