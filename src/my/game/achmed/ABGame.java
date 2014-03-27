package my.game.achmed;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;


public class ABGame extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ab_game);
		
		
		//Colocar numa funcao
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

	
	
	
	
	public void downOnClick(View view){
		DogeView.y+=2;
		findViewById(R.id.dogeView).invalidate();
		
	}

}
