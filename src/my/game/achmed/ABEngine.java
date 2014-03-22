package my.game.achmed;

import java.util.Calendar;

import my.game.achmed.R;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;

public class ABEngine {


	public static final int GAME_THREAD_DELAY = 4000;

	public static final int SPLASH_SCREEN_MUSIC = R.raw.achmed;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;

	public static Context context;
	public static Thread musicThread;

	public static Thread timeThread;
	
	/*
	public void setTime() {
	    Calendar cal = Calendar.getInstance();
	    int minutes = cal.get(Calendar.MINUTE);

	    if (DateFormat.is24HourFormat(this)) {
	        int hours = cal.get(Calendar.HOUR_OF_DAY);
	        mClockView.setText((hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes));
	    }
	    else {
	        int hours = cal.get(Calendar.HOUR);
	        mClockView.setText(hours + ":" + (minutes < 10 ? "0" + minutes : minutes) + " " + new DateFormatSymbols().getAmPmStrings()[cal.get(Calendar.AM_PM)]);
	    }
	}
	*/

	public boolean onExit(View v) {
		try
		{
			Intent bgmusic = new Intent(context, ABMusic.class);
			context.stopService(bgmusic);
			
			//mandar abaixo a thread
			
			return true;
		} catch(Exception e){
			return false;
		}
	}

}
