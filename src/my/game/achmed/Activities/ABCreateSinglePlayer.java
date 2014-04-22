package my.game.achmed.Activities;

import my.game.achmed.R;
import my.game.achmed.Database.SingleRankDataSource;
import my.game.achmed.R.layout;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class ABCreateSinglePlayer extends Activity {

	SingleRankDataSource singleRankDataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ab_create_single_player);
		
		singleRankDataSource = new SingleRankDataSource(this);
		
		final EditText playerName = (EditText) findViewById(R.id.playerName);
		final Button createPlayer = (Button) findViewById(R.id.createPlayerName);
		
		createPlayer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String name = playerName.getText().toString();
				if(name == "") {
					return;
				}
				
				singleRankDataSource.open();
				boolean playerAdded = singleRankDataSource.createPlayer(name);
				singleRankDataSource.close();
				
				if(playerAdded) {
					
					Intent intent = new Intent();
					
					
				} else {
					
					Toast.makeText(getApplicationContext(), "Nickname already taken... ", Toast.LENGTH_SHORT).show();
					
				}
				
			}
			
		});

	}
}
