package my.game.achmed;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView.FindListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class ABGame extends Activity {

	private ABGameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.w("MYTAG", "onCreate ABGAME class");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ab_game);
		
		gameView = 
	             (ABGameView)findViewById(R.id.game_frame);
		
		//TODO se calhar uma boa opcao de desenho e passar isto para o constructor de ABGameView
		gameView.setRenderer(new ABGameRenderer());

		
		final ImageButton setaEsquerda = (ImageButton) findViewById(R.id.arrow_left);

		setaEsquerda.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {

	            if(event.getAction() == (MotionEvent.ACTION_UP)){
	                //Do whatever you want after press
	            	Log.w("TOUCH", "seta esquerda largada");
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT_RELEASE;
	            	setaEsquerda.setImageResource(R.drawable.arrow_left_normal);
	            	
	            	
	            }
	            else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
	                //Do whatever you want during press
	            	Log.w("TOUCH", "seta esquerda pressionada");
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT;
	            	setaEsquerda.setImageResource(R.drawable.arrow_left);
	            	
	            }
	            return true;
	        }
	    });
	    
	    final ImageButton setaDireita = (ImageButton) findViewById(R.id.arrow_right);

	    setaDireita.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {

	            if(event.getAction() == (MotionEvent.ACTION_UP)){
	                //Do whatever you want after press
	            	Log.w("TOUCH", "seta direita largada");
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT_RELEASE;
	            	setaDireita.setImageResource(R.drawable.arrow_right_normal);
	            	
	            }
	            else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
	                //Do whatever you want during press
	            	Log.w("TOUCH", "seta direita pressionada");
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT;
	            	setaDireita.setImageResource(R.drawable.arrow_right);
	            	
	            }
	            return true;
	        }
	    });
	    
	    final ImageButton setaCima = (ImageButton) findViewById(R.id.arrow_up);

	    setaCima.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {

	            if(event.getAction() == (MotionEvent.ACTION_UP)){
	                //Do whatever you want after press
	            	Log.w("TOUCH", "seta cima largada");
	            	
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP_RELEASE;
	            	
	            	setaCima.setImageResource(R.drawable.arrow_up_normal);
	            	
	            }
	            else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
	                //Do whatever you want during press
	            	Log.w("TOUCH", "seta cima pressionada");
	            	
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP;
	            	
	            	setaCima.setImageResource(R.drawable.arrow_up);
	            	
	            }
	            return true;
	        }
	    });
	    
	    
	    final ImageButton setaBaixo = (ImageButton) findViewById(R.id.arrow_down);

	    setaBaixo.setOnTouchListener(new View.OnTouchListener() {

	        @Override
	        public boolean onTouch(View v, MotionEvent event) {

	            if(event.getAction() == (MotionEvent.ACTION_UP)){
	                //Do whatever you want after press
	            	Log.w("TOUCH", "seta baixo largada");
	            	
	              	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;
	            	
	            	setaBaixo.setImageResource(R.drawable.arrow_down_normal);
	            	
	            }
	            else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {
	                //Do whatever you want during press
	            	Log.w("TOUCH", "seta baixo pressionada");
	            	
	            	ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN;
	            	
	            	setaBaixo.setImageResource(R.drawable.arrow_down);
	            	
	            }
	            return true;
	        }
	    });
	    
		
		
//		setContentView(R.layout.ab_game);

		//Colocar numa funcao
		//		Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");
		//
		//		TextView textView_score  =(TextView)findViewById(R.id.score);
		//		TextView textView_numPlayers =(TextView)findViewById(R.id.numPlayers);
		//
		//		final TextView textView_time = (TextView)findViewById(R.id.time);
		//
		//		TextView textView_namePlayer = (TextView)findViewById(R.id.namePlayer);
		//
		//
		//		textView_score.setTypeface(font);
		//		textView_numPlayers.setTypeface(font);
		//		textView_time.setTypeface(font);
		//		textView_namePlayer.setTypeface(font);



	}

	//	@Override
	//	protected void onResume() {
	//		super.onResume();
	//		gameView.onResume();
	//		
	//	}
	//	@Override
	//	protected void onPause() {
	//		super.onPause();
	//		gameView.onPause();
	//	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();
		
		Log.w("MYTAG2", "event.getX(): " + x + "           evetn.getY(): " + y);
		Log.w("MYTAG2", "height: " + ABEngine.displayHeight + "      display width" + ABEngine.displayWidth); 
	
		int height = ABEngine.displayHeight / 4;
		int playableArea = ABEngine.displayHeight - height;

		if (y > playableArea){
			switch (event.getAction()){

			case MotionEvent.ACTION_DOWN:

				if(x < ABEngine.displayWidth / 2) {
					
					ABEngine.playerFlightAction =
							ABEngine.PLAYER_BANK_LEFT_1;

				} else{
					
					ABEngine.playerFlightAction =
							ABEngine.PLAYER_BANK_RIGHT_1;
				}
				break;

			case MotionEvent.ACTION_UP:
				ABEngine.playerFlightAction = ABEngine.PLAYER_RELEASE;
				break;

			}
		}
		return false;
	}
	
	
	
	
	
	
}
