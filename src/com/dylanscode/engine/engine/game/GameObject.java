package com.dylanscode.engine.engine.game;

import com.dylanscode.engine.Mesh;
import org.joml.Vector3f;

public class GameObject {
    private Mesh mesh;

    private Vector3f position;

    private Vector3f rotation;

    private float scale;

    public GameObject(Mesh mesh){
        this.mesh = mesh;
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = 1;
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
    public void setPosition(float x, float y, float z){
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    public void setRotation(float x, float y, float z){
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
    public void setScale(float scale){
        this.scale = scale;
    }

    public Mesh getMesh() {
        return mesh;
    }
}
