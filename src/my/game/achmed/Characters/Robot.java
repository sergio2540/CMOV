package my.game.achmed.Characters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.ABEngine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public abstract class Robot extends Character {



    protected ACTION robotAction = ACTION.LEFT;


    private int counter = 0;

    private int getCounter(){
	return counter;
    }

    private void setCounter(int counter){
	this.counter = counter;
    }

    private final int speed = 10;

    private int getSpeed() {
	return speed;
    }

    private final Random r = new Random();

    private final int[] textures = new int[1];

    protected int[] getTextures(){
	return textures;
    }

    private final static float vertices[] = {

	0.0f, 0.0f, 0.0f,
	1.0f, 0.0f, 0.0f,
	0.0f, 1.0f, 0.0f,
	1.0f, 1.0f, 0.0f,

    };

    private final static float texture[] = {
	0.0f, 0.0f, //inferior esquerdo
	0.08333f, 0.0f, //inferior direito
	0.0f, 0.125f, //superior esquerdo
	0.08333f, 0.125f,  //superior direito
    };

    private final static byte indices[] = {

	2,0,3,
	0,1,3,

    };

    private final boolean isDead = false;

    @Override
    public boolean isDead(){
	return isDead;
    }

    public Robot() {

	super(vertices,texture,indices);
    }

    public void draw(GL10 gl) {

	//Se calhar podia ir para Characters
	FloatBuffer vertexBuffer = super.getVertexBuffer();
	FloatBuffer textureBuffer = super.getTextureBuffer();
	ByteBuffer indexBuffer = super.getIndexBuffer();


	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
	gl.glFrontFace(GL10.GL_CCW);
	gl.glEnable(GL10.GL_CULL_FACE);
	gl.glCullFace(GL10.GL_BACK);
	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
	gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
		GL10.GL_UNSIGNED_BYTE, indexBuffer);
	gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	gl.glDisable(GL10.GL_CULL_FACE);

    }

    public void loadTexture(GL10 gl, int texture, Context context) {
	InputStream imagestream =
		context.getResources().openRawResource(texture);
	Bitmap bitmap = null;
	try {
	    bitmap = BitmapFactory.decodeStream(imagestream);
	}catch(Exception e){
	}finally {
	    try {
		imagestream.close();
		imagestream = null;
	    } catch (IOException e) {
	    }
	}

	gl.glGenTextures(1, textures, 0);
	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
		GL10.GL_NEAREST);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
		GL10.GL_LINEAR);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
		GL10.GL_REPEAT);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
		GL10.GL_REPEAT);

	GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

	bitmap.recycle();

    }

    public void changeRobotAction() {

	//int[] position = { ABEngine.PLAYER_LEFT, ABEngine.PLAYER_DOWN, ABEngine.PLAYER_RIGHT,
	//	ABEngine.PLAYER_UP };
	ACTION leftRightDecision;
	ACTION upDownDecision;

	ACTION correctDecision;
	ACTION wrongDecision;

	ACTION[] wrongPositions = new ACTION[3];


	float xPlayerPosition = ABEngine.X_POSITION;
	float yPlayerPosition = ABEngine.Y_POSITION;
	
	float xRobotPosition = this.getXPosition();
	float yRobotPosition = this.getYPosition();

	//check if lefft or right
	float xDistance = xRobotPosition - xPlayerPosition;
	if(xDistance >= 0){
	    leftRightDecision = ACTION.LEFT;
	    wrongPositions[0] = ACTION.RIGHT;
	}
	else {
	    leftRightDecision = ACTION.RIGHT;
	    wrongPositions[0] = ACTION.LEFT;
	}

	//check if up or down.
	float yDistance = yRobotPosition - yPlayerPosition;
	if(yDistance >= 0){
	    upDownDecision = ACTION.DOWN;
	    wrongPositions[1] = ACTION.UP;
	}
	else {
	    upDownDecision = ACTION.UP;
	    wrongPositions[1] = ACTION.DOWN;
	}

	if (xDistance  == 0){
	    correctDecision = upDownDecision;
	}

	else if (yDistance == 0){
	    correctDecision = leftRightDecision;
	}
	else if(Math.abs(xDistance) < Math.abs(yDistance))
	    correctDecision = leftRightDecision;

	else if(Math.abs(xDistance) > Math.abs(yDistance))
	    correctDecision= upDownDecision;


	//if its equally far.
	else{

	    ACTION[] possibleReturns = {leftRightDecision, upDownDecision};
	    correctDecision = possibleReturns[r.nextInt(1+1)];
	}

	if(r.nextInt(100) < 45){
	    this.robotAction = correctDecision;
	}else {
	    wrongDecision = wrongPositions[r.nextInt(1+1)];
	   this.robotAction = wrongDecision;
	}



    }

    @Override
    public abstract boolean moveUp(GL10 gl);
    @Override
    public abstract boolean moveDown(GL10 gl);
    @Override
    public abstract boolean moveLeft(GL10 gl);
    @Override
    public abstract boolean moveRight(GL10 gl);

    public void move(GL10 gl) {

	switch (this.robotAction) {
	case LEFT : 

	    float pos = this.getXPosition() - (100.f - this.getSpeed()*this.getCounter());

	    if(!ABEngine.detectColision(pos,this.getYPosition(),this.robotAction)) {

		this.setXPosition(this.getXPosition() - this.getSpeed());
		this.translate(gl);

	    } else {

		this.translate(gl);

	    }

	    this.moveLeft(gl);



	    this.setCounter((this.getCounter() + 1) % ABEngine.MAX_COUNTER);

	    if(this.getCounter() == 0) {

		this.setCounter(0);
		changeRobotAction();
	    }

	    break;

	case RIGHT : 

	    pos = this.getXPosition() + (100.f - this.getSpeed()* this.getCounter());


	    if(!ABEngine.detectColision(pos,this.getYPosition(),robotAction)) {

		this.setXPosition(this.getXPosition() + this.getSpeed());

		this.translate(gl);


	    } else {

		this.translate(gl);

	    }
	    
	    this.moveRight(gl);

	    this.setCounter((this.getCounter() + 1) % ABEngine.MAX_COUNTER);

	    if(this.getCounter() == 0) {

		this.setCounter(0);
		changeRobotAction();
	    }

	    break;

	case UP :

	    pos = this.getYPosition() + (100.f - this.getSpeed() * this.getCounter());


	    if(!ABEngine.detectColision(this.getXPosition(), pos,robotAction)) {

		this.setYPosition(this.getYPosition() + this.getSpeed());

		this.translate(gl);

	    } else {

		this.translate(gl);

	    }

	    this.moveUp(gl);

	    this.setCounter((this.getCounter() + 1) % ABEngine.MAX_COUNTER);

	    if(this.getCounter() == 0) {

		this.setCounter(0);
		changeRobotAction();
	    }

	    break;

	case DOWN : 

	    pos = this.getYPosition() - (100.f - this.getSpeed() * this.getCounter());

	    if(!ABEngine.detectColision(this.getXPosition(), pos,robotAction)) {

		this.setYPosition(this.getYPosition() - this.getSpeed());
		this.translate(gl);

	    } else {

		this.translate(gl);

	    }



	    this.moveDown(gl);

	    this.setCounter((this.getCounter() + 1) % ABEngine.MAX_COUNTER);

	    if(this.getCounter() == 0) {

		this.setCounter(0);
		changeRobotAction();
	    }


	    break;
	    
	    default:
		break;

	}
    }

    private void translate(GL10 gl){

	float x =  this.getXPosition()/100f;
	float y =  this.getYPosition()/100f;
	gl.glTranslatef(x,y, 0.5f);
	
	//Altera posicao do robot na matriz
	int mtx_x = ABEngine.getXMatrixPosition(this.getXPosition(),robotAction);
	int mtx_y = ABEngine.getYMatrixPosition(this.getYPosition(),robotAction);
	ABEngine.setObject(mtx_x,mtx_y,'R');

    }


}
