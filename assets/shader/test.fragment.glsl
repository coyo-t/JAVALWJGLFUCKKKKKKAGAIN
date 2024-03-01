
uniform sampler2D tex;

in VT_OUT {
	vec4 tint;
	vec2 texco;
} v_v;

out vec4 FragColor;

void main ()
{
	FragColor = texture(tex, v_v.texco) * v_v.tint;
}
