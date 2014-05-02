package my.game.achmed;

import my.game.achmed.Activities.ABLevelMenu;
import my.game.achmed.Activities.ABMainMenu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ABAboutMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ab_about_menu);
		
		TextView textView_Sergio;
		TextView textView_Pedro;
		TextView textView_Bruno;
		final TextView textView_exit;

		Typeface font=Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");

		textView_Sergio =(TextView)findViewById(R.id.dev_sergio);
		textView_Pedro =(TextView)findViewById(R.id.dev_pedro);
		textView_Bruno = (TextView)findViewById(R.id.dev_bruno);
		textView_exit =(TextView)findViewById(R.id.about_exit);


		textView_Sergio.setTypeface(font);
		textView_Pedro.setTypeface(font);
		textView_Bruno.setTypeface(font);
		textView_exit.setTypeface(font);

		textView_Sergio.setTextColor(Color.WHITE);
		textView_Pedro.setTextColor(Color.WHITE);
		textView_Bruno.setTextColor(Color.WHITE);
		textView_exit.setTextColor(Color.RED);
		
		textView_exit.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {

			Intent mainMenu = new Intent(getApplicationContext(),
				ABMainMenu.class);

			ABAboutMenu.this.startActivity(mainMenu);
			ABAboutMenu.this.finish();

		    }
		});

		
	}

}
