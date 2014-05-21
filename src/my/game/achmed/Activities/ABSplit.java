package my.game.achmed.Activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Adapters.EnemiesAdapter;
import my.game.achmed.Adapters.SplitPeersAdapter;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.SPLIT_ACTION;
import my.game.achmed.Characters.Players.ABAchmed;
import my.game.achmed.Characters.Players.ABGreenOgre;
import my.game.achmed.Characters.Players.ABPurpleBat;
import my.game.achmed.Characters.Players.ABRedMermaid;
import my.game.achmed.State.Event;
import my.game.achmed.State.Peer;
import my.game.achmed.State.SplitState;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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

	//private volatile static List<Peer> peers;
	//private static SplitPeersAdapter splitPeersAdapter;
	private static EnemiesAdapter enemiesAdapter;
	//private static Map<Character, String> enemiesList;
	private ArrayList<String> enemiesList;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_absplit);

		this.initializeGroupPeersList();
//preciso de arranjar uma associacao playerid->nome da personagem
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

		List<Character> selectedPlayers = this.getPeersSelected(v);

		broadcastSplit(selectedPlayers);
		
		finish();
		
	}


	private void initializeGroupPeersList(){

		//splitPeersAdapter = new SplitPeersAdapter(this, R.id.splitDevList, peers);
		enemiesList = new ArrayList<String>();
		for(Entry<Character, Player> ent : ABEngine.ENEMIES.entrySet()){
			
			Character c = ent.getKey();
			if(c == '1'){
				enemiesList.add("Green Ogre");
				//p = new ABGreenOgre(x, y);
			} else if (c == '2'){
				enemiesList.add("Red Mermaid");
				//p = new ABRedMermaid(x, y);
			} else if(c == '3'){
				enemiesList.add("Achmed");

				//p = new ABAchmed(x,y);  
			} else {
				enemiesList.add("Purple Bat");
				//p = new ABPurpleBat(x,y); 
			}
		}
		enemiesAdapter = new EnemiesAdapter(this, R.id.splitDevList, enemiesList);

		ListView view = (ListView) findViewById(R.id.splitDevList);
		view.setAdapter(enemiesAdapter);

		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				//connect(position);
				//playersToCreateGroup.add(position);
				

			}
		});

	}


	public void broadcastSplit(List<Character> selectedPlayers) {

		

		///for(String player : selectedPlayers){

		//descobrir endereco do peer actual
		
		final WifiManager wifiManager = (WifiManager) ABEngine.context.getSystemService(Context.WIFI_SERVICE);
	    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		ABEngine.sendSplitAction(ABEngine.PLAYER.getID(), SPLIT_ACTION.SPLIT,connectionInfo.getMacAddress(), selectedPlayers);
		
		//ABEngine.mManager.removeGroup(ABEngine.mChannel, new ActionListener() {

		//	@Override
		//	public void onSuccess() {
				// TODO Auto-generated method stub

			//}

			//@Override
			//public void onFailure(int reason) {
				// TODO Auto-generated method stub

		//	}
		//});


		

	}

	public List<Character> getPeersSelected(View v) {

		List<Character> peersSelected = new ArrayList<Character>();


		ListView list = (ListView) findViewById(R.id.splitDevList);
		LinearLayout view;


		
		for(int i = 0; i < list.getCount(); i++) {
			view = (LinearLayout) list.getChildAt(i);

			CheckBox cBox = (CheckBox) view.findViewById(R.id.checkBox1);
			TextView txv = (TextView)view.findViewById(R.id.peerSplit);

			if(cBox.isChecked()) {
				
				 String c = (String) txv.getText();
				if(c.equals("Green Ogre")){
					peersSelected.add('1');
					//p = new ABGreenOgre(x, y);
				} else if (c.equals("Red Mermaid")){
					peersSelected.add('2');
					//p = new ABRedMermaid(x, y);
				} else if(c.equals("Achmed")){
					peersSelected.add('3');

					//p = new ABAchmed(x,y);  
				} else {
					peersSelected.add('4');
					//p = new ABPurpleBat(x,y); 
				}
				
				Log.w("peers", "" + peersSelected.get(i));
			}


		}

		return peersSelected;

	}


}
