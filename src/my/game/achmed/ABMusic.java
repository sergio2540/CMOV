package my.game.achmed;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ABMusic extends Service {

	public static boolean isRunning = false;
	MediaPlayer player;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		setMusicOptions(this, ABEngine.LOOP_BACKGROUND_MUSIC, ABEngine.R_VOLUME, ABEngine.L_VOLUME,
				ABEngine.SPLASH_SCREEN_MUSIC);

	}

	public void setMusicOptions(Context context, boolean isLooped, int rVolume,
			int lVolume, int soundFile){
		player = MediaPlayer.create(context, soundFile);
		player.setLooping(isLooped);
		player.setVolume(rVolume,lVolume);

	}

	@Override
	public void onDestroy() {
		player.stop();
		player.release();
	}

	@Override
	public void onLowMemory() {
		player.stop();
	}
	
	public void stop() {
		player.stop();
	}
	
	public void start() {
		player.start();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		try
		{
			player.start();
			isRunning = true;
		} catch(Exception e){
			isRunning = false;
			player.stop();
		}

		return 1;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		player.start();
	}

	public IBinder onUnBind(Intent intent) {
		return null;
	}

}
