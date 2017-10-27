package com.dylanscode.engine;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
class ShaderLoader
{
	private final int p_ID;

	private int vertex_shader_ID;

	private int fragment_shader_ID;

	private final Map<String,Integer> uniform_map;
	public ShaderLoader() {
	    uniform_map = new HashMap<>();
		p_ID = GL20.glCreateProgram();
		if (p_ID == 0)
		{
			System.err.println("Unable to create Shader");
		}
	}

	private int create(String code, int type)
	{
		int ID = GL20.glCreateShader(type);
		if (ID == 0)
		{
			System.err.println("Unable to create Shader: type = 0");
		}
		GL20.glShaderSource(ID, code);
		GL20.glCompileShader(ID);
		if (GL20.glGetShaderi(ID, GL20.GL_COMPILE_STATUS) == 0)
		{
			System.err.println("Error compiling shader code!");
		}
		GL20.glAttachShader(p_ID, ID);
		return ID;
	}

	public void createFragmentShader(String code)
	{
		fragment_shader_ID = create(code, GL20.GL_FRAGMENT_SHADER);
	}

	public void createVertexShader(String code)
	{
		vertex_shader_ID = create(code, GL20.GL_VERTEX_SHADER);
	}

	public void link()
	{
		GL20.glLinkProgram(p_ID);
		if (GL20.glGetProgrami(p_ID, GL20.GL_LINK_STATUS) == 0)
		{
			System.err.println("Error on linking shader");
		}
		if (vertex_shader_ID != 0)
		{
			GL20.glDetachShader(p_ID, vertex_shader_ID);
		}
		if (fragment_shader_ID != 0)
		{
			GL20.glDetachShader(p_ID, fragment_shader_ID);
		}
		GL20.glValidateProgram(p_ID);
		if (GL20.glGetProgrami(p_ID, GL20.GL_VALIDATE_STATUS) == 0)
		{
			System.err.println("Error on validating shader");
		}
	}
	public void setBinded(boolean shouldBind) {
		if(shouldBind) {
			GL20.glUseProgram(p_ID);
		}
		else {
			GL20.glUseProgram(0);
		}
	}
	public void clean() {
		setBinded(false);
		if(p_ID == 0) {
			GL20.glDeleteProgram(p_ID);
		}
	}

    public void createUniform(String uniformName) {
        int uniformLocation = GL20.glGetUniformLocation(p_ID, uniformName);
        if (uniformLocation < 0) System.err.println("Could not initialize uniform name");
        uniform_map.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer floats = stack.mallocFloat(16);
            value.get(floats);
            GL20.glUniformMatrix4fv(uniform_map.get(uniformName), false, floats);
        }
    }
}
