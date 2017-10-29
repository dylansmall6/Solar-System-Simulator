package com.dylanscode.engine.engine.game;

import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.*;

public class Texture {
    private final int ID;

    public Texture(String fileName){
        this.ID = generateID(fileName);
    }

    public int getID() {
        return ID;
    }

    public static int generateID(String fileName){
        try{
            PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4*decoder.getHeight()*decoder.getWidth());
            decoder.decode(byteBuffer,decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();

            int ID = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D,ID);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT,1);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            return ID;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }

    }
    public void bind(){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,ID);
    }
    public void cleanup(){
        GL11.glDeleteTextures(ID);
    }
}
