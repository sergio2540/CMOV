package my.game.achmed.OpenGL;


import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.ABBomb;
import my.game.achmed.Characters.ABFire;
import my.game.achmed.Characters.ABMap;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;
import my.game.achmed.Characters.Players.ABAchmed;
import my.game.achmed.Characters.Players.ABGreenOgre;
import my.game.achmed.Characters.Players.ABRedMermaid;
import my.game.achmed.Characters.Robots.ABGreenRobot;
import my.game.achmed.Characters.Robots.ABRedRobot;
import my.game.achmed.Characters.Robots.ABYellowRobot;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class ABGameRenderer implements Renderer {


    private final Random r = new Random();

    private final ABMap map = new ABMap();

    private final ABBomb bomb = new ABBomb();
    private final ABFire fire = new ABFire();
    
    
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {


	gl.glMatrixMode(GL10.GL_PROJECTION);
	gl.glDepthRangef(1f, 0f);
	gl.glViewport(0, 0, width, height);

	gl.glLoadIdentity();

	float x_max=1;
	float x_min=0;

	float y_max=1;
	float y_min=0;

	float z_max=-10;
	float z_min=10;

	float rac = (float)width/height;

	if(rac > 1){

	    ABEngine.start_x = (float) Math.sqrt(Math.pow(rac*x_min,2) +  Math.pow(rac*x_max,2))/2;
	    ABEngine.start_y = (float) Math.sqrt(Math.pow(y_min,2) + Math.pow(y_max,2))/2;

	    gl.glOrthof(rac*x_min, rac*x_max,y_min,y_max,z_min,z_max);
	}
	else {

	    ABEngine.start_x = (float) Math.sqrt( Math.pow(x_min,2) +  Math.pow(x_max,2))/2;
	    ABEngine.start_y = (float) Math.sqrt(Math.pow(y_min/rac,2) + Math.pow(y_max/rac,2))/2;

	    gl.glOrthof(x_min,x_max, y_min/rac, y_max/rac, z_min, z_max);
	}
	
	

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig arg1) {


	gl.glEnable(GL10.GL_TEXTURE_2D);
	gl.glClearDepthf(1.0f);
	gl.glEnable(GL10.GL_DEPTH_TEST);
	gl.glDepthFunc(GL10.GL_LEQUAL);
	gl.glEnable(GL10.GL_ALPHA_TEST);
	gl.glEnable(GL10.GL_BLEND);
	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

	//TEXTURES

	map.loadTexture(gl, ABEngine.GAME_MAP, ABEngine.context);
	bomb.loadTexture(gl, ABEngine.GAME_BOMB, ABEngine.context);
	fire.loadTexture(gl, ABEngine.GAME_FIRE, ABEngine.context);
	
	if (ABEngine.PLAYER != null)
	    ABEngine.PLAYER.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
	for(Player p : ABEngine.PLAYERS){
	    p.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
	}
	
	for(Robot r : ABEngine.ROBOTS){
	    r.loadTexture(gl, ABEngine.GAME_ROBOTS, ABEngine.context);
	}
    }

    @Override
    public void onDrawFrame(GL10 gl) {

	try {
	    Thread.sleep(ABEngine.GAME_THREAD_FPS_SLEEP);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	//clear();

	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
	gl.glClearDepthf(1.0f); 
	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

	//não trocar a ordem das linhas 
	drawMap(gl, ABEngine.game_map);
	dropBomb(gl);
	movePlayer(gl);
	movePlayers(gl);
	explodeBomb(gl, ABEngine.game_map);
	moveGreenRobot(gl, ABEngine.game_map);


    }

    private void moveGreenRobot(GL10 gl, char[][] game_map) {

	//Esta morto nao faz nada
	//if(ABEngine.ROBOT_IS_DEAD[1])
	   // return;

	

	for(Robot r : ABEngine.ROBOTS){
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    gl.glPushMatrix();
	    gl.glScalef(.05f, .05f, 1f);

	    gl.glTranslatef(ABEngine.START_X,ABEngine.START_Y, 0.2f);



	    r.move(gl); 


	    gl.glPopMatrix();
	}

    }

    private void dropBomb(GL10 gl) {

	switch (ABEngine.BOMB_ACTION) {

	case ABEngine.NO_BOMB:

	    ABEngine.BOMB_DROPPED = false;
	    break;

	case ABEngine.DROP_BOMB:

	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    gl.glPushMatrix();
	    gl.glScalef(.05f, .05f, 1f);
	    
	    gl.glTranslatef(ABEngine.START_X,ABEngine.START_Y, 0.3f);
	    
	    
	    if(!ABEngine.BOMB_DROPPED) {
		bomb.x_position = ABEngine.PLAYER.getXPosition();
		bomb.y_position = ABEngine.PLAYER.getYPosition();
	    }
	    
	    gl.glTranslatef( bomb.x_position/100f,bomb.y_position/100f, 0.3f);

	    gl.glMatrixMode(GL10.GL_TEXTURE);
	    gl.glLoadIdentity();
	    gl.glScalef(1f, 1f, 1f);
	    gl.glTranslatef(0.250f, 0.835f , 0f);
	    bomb.draw(gl);

	    //o timer depende do nivel
	    bomb.setTimerToBombExplosion(ABEngine.BOMB_TIME_TO_EXPLOSION);
	    gl.glPopMatrix();
	    
	    ABEngine.BOMB_DROPPED = true;


	    break;

	}

    }

    public void movePlayers(GL10 gl) {

	
	for(Player p : ABEngine.PLAYERS){
	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    gl.glPushMatrix();
	    gl.glScalef(.05f, .05f, 1f);
	    gl.glTranslatef(ABEngine.START_X, ABEngine.START_Y, 0.5f);
	    
	    p.move(gl);
	    if (p.getCounter() == 0)
		p.changePlayerAction();
	    
	    gl.glPopMatrix();
	}

    }
    
    public void movePlayer(GL10 gl) {

	    gl.glMatrixMode(GL10.GL_MODELVIEW);
	    gl.glLoadIdentity();
	    gl.glPushMatrix();
	    gl.glScalef(.05f, .05f, 1f);
	    gl.glTranslatef(ABEngine.START_X, ABEngine.START_Y, 0.5f);
	    
	    ABEngine.PLAYER.move(gl);

	    gl.glPopMatrix();
	

    }

    public void drawMap(GL10 gl, char[][] game_matrix) {
	
	ABEngine.START_X = (ABEngine.start_x/0.05f) - ABEngine.game_map[0].length/2.f;
	ABEngine.START_Y = (ABEngine.start_y/0.05f) - ABEngine.game_map.length/2.f;

	for(int x=0; x< game_matrix[0].length ; x++) {
	    for(int y=0; y< game_matrix.length; y++) {

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(.05f, .05f, 1f);
		gl.glTranslatef(ABEngine.START_X + x,ABEngine.START_Y +y, 0f);

		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();

		switch(game_matrix[game_matrix.length - 1 - y][x]) {

		case 'W':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.9375f, 0.0625f, 0f);
		    map.draw(gl);
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    break;

		case 'O':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.0625f, 0.0625f, 0f);
		    map.draw(gl); 
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    break;

		case '-':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    break;

		case '1':


		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    if(ABEngine.FIRST_MAP_DRAW) {
			Player player = new ABRedMermaid(x*100,y*100);
			player.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
			ABEngine.PLAYER = player;
		    }

		    break;
		case '2':


		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    if(ABEngine.FIRST_MAP_DRAW) {
			Player player = new ABGreenOgre(x*100,y*100);
			player.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
			ABEngine.PLAYERS.add(player);
		    }

		    break;

		case '3':


		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    if(ABEngine.FIRST_MAP_DRAW) {
			Player player = new ABAchmed(x*100,y*100);
			player.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
			ABEngine.PLAYERS.add(player);
		    }

		    break;


		case 'R':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 
		    gl.glPopMatrix();
		    gl.glLoadIdentity();

		    if(ABEngine.FIRST_MAP_DRAW) {


			int i = r.nextInt(3);
			//int opt = opt_robots[i];

			Robot robot = null;

			if (i == 0){
			    robot = new ABGreenRobot(x*100, y*100);   
			}

			if (i == 1){
			    robot = new ABRedRobot(x*100, y*100);
			}

			if (i == 2) {
			    robot = new ABYellowRobot(x*100, y*100);

			}
			
			robot.loadTexture(gl, ABEngine.GAME_ROBOTS, ABEngine.context);
			ABEngine.ROBOTS.add(robot);


		    }

		    break;

		}


	    }

	}

	ABEngine.FIRST_MAP_DRAW = false;

    }

    public void explodeBomb(GL10 gl, char[][] game_map) {

	if(ABEngine.BOMB_ACTION == ABEngine.BOMB_EXPLOSION) {

	    int rounds = 0;

	    float x = 0;
	    float y = 0;
	    float inc = 0;




	    for(int i = 0; i < ABEngine.EXPLOSION_RADIUS * 4 + 1; i++) {


		boolean canBurn = ABEngine.burn((bomb.x_position + x),(bomb.y_position + y));

		if(canBurn){

		    gl.glMatrixMode(GL10.GL_MODELVIEW);
		    gl.glLoadIdentity();
		    gl.glPushMatrix();
		    gl.glScalef(.05f, .05f, 1f);

		    gl.glTranslatef( ABEngine.START_X +  (bomb.x_position + x)/100f, ABEngine.START_Y + (bomb.y_position + y)/100f, 0.8f);


		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.666f, 0.75f, 0.3f);
		    fire.draw(gl);
		    gl.glPopMatrix();
		    gl.glLoadIdentity();
		}


		if(rounds % 4 == 0) {
		    Log.w("BOMB", "round4");
		    //inc++; < ABEngine.RAdISU
		    x = -100 - inc; 
		    y = 0;

		}

		if(rounds % 4 == 1) {
		    //						Log.w("BOMB", "round3");
		    x = 100 + inc; 
		    y = 0;
		}

		if(rounds % 4 == 2) {
		    //						Log.w("BOMB", "round2");
		    x = 0; 
		    y = -100 - inc;
		}

		if(rounds % 4 == 3) {
		    //						Log.w("BOMB", "round1");
		    x = 0; 
		    y = 100 + inc;

		    inc += 100;
		}

		rounds++;

	    }


	    ABEngine.BOMB_ACTION = ABEngine.NO_BOMB;

	} else {


	}

    }

}
