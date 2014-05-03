package my.game.achmed.Multiplayer;

import java.io.Serializable;

public abstract class State implements Serializable {
	

	protected char playerId;
	protected Event event;
	
	
	public State(char pId, Event e){
		
		playerId = pId;
		event = e;
		
	} 
	
	public char getPlayerId() {
		return playerId;
	}



	public Event getEvent() {
		return event;
	}




}
