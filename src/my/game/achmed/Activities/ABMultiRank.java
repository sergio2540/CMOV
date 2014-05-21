package my.game.achmed.Activities;

import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Database.SingleRankDataSource;
import my.game.achmed.Score.MultiPlayerScore;
import my.game.achmed.Score.MultiplayerRank;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup;

public class ABMultiRank extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getBundleExtra("MR");
		MultiplayerRank multiRanks = (MultiplayerRank) extras.getSerializable("MR");
		
		multiRanks.sortPlayersScore();
		
		final List<MultiPlayerScore> scores = multiRanks.getMultiplayerScoreList();

		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = inflater.inflate(R.layout.ab_single_rank, null);
		setContentView(vi);

		vi.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent mainMenu = new Intent(getApplicationContext(),
						ABMainMenu.class);
				mainMenu.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(mainMenu);
				finish();

			}
		}, 4000);

		TextView highscore = (TextView) findViewById(R.id.highscore);

		final Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");
		highscore.setTypeface(font);
		highscore.setText(R.string.highscore);
		highscore.setTextColor(Color.BLUE);

		ListView ranks = (ListView) findViewById(R.id.ranklist);

		ArrayAdapter<MultiPlayerScore> adapter = new ArrayAdapter<MultiPlayerScore>(this,
				android.R.layout.simple_list_item_1, scores) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				boolean highlight = scores.get(position).highlight;
				View view = super.getView(position, convertView, parent);
				TextView textview = (TextView) view;
				textview.setTypeface(font);
				if(highlight) {
					textview.setTextColor(Color.BLACK);
					textview.setBackgroundColor(Color.WHITE);
				}
				textview.setGravity(Gravity.CENTER);
				return textview;
			}

		};

		ranks.setAdapter(adapter);

	}

}

