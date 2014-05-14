package my.game.achmed.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class LeaveState extends State {

	private static final long serialVersionUID = 1L;

	public LeaveState(char pId, Event e) {
		super(pId, e);
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
