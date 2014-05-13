package my.game.achmed.Multiplayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.Handler;
import android.util.Log;

public class ClientCom {

	InHandler handlerIn = new InHandler();
	OutHandler handlerOut = new OutHandler();

	ClientInThread threadIn;
	ClientOutThread threadOut;

	Socket groupOwner;
	String goIp;


	public ClientCom(String ip) {
		this.goIp = ip;
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

	public void startInThread() {

		threadIn = new ClientInThread(this.groupOwner, this.handlerIn);

	
	}

	public void startOutThread() {

		threadOut = new ClientOutThread(this.groupOwner, this.handlerOut);
			
		
	}
	

	
}
