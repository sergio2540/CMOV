package my.game.achmed.Characters.Robots;

import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.Characters.Robot;

import android.content.Context;

public class ABYellowRobot extends Robot {

	private static final long serialVersionUID = 1L;

	public ABYellowRobot(float xpos, float ypos) {
		super(xpos,ypos);

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
	public void changeRobotAction() {
		super.changeRobotAction();
	}

	@Override
	public boolean moveUp(GL10 gl) {
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glScalef(1f, 1f, 1f);
		gl.glTranslatef(0.33332f, 0.0f, 0f);
		this.draw(gl);
		return false;
	}

	@Override
	public boolean moveDown(GL10 gl) {
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glScalef(1f, 1f, 1f);
		gl.glTranslatef(0.33332f, 0.375f, 0f);
		this.draw(gl);
		return false;
	}

	@Override
	public boolean moveLeft(GL10 gl) {

		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glScalef(1f, 1f, 1f);
		gl.glTranslatef(0.33332f, 0.125f, 0f);
		this.draw(gl);


		return false;
	}

	@Override
	public boolean moveRight(GL10 gl) {
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glScalef(1f, 1f, 1f);
		gl.glTranslatef(0.33332f, 0.250f, 0f);
		this.draw(gl);
		return false;
	}

	@Override
	public void move(GL10 gl){
		super.move(gl);
	}

}