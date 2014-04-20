package my.game.achmed.Characters.Players;

import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.Characters.Player;

import android.content.Context;

public class ABGreenOgre extends Player {

    private final float x_position = 0;
    private final float y_position = 0;

    public ABGreenOgre() {
	super();
    }

    @Override
    public void draw(GL10 gl) {
	super.draw(gl);
    }

    @Override
    public void loadTexture(GL10 gl, int texture, Context context) {
	super.loadTexture(gl, texture, context);
    }

    @Override
    public boolean moveUp(GL10 gl) {
	gl.glMatrixMode(GL10.GL_TEXTURE);
	gl.glLoadIdentity();
	gl.glScalef(1f, 1f, 1f);
	gl.glTranslatef(0.332f, 0.625f, 0f);
	this.draw(gl);
	return false;
    }

    @Override
    public boolean moveDown(GL10 gl) {
	gl.glMatrixMode(GL10.GL_TEXTURE);
	gl.glLoadIdentity();
	gl.glScalef(1f, 1f, 1f);
	gl.glTranslatef(0.83f,0.625f, 0f);
	this.draw(gl);
	return false;

    }

    @Override
    public boolean moveLeft(GL10 gl) {
	gl.glMatrixMode(GL10.GL_TEXTURE);
	gl.glLoadIdentity();
	gl.glScalef(1f, 1f, 1f);
	gl.glTranslatef(0.581f, 0.625f, 0f);
	this.draw(gl);
	return false;
    }

    @Override
    public boolean moveRight(GL10 gl) {
	gl.glMatrixMode(GL10.GL_TEXTURE);
	gl.glLoadIdentity();
	gl.glScalef(1f, 1f, 1f);
	gl.glTranslatef(0.083f, 0.625f, 0f);
	this.draw(gl);
	return false;
    }



}
