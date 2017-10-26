package com.dylanscode.engine;

import org.lwjgl.opengl.GL20;

class ShaderLoader
{
	private final int p_ID;

	private int vertex_shader_ID;

	private int fragment_shader_ID;

	public ShaderLoader() {
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

}
