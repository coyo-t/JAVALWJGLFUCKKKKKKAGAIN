package game.vertex

import game.vertex.VertexType.VERTEX_TYPE_F32
import game.vertex.VertexType.VERTEX_TYPE_S8
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer

class VertexFormat
{
	class Entry
	{
		var type: VertexType? = null
		var index: Int = 0
		var count: Int = 0
		var offset: Int = 0
		var normalized: Boolean = false

		fun sizeof_bytes () = type!!.get_size_bytes() * count
		fun get_type ()  = type
		fun get_index () = index
		fun get_count () = count
		fun get_byte_offset () = offset
		fun is_normalized () = normalized
	}


	private var building = false
	private var size_bytes = 0
	private var element_count = 0

	private var entries: Array<Entry?> = arrayOfNulls(12)

	fun begin(): VertexFormat
	{
		if (building) throw RuntimeException("Vertex format already building!")
		building = true
		size_bytes = 0
		element_count = 0
		return this
	}

	@JvmOverloads
	fun add_custom (type: VertexType, count: Int, normalized: Boolean = false): VertexFormat
	{
		val entry: Entry?
		if (entries[element_count] == null)
		{
			entry = Entry()
			entries[element_count] = entry
		}
		else
		{
			entry = entries[element_count]
		}
		entry!!.index = element_count
		entry.type = type
		entry.offset = size_bytes
		entry.normalized = normalized
		entry.count = count

		element_count++
		size_bytes += entry.sizeof_bytes()
		return this
	}

	@JvmOverloads
	fun add_float (count:Int=1) = add_custom(VERTEX_TYPE_F32, count)

	fun end ()
	{
		if (!building) throw RuntimeException("Vertex format isn't building!")
		building = false
	}

	fun enable_attrib_array ()
	{
		for (i in 0..<element_count)
		{
			val entry = entries[i]
			val ei = entry!!.index
			glVertexAttribPointer(
				ei,
				entry.count,
				entry.type!!.get_gl_type(),
				entry.normalized,
				size_bytes,
				entry.offset.toLong()
			)
			glEnableVertexAttribArray(ei)
		}
	}

	fun get_element_count () = element_count

	fun get_size_bytes () = size_bytes

	fun destroy()
	{
	}
}
