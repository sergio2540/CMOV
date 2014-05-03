package my.game.achmed.Multiplayer;

import my.game.achmed.Activities.ABGame;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private final WifiP2pManager mManager;
    private final Channel mChannel;
    private final ABGame mActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel mChannel, ABGame activity) {

	super();
	this.mManager = manager;
	this.mChannel = mChannel;
	this.mActivity = activity;
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
		//mManager.requestPeers(mChannel, mActivity);


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
