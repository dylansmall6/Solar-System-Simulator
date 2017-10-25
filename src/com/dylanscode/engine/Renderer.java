package com.dylanscode.engine;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.system.MemoryUtil;
import com.dylanscode.engine.Utils;
import com.dylanscode.engine.Window;
import com.dylanscode.engine.ShaderLoader;
public class Renderer {

    private int VBO_ID;

    private int VAO_ID;

    private ShaderLoader shaderLoader;

    public Renderer() {
    }

    public void init() throws Exception {
    	shaderLoader = new ShaderLoader();
    	shaderLoader.createVertexShader(Utils.loadResource("/vertexshader.vs"));
    	shaderLoader.createFragmentShader(Utils.loadResource("/fragshader.fs"));
    	shaderLoader.link();

        float[] vertices = new float[]{
            0.0f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f
        };

        FloatBuffer verticesBuffer = null;
        try {
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            VAO_ID = glGenVertexArrays();
            glBindVertexArray(VAO_ID);

            VBO_ID = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            glBindBuffer(GL_ARRAY_BUFFER, 0);

            glBindVertexArray(0);
        } finally {
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderLoader.setBinded(true);

        glBindVertexArray(VAO_ID);
        glEnableVertexAttribArray(0);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        shaderLoader.setBinded(false);
    }

    public void cleanup() {
        if (shaderLoader != null) {
        	shaderLoader.clean();
        }

        glDisableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(VBO_ID);

        glBindVertexArray(0);
        glDeleteVertexArrays(VAO_ID);
    }
}
