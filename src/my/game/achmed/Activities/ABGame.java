package my.game.achmed.Activities;

import java.text.SimpleDateFormat;
import java.util.Date;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.OpenGL.ABGameRenderer;
import my.game.achmed.OpenGL.ABGameSurfaceView;
import my.game.achmed.R.drawable;
import my.game.achmed.R.id;
import my.game.achmed.R.layout;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ABGame extends Activity {

    private ABGameSurfaceView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	Log.w("MYTAG", "onCreate ABGAME class");
	super.onCreate(savedInstanceState);
	setContentView(R.layout.ab_game);
	gameView = (ABGameSurfaceView)findViewById(R.id.game_frame);

	//TODO se calhar uma boa opcao de desenho e passar isto para o constructor de ABGameSurfaceView
	gameView.setRenderer(new ABGameRenderer());

	final ImageButton setaEsquerda = (ImageButton) findViewById(R.id.arrow_left);

	setaEsquerda.setOnTouchListener(new View.OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

		if(event.getAction() == (MotionEvent.ACTION_UP)){
		    Log.w("ma","x" + ABEngine.X_POSITION + "y" + ABEngine.X_POSITION);
		    //ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT_RELEASE;
		    ABEngine.STOP = true;
		    setaEsquerda.setImageResource(R.drawable.arrow_left_normal);

		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT;
			setaEsquerda.setImageResource(R.drawable.arrow_left);
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
		    Log.w("ma","x" + ABEngine.X_POSITION + "y" + ABEngine.X_POSITION);
		    //ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT_RELEASE;
		    ABEngine.STOP = true;
		    setaDireita.setImageResource(R.drawable.arrow_right_normal);

		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT;
			setaDireita.setImageResource(R.drawable.arrow_right);
		    }

		}
		return true;
	    }
	});

	final ImageButton setaCima = (ImageButton) findViewById(R.id.arrow_up);

	setaCima.setOnTouchListener(new View.OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

		if(event.getAction() == (MotionEvent.ACTION_UP)){
		    Log.w("ma","x" + ABEngine.X_POSITION + "y" + ABEngine.X_POSITION);
		    //ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP_RELEASE;
		    ABEngine.STOP = true;
		    setaCima.setImageResource(R.drawable.arrow_up_normal);


		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.STOPPED) {
			ABEngine.STOP = false;
			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP;
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
			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN;
			setaBaixo.setImageResource(R.drawable.arrow_down);
		    }

		}
		return true;
	    }
	});

	final ImageButton dot = (ImageButton) findViewById(R.id.dot);

	dot.setOnTouchListener(new View.OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

		if(event.getAction() == (MotionEvent.ACTION_UP)){

		    //ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;
		    dot.setImageResource(R.drawable.dot_normal);

		} else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    switch (ABEngine.PLAYER_ACTION) {

		    case ABEngine.PLAYER_UP:

			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_UP_RELEASE;

			break;

		    case ABEngine.PLAYER_DOWN:

			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_DOWN_RELEASE;

			break;

		    case ABEngine.PLAYER_LEFT:

			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_LEFT_RELEASE;

			break;

		    case ABEngine.PLAYER_RIGHT:

			ABEngine.PLAYER_ACTION = ABEngine.PLAYER_RIGHT_RELEASE;

			break;
		    }

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


	final ImageButton pause = (ImageButton) findViewById(R.id.pause);

	pause.setOnClickListener(new OnClickListener(){
	    int pauseOrPlay = R.drawable.pause;
	    @Override
	    public void onClick(View v) {
		ImageButton button = (ImageButton) findViewById(R.id.pause);

		if(pauseOrPlay ==  R.drawable.pause){
		    button.setImageResource(R.drawable.play);
		    this.pauseOrPlay = R.drawable.play;
		}
		else {
		    button.setImageResource(R.drawable.pause);
		    this.pauseOrPlay = R.drawable.pause;

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

    }

    @Override
    protected void onResume() {
	super.onResume();
	gameView.onResume();

    }


    @Override
    protected void onPause() {
	super.onPause();
	gameView.onPause();
    }
    
    @Override
    public void finish() {
	super.finish();
	gameView.onFinishTemporaryDetach();
    }

    @Override
    public void onBackPressed() {
	Intent ab_dialog = new Intent(getApplicationContext(), ABGameDialog.class);
	startActivity(ab_dialog);
	this.finish();
    }
}
