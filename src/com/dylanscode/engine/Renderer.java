package com.dylanscode.engine;

import com.dylanscode.engine.engine.game.Camera;
import com.dylanscode.engine.engine.game.GameObject;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;


public class Renderer {

    private ShaderLoader shaderLoader;

    private static final float FOV = (float) Math.toRadians(60.0F);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private Transformation transformation;

    public Renderer(){
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
    	shaderLoader = new ShaderLoader();
    	shaderLoader.createVertexShader(Utils.loadResource("/vertexshader.vs"));
    	shaderLoader.createFragmentShader(Utils.loadResource("/fragshader.fs"));
    	shaderLoader.link();

    	float aspect_ratio = (float) window.getWidth() / window.getHeight();
    	shaderLoader.createUniform("projectionMatrix");
    	shaderLoader.createUniform("objectViewMatrix");
    	shaderLoader.createUniform("texture_sampler");

    	window.setClearColor(0.0f,0.0f,0.0f,0.0f);
    }

    private void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Camera camera, GameObject[] gameObjects) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderLoader.setBinded(true);

        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV,window.getWidth(),window.getHeight(),Z_NEAR,Z_FAR);
        shaderLoader.setUniform("projectionMatrix",projectionMatrix);

        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shaderLoader.setUniform("texture_sampler",0);

        for(GameObject gameObject : gameObjects){
            Matrix4f objectViewMatrix = transformation.getObjectViewMatrix(gameObject,viewMatrix);
            shaderLoader.setUniform("objectViewMatrix",objectViewMatrix);
            gameObject.getMesh().render();
        }

        shaderLoader.setBinded(false);
    }

    public void cleanup() {
        if (shaderLoader != null) {
            shaderLoader.clean();
        }
    }
}
