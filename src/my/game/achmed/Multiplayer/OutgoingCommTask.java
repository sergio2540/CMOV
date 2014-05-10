package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import my.game.achmed.Characters.CHARACTER_ACTION;
import android.os.AsyncTask;
import android.util.Log;

//Used to connect to group owner.
public class OutgoingCommTask extends AsyncTask<String, Void, String> {

	Socket groupOwner;
	ReceiveCommTask mComm;

	@Override
	protected void onPreExecute() {
		//mTextOutput.setText("Connecting...");
	}

	@Override
	protected String doInBackground(String... params) {

		while(true){
			try {

				groupOwner = new Socket();
				groupOwner.bind(null);
				groupOwner.connect(new InetSocketAddress(params[0],9091), 500);
				break;

			} catch (Exception e) {
				Log.w("out", e.toString());
			}  
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
			mComm.setGroupOwner(groupOwner);
			mComm.execute();
		}		
	}

	@Override
	protected void onCancelled(){

		if(groupOwner != null)
			try {
				groupOwner.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}

		if(mComm != null)
			mComm.cancel(true);
	}


}
