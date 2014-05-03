package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import my.game.achmed.Characters.BOMB_ACTION;

public class BombState extends State{

	private BOMB_ACTION bombAction;

	public BombState(char pId, Event e, BOMB_ACTION pAction){
		super(pId, e);
		playerId = pId;
	}
	
	public BOMB_ACTION getBombAction() {
		return bombAction;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(playerId);
		stream.writeObject(event);
		stream.writeObject(bombAction);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		bombAction = (BOMB_ACTION) stream.readObject();
	}
	
	
}
