package my.game.achmed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ABMainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.ab_main_menu);

	TextView textView_single;
	TextView textView_multiplayer;
	TextView textView_settings;
	TextView textView_about;
	final TextView textView_exit;
	
	Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

	textView_single =(TextView)findViewById(R.id.single);
	textView_multiplayer =(TextView)findViewById(R.id.multiplayer);
	textView_settings = (TextView)findViewById(R.id.settings);
	textView_about = (TextView)findViewById(R.id.about);
	textView_exit =(TextView)findViewById(R.id.exit);
	
	
	textView_single.setTypeface(font);
	textView_multiplayer.setTypeface(font);
	textView_settings.setTypeface(font);
	textView_about.setTypeface(font);
	textView_exit.setTypeface(font);
	
	textView_single.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
		
		Intent mainMenu= new Intent(ABMainMenu.this,
			ABGame.class);
			ABMainMenu.this.startActivity(mainMenu);
			ABMainMenu.this.finish();
			//overridePendingTransition(R.layout.fade_in,R.layout.fade_out);
		    // setText() sets the string value of the TextView
		    //textView_exit.setText("Clicked");
		
	    }
	});






	ABEngine.musicThread = new Thread(){
	    @Override
	    public void run(){

		Intent backGroundMusic = new
			Intent(getApplicationContext(), ABMusic.class);
		startService(backGroundMusic);
		ABEngine.context = getApplicationContext();

	    }
	};

	ABEngine.musicThread.start();

    }

}
