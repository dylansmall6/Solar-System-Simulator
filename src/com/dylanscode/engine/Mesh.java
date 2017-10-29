package com.dylanscode.engine;

import com.dylanscode.engine.engine.game.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Mesh {
    private final int VAO_ID;

    private final int POSITION_VBO_ID;

    private final int TEXTURE_COORDS_VBO_ID;

    private final int INDICES_VBO_ID;

    private final int VERTEX_COUNT;

    private final Texture TEXTURE;

    public Mesh(float[] positions,float[] textureCoordinates, int[] indices, Texture texture){
        FloatBuffer positionBuffer = null, textureCoordinatesBuffer = null;
        IntBuffer indicesBuffer = null;
        try{
            this.TEXTURE = texture;
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
            TEXTURE_COORDS_VBO_ID = GL15.glGenBuffers();
            textureCoordinatesBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
            textureCoordinatesBuffer.put(textureCoordinates).flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,TEXTURE_COORDS_VBO_ID);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER,textureCoordinatesBuffer,GL15.GL_STATIC_DRAW);
            GL20.glVertexAttribPointer(1,2,GL11.GL_FLOAT,false,0,0);
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
            if(textureCoordinatesBuffer != null){
                MemoryUtil.memFree(textureCoordinatesBuffer);
            }
            if(indicesBuffer != null){
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public void render(){
        glBindVertexArray(VAO_ID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, VERTEX_COUNT, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
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
        GL15.glDeleteBuffers(TEXTURE_COORDS_VBO_ID);
        GL15.glDeleteBuffers(INDICES_VBO_ID);

        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(VAO_ID);
    }
}
