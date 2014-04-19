package my.game.achmed.Activities;

import my.game.achmed.R;
import my.game.achmed.R.layout;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class ABGameDialog extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

	Log.w("MYTAG", "onCreate ABGAME class");
	super.onCreate(savedInstanceState);

	Dialog dialog = new Dialog(this);

	LinearLayout backgroundImg = (LinearLayout) ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ab_game_dialog, null);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
	dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	backgroundImg.setBackgroundColor(Color.TRANSPARENT);
	dialog.setContentView(backgroundImg);

	dialog.show();

    }

    @Override
    protected void onResume() {
	super.onResume();


    }


    @Override
    protected void onPause() {
	super.onPause();
    }

    
    public void onClickResume(View v){
	
	Intent ab_game = new Intent(getApplicationContext(), ABGame.class);
	startActivity(ab_game);
	this.finish();
    
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