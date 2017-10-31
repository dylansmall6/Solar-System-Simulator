#version 330

layout (location =0) in vec3 position;
layout (location =1) in vec2 in_texture_coordinate;

out vec2 out_texture_coordinate;

uniform mat4 objectViewMatrix;
uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * objectViewMatrix * vec4(position, 1.0);
    out_texture_coordinate = in_texture_coordinate;
}