package com.dylanscode.engine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private final int VAO_ID;

    private final int POSITION_VBO_ID;

    private final int COLOR_VBO_ID;

    private final int INDICES_VBO_ID;

    private final int VERTEX_COUNT;

    public Mesh(float[] positions,float[] colors, int[] indices){
        FloatBuffer positionBuffer = null, colorBuffer = null;
        IntBuffer indicesBuffer = null;
        try{
            //Calculating VERTEX_COUNT
            VERTEX_COUNT = indices.length;
            //Creating VAOs
            VAO_ID = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(VAO_ID);
            //Creating position VBO
            POSITION_VBO_ID = GL15.glGenBuffers();
            positionBuffer = MemoryUtil.memAllocFloat(positions.length);
            positionBuffer.put(positions).flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,POSITION_VBO_ID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,positionBuffer,GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(0,3, GL11.GL_FLOAT,false,0,0);
            //Creating color VBO
            COLOR_VBO_ID = GL15.glGenBuffers();
            colorBuffer = MemoryUtil.memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,COLOR_VBO_ID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,colorBuffer,GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1,3,GL11.GL_FLOAT,false,0,0);
            //Creating Indices VBO
            INDICES_VBO_ID = GL15.glGenBuffers();
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,INDICES_VBO_ID);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,indicesBuffer,GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
            GL30.glBindVertexArray(0);
        }finally{
            if(positionBuffer != null){
                MemoryUtil.memFree(positionBuffer);
            }
            if(colorBuffer != null){
                MemoryUtil.memFree(colorBuffer);
            }
            if(indicesBuffer != null){
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public int getVAO_ID() {
        return VAO_ID;
    }

    public int getVERTEX_COUNT() {
        return VERTEX_COUNT;
    }

    public void cleanup(){
        GL20.glDisableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
        GL15.glDeleteBuffers(POSITION_VBO_ID);
        GL15.glDeleteBuffers(COLOR_VBO_ID);
        GL15.glDeleteBuffers(INDICES_VBO_ID);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(VAO_ID);
    }
}
