package my.game.achmed.Activities;

import java.util.List;

import javax.xml.datatype.Duration;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Database.SingleRankDataSource;
import my.game.achmed.R.layout;
import my.game.achmed.Rank.Rank;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
		final ListView playerNamesList = (ListView) findViewById(R.id.playerslist);


		singleRankDataSource.open();
		List<String> playerNamesDb = singleRankDataSource.getPlayerNamesList();
		singleRankDataSource.close();

		final Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, playerNamesDb) {

			@Override public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView textview = (TextView) view;
				textview.setTypeface(font);
				textview.setGravity(Gravity.CENTER);
				return textview;
			}

		};

		playerNamesList.setAdapter(adapter);

		playerNamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ABEngine.PLAYER_NICK = 
						(String) playerNamesList.getItemAtPosition(position);
				
				Log.w("NICK", ABEngine.PLAYER_NICK + " " + "click da lista");

				Intent mainMenu = new Intent(getApplicationContext(), ABMainMenu.class);

				ABCreateSinglePlayer.this.startActivity(mainMenu);
				ABCreateSinglePlayer.this.finish();

			}
		});    


		createPlayer.setBackgroundColor(Color.DKGRAY);
		createPlayer.setTextColor(Color.WHITE);
		
		createPlayer.setTypeface(font);
		createPlayer.setTextSize(16);

		createPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String name = playerName.getText().toString().trim();
				int numChars = name.length();

				if(numChars < 3 || numChars > 10) {
					Toast.makeText(getApplicationContext(), "Please insert a nick with 3 to 10 characters :)", Toast.LENGTH_SHORT).show();
				} else {

					singleRankDataSource.open();
					boolean playerAdded = singleRankDataSource.createPlayer(name);
					singleRankDataSource.close();

					if(playerAdded) {
						ABEngine.PLAYER_NICK = name;
						Log.w("NICK", ABEngine.PLAYER_NICK + " " + "click da lista");
						Intent mainMenu = new Intent(getApplicationContext(), ABMainMenu.class);
						ABCreateSinglePlayer.this.startActivity(mainMenu);
						ABCreateSinglePlayer.this.finish();

					} else {

						Toast.makeText(getApplicationContext(), "Nickname already taken... ", Toast.LENGTH_SHORT).show();

					}
				}

			}

		});

	}
}
