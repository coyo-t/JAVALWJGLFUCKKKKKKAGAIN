package game.drawshit;

import game.vertex.VertexFormat;
import game.vertex.VertexType;

public class VertexFormatShit
{

	static VertexFormat working_format;

	public static void vertex_format_begin ()
	{
		working_format = new VertexFormat();
		working_format.begin();
	}

	public static void vertex_format_add_float1 ()
	{
		working_format.add_float(1);
	}

	public static void vertex_format_add_float2 ()
	{
		working_format.add_float(2);
	}
	
	public static void vertex_format_add_float3 ()
	{
		working_format.add_float(3);
	}

	public static void vertex_format_add_float4 ()
	{
		working_format.add_float(4);
	}
	
	public static void vertex_format_add_texco ()
	{
		working_format.add_float(2);
	}
	
	public static void vertex_format_add_normal ()
	{
		working_format.add_float(3);
	}
	
	public static void vertex_format_add_position_2d ()
	{
		working_format.add_float(2);
	}
	
	public static void vertex_format_add_position_3d ()
	{
		working_format.add_float(3);
	}
	
	// This is mildly annoying >:/
	// should be U8, but Java Stuff.
	public static void vertex_format_add_colour4x8 ()
	{
		working_format.add_custom(VertexType.VERTEX_TYPE_S8, 4, true);
	}
	
	public static void vertex_format_add_colour3x8 ()
	{
		working_format.add_custom(VertexType.VERTEX_TYPE_S8, 3, true);
	}
	
	public static void vertex_format_add_custom (VertexType type, int count)
	{
		vertex_format_add_custom(type, count, false);
	}
	
	public static void vertex_format_add_custom (VertexType type, int count, boolean normalized)
	{
		working_format.add_custom(type, count, normalized);
	}
	
	public static VertexFormat vertex_format_end ()
	{
		working_format.end();
		return working_format;
	}
	
	public static void vertex_format_delete (VertexFormat f)
	{
		f.destroy();
	}
}
