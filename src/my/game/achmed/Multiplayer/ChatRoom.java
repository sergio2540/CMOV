package my.game.achmed.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;

public class ChatRoom extends Activity implements GroupInfoListener, OnItemClickListener,PeerListListener {
	
	public static final String TAG = "simplechat";
	private SimWifiP2pManager mManager = null;
	private Channel mChannel = null;
	private Messenger mService = null;
	private boolean mBound = false;
	private SimWifiP2pSocketServer mSrvSocket = null;
	private SimWifiP2pSocket mCliSocket = null;
	private ReceiveCommTask mComm = null;
	public static ArrayList<String> gos = new ArrayList<String>();
	public static ArrayAdapter adapter;

	private String groupOwnerToConnect  = null;
	//private SimWifiP2pSocketServer mSrvSocket = null;
	//private ReceiveCommTask mComm = null;
	//private SimWifiP2pSocket mCliSocket = null;
	//private TextView mTextInput;
	//private TextView mTextOutput;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		
		//lista para guardar users perto.
		gos = new ArrayList<String>();
		adapter = new ArrayAdapter(this, R.layout.textview,gos);
		((ListView) findViewById(R.id.listView1)).setAdapter(adapter);
		((ListView)findViewById(R.id.listView1)).setOnItemClickListener(this);
		
		findViewById(R.id.idInGroupButton).setOnClickListener(listenerInGroupButton);
	


		Intent intent = new Intent(this, SimWifiP2pService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mBound = true;

		// spawn the chat server background task
		new IncommingCommTask().execute();

		//queremos receber notificacoes de grupos
		//IntentFilter filter = new IntentFilter();
		//filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);

		//SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver(this);
		//registerReceiver(receiver, filter);
		//new IncommingCommTask().execute();
		

		// register broadcast receiver
		//IntentFilter filter = new IntentFilter();
		//filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
		//filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
		//filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
		//filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
		//SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver(this);
		//registerReceiver(receiver, filter);
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}

	
	/*private OnClickListener listenerInRangeButton = new OnClickListener() {
    public void onClick(View v){
    	if (mBound) {
            SimpleChatActivity.mManager.requestPeers(mChannel, (PeerListListener) ChatRoom.this);
    	} else {
        	Toast.makeText(v.getContext(), "Service not bound",
        		Toast.LENGTH_SHORT).show();
        }
    }
};
*/
	
	private OnClickListener listenerInGroupButton = new OnClickListener() {

		@Override
		public void onClick(View v){
			if (mBound) {
				Log.d("Check Network Event:","Called check Network!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				mManager.requestGroupInfo(mChannel, (GroupInfoListener) ChatRoom.this);
			} else {
				Toast.makeText(v.getContext(), "Service not bound",
						Toast.LENGTH_SHORT).show();
			}
		}


	};
	//usado para nos ligar-mos ao elemento da lista que esacolhemos
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		groupOwnerToConnect = (String) adapter.getItem((int) arg3);//WTF???
		if (mBound) {
			//mManager.requestGroupInfo(mChannel, (GroupInfoListener) ChatRoom.this);
			Intent chatWindow = new Intent(this, ChatWindow.class);
			chatWindow.putExtra("ENDPOINT",groupOwnerToConnect);
			startActivity(chatWindow);
		
		} else {
			Toast.makeText(this, "Service not bound",
					Toast.LENGTH_SHORT).show();
		}

	}



	@Override
	public void onGroupInfoAvailable(SimWifiP2pDeviceList devices, 
			SimWifiP2pInfo groupInfo) {

		//new OutgoingCommTask().execute(mTextInput.getText().toString());

		

		for (String deviceName : groupInfo.getDevicesInNetwork()) {
			//if(deviceName.equals(groupOwnerToConnect)){
				SimWifiP2pDevice device = devices.getByName(deviceName);
				//Intent chatWindow = new Intent(this, ChatWindow.class);
				//chatWindow.putExtra("ENDPOINT",device.getVirtIp());
				//startActivity(chatWindow);
				Log.d("Goup info avaliabele:","In function group info.!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				gos.add(device.getVirtIp());
				adapter.notifyDataSetChanged();
				
				
				//new OutgoingCommTask().execute(device.getVirtIp());
			//}
			//SimWifiP2pDevice device = devices.getByName(deviceName);
			//String devstr = "" + deviceName + " (" + 
				//	((device == null)?"??":device.getVirtIp()) + ")\n";
			//peersStr.append(devstr);
		}

		// compile list of network members
		//StringBuilder peersStr = new StringBuilder();
		//for (String deviceName : groupInfo.getDevicesInNetwork()) {
		//SimWifiP2pDevice device = devices.getByName(deviceName);
		//String devstr = "" + deviceName + " (" + 
		//	((device == null)?"??":device.getVirtIp()) + ")\n";
		//peersStr.append(devstr);
		//}

		// display list of network members
		//new AlertDialog.Builder(this)
		//.setTitle("Devices in WiFi Network")
		//.setMessage(peersStr.toString())
		//.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
		//public void onClick(DialogInterface dialog, int which) { 
		//}
		//})
		//.show();
	}
//relacionado com os binds de servico.
	private ServiceConnection mConnection = new ServiceConnection() {
		// callbacks for service binding, passed to bindService()

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			mManager = new SimWifiP2pManager(mService);
			mChannel = mManager.initialize(getApplication(), getMainLooper(), null);
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService = null;
			mManager = null;
			mChannel = null;
			mBound = false;
		}
	};
/*
//recebe ligacoes.
	public class IncommingCommTask extends AsyncTask<Void, SimWifiP2pSocket, Void> {

		
		@Override
		protected Void doInBackground(Void... params) {

			Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

			try {
				mSrvSocket = new SimWifiP2pSocketServer(
						Integer.parseInt(getString(R.string.port)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				try {
					SimWifiP2pSocket sock = mSrvSocket.accept();
					if (mCliSocket != null && mCliSocket.isClosed()) {
						mCliSocket = null;
					}
					if (mCliSocket != null) {
						Log.d(TAG, "Closing accepted socket because mCliSocket still active.");
						sock.close();
					} else {
						publishProgress(sock);
					}
				} catch (IOException e) {
					Log.d("Error accepting socket:", e.getMessage());
					break;
					//e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(SimWifiP2pSocket... values) {
			mCliSocket = values[0];
			mComm = new ReceiveCommTask();
			mComm.execute(mCliSocket);
		}
	}
*/
	//para receber mensagens.
	/*public class ReceiveCommTask extends AsyncTask<SimWifiP2pSocket, String, Void> {
		SimWifiP2pSocket s;

		@Override
		protected Void doInBackground(SimWifiP2pSocket... params) {
			BufferedReader sockIn;
			String st;

			s = params[0];
			try {
				sockIn = new BufferedReader(new InputStreamReader(s.getInputStream()));

				while ((st = sockIn.readLine()) != null) {
					publishProgress(st);
				}
			} catch (IOException e) {
				Log.d("Error reading socket:", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			//mTextOutput.setText("");
			findViewById(R.id.idSendButton).setEnabled(true);
			findViewById(R.id.idDisconnectButton).setEnabled(true);
			findViewById(R.id.idConnectButton).setEnabled(false);
			//mTextInput.setHint("");
			//mTextInput.setText("");

		}

		@Override
		protected void onProgressUpdate(String... values) {
			//mTextOutput.append(values[0]+"\n");
			//Toast.makeText(, values, Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!s.isClosed()) {
				try {
					s.close();
				}
				catch (Exception e) {
					Log.d("Error closing socket:", e.getMessage());
				}
			}
			s = null;
			if (mBound) {
				//guiUpdateDisconnectedState();
			} else {
				//guiUpdateInitState();
			}
		}
	}
*/
	@Override
	public void onPeersAvailable(SimWifiP2pDeviceList peers) {
		StringBuilder peersStr = new StringBuilder();
		
		// compile list of devices in range
		for (SimWifiP2pDevice device : peers.getDeviceList()) {
			String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ")\n";
			peersStr.append(devstr);
		}

		// display list of devices in range
		new AlertDialog.Builder(this)
	    .setTitle("Devices in WiFi Range")
	    .setMessage(peersStr.toString())
	    .setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        }
	     })
	     .show();
	}

//para nos ligarmos a outro user.
	/*public class OutgoingCommTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			//mTextOutput.setText("Connecting...");
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				mCliSocket = new SimWifiP2pSocket(params[0],
						Integer.parseInt(getString(R.string.port)));
			} catch (UnknownHostException e) {
				return "Unknown Host:" + e.getMessage();
			} catch (IOException e) {
				return "IO error:" + e.getMessage();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				//mTextOutput.setText(result);
				//findViewById(R.id.idConnectButton).setEnabled(true);
			}
			else {
				mComm = new ReceiveCommTask();
				mComm.execute(mCliSocket);
			}		
		}
	}
*/

	/*	public class IncommingCommTask extends AsyncTask<Void, SimWifiP2pSocket, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			Log.d(TAG, "IncommingCommTask started (" + this.hashCode() + ").");

			try {
				mSrvSocket = new SimWifiP2pSocketServer(
						Integer.parseInt(getString(R.string.port)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {
				try {
					SimWifiP2pSocket sock = mSrvSocket.accept();
					if (mCliSocket != null && mCliSocket.isClosed()) {
						mCliSocket = null;
					}
					if (mCliSocket != null) {
						Log.d(TAG, "Closing accepted socket because mCliSocket still active.");
						sock.close();
					} else {
						publishProgress(sock);
					}
				} catch (IOException e) {
					Log.d("Error accepting socket:", e.getMessage());
					break;
					//e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(SimWifiP2pSocket... values) {
			mCliSocket = values[0];
			mComm = new ReceiveCommTask();
			mComm.execute(mCliSocket);
		}
	}
	 */




}
