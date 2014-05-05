package my.game.achmed.Activities;

import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.Level;
import my.game.achmed.R;
import my.game.achmed.Multiplayer.IncommingCommTask;
import my.game.achmed.Multiplayer.Peer;
import my.game.achmed.Multiplayer.WiFiDirectBroadcastReceiver;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;




public class ABMultiplayer extends Activity {

    private WifiP2pManager mManager;

    private Channel mChannel;

    private WiFiDirectBroadcastReceiver mReceiver;

    private IntentFilter mIntentFilter;

    private boolean mBound;
    
    public static List<Peer> peers;
    public static ArrayAdapter<Peer> peersAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.ab_multiplayer);

	
	    //Set up multiplayer
	    mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    mChannel = mManager.initialize(this, getMainLooper(), null);
	    mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

	    // register broadcast receiver
	    mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

	    registerReceiver(mReceiver, mIntentFilter);
	    mBound = true;



	    mManager.discoverPeers(mChannel, new ActionListener() {

		@Override
		public void onSuccess() {
		   
		    //new IncommingCommTask().execute(R.string.service_port);


		}

		@Override
		public void onFailure(int reason) {
		    // TODO Auto-generated method stub

		}
	    });

	    peers = new ArrayList<Peer>();
	    peersAd = new ArrayAdapter<Peer>(this, android.R.layout.simple_list_item_1, peers); 
	    //peersAd = new ArrayAdapter<String>(this, R., textViewResourceId);
	    ListView view = (ListView) findViewById(R.id.deviceslist);
	    view.setAdapter(peersAd);
	    peersAd.setNotifyOnChange(true);

	    view.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		    WifiP2pConfig config = new WifiP2pConfig();
		    config.deviceAddress = peersAd.getItem(position).getPeerAddress();
		    config.groupOwnerIntent = 0;
		    
		    mManager.connect(mChannel, config, new ActionListener(){

			@Override
			public void onFailure(int arg0) {
			    // TODO Auto-generated method stub

			}

			@Override
			public void onSuccess() {
			    
			    Intent mainMenu = new Intent(getApplicationContext(), ABGame.class);
			    ABMultiplayer.this.startActivity(mainMenu);
			    ABMultiplayer.this.finish();

			}});
		}
	    });
	    
	    Button createNewGame  = (Button) findViewById(R.id.createNewGame);
	    
	    
	    createNewGame.setOnClickListener(new OnClickListener() {
	        
	        @Override
	        public void onClick(View v) {
	            Intent levelMenu = new Intent(getApplicationContext(), ABLevelMenu.class);
		    ABMultiplayer.this.startActivity(levelMenu);
		    ABMultiplayer.this.finish();
	    	
	        }
	    });
	}
    
}
