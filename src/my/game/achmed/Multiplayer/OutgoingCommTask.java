package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import my.game.achmed.ABEngine;
import my.game.achmed.R;
import android.os.AsyncTask;

//para nos ligarmos a outro user.
public class OutgoingCommTask extends AsyncTask<String, Void, String> {
	
	Socket gropuOwner;
	ReceiveCommTask mComm;

	@Override
	protected void onPreExecute() {
		//mTextOutput.setText("Connecting...");
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			gropuOwner = new Socket(params[0], Integer.parseInt(ABEngine.context.getResources().getString(R.string.port)));
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
			mComm.execute(gropuOwner);
		}		
	}

	public Socket getGropuOwner() {
		return gropuOwner;
	}
}
