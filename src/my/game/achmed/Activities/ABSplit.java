package my.game.achmed.Activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.R.id;
import my.game.achmed.R.layout;
import my.game.achmed.R.menu;
import my.game.achmed.State.Peer;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ABSplit extends Activity {

	private static List<Peer> peers;
	private Set<Integer> playersToCreateGroup;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_absplit);

		//peers = new ArrayList<Peer>();

		playersToCreateGroup = new HashSet<Integer>();


		initializeGroupPeersList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.absplit, menu);
		return true;
	}

	public void splitGroup(View v){

		connect();

	}


	private void initializeGroupPeersList(){

		//peersAd = new ArrayAdapter<String>(this, R., textViewResourceId);
		int index = 0;
		ListView view = (ListView) findViewById(R.id.splitDevList);
		for(Peer p : peers){
			CheckBox box = new CheckBox(ABEngine.context);
			box.setText(p.getPeerName());
			view.addView(box, index);
			
			index++;

		}


		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//connect(position);
				playersToCreateGroup.add(position);

			}
		});


	}

	public static void setPeers(List<Peer> peers2) {

		peers = peers2;
	}


	public void connect(){

		for(Integer player : playersToCreateGroup){

			WifiP2pConfig config = new WifiP2pConfig();
			config.deviceAddress = peers.get(player).getPeerAddress();
			config.groupOwnerIntent = 0;
			ABEngine.mManager.connect(ABEngine.mChannel, config, new ActionListener(){
				@Override
				public void onFailure(int arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess() {


				}

			});


		}



	}


}
