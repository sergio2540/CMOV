package my.game.achmed.Activities;

import my.game.achmed.ABEngine;
import my.game.achmed.Level;
import my.game.achmed.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;




public class ABLevelMenu extends Activity {

    private AsyncTask<Integer, Boolean, Boolean> loadLevel; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.ab_level_menu);

	Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

	TextView textView_level1 = (TextView)findViewById(R.id.level1);
	TextView textView_level2 = (TextView)findViewById(R.id.level2);
	TextView textView_level3 = (TextView)findViewById(R.id.level3);
	TextView textView_back = (TextView)findViewById(R.id.back);

	textView_level1.setTypeface(font);
	textView_level2.setTypeface(font);
	textView_level3.setTypeface(font);
	textView_back.setTypeface(font);

	textView_level1.setTextColor(Color.WHITE);
	textView_level2.setTextColor(Color.WHITE);
	textView_level3.setTextColor(Color.WHITE);
	textView_back.setTextColor(Color.RED);


	textView_level1.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {

		if(loadLevel.getStatus()== AsyncTask.Status.RUNNING){
		    loadLevel.cancel(true);
		}

		loadLevel.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,1);
	    }
	});

	textView_level2.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {

		if(loadLevel.getStatus()== AsyncTask.Status.RUNNING){
		    loadLevel.cancel(true);
		}

		loadLevel.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,2);
	    }
	});

	textView_level3.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {

		if(loadLevel.getStatus()== AsyncTask.Status.RUNNING){
		    loadLevel.cancel(true);
		}

		loadLevel.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,3);
	    }
	});

	textView_back.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {

		if(ABEngine.isOnMultiplayer){
		    Intent mainMenu = new Intent(getApplicationContext(),
			    ABMultiplayer.class);
		    ABLevelMenu.this.startActivity(mainMenu);
		    ABLevelMenu.this.finish();
		}else{
		    Intent mainMenu = new Intent(getApplicationContext(),
			    ABMainMenu.class);  
		    ABLevelMenu.this.startActivity(mainMenu);
		    ABLevelMenu.this.finish();
		}



	    }
	});

    }



    @Override
    public void onResume(){
	super.onResume();

	loadLevel = new AsyncTask<Integer, Boolean, Boolean>() {

	    @Override
	    protected void onPostExecute(Boolean levelLoaded) {

		if(levelLoaded) {
		    Intent game = new Intent(getApplicationContext(),
			    ABGame.class);
		    ABLevelMenu.this.startActivity(game);
		    //ABLevelMenu.this.finish();

		} else {
		    Intent mainMenu = new Intent(getApplicationContext(),
			    ABMainMenu.class);
		    ABLevelMenu.this.startActivity(mainMenu);
		    //Como avancou para tras o levelMenu deve terminar
		    ABLevelMenu.this.finish();
		}

	    }


	    @Override
	    protected Boolean doInBackground(Integer... num) {
		Level level = Level.load("level" + num[0].intValue());
		if(level == null){
		    return false;
		}

		ABEngine.LEVEL = level;
		ABEngine.create_map(level.getGameLevelMatrix());
		ABEngine.PLAYER = null;

		return true;
	    }

	};

    }

    @Override
    public void onPause(){
	super.onPause();
	//activity deixa de estar visivel pq esta no jogo
	if(loadLevel.getStatus()== AsyncTask.Status.RUNNING){
	    loadLevel.cancel(true);
	}
    }

    @Override
    public void onDestroy(){
	super.onDestroy();
    }
}