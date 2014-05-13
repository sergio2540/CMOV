package my.game.achmed.Multiplayer;

import java.net.Socket;

import android.os.Looper;

public class ClientOutThread implements Runnable {

	Socket go;
	OutHandler outHandler;
	
	public ClientOutThread(Socket groupOwner) {
		this.go = groupOwner;
	}

	@Override
	public void run() {
		
		Looper.prepare();
        
		this.outHandler = new OutHandler(go);
		
		Looper.loop();
		
	}
	
}
