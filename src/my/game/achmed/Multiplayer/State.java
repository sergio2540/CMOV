package my.game.achmed.Multiplayer;

import java.io.Serializable;

public abstract class State implements Serializable {
	

	protected int playerId;
	protected Event event;
	
	
	public State(int pId, Event e){
		
		playerId = pId;
		event = e;
		
	} 
	
	public int getPlayerId() {
		return playerId;
	}



	public Event getEvent() {
		return event;
	}




}
