package my.game.achmed.Multiplayer;

import my.game.achmed.ABEngine;
import my.game.achmed.Characters.Player;
import android.os.Handler;
import android.os.Message;

public class InHandler extends Handler {


	@Override
	public void handleMessage(Message msg) {

		State state = (State) msg.getData().getSerializable("State");
		processStateMessage(state);

	}

	private void processStateMessage(State message){

		switch(message.getEvent()){
		//Players action
		case PLAYER:
			PlayerState pState = (PlayerState) message;
			if(pState.getPlayerId() == ABEngine.PLAYER.getID() ){
				return;
			}

			ABEngine.setPlayerAction(pState.getPlayerId(), pState.getPlayerAction());
			Player player = ABEngine.ENEMIES.get(pState.getPlayerId());
			player.STOP = pState.isStop();
			player.STOPPED = pState.isStopped();
			player.HIDDEN = pState.isHidden();
//			if(isGroupOwner()) {
//				broadCastPlayerEvent(pState);
//			}

			break;
			//Players bomb
		case BOMB:
			BombState bState = (BombState) message;
			if(bState.getPlayerId() == ABEngine.PLAYER.getID() ){
				return;
			}
			ABEngine.setBombAction(bState.getPlayerId(), bState.getBombAction());
			break;
			//Received by the client
		case INIT://e aqui vao haver envios de volta?
			InitState iState = (InitState) message;


			if(ABEngine.LEVEL == null){
				ABEngine.LEVEL = iState.getLevel();
				ABEngine.create_map(iState.getLevel().getGameLevelMatrix()); 
				ABEngine.PLAYER = Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY());
				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
				ABEngine.loadingEvent.doLoadingEvent(true);

			} else {

				//characters.put(iState.getIp().getHostAddress(), iState.getPlayerId());
				ABEngine.ENEMIES.put(iState.getPlayerId(), Player.create(iState.getPlayerId(), iState.getCoordX(),iState.getCoordY()));


			}


			break;

		case LEAVE:

			LeaveState lState = (LeaveState) message;

			ABEngine.ENEMIES.remove(lState.getPlayerId());

			break;

		}
	}

}
