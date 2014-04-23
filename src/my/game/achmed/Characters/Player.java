package my.game.achmed.Characters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.Players.ABAchmed;
import my.game.achmed.Characters.Players.ABGreenOgre;
import my.game.achmed.Characters.Players.ABRedMermaid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public abstract class Player extends Character {

	private final static int COUNTER_MAX = 10; 

	private final char id;

	private ABBomb bomb;

	private void setBomb(ABBomb bomb){
		this.bomb = bomb; 
	}

	private float score;

	public float getScore(){
		return score;
	}
	public void setScore(float score){
		this.score = score;
		if(ABEngine.PLAYER.getID() == id)
			ABEngine.updateScore(ABEngine.PLAYER.getScore());
	}

	private final float pointsPerOpponentKilled;
	private final float pointsPerRobotKilled;


	private final float speed = 10;

	private float getSpeed(){
		return speed;
	}


	private final Random r = new Random();

	protected CHARACTER_ACTION playerAction = CHARACTER_ACTION.LEFT_RELEASE;

	public void setAction(CHARACTER_ACTION playerAction) {
		this.playerAction = playerAction;
	}

	private int counter = 0;

	public int getCounter() {
		return counter;
	}

	private void setCounter(int counter) {
		this.counter = counter;
	}

	private final int[] textures = new int[1];

	private final static float vertices[] = {

		0.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f,
		0.0f, 1.0f, 0.0f,
		1.0f, 1.0f, 0.0f,

	};

	private final static float texture[] = {
		0.0f, 0.0f, //inferior esquerdo
		0.083f, 0.0f, //inferior direito
		0.0f, 0.125f, //superior esquerdo
		0.083f, 0.125f,  //superior direito
	};

	private final static byte indices[] = {

		2,0,3,
		0,1,3,

	};

	private boolean isDead = false;

	@Override
	public boolean isDead(){
		return isDead;
	}

	protected Player(char id, float xpos, float ypos) {

		super(vertices, texture,indices);
		super.setXPosition(xpos);
		super.setYPosition(ypos);

		this.id = id;
		this.bomb = null;

		this.pointsPerOpponentKilled = ABEngine.LEVEL.getPointsPerOpponentKilled();
		this.pointsPerRobotKilled = ABEngine.LEVEL.getPointsPerRobotKilled();

	}

	public static Player create(char id, float x, float y) {

		Player p;

		if(id == '1'){

			p = new ABGreenOgre(x, y);

		} else if (id == '2'){
			p = new ABRedMermaid(x, y);
		}
		else {

			p = new ABAchmed(x,y);  
		}

		ABBomb b = new ABBomb(p);
		p.setBomb(b);

		return p;
	}



	public void draw(GL10 gl) {

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

		bomb.loadTexture(gl, ABEngine.GAME_BOMB, ABEngine.context);

	}


	public abstract boolean moveUp(GL10 gl);


	public abstract boolean moveDown(GL10 gl);

	public abstract boolean moveLeft(GL10 gl);


	public abstract boolean moveRight(GL10 gl);


	public void changePlayerAction() {


		CHARACTER_ACTION leftRightDecision;
		CHARACTER_ACTION upDownDecision;

		CHARACTER_ACTION correctDecision;
		CHARACTER_ACTION wrongDecision;

		CHARACTER_ACTION[] wrongPositions = new CHARACTER_ACTION[3];

		//Choose a random player do catch
		List<Player> players = new ArrayList<Player>();
		if(ABEngine.PLAYER != null)
			players.add(ABEngine.PLAYER);
		players.addAll(ABEngine.PLAYERS.values());


		int p = r.nextInt(players.size());
		Player player = players.get(p);

		float xPlayerPosition = player.getXPosition();
		float yPlayerPosition = player.getYPosition();

		float xRobotPosition = this.getXPosition();
		float yRobotPosition = this.getYPosition();

		//check if lefft or right
		float xDistance = xRobotPosition - xPlayerPosition;
		if(xDistance >= 0){
			leftRightDecision = CHARACTER_ACTION.LEFT;
			wrongPositions[0] = CHARACTER_ACTION.RIGHT;
		}
		else {
			leftRightDecision = CHARACTER_ACTION.RIGHT;
			wrongPositions[0] = CHARACTER_ACTION.LEFT;
		}

		//check if up or down.
		float yDistance = yRobotPosition - yPlayerPosition;
		if(yDistance >= 0){
			upDownDecision = CHARACTER_ACTION.DOWN;
			wrongPositions[1] = CHARACTER_ACTION.UP;
		}
		else {
			upDownDecision = CHARACTER_ACTION.UP;
			wrongPositions[1] = CHARACTER_ACTION.DOWN;
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

			CHARACTER_ACTION[] possibleReturns = {leftRightDecision, upDownDecision};
			correctDecision = possibleReturns[r.nextInt(1+1)];
		}

		if(r.nextInt(100) < 90){
			this.playerAction = correctDecision;
		}else {
			wrongDecision = wrongPositions[r.nextInt(1+1)];
			this.playerAction = wrongDecision;
			this.getBomb().drop();
		}



	}

	public synchronized void move(GL10 gl) {

		if(isDead()){
			return;
		}

		this.cleanFromMatrix();


		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.05f, .05f, 1f);
		gl.glTranslatef(ABEngine.START_X, ABEngine.START_Y, 0.5f);

		switch (this.playerAction) {
		case LEFT : 

			float pos = this.getXPosition() - (100.f - this.getSpeed()*this.getCounter());

			if(!ABEngine.detectColision(pos,this.getYPosition(),this.playerAction)) {
				this.setXPosition(this.getXPosition() - this.getSpeed());
			}

			this.translate(gl);
			this.moveLeft(gl);

			this.setCounter((this.getCounter() + 1) % COUNTER_MAX);

			if(this.getCounter() == 0 && ABEngine.STOP){

				ABEngine.STOPPED = true;
				this.setCounter(0);
				playerAction = CHARACTER_ACTION.LEFT_RELEASE;
			}

			break;

		case RIGHT : 

			pos = this.getXPosition() + (100.f - this.getSpeed()* this.getCounter());


			if(!ABEngine.detectColision(pos,this.getYPosition(),playerAction)) {
				this.setXPosition(this.getXPosition() + this.getSpeed());
			}

			this.translate(gl);
			this.moveRight(gl);

			this.setCounter((this.getCounter() + 1) % COUNTER_MAX );

			if(this.getCounter() == 0 && ABEngine.STOP) {

				ABEngine.STOPPED = true;
				this.setCounter(0);
				playerAction = CHARACTER_ACTION.RIGHT_RELEASE;

			}

			break;

		case UP :

			pos = this.getYPosition() + (100.f - this.getSpeed() * this.getCounter());


			if(!ABEngine.detectColision(this.getXPosition(), pos,playerAction)) {
				this.setYPosition(this.getYPosition() + this.getSpeed());
			}

			this.translate(gl);
			this.moveUp(gl);

			this.setCounter((this.getCounter() + 1) % COUNTER_MAX );

			if(this.getCounter() == 0 && ABEngine.STOP) {

				ABEngine.STOPPED = true;
				this.setCounter(0);
				playerAction = CHARACTER_ACTION.UP_RELEASE;

			}

			break;

		case DOWN : 

			pos = this.getYPosition() - (100.f - this.getSpeed() * this.getCounter());

			if(!ABEngine.detectColision(this.getXPosition(), pos, playerAction)) {
				this.setYPosition(this.getYPosition() - this.getSpeed());
			}

			this.translate(gl);
			this.moveDown(gl);

			this.setCounter((this.getCounter() + 1) % COUNTER_MAX );

			if(this.getCounter() == 0 && ABEngine.STOP) {
				ABEngine.STOPPED = true;
				this.setCounter(0);
				playerAction = CHARACTER_ACTION.DOWN_RELEASE;

			}


			break;

		case LEFT_RELEASE:

			this.translate(gl);
			this.moveLeft(gl);

			break;
		case RIGHT_RELEASE:

			this.translate(gl);
			this.moveRight(gl);

			break;
		case UP_RELEASE:

			this.translate(gl);
			this.moveUp(gl);

			break;
		case DOWN_RELEASE:

			this.translate(gl);
			this.moveDown(gl);

			break;

		}

		this.putInMatrix();
		gl.glPopMatrix();


		getBomb().draw(gl);
	}



	private void translate(GL10 gl){

		float x =  this.getXPosition()/100f;
		float y =  this.getYPosition()/100f;
		gl.glTranslatef(x,y, 0.5f);

	}

	public boolean isInRange(float mtx_x, float mtx_y) {

		int r_mtx_x = ABEngine.getXMatrixPosition(this.getXPosition(), playerAction);
		int r_mtx_y = ABEngine.getYMatrixPosition(this.getYPosition(),playerAction);

		if(mtx_x == r_mtx_x && mtx_y == r_mtx_y){
			return true;
		}

		return false;
	}

	protected void cleanFromMatrix(){

		//Altera antiga posicao do player na matriz
		int mtx_x = ABEngine.getXMatrixPosition(this.getXPosition(), playerAction);
		int mtx_y = ABEngine.getYMatrixPosition(this.getYPosition(), playerAction);

		ABEngine.setObject(mtx_x,mtx_y,'-');

	}


	protected void putInMatrix(){

		//Altera antiga posicao do player na matriz
		int mtx_x = ABEngine.getXMatrixPosition(this.getXPosition(),playerAction);
		int mtx_y = ABEngine.getYMatrixPosition(this.getYPosition(),playerAction);

		ABEngine.setObject(mtx_x,mtx_y,id);

	}

	public  void killRobot(float mtx_x, float mtx_y) {

		int i = 0;
		for(Robot r : ABEngine.ROBOTS){
			if(r.isInRange(mtx_x,mtx_y)){
				r.kill();

				if(ABEngine.PLAYER.getID() == id){
					robotKilled();
				}

				break;
			}
			i++;
		}
		ABEngine.ROBOTS.remove(i);

	}

	public void killPlayer(float mtx_x, float mtx_y) {
		if(ABEngine.PLAYER.isInRange(mtx_x, mtx_y)){
			ABEngine.PLAYER.kill();
			// ABEngine.PLAYER = null;
		}
		else {

			Map<java.lang.Character,Player> temp = new TreeMap<java.lang.Character,Player>();
			temp.putAll(ABEngine.PLAYERS);

			for(Player p : temp.values()){

				if(p.isInRange(mtx_x, mtx_y)){
					p.kill();

					if(ABEngine.PLAYER.getID() == id){
						opponentKilled();
					}

					//ABEngine.PLAYERS.remove(p.getID());

				}
			}
		}
	}


	public void kill(){

		//Elimina da matriz
		int mtx_x = ABEngine.getXMatrixPosition(this.getXPosition(),playerAction);
		int mtx_y = ABEngine.getYMatrixPosition(this.getYPosition(),playerAction);
		ABEngine.setObject(mtx_x,mtx_y,'-');

		isDead = true;

		if(ABEngine.PLAYER.getID() == this.getID()) {
			
			ABEngine.endGame(false);

		}

	}



	public void opponentKilled(){
		setScore(score + pointsPerOpponentKilled);
	}

	public void robotKilled(){
		setScore(score = score + pointsPerRobotKilled);
	}

	public char getID() {
		return id;
	}

	public ABBomb getBomb() {
		return bomb;
	}

}