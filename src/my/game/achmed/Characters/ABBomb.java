package my.game.achmed.Characters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.ABEngine;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class ABBomb extends Character {

    private float xpos;
    private float ypos;

    @Override
    public float getXPosition(){
	return xpos;
    }

    @Override
    public float getYPosition(){
	return ypos;
    }
    private boolean isDropped = false;

    private boolean isNotDropped(){
	return !isDropped;
    }

    private final ABFire fire; 
    private final Player player;

    private BOMB_ACTION bombAction;

    private final int[] textures = new int[1];

    private static final float vertices[] = {

	0.0f, 0.0f, 0.0f,
	1.0f, 0.0f, 0.0f,
	0.0f, 1.0f, 0.0f,
	1.0f, 1.0f, 0.0f,

    };

    private static final float texture[] = {
	0.0f, 0.0f, //inferior esquerdo
	0.250f, 0.0f, //inferior direito
	0.0f, 0.167f, //superior esquerdo
	0.250f, 0.167f,  //superior direito
    };

    private static final byte indices[] = {

	2,0,3,
	0,1,3,

    };

    public ABBomb(Player player) {
	
	super(vertices, texture, indices);
	
	this.player = player; 
	
	this.fire = new ABFire(); 
	this.bombAction = BOMB_ACTION.NO_BOMB;
	
    }

    public void setTimerToBombExplosion(int delay) {

	Timer timer = new Timer();

	timer.schedule(

		new TimerTask() {
		    @Override
		    public void run() {
			explode();
		    }

		}, delay);

    }

    public void drawTexture(GL10 gl) {


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


	fire.loadTexture(gl, ABEngine.GAME_FIRE, ABEngine.context);

    }

    public void draw(GL10 gl){

	switch (bombAction) {

	case NO_BOMB:
	    isDropped = false;
	    break;

	case BOMB_EXPLOSION:
	    drawExplosion(gl);
	    break;

	case BOMB_DROP:
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    gl.glPushMatrix();
	    gl.glScalef(.05f, .05f, 1f);

	    gl.glTranslatef(ABEngine.START_X,ABEngine.START_Y, 0.7f);


	    if(isNotDropped()) {
		//int [] pos = getPosition();
		//this.xpos = pos[0];
		//this.ypos = pos[1];
		this.xpos = player.getXPosition();
		this.ypos = player.getYPosition();
	    }

	    gl.glTranslatef(getXPosition()/100f,getYPosition()/100f, 0.7f);

	    gl.glMatrixMode(GL10.GL_TEXTURE);
	    gl.glLoadIdentity();
	    gl.glScalef(1f, 1f, 1f);
	    gl.glTranslatef(0.250f, 0.835f , 0f);
	    drawTexture(gl);

	    //o timer depende do nivel
	    setTimerToBombExplosion(ABEngine.BOMB_TIME_TO_EXPLOSION);
	    gl.glPopMatrix();

	    isDropped = true;

	    break;

	default:
	    break;
	}
    }


    private void drawExplosion(GL10 gl){


	int rounds = 0;

	float x = 0;
	float y = 0;
	float inc = 0;




	for(int i = 0; i < ABEngine.EXPLOSION_RADIUS * 4 + 1; i++) {

	    float b_x = getXPosition() + x;
	    float b_y = getYPosition() + y;

	    boolean canBurn = burn(b_x,b_y);

	    if(canBurn){

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.05f, .05f, 1f);

		gl.glTranslatef(ABEngine.START_X +  b_x/100f, ABEngine.START_Y + b_y/100f, 0.8f);


		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(0.666f, 0.75f, 0.3f);
		fire.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	    }


	    if(rounds % 4 == 0) {
		x = -100 - inc; 
		y = 0;
	    }

	    if(rounds % 4 == 1) {
		x = 100 + inc; 
		y = 0;
	    }

	    if(rounds % 4 == 2) {
		x = 0; 
		y = -100 - inc;
	    }

	    if(rounds % 4 == 3) {
		x = 0; 
		y = 100 + inc;
		inc += 100;
	    }

	    rounds++;

	}


	setAction(BOMB_ACTION.NO_BOMB);

    }

    //Remove objectos queimados
    public static boolean burn(float x, float y) {

	int mtx_x = ABEngine.getXMatrixPosition(x);
	int mtx_y = ABEngine.getYMatrixPosition(y);

	if(ABEngine.isNotInMatrixRange(mtx_x,mtx_y)){
	    return false;
	}

	char obj = ABEngine.getObject(mtx_x,mtx_y);


	if (!(obj == 'W')) {


	    if((obj == '1') || (obj == '2') || (obj == '3')) {
		if(ABEngine.PLAYER.getID() == obj){
		    ABEngine.PLAYER.kill();
		}
		else {
		    Player p = ABEngine.PLAYERS.get(obj);

		    if(p != null){
			p.kill();
			ABEngine.PLAYER.opponentKilled();
		    }
		}

	    }


	    if (obj == 'R'){

		for(Robot r : ABEngine.ROBOTS){
		    if(r.isInRange(mtx_x,mtx_y)){
			r.kill();
			ABEngine.PLAYER.robotKilled();
		    }
		}


		ABEngine.updateScore(ABEngine.PLAYER.getScore());

	    }

	    ABEngine.setObject(mtx_x,mtx_y,'-');


	    return true;

	}else {
	    return false;
	}

    }
    
    private int[] getPosition() {
	int [] pos = new int[1];
	
	pos[0] = ABEngine.getXMatrixPosition(player.getXPosition(),player.playerAction);
	pos[1] = ABEngine.getYMatrixPosition(player.getYPosition(),player.playerAction);
	/*
	switch (this.playerAction) {
	case LEFT : 

	    float pos = this.getXPosition() - (100.f - this.getSpeed()*this.getCounter());

	    if(!ABEngine.detectColision(pos,this.getYPosition(),this.playerAction)) {
		this.setXPosition(this.getXPosition() - this.getSpeed());
	    }

	    this.translate(gl);
	    this.moveLeft(gl);

	    this.setCounter((this.getCounter() + 1) % ABEngine.MAX_COUNTER);

	    if(this.getCounter() == 0) {

		ABEngine.STOPPED = true;
		this.setCounter(0);
		playerAction = CHARACTER_ACTION.LEFT_RELEASE;
	    }

	    break;

	case RIGHT : 

	    

	    break;

	case UP :

	    

	    break;

	case DOWN : 

	    

	   


	    break;

	case LEFT_RELEASE:

	   

	    break;
	case RIGHT_RELEASE:

	   

	    break;
	case UP_RELEASE:

	   

	    break;
	case DOWN_RELEASE:

	   

	    break;

	}
	*/
	return pos;
	
    }

    private void setAction(BOMB_ACTION action) {
	bombAction = action;
    }

    public void drop() {
	setAction(BOMB_ACTION.BOMB_DROP);
    }	

    private void explode() {
	setAction(BOMB_ACTION.BOMB_EXPLOSION);

    }
}