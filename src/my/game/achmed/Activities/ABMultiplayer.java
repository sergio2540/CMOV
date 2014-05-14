package my.game.achmed.Activities;

import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.Level;
import my.game.achmed.R;
import my.game.achmed.Events.LoadingEvent;
import my.game.achmed.Listeners.OnLoadingEventListener;
import my.game.achmed.Multiplayer.p2p.WiFiDirectBroadcastReceiver;
import my.game.achmed.State.Peer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ReceiverCallNotAllowedException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;




public class ABMultiplayer extends Activity {

	private WifiP2pManager mManager;

	private Channel mChannel;

	private WiFiDirectBroadcastReceiver mReceiver;

	private IntentFilter mIntentFilter;

	public Dialog waitPopUp;

	public static List<Peer> peers;
	public static ArrayAdapter<Peer> peersAd;


	private boolean mBound = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ab_multiplayer);

		waitPopUp = new Dialog(this);

		FrameLayout wait_xml = (FrameLayout) ((LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.ab_wait_dialog, null);
		waitPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		waitPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.BLACK));
		wait_xml.setBackgroundColor(Color.BLACK);
		waitPopUp.setContentView(wait_xml);

		//Set up multiplayer
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);

		//mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

		// register broadcast receiver
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		//registerReceiver(mReceiver, mIntentFilter);


		ABEngine.loadingEvent.addOnEventListener(new OnLoadingEventListener() {
			@Override
			public void onLoadingEvent(boolean loaded) {
				waitPopUp.cancel();
				//Toast.makeText(ABEngine.context, "on loading event", Toast.LENGTH_SHORT).show();

				Intent abGame = new Intent(getApplicationContext(), ABGame.class);
				ABMultiplayer.this.startActivity(abGame);
				//ABMultiplayer.this.finish();
			}

		});



		peers = new ArrayList<Peer>();
		peersAd = new ArrayAdapter<Peer>(this, android.R.layout.simple_list_item_1, peers); 

		Button createNewGame  = (Button) findViewById(R.id.createNewGame);


		createNewGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//cria grupo
				mManager.createGroup(mChannel, new ActionListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub

					}


					@Override
					public void onFailure(int reason) {
						// TODO Auto-generated method stub

					}

				});


				Intent levelMenu = new Intent(getApplicationContext(), ABLevelMenu.class);
				ABMultiplayer.this.startActivity(levelMenu);
			}
		});




	}

	@Override
	public void onResume(){
		super.onResume();
		if(!mBound){

			mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
			registerReceiver(mReceiver, mIntentFilter);
			mBound = true;

			mManager.removeGroup(mChannel, new ActionListener() {

				@Override
				public void onSuccess() {
					findPeers();
				}

				@Override
				public void onFailure(int reason) {
					findPeers();

				}
			});

		}
	}

	@Override
	public void onPause(){
		super.onPause();
		//unregisterReceiver(mReceiver);
	}

	@Override
	public void onStop(){
		super.onStop();
		//unregisterReceiver(mReceiver);
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Toast.makeText(ABMultiplayer.this, "onDestroy.", Toast.LENGTH_LONG).show();

		if(mBound){

			mManager.removeGroup(mChannel, new ActionListener() {

				@Override
				public void onSuccess() {
					Toast.makeText(ABMultiplayer.this, "remove.", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onFailure(int reason) {
					Toast.makeText(ABMultiplayer.this, "remove.", Toast.LENGTH_LONG).show();

				}
			});


//			if( mReceiver.getCurrentIncommingTask() != null && mReceiver.getCurrentIncommingTask().getStatus() == Status.RUNNING)
//				mReceiver.getCurrentIncommingTask().cancel(true);
//
//			if( mReceiver.getCurrentOutgoingTask() != null && mReceiver.getCurrentOutgoingTask().getStatus() == Status.RUNNING)
//				mReceiver.getCurrentOutgoingTask().cancel(true);

			unregisterReceiver(mReceiver);

		}

	}

	public void findPeers(){

		mManager.discoverPeers(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {

				//new IncommingCommTask().execute(R.string.service_port);

				peersList();


			}

			@Override
			public void onFailure(int reason) {
				// TODO Auto-generated method stub

			}
		});


	}


	public void peersList(){

		//peersAd = new ArrayAdapter<String>(this, R., textViewResourceId);
		ListView view = (ListView) findViewById(R.id.deviceslist);
		view.setAdapter(peersAd);
		peersAd.setNotifyOnChange(true);

		view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				connect(position);
			}
		});


	}


	public void connect(int position){

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

				waitPopUp.show();

			}

		});
	}

}