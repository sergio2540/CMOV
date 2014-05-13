package my.game.achmed.Multiplayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class ClientInThread implements Runnable {

	Socket go;
	InHandler inHandler;

	public ClientInThread(Socket groupOwner, InHandler inHandler) {
		this.go = groupOwner;
		this.inHandler = inHandler;
	}


	@Override
	public void run() {

	}

	private void readSocket(Socket s){
		
		ObjectInputStream objStream = null;
		State message = null;
		Bundle bundle;

		try {
			
			InputStream i = s.getInputStream();
			objStream = new ObjectInputStream(i);

			while((message = (State) objStream.readObject()) != null){
				
				//this.inHandler.sendMessage(new Message().);
			}

		} catch (EOFException e){
			//Log.w("readsocket", e.getMessage());
		}
		catch (ClassNotFoundException e){

			//Log.w("readsocket", e.getMessage());

		} catch (IOException e) {
			//Log.w("readsocket", e.getMessage());
		}

		finally{

		}


	}


}
