package my.game.achmed.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import my.game.achmed.Multiplayer.ChatRoom.ReceiveCommTask;
import android.view.View;
import android.view.View.OnClickListener;
import pt.utl.ist.cmov.simplechat.R;
import pt.utl.ist.cmov.simplechat.R.id;
import pt.utl.ist.cmov.simplechat.R.layout;
import pt.utl.ist.cmov.simplechat.R.menu;
import pt.utl.ist.cmov.simplechat.R.string;
import pt.utl.ist.cmov.wifidirect.sockets.SimWifiP2pSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class ChatWindow extends Activity {

	private SimWifiP2pSocket mCliSocket = null;
	private ReceiveCommTask mComm = null;
	private EditText mTextInput;
	private TextView mTextOutput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);
		Bundle textBundle = getIntent().getExtras();
		new OutgoingCommTask().execute(textBundle.getString("ENDPOINT"));
		mTextInput = (EditText) findViewById(R.id.editText1);
		mTextOutput = (TextView) findViewById(R.id.textView1);
		findViewById(R.id.idSendButton).setOnClickListener(listenerSendButton);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_window, menu);
		return true;
	}
	
	
	private OnClickListener listenerSendButton = new OnClickListener() {
	@Override
	public void onClick(View v) {
		findViewById(R.id.idSendButton).setEnabled(false);
		try {
			mCliSocket.getOutputStream().write( (mTextInput.getText().toString()+"\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mTextInput.setText("");
		findViewById(R.id.idSendButton).setEnabled(true);
		findViewById(R.id.idDisconnectButton).setEnabled(true);
	}
};


	
	//para nos ligarmos a outro user.
		public class OutgoingCommTask extends AsyncTask<String, Void, String> {

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
		
		
		
		
		//para receber mensagens.
		public class ReceiveCommTask extends AsyncTask<SimWifiP2pSocket, String, Void> {
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
						mTextOutput.append(st);

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
				//if (mBound) {
					//guiUpdateDisconnectedState();
				//} else {
					//guiUpdateInitState();
				//}
			}
		}


}
