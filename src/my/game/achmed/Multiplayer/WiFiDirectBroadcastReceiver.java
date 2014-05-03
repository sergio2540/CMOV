package my.game.achmed.Multiplayer;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import my.game.achmed.Activities.ABGame;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

	private final WifiP2pManager mManager;
	private final Channel mChannel;
	private final ABGame mActivity;
	private List<WifiP2pDevice> peers;
	private IncommingCommTask currentIncommingTask = null;
	private OutgoingCommTask currentOutgoingTask = null;

	public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel mChannel, ABGame activity) {

		super();
		this.mManager = manager;
		this.mChannel = mChannel;
		this.mActivity = activity;
		this.peers = new ArrayList<WifiP2pDevice>();
	}
	
	public List<WifiP2pDevice> getPeers(){
		
		return peers;
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


			mManager.requestPeers(mChannel, new PeerListListener() {
				
				@Override
				public void onPeersAvailable(WifiP2pDeviceList peerList) {

					
					  // Out with the old, in with the new.
		            peers.clear();
		            peers.addAll(peerList.getDeviceList());
		            
		            for (WifiP2pDevice dev : peerList.getDeviceList())
		            	ABGame.peers.add(new Peer(dev.deviceName, dev.deviceAddress));
		            

		            // If an AdapterView is backed by this data, notify it
		            // of the change.  For instance, if you have a ListView of available
		            // peers, trigger an update.
		            //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
		            if (peers.size() == 0) {
		                Log.d("Peers List", "No devices found");
		                return;
		            }
					
					
					
					
					
				}
			});
				
				Toast.makeText(mActivity, "Peer list changed",
						Toast.LENGTH_SHORT).show();

			}

			Toast.makeText(mActivity, "Peer list changed error",
					Toast.LENGTH_SHORT).show();



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

						if(info.groupFormed && info.isGroupOwner){
							//temos de verificar se somos owners e ja estamos num grupo.
							   //We start the server on this peer.
							if(currentOutgoingTask != null)
							{
								currentOutgoingTask = null;
								//fechar socket da task!!!
								currentIncommingTask = new IncommingCommTask();
							    currentIncommingTask.execute();

							}
							Toast.makeText(mActivity, "Group owner.", Toast.LENGTH_LONG).show();
						}
						else if (info.groupFormed) {
							if(currentIncommingTask != null)
							{
								currentIncommingTask.cancel(true);
								currentIncommingTask = null;
								currentOutgoingTask = new OutgoingCommTask();
								currentOutgoingTask.execute(goAddress.getHostAddress());

							}
							currentOutgoingTask = new OutgoingCommTask();
							currentOutgoingTask.execute(goAddress.getHostAddress());
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
