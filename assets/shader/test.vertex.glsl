
layout (std140) uniform MATRIX {
	mat4 world;
	mat4 view;
	mat4 projection;
} matrix;

#define M_WVP (matrix.projection * matrix.view * matrix.world)

in vec3 in_co;
in vec4 in_tint;
in vec2 in_texco;

out VT_OUT {
	vec4 tint;
	vec2 texco;
} v_v;

void main ()
{
	gl_Position = M_WVP * vec4(in_co, 1.0);
	v_v.tint = in_tint;
	v_v.texco = in_texco;
}

