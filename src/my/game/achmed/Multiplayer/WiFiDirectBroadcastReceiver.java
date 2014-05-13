package my.game.achmed.Multiplayer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import my.game.achmed.Activities.ABGame;
import my.game.achmed.Activities.ABMultiplayer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private final WifiP2pManager mManager;
	private WifiP2pGroup mGroup;
	private final Channel mChannel;
	private final ABMultiplayer mActivity;

	private IncommingCommTask currentIncommingTask = null;
	private OutgoingCommTask currentOutgoingTask = null;
	
	public static ClientCom client;
	public static ServerCom server;

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel mChannel, ABMultiplayer activity) {

		super();
		this.mManager = manager;
		this.mChannel = mChannel;
		this.mActivity = activity;

		//currentIncommingTask = new IncommingCommTask();

		//currentIncommingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,R.string.port);

	}

	public IncommingCommTask getCurrentIncommingTask() {
		return currentIncommingTask;
	}


	public OutgoingCommTask getCurrentOutgoingTask() {
		return currentOutgoingTask;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

			// This action is triggered when the WDSim service changes state:
			// - creating the service generates the WIFI_P2P_STATE_ENABLED event
			// - destroying the service generates the WIFI_P2P_STATE_DISABLED event

			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				Toast.makeText(mActivity, "WiFi Direct enabled",
						Toast.LENGTH_SHORT).show();



			} else {
				Toast.makeText(mActivity, "WiFi Direct disabled",
						Toast.LENGTH_SHORT).show();
			}

		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

			WifiP2pDeviceList p2plist = (WifiP2pDeviceList) intent.getParcelableExtra(
					WifiP2pManager.EXTRA_P2P_DEVICE_LIST);


			// Request available peers from the wifi p2p manager. This is an
			// asynchronous call and the calling activity is notified with a
			// callback on PeerListListener.onPeersAvailable()



			if (mManager != null) {

				mManager.requestGroupInfo(mChannel, new GroupInfoListener() {

					@Override
					public void onGroupInfoAvailable(WifiP2pGroup group) {
						if (mGroup == null){

							mGroup = group;
						}
						else {

							//verifica quem saiu
							getWhoLeft(group);

						}
					}

					private void getWhoLeft(WifiP2pGroup group) {

						if(group == null) {
							Toast.makeText(mActivity, "grupo a null", Toast.LENGTH_LONG).show();
						}

						//			if(group.getClientList() == null) {
						//			    Toast.makeText(mActivity, "clientes a null", Toast.LENGTH_LONG).show();
						//			    return;
						//			}

						List<WifiP2pDevice> whoLeft = new ArrayList<WifiP2pDevice>();

						String list = "[";

						if(group != null){
							for (WifiP2pDevice g : group.getClientList()){
								String n = g.deviceName; 
								list += n + ", ";
							}
						}

						list += "]";

						//Toast.makeText(mActivity,list, Toast.LENGTH_LONG).show();


						whoLeft.addAll(mGroup.getClientList());

						if(group != null){
							whoLeft.removeAll(group.getClientList());
						}

						mGroup = group;
						list = "::";
						for(WifiP2pDevice dev : whoLeft){

							list += dev.deviceName + " -";


							//
							//			    if(!ReceiveCommTask.characters.isEmpty()){
							//				char c = ReceiveCommTask.characters.get();
							//				ABEngine.ENEMIES.remove(c);
							//			    }		

						}

						Toast.makeText(mActivity, list,
								Toast.LENGTH_LONG).show();

						//eliminar do jogo


					}
				});



				mManager.requestPeers(mChannel, new PeerListListener() {

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peerList) {


						// Out with the old, in with the new.


						ABMultiplayer.peers.clear();

						boolean haveGO = false;

						for (WifiP2pDevice dev : peerList.getDeviceList()){

							if(dev.isGroupOwner()){
								haveGO = true;
								ABMultiplayer.peers.add(new Peer(dev.deviceName, dev.deviceAddress));
							}

							Toast.makeText(mActivity, "found device " + dev.deviceAddress,
									Toast.LENGTH_SHORT).show();
						}


						//Nao existem grupos formados mostra todos
//									if (!haveGO){
//						
//									    for (WifiP2pDevice dev : peerList.getDeviceList()){
//										ABMultiplayer.peers.add(new Peer(dev.deviceName, dev.deviceAddress));    
//									    }
//						
//									}

						ABMultiplayer.peersAd.notifyDataSetChanged();

						// If an AdapterView is backed by this data, notify it
						// of the change.  For instance, if you have a ListView of available
						// peers, trigger an update.
						//((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();


					}
				});

			}

		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

			WifiP2pInfo ginfo = (WifiP2pInfo) intent.getParcelableExtra(
					WifiP2pManager.EXTRA_WIFI_P2P_INFO);


			NetworkInfo ninfo = (NetworkInfo) intent.getParcelableExtra(
					WifiP2pManager.EXTRA_NETWORK_INFO);

			if (ninfo.isConnected()){



				mManager.requestConnectionInfo(mChannel, new ConnectionInfoListener() {

					@Override
					public void onConnectionInfoAvailable(WifiP2pInfo info) {
						InetAddress goAddress = info.groupOwnerAddress;

						Toast.makeText(mActivity, "onConnectionInfoAvailable.", Toast.LENGTH_LONG).show();

						if(info.groupFormed && info.isGroupOwner){
							//temos de verificar se somos owners e ja estamos num grupo.
							//We start the server on this peer.
							Toast.makeText(mActivity, "Group owner.", Toast.LENGTH_LONG).show();
							if(server == null)
							server = new ServerCom();
							if(client == null)
							client = new ClientCom(goAddress.getHostAddress());

						}
						else if (info.groupFormed) {
							
							if(client == null)
							client = new ClientCom(goAddress.getHostAddress());

							//			    if(currentIncommingTask.getStatus() == Status.RUNNING || currentIncommingTask.getStatus() == Status.PENDING)
							//currentIncommingTask.cancel(true);

							//			    if (currentOutgoingTask.getStatus() == Status.RUNNING || currentOutgoingTask.getStatus() == Status.PENDING)
							//				currentOutgoingTask.cancel(true);

							//currentOutgoingTask = new OutgoingCommTask();
							//currentOutgoingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, goAddress.getHostAddress());


							Toast.makeText(mActivity, "Group formed.", Toast.LENGTH_LONG).show();

						}

					} 

				}
						);
			}
			//	    WifiP2pGroup gp2p = (WifiP2pGroup) intent.getParcelableExtra(
			//		    WifiP2pManager.EXTRA_WIFI_P2P_GROUP);



			//ginfo.print();
			boolean isFormedGroup = ginfo.groupFormed;
			Toast.makeText(mActivity, "Network membership changed ",
					Toast.LENGTH_SHORT).show();

		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			//
			//	    WifiP2pInfo ginfo = (WifiP2pInfo) intent.getSerializableExtra(
			//		    WifiP2pManager.EXTRA_WIFI_P2P_INFO);
			//	    
			//	    WifiP2pDevice dinfo = (WifiP2pDevice) intent.getSerializableExtra(
			//		    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
			//
			//	    //ginfo.print();
			//	    Toast.makeText(mActivity, dinfo.deviceName + ":" + (ginfo.isGroupOwner ? "GO" : "NOT GO!"),Toast.LENGTH_LONG).show();/////novaaaaa
			//
			//	    ChatRoom.gos.add(dinfo.deviceName);
			//	    ChatRoom.adapter.notifyDataSetChanged(); 
			//	    
			Toast.makeText(mActivity, "Group ownership changed",
					Toast.LENGTH_SHORT).show();

		}
	}
}
