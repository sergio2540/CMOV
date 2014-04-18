package my.game.achmed;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class ABGameRenderer implements Renderer {

	private final ABMap map = new ABMap();
	private final ABAchmedPlayer achmed = new ABAchmedPlayer();
	private final ABBomb bomb = new ABBomb();
	private final ABFire fire = new ABFire();
	private final ABRobot robot = new ABRobot();

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.w("MYTAG", "onSurfaceChanged");

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
		Log.w("MYTAG", "onSurfaceCreate");

		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glEnable(GL10.GL_ALPHA_TEST);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		//TEXTURES

		map.loadTexture(gl, ABEngine.GAME_MAP, ABEngine.context);
		achmed.loadTexture(gl, ABEngine.GAME_PLAYER, ABEngine.context);
		bomb.loadTexture(gl, ABEngine.GAME_BOMB, ABEngine.context);
		fire.loadTexture(gl, ABEngine.GAME_FIRE, ABEngine.context);
		robot.loadTexture(gl, ABEngine.GAME_ROBOTS, ABEngine.context);


	}

	@Override
	public void onDrawFrame(GL10 gl) {
		Log.w("MYTAG", "onDrawFrame");
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

		//n�o trocar a ordem das linhas 
		drawMap(gl, ABEngine.game_map);
		dropBomb(gl);
		moveAchmed(gl, ABEngine.game_map);
		explodeBomb(gl, ABEngine.game_map);
		moveGreenRobot(gl, ABEngine.game_map);


	}

	private void moveGreenRobot(GL10 gl, char[][] game_map) {

	    	//Esta morto nao faz nada
	    	if(ABEngine.ROBOT_IS_DEAD[1])
	    	    return;
	    	
	    	float x = ABEngine.START_X;
	    	float y = ABEngine.START_Y;
	    
	    	
		switch (ABEngine.GREEN_ROBOT_ACTION) {
		
		case ABEngine.PLAYER_LEFT : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			float pos = ABEngine.GREEN_ROBOT_X - (100.f - ABEngine.ROBOT_SPEED * ABEngine.ROBOT_COUNTER);
			
			if(!ABEngine.detectColision(pos, ABEngine.GREEN_ROBOT_Y,ABEngine.GREEN_ROBOT_ACTION)) {
			    
				ABEngine.GREEN_ROBOT_X -= ABEngine.ROBOT_SPEED;
				
				x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;

				gl.glTranslatef(x,y, 0.2f);

			} else {
				
			    	x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;
				
				gl.glTranslatef(x,y, 0.2f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.08333f, 0.625f, 0f);

			robot.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			
			ABEngine.ROBOT_COUNTER = (ABEngine.ROBOT_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ROBOT_COUNTER == 0) {
				
				ABEngine.ROBOT_COUNTER = 0;
				robot.changeRobotAction();
			}

			break;

		case ABEngine.PLAYER_RIGHT : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			pos = ABEngine.GREEN_ROBOT_X + (100.f - ABEngine.ROBOT_SPEED * ABEngine.ROBOT_COUNTER);;
			
			
			if(!ABEngine.detectColision(pos, ABEngine.GREEN_ROBOT_Y, ABEngine.GREEN_ROBOT_ACTION)) {
			    
				ABEngine.GREEN_ROBOT_X += ABEngine.ROBOT_SPEED;
				
				x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;
				
				gl.glTranslatef(x,y, 0.5f);

			} else {
				
			    x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;
				gl.glTranslatef(x,y, 0.5f);

			}



			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.08333f, 0.75f, 0f);

			robot.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			
			ABEngine.ROBOT_COUNTER = (ABEngine.ROBOT_COUNTER + 1) % ABEngine.MAX_COUNTER;
			
			if(ABEngine.ROBOT_COUNTER == 0) {
				
				ABEngine.ROBOT_COUNTER = 0;
				robot.changeRobotAction();
			}

			break;

		case ABEngine.PLAYER_UP :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);


			pos = ABEngine.GREEN_ROBOT_Y + (100.f - ABEngine.ROBOT_SPEED * ABEngine.ROBOT_COUNTER);
			
			
			if(!ABEngine.detectColision(ABEngine.GREEN_ROBOT_X, pos,ABEngine.GREEN_ROBOT_ACTION)) {

				ABEngine.GREEN_ROBOT_Y += ABEngine.ROBOT_SPEED;
				x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;
				
				gl.glTranslatef(x,y, 0.5f);

			} else {
				
			    	x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;
				
				gl.glTranslatef(x,y, 0.5f);

			}


			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.08333f, 0.875f, 0f);

			robot.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			
			
			ABEngine.ROBOT_COUNTER = (ABEngine.ROBOT_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ROBOT_COUNTER == 0) {
				
				ABEngine.ROBOT_COUNTER = 0;
				robot.changeRobotAction();
			}

			break;

		case ABEngine.PLAYER_DOWN : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			pos = ABEngine.GREEN_ROBOT_Y - (100.f - ABEngine.ROBOT_SPEED * ABEngine.ROBOT_COUNTER);
			
			
			if(!ABEngine.detectColision(ABEngine.GREEN_ROBOT_X, pos,ABEngine.GREEN_ROBOT_ACTION)) {

				ABEngine.GREEN_ROBOT_Y -= ABEngine.ROBOT_SPEED;
				
				x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;

				gl.glTranslatef(x,y, 0.5f);

			} else {
				
			    x +=  ABEngine.GREEN_ROBOT_X/100f;
				y +=  ABEngine.GREEN_ROBOT_Y/100f;
				
				gl.glTranslatef(x,y, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.08333f, 0.5f, 0f);

			robot.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
			
			ABEngine.ROBOT_COUNTER = (ABEngine.ROBOT_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ROBOT_COUNTER == 0) {
				
				ABEngine.ROBOT_COUNTER = 0;
				robot.changeRobotAction();
			}


			break;

//		case ABEngine.PLAYER_LEFT_RELEASE :
//
//			gl.glMatrixMode(GL10.GL_MODELVIEW);
//			gl.glLoadIdentity();
//			gl.glPushMatrix();
//			gl.glScalef(.05f, .05f, 1f);
//			x +=  ABEngine.GREEN_ROBOT_X/100f;
//			y +=  ABEngine.GREEN_ROBOT_Y/100f;
//			gl.glTranslatef(x,y, 0.5f);
//
//			gl.glMatrixMode(GL10.GL_TEXTURE);
//			gl.glLoadIdentity();
//			gl.glScalef(1f, 1f, 1f);
//			gl.glTranslatef(0.08333f, 0.625f, 0f);
//
//			robot.draw(gl);
//			gl.glPopMatrix();
//			gl.glLoadIdentity();
//
//			break; 
//
//		case ABEngine.PLAYER_RIGHT_RELEASE : 
//
//			gl.glMatrixMode(GL10.GL_MODELVIEW);
//			gl.glLoadIdentity();
//			gl.glPushMatrix();
//			gl.glScalef(.05f, .05f, 1f);
//			x +=  ABEngine.GREEN_ROBOT_X/100f;
//			y +=  ABEngine.GREEN_ROBOT_Y/100f;
//
//			gl.glTranslatef(x,y, 0.5f);
//
//			gl.glMatrixMode(GL10.GL_TEXTURE);
//			gl.glLoadIdentity();
//			gl.glScalef(1f, 1f, 1f);
//			gl.glTranslatef(0.08333f, 0.75f, 0f);
//
//			robot.draw(gl);
//			gl.glPopMatrix();
//			gl.glLoadIdentity();
//
//			break;
//
//		case ABEngine.PLAYER_UP_RELEASE :
//
//			gl.glMatrixMode(GL10.GL_MODELVIEW);
//			gl.glLoadIdentity();
//			gl.glPushMatrix();
//			gl.glScalef(.05f, .05f, 1f);
//			x +=  ABEngine.GREEN_ROBOT_X/100f;
//			y +=  ABEngine.GREEN_ROBOT_Y/100f;
//
//			gl.glTranslatef(x,y, 0.5f);
//
//			gl.glMatrixMode(GL10.GL_TEXTURE);
//			gl.glLoadIdentity();
//			gl.glScalef(1f, 1f, 1f);
//			gl.glTranslatef(0.08333f, 0.875f, 0f);
//
//			robot.draw(gl);
//			gl.glPopMatrix();
//			gl.glLoadIdentity();
//
//			break;
//
//		case ABEngine.PLAYER_DOWN_RELEASE :
//
//			gl.glMatrixMode(GL10.GL_MODELVIEW);
//			gl.glLoadIdentity();
//			gl.glPushMatrix();
//			gl.glScalef(.05f, .05f, 1f);
//			
//			
//			x +=  ABEngine.GREEN_ROBOT_X/100f;
//			y +=  ABEngine.GREEN_ROBOT_Y/100f;
//			gl.glTranslatef(x,y, 0.5f);
//
//			gl.glMatrixMode(GL10.GL_TEXTURE);
//			gl.glLoadIdentity();
//			gl.glScalef(1f, 1f, 1f);
//			gl.glTranslatef(0.08333f, 0.5f, 0f);
//
//			robot.draw(gl);
//			gl.glPopMatrix();
//			gl.glLoadIdentity();
//
//			break;
			
			
			

		}
		
		//Altera posicao do robot
		int mtx_x = ABEngine.getXMatrixPosition(ABEngine.GREEN_ROBOT_X,ABEngine.GREEN_ROBOT_ACTION);
		int mtx_y = ABEngine.getYMatrixPosition(ABEngine.GREEN_ROBOT_Y,ABEngine.GREEN_ROBOT_ACTION);
		ABEngine.setObject(mtx_x,mtx_y,'R');

	}

	private void dropBomb(GL10 gl) {
	    
	    
	    	float x = ABEngine.START_X;
	    	float y = ABEngine.START_Y;

		switch (ABEngine.BOMB_ACTION) {

		case ABEngine.NO_BOMB:

			ABEngine.BOMB_DROPPED = false;
			break;

		case ABEngine.DROP_BOMB:

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			if(!ABEngine.BOMB_DROPPED) {
				bomb.x_position = ABEngine.X_POSITION;
				bomb.y_position = ABEngine.Y_POSITION;
			}
			
			
			
			x += bomb.x_position/100f;
			y += bomb.y_position/100f;
			
			
			
			//Log.w("BOMB", "TRUE: x" + x + "y" + y);
			gl.glTranslatef(x,y, 0.3f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.250f, 0.835f , 0f);
			bomb.draw(gl);

			//o timer depende do nivel
			bomb.setTimerToBombExplosion(ABEngine.BOMB_TIME_TO_EXPLOSION);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			ABEngine.BOMB_DROPPED = true;


			break;

		}

	}

	public void moveAchmed(GL10 gl, char[][] game_matrix) {

		//Player.Create(ENUM.BLACKSKIN);
	    
	    	//if(ABEngine.PLAYER_IS_DEAD[1])
	    	    //return;
	    	
	    	float x = ABEngine.START_X;
	    	float y = ABEngine.START_Y;
	    	
		switch (ABEngine.PLAYER_ACTION) {

		case ABEngine.PLAYER_LEFT : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			float pos = ABEngine.X_POSITION - (100.f - ABEngine.ACHMED_SPEED * ABEngine.ACHMED_COUNTER);
			
			if(!ABEngine.detectColision(pos, ABEngine.Y_POSITION,ABEngine.PLAYER_ACTION)) {
			   
				ABEngine.X_POSITION -= ABEngine.ACHMED_SPEED;
				x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;
				gl.glTranslatef(x, y, 0.5f);

			} else {
			    	x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;
				gl.glTranslatef(x,y, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.581f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			ABEngine.ACHMED_COUNTER = (ABEngine.ACHMED_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ACHMED_COUNTER == 0 && ABEngine.STOP) {
				ABEngine.STOPPED = true;
				ABEngine.ACHMED_COUNTER = 0;
				ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT_RELEASE;
			}



			break;

		case ABEngine.PLAYER_RIGHT : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);
			
			pos = ABEngine.X_POSITION + (100.f - ABEngine.ACHMED_SPEED * ABEngine.ACHMED_COUNTER);

			if(!ABEngine.detectColision(pos, ABEngine.Y_POSITION,ABEngine.PLAYER_ACTION)) {

				ABEngine.X_POSITION += ABEngine.ACHMED_SPEED;
				
				x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;

				gl.glTranslatef(x, y, 0.5f);

			} else {
			    
			    	x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;

				gl.glTranslatef(x,y, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.083f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			ABEngine.ACHMED_COUNTER = (ABEngine.ACHMED_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ACHMED_COUNTER == 0 && ABEngine.STOP) {
				ABEngine.STOPPED = true;
				ABEngine.ACHMED_COUNTER = 0;
				ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT_RELEASE;
			}


			break;

		case ABEngine.PLAYER_UP :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);


			pos = ABEngine.Y_POSITION + (100f - ABEngine.ACHMED_SPEED * ABEngine.ACHMED_COUNTER);

			if(!ABEngine.detectColision(ABEngine.X_POSITION, pos,ABEngine.PLAYER_ACTION)) {
			   
				ABEngine.Y_POSITION += ABEngine.ACHMED_SPEED;
				
				x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;
				
				gl.glTranslatef(x, y, 0.5f);

			} else {
			    
			    	x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;
				gl.glTranslatef(x, y, 0.5f);

			}


			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.332f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			ABEngine.ACHMED_COUNTER = (ABEngine.ACHMED_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ACHMED_COUNTER == 0 && ABEngine.STOP) {
				ABEngine.STOPPED = true;
				ABEngine.ACHMED_COUNTER = 0;
				ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP_RELEASE;
			}

			break;

		case ABEngine.PLAYER_DOWN : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			pos = ABEngine.Y_POSITION - (100f - ABEngine.ACHMED_SPEED * ABEngine.ACHMED_COUNTER);

			if(!ABEngine.detectColision(ABEngine.X_POSITION, pos, ABEngine.PLAYER_ACTION)) {
			   
				ABEngine.Y_POSITION -= ABEngine.ACHMED_SPEED;
				
				x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;

				gl.glTranslatef(x,y, 0.5f);

			} else {
			    
			    	x +=  ABEngine.X_POSITION/100f;
				y +=  ABEngine.Y_POSITION/100f;


				gl.glTranslatef(x,y, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.83f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			ABEngine.ACHMED_COUNTER = (ABEngine.ACHMED_COUNTER + 1) % ABEngine.MAX_COUNTER;

			if(ABEngine.ACHMED_COUNTER == 0 && ABEngine.STOP) {
				ABEngine.STOPPED = true;
				ABEngine.ACHMED_COUNTER = 0;
				ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;
			}


			break;

		case ABEngine.PLAYER_LEFT_RELEASE :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);
			
			x +=  ABEngine.X_POSITION/100f;
			y +=  ABEngine.Y_POSITION/100f;

			gl.glTranslatef(x,y, 0.5f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.581f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break; 

		case ABEngine.PLAYER_RIGHT_RELEASE : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);
			x +=  ABEngine.X_POSITION/100f;
			y +=  ABEngine.Y_POSITION/100f;
			gl.glTranslatef(x, y, 0.5f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.083f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		case ABEngine.PLAYER_UP_RELEASE :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);
			x +=  ABEngine.X_POSITION/100f;
			y +=  ABEngine.Y_POSITION/100f;
			gl.glTranslatef(x, y, 0.5f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.332f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		case ABEngine.PLAYER_DOWN_RELEASE :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);
			
			x +=  ABEngine.X_POSITION/100f;
			y +=  ABEngine.Y_POSITION/100f;

			gl.glTranslatef(x,y, 0.5f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.83f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		}
		
		//Altera posicao do player
		int mtx_x = ABEngine.getXMatrixPosition(ABEngine.X_POSITION,ABEngine.PLAYER_ACTION);
		int mtx_y = ABEngine.getYMatrixPosition(ABEngine.Y_POSITION,ABEngine.PLAYER_ACTION);
		
		
		ABEngine.setObject(mtx_x,mtx_y,'1');

	}

	public void drawMap(GL10 gl, char[][] game_matrix) {

	    	ABEngine.START_X = (ABEngine.start_x/0.05f) - game_matrix[0].length/2.f;
		ABEngine.START_Y = (ABEngine.start_y/0.05f) - game_matrix.length/2.f;
		
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
						ABEngine.X_POSITION = x*100;
						ABEngine.Y_POSITION = y*100;
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
						ABEngine.GREEN_ROBOT_X = x*100;
						ABEngine.GREEN_ROBOT_Y = y*100;
						robot.changeRobotAction();
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
