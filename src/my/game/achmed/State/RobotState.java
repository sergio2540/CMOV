package my.game.achmed.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import my.game.achmed.Characters.CHARACTER_ACTION;

public class RobotState extends State {

	private static final long serialVersionUID = 1L;

	private CHARACTER_ACTION robotAction;
	
	public RobotState(char id, Event e, CHARACTER_ACTION rAction) {
		super(id, e);
		robotAction = rAction;
	}
	
	public CHARACTER_ACTION getRobotAction() {
		return robotAction;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeChar(playerId);
		stream.writeObject(event);
		stream.writeObject(robotAction);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		robotAction = (CHARACTER_ACTION) stream.readObject();
	}
	
	
}
