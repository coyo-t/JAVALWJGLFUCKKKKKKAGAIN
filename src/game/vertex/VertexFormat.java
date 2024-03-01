package game.vertex;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class VertexFormat
{
	
	public static class Entry
	{
		VertexType type;
		int index;
		int count;
		int offset;
		boolean normalized;
		
		public int sizeof_bytes ()
		{
			return type.sizeof * count;
		}
	}
	

	boolean building = false;
	int size_bytes;
	int element_count = 0;
	
	Entry[] entries = new Entry[12];
	
	public VertexFormat begin ()
	{
		if (building)
			throw new RuntimeException("Vertex format already building!");
		building = true;
		size_bytes = 0;
		element_count = 0;
		return this;
	}
	
	public VertexFormat add_custom (VertexType type, int count, boolean normalized)
	{
		final Entry entry;
		if (entries[element_count] == null)
		{
			entries[element_count] = entry = new Entry();
		}
		else
		{
			entry = entries[element_count];
		}
		entry.index = element_count;
		entry.type = type;
		entry.offset = size_bytes;
		entry.normalized = normalized;
		entry.count = count;
		
		element_count++;
		size_bytes += entry.sizeof_bytes();
		return this;
	}
	
	public VertexFormat add_custom (VertexType type, int count)
	{
		return add_custom(type, count, false);
	}
	
	
	public VertexFormat add_float1 ()
	{
		return add_custom(VertexType.VERTEX_TYPE_F32, 1);
	}
	
	public VertexFormat add_float2 ()
	{
		return add_custom(VertexType.VERTEX_TYPE_F32, 2);
	}
	
	public VertexFormat add_float3 ()
	{
		return add_custom(VertexType.VERTEX_TYPE_F32, 3);
	}
	
	public VertexFormat add_float4 ()
	{
		return add_custom(VertexType.VERTEX_TYPE_F32, 4);
	}
	
	public VertexFormat add_position_2d ()
	{
		return add_float2();
	}
	
	public VertexFormat add_position_3d ()
	{
		return add_float3();
	}
	
	public VertexFormat add_normal ()
	{
		return add_float3();
	}
	
	public VertexFormat add_texco ()
	{
		return add_float2();
	}
	
	// This is mildly annoying >:/
	// should be U8, but Java Stuff.
	public VertexFormat add_colour4x8 ()
	{
		return add_custom(VertexType.VERTEX_TYPE_S8, 4, true);
	}
	
	public VertexFormat add_colour3x8 ()
	{
		return add_custom(VertexType.VERTEX_TYPE_S8, 3, true);
	}
	
	public void end ()
	{
		if (!building)
			throw new RuntimeException("Vertex format isn't building!");
		building = false;
	}
	
	public void enable_attrib_array ()
	{
		for (var i = 0; i < element_count; i++)
		{
			final var entry = entries[i];
			final var ei = entry.index;
			glVertexAttribPointer(
				ei,
				entry.count,
				entry.type.gl,
				entry.normalized,
				size_bytes,
				entry.offset
			);
			glEnableVertexAttribArray(ei);
		}
	}
	
	public int get_size_bytes ()
	{
		return size_bytes;
	}
	
	public void destroy ()
	{
	
	}
}
