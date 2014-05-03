package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import my.game.achmed.Characters.CHARACTER_ACTION;

public class PlayerState extends State {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CHARACTER_ACTION playerAction;
	
	
	public CHARACTER_ACTION getPlayerAction() {
		return playerAction;
	}


	public PlayerState(char pId, Event e, CHARACTER_ACTION pAction){
		super(pId, e);
		playerId = pId;
		playerAction = pAction;
			
		
	}
	
	
	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(playerId);
		stream.writeObject(event);
		stream.writeObject(playerAction);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		playerAction = (CHARACTER_ACTION) stream.readObject();
	}
	
	
	
}
