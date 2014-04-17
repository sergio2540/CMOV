package my.game.achmed;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ABGame extends Activity {

	private ABGameView gameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.w("MYTAG", "onCreate ABGAME class");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ab_game);
		gameView = (ABGameView)findViewById(R.id.game_frame);

		//TODO se calhar uma boa opcao de desenho e passar isto para o constructor de ABGameView
		gameView.setRenderer(new ABGameRenderer());

		final ImageButton setaEsquerda = (ImageButton) findViewById(R.id.arrow_left);

		setaEsquerda.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT_RELEASE;
					setaEsquerda.setImageResource(R.drawable.arrow_left_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

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

					ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT_RELEASE;
					setaDireita.setImageResource(R.drawable.arrow_right_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

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

					ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP_RELEASE;
					setaCima.setImageResource(R.drawable.arrow_up_normal);
					
				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

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

					ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;
					setaBaixo.setImageResource(R.drawable.arrow_down_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

					ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN;
					setaBaixo.setImageResource(R.drawable.arrow_down);

				}
				return true;
			}
		});
		
		
		final ImageButton bombButton = (ImageButton) findViewById(R.id.bomb_button);
		
		bombButton.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){
					/*
					createPalyer(Characters.)
					player.Down();
					*/

					//ABEngine.BOMB_ACTION = ABEngine.NO_BOMB;
					bombButton.setImageResource(R.drawable.bomb_button_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

					ABEngine.BOMB_ACTION = ABEngine.DROP_BOMB;
					bombButton.setImageResource(R.drawable.bomb_button_pressed);

				}
				return true;
			}
		});

		//TODO: Colocar numa funcao
		Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");
		
		TextView textView_score  =(TextView)findViewById(R.id.score);
		TextView textView_numPlayers =(TextView)findViewById(R.id.numPlayers);
		final TextView textView_time = (TextView)findViewById(R.id.time);
		TextView textView_namePlayer = (TextView)findViewById(R.id.namePlayer);
		
		textView_score.setTypeface(font);
		textView_numPlayers.setTypeface(font);
		textView_time.setTypeface(font);
		textView_namePlayer.setTypeface(font);

		//TODO: Colocar em ABEngine
		new CountDownTimer(ABEngine.GAME_DURATION, ABEngine.UPDATE_INTERVAL) {

			private final Date date = new Date();
			private final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");

			@Override
			public void onTick(long millisUntilFinished) {
				date.setTime(millisUntilFinished);
				((TextView)findViewById(R.id.time)).setText(formatter.format(date));
			}

			@Override
			public void onFinish() {
				date.setTime(0);
				((TextView)findViewById(R.id.time)).setText(formatter.format(date));
				((TextView)findViewById(R.id.time)).setText("Over!!");

			}
		}.start();

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

}
