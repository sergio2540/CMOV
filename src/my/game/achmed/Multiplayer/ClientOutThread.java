package my.game.achmed.Multiplayer;

import java.net.Socket;

public class ClientOutThread implements Runnable {

	Socket go;

	public ClientOutThread(Socket groupOwner, OutHandler outHandler) {
		this.go = groupOwner;
	}

	@Override
	public void run() {
		
		
		
		

	}

}
