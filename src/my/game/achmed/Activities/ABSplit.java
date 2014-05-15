package my.game.achmed.Activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Adapters.SplitPeersAdapter;
import my.game.achmed.State.Peer;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ABSplit extends Activity {

	private volatile static List<Peer> peers;
	private Set<Integer> playersToCreateGroup;
	private static SplitPeersAdapter splitPeersAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_absplit);

		playersToCreateGroup = new HashSet<Integer>();
		this.initializeGroupPeersList();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		TextView tView = (TextView) findViewById(R.id.devices);
		final Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Kraash Black.ttf");
		tView.setTypeface(font);
		tView.setText("Online Players");
		tView.setTextColor(Color.BLUE);
		tView.setTextSize(16);
		
		Button b = (Button) findViewById(R.id.split);
		b.setBackgroundColor(Color.DKGRAY);
		b.setTextColor(Color.WHITE);
		b.setTypeface(font);
		b.setTextSize(16);
		
	}

	public void splitGroup(View v){
		
		List<Peer> selectedPlayers = this.getPeersSelected(v);

		//connect(selectedPlayers);

	}


	private void initializeGroupPeersList(){

		splitPeersAdapter = new SplitPeersAdapter(this, R.id.splitDevList, peers);
		
		ListView view = (ListView) findViewById(R.id.splitDevList);
		view.setAdapter(splitPeersAdapter);

		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//connect(position);
				//playersToCreateGroup.add(position);

			}
		});
		
	}

	public static void setPeers(List<Peer> peers2) {

		peers = peers2;
		//splitPeersAdapter.notifyDataSetChanged();
	}


	public void connect(List<Peer> selectedPlayers) {

		for(Peer player : selectedPlayers){

			WifiP2pConfig config = new WifiP2pConfig();
			config.deviceAddress = player.getPeerAddress();
			config.groupOwnerIntent = 0;
//			ABEngine.mManager.connect(ABEngine.mChannel, config, new ActionListener(){
//				@Override
//				public void onFailure(int reason) {
//
//				}
//
//				@Override
//				public void onSuccess() {
//
//
//				}
//
//			});


		}

	}
	
	public List<Peer> getPeersSelected(View v) {
		
		List<Peer> peersSelected = new ArrayList<Peer>();
		
		
		ListView list = (ListView) findViewById(R.id.splitDevList);
		LinearLayout view;
		
		
		
		for(int i = 0; i < list.getCount(); i++) {
			view = (LinearLayout) list.getChildAt(i);
			
			CheckBox cBox = (CheckBox) view.findViewById(R.id.checkBox1);
			
			if(cBox.isChecked()) {
				peersSelected.add(splitPeersAdapter.getItem(i));
				Log.w("peers", "" + peersSelected.get(i));
			}
			
				
		}
		
		return peersSelected;
		
	}


}
