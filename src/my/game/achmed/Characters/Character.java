package my.game.achmed.Characters;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.TreeMap;

import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.ABEngine;


public abstract class Character {

    private float xpos;
    private float ypos;

    public float getXPosition(){
	return xpos;
    }

    public float getYPosition(){
	return ypos;
    }

    protected void setXPosition(float xpos ){
	this.xpos = xpos;
    }

    protected void setYPosition(float ypos){
	this.ypos = ypos;
    }



    private final FloatBuffer vertexBuffer;

    protected FloatBuffer getVertexBuffer(){
	return vertexBuffer;
    }

    private final FloatBuffer textureBuffer;

    protected FloatBuffer getTextureBuffer(){
	return textureBuffer;
    }

    private final ByteBuffer indexBuffer;

    protected ByteBuffer getIndexBuffer(){
	return indexBuffer;
    }

    private final boolean isDead = false;

    protected boolean isDead(){
	return isDead;
    }

    public Character(final float vertices[], final float texture[], final byte indices[]) {

	ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
	byteBuf.order(ByteOrder.nativeOrder());

	vertexBuffer = byteBuf.asFloatBuffer();
	vertexBuffer.put(vertices);
	vertexBuffer.position(0);

	byteBuf = ByteBuffer.allocateDirect(texture.length * 4);
	byteBuf.order(ByteOrder.nativeOrder());

	textureBuffer = byteBuf.asFloatBuffer();
	textureBuffer.put(texture);
	textureBuffer.position(0);

	indexBuffer = ByteBuffer.allocateDirect(indices.length);
	indexBuffer.put(indices);
	indexBuffer.position(0);
    }    
}