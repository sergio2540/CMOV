package my.game.achmed.OpenGL;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.ABMap;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;
import my.game.achmed.Characters.Robots.ABGreenRobot;
import my.game.achmed.Characters.Robots.ABRedRobot;
import my.game.achmed.Characters.Robots.ABYellowRobot;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class ABGameRenderer implements Renderer {


    private final Random r = new Random();

    private final ABMap map = new ABMap();

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


	if (ABEngine.PLAYER != null){
	    ABEngine.PLAYER.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
	}
	for(Player p : ABEngine.PLAYERS.values()){
	    p.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
	}

	for(Robot r : ABEngine.ROBOTS){
	    r.loadTexture(gl, ABEngine.GAME_ROBOTS, ABEngine.context);
	}
    }



    private void draw(GL10 gl){

	//n�o trocar a ordem das linhas 
	drawMap(gl);
	
	
	if(ABEngine.ROBOTS.size() != 0)
	    moveRobots(gl);

	if(ABEngine.PLAYERS.size() != 0)
	    movePlayers(gl);

	if(ABEngine.PLAYER != null)
	    movePlayer(gl);

    }


    private void clear(GL10 gl){
	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
	gl.glClearDepthf(1.0f); 
	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

	try {
	    Thread.sleep(ABEngine.GAME_THREAD_FPS_SLEEP);
	    clear(gl);
	    draw(gl);

	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

    }

    private void moveRobots(GL10 gl) {

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

    public void movePlayers(GL10 gl) {

	Map<Character,Player> temp = new TreeMap<Character,Player>();
	temp.putAll(ABEngine.PLAYERS);

	for(Player p : temp.values()){


	    synchronized (p) {

		p.move(gl);

		if (p.getCounter() == 0)
		    p.changePlayerAction();

	    }
	}
    }

    public void movePlayer(GL10 gl) {
	ABEngine.PLAYER.move(gl);
    }

    public synchronized void drawMap(GL10 gl) {

	int x_max =  ABEngine.getMaxX();
	int y_max =  ABEngine.getMaxY();

	ABEngine.START_X = (ABEngine.start_x/0.05f) - x_max/2.f;
	ABEngine.START_Y = (ABEngine.start_y/0.05f) - y_max/2.f;


	//List<Player> t = new ArrayList<Player>();
	//t.addAll(ABEngine.PLAYERS.values());
	//t.add(ABEngine.PLAYER);

	List<String> rs = new ArrayList<String>();
	List<String> ps = new ArrayList<String>();

	for(int x=0; x < x_max; x++) {
	    for(int y=0; y < y_max; y++) {

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		//gl.glPushMatrix();
		gl.glScalef(.05f, .05f, 1f);

		gl.glTranslatef(ABEngine.START_X + x, ABEngine.START_Y + y, 0f);

		switch(ABEngine.getObject(x,y)) {

		case 'W':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.9375f, 0.0625f, 0f);
		    map.draw(gl);

		    break;

		case 'O':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.0625f, 0.0625f, 0f);
		    map.draw(gl); 

		    break;

		case '-':

		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 

		    break;

		case '1':

		    ps.add("1");
		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl);

		    if(ABEngine.FIRST_MAP_DRAW) {
			Player player = Player.create('1',x*100,y*100);
			player.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
			ABEngine.PLAYERS.put('1',player);
		    } else {

//			boolean in = false;
//			for(Player p : t){
//			    if(p.getID() == '1'){
//				in = true;
//			    }
//			}
//
//			if(!in){
//			    ABEngine.setObject(x,y,'-');
//			}
		    }

		    break;
		case '2':

		    ps.add("2");
		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 

		    if(ABEngine.FIRST_MAP_DRAW) {
			Player player = Player.create('2',x*100,y*100);
			player.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
			ABEngine.PLAYERS.put('2',player);
		    }else {

//			boolean in = false;
//			for(Player p : t){
//			    if(p.getID() == '2'){
//				in = true;
//			    }
//			}
//
//			if(!in){
//			    ABEngine.setObject(x,y,'-');
//			}
		    }

		    break;

		case '3':

		    ps.add("3");
		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl); 

		    if(ABEngine.FIRST_MAP_DRAW) {
			Player player = Player.create('3', x*100,y*100);
			player.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
			ABEngine.PLAYERS.put('3',player);
		    } else {

//			boolean in = false;
//			for(Player p : t){
//			    if(p.getID() == '3'){
//				in = true;
//			    }
//			}
//
//			if(!in){
//			    ABEngine.setObject(x,y,'-');
//			}
		    }

		    break;


		case 'R':
		    rs.add("R");
		    gl.glMatrixMode(GL10.GL_TEXTURE);
		    gl.glLoadIdentity();
		    gl.glTranslatef(0.3125f, 0.375f, 0f);
		    map.draw(gl);

		    if(ABEngine.FIRST_MAP_DRAW) {
			Robot robot = Robot.create(x*100, y*100);
			robot.loadTexture(gl, ABEngine.GAME_ROBOTS, ABEngine.context);
			ABEngine.ROBOTS.add(robot);


		    }

		    break;



		}


		//gl.glLoadIdentity();

	    }

	}
	//gl.glPopMatrix();



//	Log.w("map","TRUE SIZE Robots"+ABEngine.ROBOTS.size());
//	Log.w("map","SIZE Robots"+rs.size());
//	Log.w("map","SIZE Players"+ps.size());

	//Choose random player
	if (ABEngine.FIRST_MAP_DRAW){
	    int i = 1 + r.nextInt(ABEngine.PLAYERS.values().size());

	    char k = (char)(('0')+i);

	    ABEngine.PLAYER = ABEngine.PLAYERS.get(k);
	    ABEngine.PLAYERS.remove(k);
	    Log.w("map", ""+k);

	    //ABEngine.PLAYER =  Player.create('3', 4*100,4*100);
	}

	ABEngine.FIRST_MAP_DRAW = false;

    }

}