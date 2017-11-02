package com.dylanscode.engine;

import com.dylanscode.engine.engine.game.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.dylanscode.engine.engine.game.Camera;

public class Transformation {
    private Matrix4f projectionMatrix;
    private Matrix4f objectViewMatrix;
    private Matrix4f viewMatrix;
    public Transformation(){
        projectionMatrix = new Matrix4f();
        objectViewMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
    }
    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    public Matrix4f getObjectViewMatrix(GameObject object, Matrix4f viewMatrix){
        objectViewMatrix.identity().translate(object.getPosition())
                .rotateX((float) Math.toRadians(-object.getRotation().x))
                .rotateY((float) Math.toRadians(-object.getRotation().y))
                .rotateZ((float) Math.toRadians(-object.getRotation().z))
                .scale(object.getScale());
        Matrix4f currMatrix = new Matrix4f(viewMatrix);
        return currMatrix.mul(objectViewMatrix);
    }

    public Matrix4f getViewMatrix(Camera camera) {
        viewMatrix.identity();
        viewMatrix.rotate((float)Math.toRadians(camera.getRotation().x), new Vector3f(1, 0, 0)).rotate((float)Math.toRadians(camera.getRotation().y), new Vector3f(0, 1, 0));
        viewMatrix.translate(-camera.getPosition().x,-camera.getPosition().y,-camera.getPosition().z);
        return viewMatrix;
    }
}
