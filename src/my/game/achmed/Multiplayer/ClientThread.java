package my.game.achmed.Multiplayer;

import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;

public class ClientThread implements Runnable {

	InHandler handlerIn;
	OutHandler handlerOut;

	ClientInThread threadIn;
	ClientOutThread threadOut;

	Socket groupOwner;
	String goIp;

	public ClientThread(String ip) {
		this.goIp = ip;
	}
	
	public void start() {
		this.connect();
	}

	public void connect() {
		while(true){
			try {

				groupOwner = new Socket();
				groupOwner.bind(null);
				groupOwner.connect(new InetSocketAddress(this.goIp, 9091), 500);
				this.startInThread();
				this.startOutThread();
				return;

			} catch (Exception e) {
				Log.w("out", e.toString());
			} 

		}

	}

	private void startInThread() {

		threadIn = new ClientInThread(this.groupOwner, this.handlerIn);
		new Thread(threadIn).start();

	}

	private void startOutThread() {

		threadOut = new ClientOutThread(this.groupOwner);
		new Thread(threadOut).start();
		
	}

	public void sendStateToSendingThread(State state) {


		if(this.threadOut.outHandler != null) {

			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putSerializable("State", state);
			msg.setData(bundle);
			//bundle.clear();
			this.threadOut.outHandler.sendMessage(msg);
			
		}

	}
	
	@Override
	public void run() {
		this.start();
	}
	
}
