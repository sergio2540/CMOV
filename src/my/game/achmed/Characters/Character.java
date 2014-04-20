package my.game.achmed.Characters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public abstract class Character {


    protected enum ACTION {
	UP, DOWN, LEFT, RIGHT
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

    public abstract boolean moveUp(GL10 gl);
    public abstract boolean moveDown(GL10 gl);
    public abstract boolean moveLeft(GL10 gl);
    public abstract boolean moveRight(GL10 gl);


}