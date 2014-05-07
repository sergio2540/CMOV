package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import my.game.achmed.Characters.CHARACTER_ACTION;

public class PlayerState extends State {

	private static final long serialVersionUID = 1L;

	private CHARACTER_ACTION playerAction;
	private boolean stop = true;
	public boolean isStop() {
	    return stop;
	}

	public PlayerState(char pId, Event e, CHARACTER_ACTION pAction, boolean stop, boolean stopped){
		super(pId, e);
		playerId = pId;
		playerAction = pAction;
		this.stop = stop;
		this.stopped = stopped;
		
	}
	
	public boolean isStopped() {
	    return stopped;
	}

	private boolean stopped = true;
	
	public CHARACTER_ACTION getPlayerAction() {
		return playerAction;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeChar(playerId);
		stream.writeObject(event);
		stream.writeObject(playerAction);
		stream.writeBoolean(stop);
		stream.writeBoolean(stopped);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		playerAction = (CHARACTER_ACTION) stream.readObject();
		stop = stream.readBoolean();
		stopped = stream.readBoolean();
	}
	
	
}
