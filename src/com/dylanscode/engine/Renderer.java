package com.dylanscode.engine;

import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private ShaderLoader shaderLoader;

    private static final float FOV = (float) Math.toRadians(60.0F);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Matrix4f projectionMatrix;
    public void init(Window window) throws Exception {
    	shaderLoader = new ShaderLoader();
    	shaderLoader.createVertexShader(Utils.loadResource("/vertexshader.vs"));
    	shaderLoader.createFragmentShader(Utils.loadResource("/fragshader.fs"));
    	shaderLoader.link();

    	float aspect_ratio = (float) window.getWidth() / window.getHeight();
    	projectionMatrix = new Matrix4f().perspective(FOV,aspect_ratio,Z_NEAR,Z_FAR);
    	shaderLoader.createUniform("projectionMatrix");
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
        shaderLoader.setUniform("projectionMatrix",projectionMatrix);

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
