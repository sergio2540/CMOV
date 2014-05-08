package my.game.achmed.Activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import my.game.achmed.ABEngine;
import my.game.achmed.ABMusic;
import my.game.achmed.R;
import my.game.achmed.Characters.BOMB_ACTION;
import my.game.achmed.Characters.CHARACTER_ACTION;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;
import my.game.achmed.Database.SingleRankDataSource;
import my.game.achmed.Multiplayer.Peer;
import my.game.achmed.Multiplayer.WiFiDirectBroadcastReceiver;
import my.game.achmed.OpenGL.ABGameRenderer;
import my.game.achmed.OpenGL.ABGameSurfaceView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ABGame extends Activity {

    private ABGameSurfaceView gameView;

    private Dialog backPopUp;
    private Dialog wonOrLostPopUp;

    private CountDownTimer c;

    //private long millisUntilFinished;

    SingleRankDataSource singleRankDataSource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.ab_game);


	singleRankDataSource = new SingleRankDataSource(this);



	backPopUp = new Dialog(this);

	LinearLayout backgroundImg = (LinearLayout) ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ab_game_dialog, null);
	backPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	backPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	backgroundImg.setBackgroundColor(Color.TRANSPARENT);
	backPopUp.setContentView(backgroundImg);

	wonOrLostPopUp = new Dialog(this){
	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev) {
		showRanks();
		return super.dispatchTouchEvent(ev);
	    }
	};

	LinearLayout backgroundImg2 = (LinearLayout) ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ab_wonorlost_popup, null);
	wonOrLostPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	wonOrLostPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	backgroundImg2.setBackgroundColor(Color.TRANSPARENT);
	wonOrLostPopUp.setContentView(backgroundImg2);


	gameView = (ABGameSurfaceView)findViewById(R.id.game_frame);
	//TODO se calhar uma boa opcao de desenho e passar isto para o constructor de ABGameSurfaceView
	gameView.setRenderer(new ABGameRenderer());


	//ABEngine.PLAYER = null;
	//ABEngine.PLAYERS = new TreeMap<Character,Player>();
	//ABEngine.ROBOTS = new ArrayList<Robot>();

	//if(ABEngine.LEVEL != null)

	ABEngine.create_map(ABEngine.LEVEL.getGameLevelMatrix());

	//else {
	//ABEngine.create_map(ABEngine.LEVE.getGameLevelMatrix());
	//}


	final ImageButton setaEsquerda = (ImageButton) findViewById(R.id.arrow_left);

	setaEsquerda.setOnTouchListener(new View.OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {

		if(event.getAction() == (MotionEvent.ACTION_UP)){

		    ABEngine.PLAYER.STOP = true;
		    setaEsquerda.setImageResource(R.drawable.arrow_left_normal);

		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.PLAYER.STOPPED) {
			ABEngine.PLAYER.STOP = false;

			if (ABEngine.PLAYER != null)
			    ABEngine.PLAYER.setAction(CHARACTER_ACTION.LEFT);

			setaEsquerda.setImageResource(R.drawable.arrow_left);
		    }else {
		    	ABEngine.PLAYER.setAction(CHARACTER_ACTION.LEFT);
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

		    ABEngine.PLAYER.STOP = true;
		    setaDireita.setImageResource(R.drawable.arrow_right_normal);

		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

			if(ABEngine.PLAYER.STOPPED) {
			ABEngine.PLAYER.STOP = false;

			if (ABEngine.PLAYER != null)
			    ABEngine.PLAYER.setAction(CHARACTER_ACTION.RIGHT);

			setaDireita.setImageResource(R.drawable.arrow_right);
		    }else {
		    	ABEngine.PLAYER.setAction(CHARACTER_ACTION.RIGHT);
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

		    ABEngine.PLAYER.STOP = true;
		    setaCima.setImageResource(R.drawable.arrow_up_normal);


		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.PLAYER.STOPPED) {
			ABEngine.PLAYER.STOP = false;

			if (ABEngine.PLAYER != null)
			    ABEngine.PLAYER.setAction(CHARACTER_ACTION.UP);

			setaCima.setImageResource(R.drawable.arrow_up);
		    }else {
		    	ABEngine.PLAYER.setAction(CHARACTER_ACTION.UP);
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
		    ABEngine.PLAYER.STOP = true;
		    setaBaixo.setImageResource(R.drawable.arrow_down_normal);

		}
		else if(event.getAction() == (MotionEvent.ACTION_DOWN)) {

		    if(ABEngine.PLAYER.STOPPED) {
			ABEngine.PLAYER.STOP = false;

			if (ABEngine.PLAYER != null)
			    ABEngine.PLAYER.setAction(CHARACTER_ACTION.DOWN);
			setaBaixo.setImageResource(R.drawable.arrow_down);
		    } else {
		    	ABEngine.PLAYER.setAction(CHARACTER_ACTION.DOWN);
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

		    if (ABEngine.PLAYER != null) {
			ABEngine.PLAYER.getBomb().drop();
			ABEngine.sendDropBombAction(ABEngine.PLAYER.getID(), BOMB_ACTION.BOMB_DROP);
		    }

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
		    if(!ABEngine.isOnMultiplayer) {
		    	c.cancel();
		    }
		    onPause();
		}
		else {
		    button.setImageResource(R.drawable.pause);
		    this.pauseOrPlay = R.drawable.pause;
		    c = counter((long)ABEngine.MILLIS_UNTIL_FINISHED);
		    c.start();
		    onResume();


		}

	    }

	});







	//TODO: Colocar numa funcao
	Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

	TextView textView_score  =(TextView)findViewById(R.id.score);
	//TextView textView_numPlayers =(TextView)findViewById(R.id.numPlayers);
	final TextView textView_time = (TextView)findViewById(R.id.time);
	TextView textView_namePlayer = (TextView)findViewById(R.id.namePlayer);

	textView_score.setTypeface(font);
	//textView_numPlayers.setTypeface(font);
	textView_time.setTypeface(font);
	textView_namePlayer.setTypeface(font);

	textView_namePlayer.setText(ABEngine.PLAYER_NICK);

	ABEngine.MILLIS_UNTIL_FINISHED = Math.round(ABEngine.LEVEL.getGameDurationInSeconds()*1000);

	//millisUntilFinished = Math.round(60*1000);
	c = counter((long)ABEngine.MILLIS_UNTIL_FINISHED);
	c.start();

	ABEngine.GAME = ABGame.this;
	Toast.makeText(getApplicationContext(), "onCreate Done", Toast.LENGTH_LONG);

    }


    private CountDownTimer counter(long gd) {
	return new CountDownTimer(gd, 1000) {

	    private final Date date = new Date();
	    private final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");

	    @Override
	    public void onTick(long ms) {
		date.setTime(ms);
		ABEngine.MILLIS_UNTIL_FINISHED = ms;
		((TextView)findViewById(R.id.time)).setText(formatter.format(date));
	    }

	    @Override
	    public void onFinish() {
		date.setTime(0);
		((TextView)findViewById(R.id.time)).setText(formatter.format(date));

		//TODO: nao devia ser assim!!
		if(ABEngine.PLAYER.isDead()) {
		    wonOrLost(false);
		} else {
		    wonOrLost(true);
		}


	    }
	};
    }

    @Override
    protected void onResume() {
	Log.w("resume","paused");

	super.onResume();
	gameView.onResume();

	if(ABEngine.GAME_MUSIC_SOUND){
	    Intent backMusic = new Intent(getApplicationContext(), ABMusic.class);
	    startService(backMusic);
	}

    }


    @Override
    protected void onPause() {
	Log.w("pause","paused");


	super.onPause();
	gameView.onPause();

	Intent backMusic = new Intent(getApplicationContext(), ABMusic.class);
	stopService(backMusic);

    }

    @Override
    public void finish() {
	super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
	if(keyCode == KeyEvent.KEYCODE_BACK)
	{

	    c.cancel();
	    onPause();

	    backPopUp.takeKeyEvents(false);
	    backPopUp.show();

	    Log.d("Test", "Back button pressed!");
	}
	else if(keyCode == KeyEvent.KEYCODE_HOME)
	{

	    Log.d("Test", "Home button pressed!");
	}
	return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
	super.onStart();
	//	c = counter(millisUntilFinished);
	//	c.start();
	//	onResume();
    }

    @Override
    protected void onStop() {
	super.onStop();
	c.cancel();
	onPause();

    }

    public void onClickResume(View v){
	c = counter((long)ABEngine.MILLIS_UNTIL_FINISHED);
	c.start();
	onResume();
	backPopUp.cancel();
    }

    public void onClickReload(View v){

	//Intent ab_game = new Intent(getApplicationContext(), ABGame.class);
	//startActivity(ab_game);
	//this.finish();

	ABEngine.PLAYER = null;
	ABEngine.PLAYERS = new TreeMap<Character,Player>();
	ABEngine.ROBOTS = new ArrayList<Robot>();

	ABEngine.create_map(ABEngine.LEVEL.getGameLevelMatrix());

	c = counter(Math.round(ABEngine.LEVEL.getGameDurationInSeconds()*1000));
	c.start();

	onResume();
	backPopUp.cancel();
    }

    public void wonOrLost(boolean result) {

	Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

	if(result) {

	    TextView textView = (TextView) wonOrLostPopUp.findViewById(R.id.wonorlost);
	    textView.setTypeface(font);
	    textView.setTextColor(Color.GREEN);
	    textView.setText("YOU WON !!!");

	} else {

	    TextView textView = (TextView) wonOrLostPopUp.findViewById(R.id.wonorlost);
	    textView.setTypeface(font);
	    textView.setTextColor(Color.RED);
	    textView.setText("YOU LOST !!!");

	}

	wonOrLostPopUp.show();

	//	Timer timer = new Timer();
	//
	//	timer.schedule(
	//
	//		new TimerTask() {
	//		    @Override
	//		    public void run() {
	//			//			    	wonOrLostPopUp.cancel();
	//
	//			Intent highscore = new Intent(getApplicationContext(),
	//				ABSingleRank.class);
	//
	//			ABGame.this.startActivity(highscore);
	//			ABGame.this.finish();
	//		    }
	//
	//		}, 4000);

    }



    public void showRanks() {

	String level = ABEngine.LEVEL.getLevelName();
	float score = ABEngine.PLAYER.getScore();
	String name = ABEngine.PLAYER_NICK;

	singleRankDataSource.open();
	singleRankDataSource.updatePlayerScore(name, level, score);
	singleRankDataSource.close();

	Intent highscore = new Intent(getApplicationContext(),
		ABSingleRank.class);

	ABGame.this.startActivity(highscore);
	ABGame.this.finish();
    }


    public void onClickQuit(View v) {
	Intent ab_main = new Intent(getApplicationContext(), ABMainMenu.class);
	startActivity(ab_main);
	this.finish();

    }

}
