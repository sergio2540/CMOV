package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import android.os.Handler;
import android.os.Message;

public class ServerOutHandler extends Handler {

	List<Socket> peers;

	public ServerOutHandler(List<Socket> peers) {
		this.peers = peers;
	}

	@Override
	public void handleMessage(Message msg) {

		State state = (State) msg.getData().getSerializable("State");
		this.broadCastMessage(state);

	}

	public void broadCastMessage(State state) {

		ObjectOutputStream os;

		for(Socket s : peers) {
			try {
				os = new ObjectOutputStream(s.getOutputStream());
				os.writeObject(state);
			} catch (IOException e) {
				e.printStackTrace();
			}


		}

	}

}
