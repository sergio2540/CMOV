package my.game.achmed.Activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.OpenGL.ABGameRenderer;
import my.game.achmed.OpenGL.ABGameSurfaceView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ABGame extends Activity {

	private ABGameSurfaceView gameView;

	Dialog backPopUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.ab_game);

		//BACK POP UP

		backPopUp = new Dialog(this);
		LinearLayout backgroundImg = (LinearLayout) ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ab_game_dialog, null);
		backPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		backPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		backgroundImg.setBackgroundColor(Color.TRANSPARENT);
		backPopUp.setContentView(backgroundImg);

		//!BACK POP UP

		gameView = (ABGameSurfaceView)findViewById(R.id.game_frame);

		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			
			if (ABEngine.PLAYER != null)
			ABEngine.PLAYER.setAction(CHARACTER_ACTION.LEFT);
			
			setaEsquerda.setImageResource(R.drawable.arrow_left);
		    }

		final ImageButton setaEsquerda = (ImageButton) findViewById(R.id.arrow_left);

		setaEsquerda.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					ABEngine.STOP = true;
					setaEsquerda.setImageResource(R.drawable.arrow_left_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			
			if (ABEngine.PLAYER != null)
			ABEngine.PLAYER.setAction(CHARACTER_ACTION.RIGHT);
			
			setaDireita.setImageResource(R.drawable.arrow_right);
		    }

				}
				return true;
			}
		});

		final ImageButton setaDireita = (ImageButton) findViewById(R.id.arrow_right);

		setaDireita.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					ABEngine.STOP = true;
					setaDireita.setImageResource(R.drawable.arrow_right_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			
			if (ABEngine.PLAYER != null)
			ABEngine.PLAYER.setAction(CHARACTER_ACTION.UP);
			
			setaCima.setImageResource(R.drawable.arrow_up);
		    }
		}
		return true;
	    }
	});

				}
				return true;
			}
		});

		final ImageButton setaCima = (ImageButton) findViewById(R.id.arrow_up);

		setaCima.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					ABEngine.STOP = true;
					setaCima.setImageResource(R.drawable.arrow_up_normal);


		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			
			if (ABEngine.PLAYER != null)
			    ABEngine.PLAYER.setAction(CHARACTER_ACTION.DOWN);
			
			setaBaixo.setImageResource(R.drawable.arrow_down);
		    }

					if(ABEngine.STOPPED) {
						ABEngine.STOP = false;
						ABEngine.PLAYER.setAction(CHARACTER_ACTION.UP);
						setaCima.setImageResource(R.drawable.arrow_up);
					}
				}
				return true;
			}
		});


		final ImageButton setaBaixo = (ImageButton) findViewById(R.id.arrow_down);

		setaBaixo.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					//ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;
					ABEngine.STOP = true;
					setaBaixo.setImageResource(R.drawable.arrow_down_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

					if(ABEngine.STOPPED) {
						ABEngine.STOP = false;

						ABEngine.PLAYER.setAction(CHARACTER_ACTION.DOWN);

						setaBaixo.setImageResource(R.drawable.arrow_down);
					}

				}
				return true;
			}
		});

		final ImageButton dot = (ImageButton) findViewById(R.id.dot);

		dot.setOnTouchListener(new View.OnTouchListener() {

		   // ABEngine.BOMB_ACTION = ABEngine.DROP_BOMB;
		    
		    if (ABEngine.PLAYER != null)
			ABEngine.PLAYER.getBomb().drop();
		    
		    bombButton.setImageResource(R.drawable.bomb_button_pressed);

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					//ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;
					dot.setImageResource(R.drawable.dot_normal);

				} else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

					dot.setImageResource(R.drawable.dot);

				}
				return true;
			}		
		});


		final ImageButton bombButton = (ImageButton) findViewById(R.id.bomb_button);

		bombButton.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction() == (MotionEvent.ACTION_UP)){

					//ABEngine.BOMB_ACTION = ABEngine.NO_BOMB;
					bombButton.setImageResource(R.drawable.bomb_button_normal);

				}
				else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

					// ABEngine.BOMB_ACTION = ABEngine.DROP_BOMB;

					ABEngine.PLAYER.getBomb().drop();

					bombButton.setImageResource(R.drawable.bomb_button_pressed);

				}
				return true;
			}
		});


		final ImageButton pause = (ImageButton) findViewById(R.id.pause);

		pause.setOnClickListener(new OnClickListener(){
			int pauseOrPlay = R.drawable.pause;
			@Override
			public void onClick(View v) {
				ImageButton button = (ImageButton) findViewById(R.id.pause);

				if(pauseOrPlay ==  R.drawable.pause){
					button.setImageResource(R.drawable.play);
					this.pauseOrPlay = R.drawable.play;
					onPause();

				}
				else {
					button.setImageResource(R.drawable.pause);
					this.pauseOrPlay = R.drawable.pause;
					onResume();
				}

			}

		});

		//TODO: Colocar numa funcao
		Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

		TextView textView_score  =(TextView)findViewById(R.id.score);
		TextView textView_numPlayers =(TextView)findViewById(R.id.numPlayers);
		final TextView textView_time = (TextView)findViewById(R.id.time);
		//TextView textView_namePlayer = (TextView)findViewById(R.id.namePlayer);

		textView_score.setTypeface(font);
		textView_numPlayers.setTypeface(font);
		textView_time.setTypeface(font);
		//textView_namePlayer.setTypeface(font);

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

		ABEngine.GAME = ABGame.this;

	}

	@Override
	protected void onResume() {
		super.onResume();
		gameView.onResume();
	}


	@Override
	protected void onPause() {
		Log.w("pause","paused");
		super.onPause();
		gameView.onPause();
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onBackPressed() {
		onPause();
		backPopUp.show();
	}

	public void onClickResume(View v){
		onResume();
		backPopUp.cancel();
	}

	public void onClickReload(View v){
		Intent ab_game = new Intent(getApplicationContext(), ABGame.class);
		startActivity(ab_game);
		this.finish();
	}

	public void onClickQuit(View v){
		Intent ab_main = new Intent(getApplicationContext(), ABMainMenu.class);
		startActivity(ab_main);
		this.finish();
	}
}
