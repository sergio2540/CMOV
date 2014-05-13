package my.game.achmed.Multiplayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

public class ClientInThread implements Runnable {

	private Socket goSocket;
	private InHandler inHandler;

	public ClientInThread(Socket groupOwner, InHandler inHandler) {
		this.goSocket = groupOwner;
		this.inHandler = inHandler;
	}


	@Override
	public void run() {


		new Thread(new Runnable() {

			@Override
			public void run() {
				Looper.prepare();
				inHandler = new InHandler();
				Looper.loop();
			}

		}).start();

		while(true) {
			this.readSocket();
		}

	}

	private void readSocket() {

		ObjectInputStream objStream = null;
		State message = null;
		Bundle bundle = null;
		Message msg = null;

		try {

			InputStream i = goSocket.getInputStream();
			objStream = new ObjectInputStream(i);

			while((message = (State) objStream.readObject()) != null) {

				bundle = new Bundle();
				bundle.putSerializable("State", message);
				msg = new Message();
				msg.setData(bundle);

				this.inHandler.sendMessage(msg);

			}

		} catch (EOFException e){

		} catch (ClassNotFoundException e){

		} catch (IOException e) {

		} 

		finally{

		}


	}


}
