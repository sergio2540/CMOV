package my.game.achmed;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class ABGameRenderer implements Renderer {

	private final ABMap map = new ABMap();
	private final ABAchmedPlayer achmed = new ABAchmedPlayer();
	private final ABBomb bomb = new ABBomb();
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.w("MYTAG", "onSurfaceChanged");
		
		float ratio = (float) width / height;
		
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glDepthRangef(1f, 0f);
		gl.glViewport(0, 0, width, height);
		
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, 1.0f, 0.0f, 1.0f, 10.0f, -10.0f);
		
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

		//não trocar a ordem das linhas 
		drawMap(gl, ABEngine.game_map);
		dropBomb(gl);
		moveAchmed(gl);
		
	}

	private void dropBomb(GL10 gl) {

		switch (ABEngine.BOMB_ACTION) {

		case ABEngine.NO_BOMB:
			
			break;
			
		case ABEngine.DROP_BOMB:
			
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.07f, .07f, 1f);
			gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.3f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.250f, 0.835f , 0f);
			bomb.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;
			
		}
		
	}

	public void moveAchmed(GL10 gl) {

		//Player.Create(ENUM.BLACKSKIN);

		switch (ABEngine.PLAYER_ACTION) {

		case ABEngine.PLAYER_LEFT : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			if(ABEngine.X_POSITION > 0) {

				ABEngine.X_POSITION -= ABEngine.ACHMED_SPEED;
				gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.581f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		case ABEngine.PLAYER_RIGHT : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			if(ABEngine.X_POSITION < 20) {
				ABEngine.X_POSITION += ABEngine.ACHMED_SPEED;
				gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.083f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		case ABEngine.PLAYER_UP :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			if(ABEngine.Y_POSITION < 20) {

				ABEngine.Y_POSITION += ABEngine.ACHMED_SPEED;
				gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.332f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		case ABEngine.PLAYER_DOWN : 

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			if(ABEngine.Y_POSITION > 0) {

				ABEngine.Y_POSITION -= ABEngine.ACHMED_SPEED;
				gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

			}

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.83f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();


			break;

		case ABEngine.PLAYER_LEFT_RELEASE :

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(.05f, .05f, 1f);

			gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

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

			gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

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

			gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

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

			gl.glTranslatef(ABEngine.X_POSITION, ABEngine.Y_POSITION, 0.5f);

			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glScalef(1f, 1f, 1f);
			gl.glTranslatef(0.83f, 0.875f - 0.125f, 0f);

			achmed.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();

			break;

		}

	}

	public void drawMap(GL10 gl, char[][] game_matrix) {

		float tileLocX = 0f;
		float tileLocY = 0f;

		for(int x=0; x<20; x++) {
			for(int y=0; y<20; y++) {

				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glScalef(.05f, .05f, 1f);
				gl.glTranslatef(tileLocX, tileLocY, 0f);

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
					gl.glTranslatef(0.3125f, 0.6875f, 0f);
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
					
				}
				
				tileLocY += 1;
			}

			tileLocX += 1;
			tileLocY = 0;
		}
	}
	
}
