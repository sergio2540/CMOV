package my.game.achmed.Activities;

import java.util.List;

import my.game.achmed.R;
import my.game.achmed.R.id;
import my.game.achmed.R.layout;
import my.game.achmed.Database.SingleRankDataSource;
import my.game.achmed.Rank.Rank;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Build;

public class ABSingleRank extends Activity {

    SingleRankDataSource singleRankDataSource;

    /* passar na intent
     * 
     * passar na intent para actualizar primeiro o registo do nome com o novo rank
     * 
     * */
    int level = 1; 
    String name = "João";
    int score = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View vi = inflater.inflate(R.layout.ab_single_rank, null);
	setContentView(vi);

	vi.postDelayed(new Runnable() {

	    @Override
	    public void run() {
		Intent mainMenu = new Intent(getApplicationContext(),
			ABMainMenu.class);

		startActivity(mainMenu);
		finish();

	    }
	}, 4000);

	singleRankDataSource = new SingleRankDataSource(this);

	singleRankDataSource.open();

	List<Rank> singleRanks = singleRankDataSource.getSingleHighScoreTable(level);

	TextView highscore = (TextView) findViewById(R.id.highscore);

	final Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");
	highscore.setTypeface(font);
	highscore.setText(R.string.highscore);
	highscore.setTextColor(Color.BLUE);

	ListView ranks = (ListView) findViewById(R.id.ranklist);

	ArrayAdapter<Rank> adapter = new ArrayAdapter<Rank>(this,
		android.R.layout.simple_list_item_1, singleRanks) {

	    @Override public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		TextView textview = (TextView) view;
		textview.setTypeface(font);
		textview.setGravity(Gravity.CENTER);
		return textview;
	    }

	};

	ranks.setAdapter(adapter);

	singleRankDataSource.close();

    }

}

