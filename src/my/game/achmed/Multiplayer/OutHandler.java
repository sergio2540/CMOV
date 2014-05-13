package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

public class OutHandler extends Handler {

	private Socket go;

	public OutHandler(Socket go) {
		this.go = go;
	}


	@Override
	public void handleMessage(Message msg) {

		State state = (State) msg.getData().getSerializable("State");
		sendStateMessage(state);

	}

	private void sendStateMessage(State state) {

		ObjectOutputStream os = null;
		try {

			os = new ObjectOutputStream(this.go.getOutputStream());
			os.writeObject(state);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if(os != null)
				try {
					os.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

		}

	}

}

