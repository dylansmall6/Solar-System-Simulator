package com.dylanscode.engine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Renderer {

    private ShaderLoader shaderLoader;

    public void init() throws Exception {
    	shaderLoader = new ShaderLoader();
    	shaderLoader.createVertexShader(Utils.loadResource("/vertexshader.vs"));
    	shaderLoader.createFragmentShader(Utils.loadResource("/fragshader.fs"));
    	shaderLoader.link();
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window,Mesh mesh) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderLoader.setBinded(true);

        glBindVertexArray(mesh.getVAO_ID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, mesh.getVERTEX_COUNT(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shaderLoader.setBinded(false);
    }

    public void cleanup() {
        if (shaderLoader != null) {
            shaderLoader.clean();
        }
    }
}
