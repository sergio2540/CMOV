package my.game.achmed.Multiplayer;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.Player;
import my.game.achmed.State.State;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class ServerInThread implements Runnable {

	Socket client;
	//ServerOutThread servOutThread;

	public ServerInThread(Socket s) {
		this.client = s;
		//this.servOutThread = servOutThread;
	}


	@Override
	public void run() {

		ObjectInputStream inStream = null;
		State state;

		InputStream i;
		try {
			i = client.getInputStream();

			inStream = new ObjectInputStream(i);
		} catch (StreamCorruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while(true) {

			try {




				while((state = (State) inStream.readObject()) != null){

					ABEngine.processStateMessage(state);
					ABEngine.broadCastMessage(state);

				}



			} catch (EOFException e){
				Log.e("exception", "EOFException");
			}
			catch (ClassNotFoundException e){

				Log.e("exception", "Class not Found");

			} catch (IOException e) {
				Log.e("exception", "IOException");
			}

			finally{

				//				try {
				//					//if(inStream != null)
				//						//inStream.close();
				//				} catch (IOException e) {
				//					e.printStackTrace();
				//				}

			}

		}
	}

	//	public void processStateMessage(State state) {
	//
	//
	//		//broadcast para todos os clientes
	//		Message msg = new Message();
	//		Bundle bundle = new Bundle();
	//		bundle.putSerializable("State", state);
	//		msg.setData(bundle);
	//
	//		this.updateGameState(state);
	//
	//
	//		//update
	//
	//		servOutThread.servOutHandler.sendMessage(msg);
	//
	//
	//	}

	//	public synchronized void updateGameState(State message) {
	//
	//		switch(message.getEvent()){
	//		//Players action
	//		case PLAYER:
	//			PlayerState pState = (PlayerState) message;
	//			if(pState.getPlayerId() == ABEngine.PLAYER.getID() ){
	//				return;
	//			}
	//
	//			ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());
	//			Player player = ABEngine.ENEMIES.get(pState.getPlayerId());
	//			player.STOP = pState.isStop();
	//			player.STOPPED = pState.isStopped();
	//			player.HIDDEN = pState.isHidden();
	//			//			if(isGroupOwner()) {
	//			//				broadCastPlayerEvent(pState);
	//			//			}
	//
	//			break;
	//			//Players bomb
	//		case BOMB:
	//			BombState bState = (BombState) message;
	//			if(bState.getPlayerId() == ABEngine.PLAYER.getID() ){
	//				return;
	//			}
	//			ABEngine.setBombAction(bState.getPlayerId(), bState.getBombAction());
	//			break;
	//			//Received by the client
	//		case INIT://e aqui vao haver envios de volta?
	//			InitState iState = (InitState) message;
	//
	//
	//			if(ABEngine.LEVEL == null){
	//				ABEngine.LEVEL = iState.getLevel();
	//				ABEngine.create_map(iState.getLevel().getGameLevelMatrix()); 
	//				ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());
	//				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
	//				ABEngine.loadingEvent.doLoadingEvent(true);
	//
	//			} else {
	//
	//				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
	//				ABEngine.ENEMIES.put(iState.getPlayerId(), Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY()));
	//
	//
	//			}
	//
	//
	//			break;
	//
	//		case LEAVE:
	//
	//			LeaveState lState = (LeaveState) message;
	//
	//			ABEngine.ENEMIES.remove(lState.getPlayerId());
	//
	//			break;
	//
	//		}
	//}


}




