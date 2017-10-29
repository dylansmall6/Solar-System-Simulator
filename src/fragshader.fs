#version 330

in  vec2 out_texture_coordinate;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
    fragColor = texture(texture_sampler, out_texture_coordinate);
}