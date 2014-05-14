package my.game.achmed.Multiplayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import my.game.achmed.ABEngine;
import my.game.achmed.State.State;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

public class ClientInThread implements Runnable {

    private final Socket goSocket;

    public ClientInThread(Socket groupOwner) {
	this.goSocket = groupOwner;
	
    }


    @Override
    public void run() {

	while(true) {
	    this.readSocket();
	}

    }

    private void readSocket() {

	ObjectInputStream objStream = null;
	State message = null;
	
	try {

	    InputStream i = goSocket.getInputStream();
	    objStream = new ObjectInputStream(i);
	  
	    
	    while((message = (State) objStream.readObject()) != null) {
		
		ABEngine.processStateMessage(message);
		

	    }

	} catch (EOFException e){
	    System.out.println(e);
	} catch (ClassNotFoundException e){
	    System.out.println(e);
	} catch (IOException e) {
	    System.out.println(e);
	} 

	finally{
	    
//	    if(objStream != null){
//		objStream.close();
//	    }

	}


    }


}
