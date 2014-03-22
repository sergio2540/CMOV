package my.game.achmed;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
		
		
		/*
		ABEngine.timeThread = new Thread(){
		    @Override
		    public void run(){

			textView_time.setT

		    }
		};
		*/
		
		findViewById(R.id.dogeView).setOnTouchListener(new OnTouchListener(){
		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				float counter = 0;
				float steps = 5;
				float stepSize = 0;
				if(Float.compare(DogeView.x, event.getX()) == 0){
					stepSize = event.getY()/steps;
					while((counter+=stepSize) != event.getY())
					{
						Log.v("LOL","ON YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						DogeView.y = counter;
						findViewById(R.id.dogeView).invalidate();

					}
					return true;

				}
				else if (Float.compare(DogeView.y, event.getY()) == 0){
					stepSize = event.getX()/steps;
					while((counter+=stepSize) != event.getX())
					{
						Log.v("LOL","ON XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX!!!!!!!!!!!!!!!!!!!!!!!!!");
						DogeView.x = counter;
						findViewById(R.id.dogeView).invalidate();
					}
					return true;

				}

				return false;

				
			}
			
		});
		
	}

	
	
	
	
	public void downOnClick(View view){
		DogeView.y+=2;
		findViewById(R.id.dogeView).invalidate();
		
	}

}
