package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import my.game.achmed.Characters.BOMB_ACTION;

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
	
	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeChar(playerId);
		stream.writeObject(event);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
	}
}
