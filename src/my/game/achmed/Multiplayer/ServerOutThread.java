package my.game.achmed.Multiplayer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.os.Looper;

public class ServerOutThread implements Runnable {

	ServerOutHandler servOutHandler;
	volatile List<Socket> peersSockets;

	public ServerOutThread() {
		this.peersSockets = new ArrayList<Socket>();
	}

	@Override
	public void run() {

		Looper.prepare();

		servOutHandler = new ServerOutHandler(peersSockets);

		Looper.loop();

	}

	public void addPeerSocket(Socket socket) {
		this.peersSockets.add(socket);
	}


}
