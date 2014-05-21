package my.game.achmed.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class SplitPlayersState extends State {
	

	private static final long serialVersionUID = 1L;
	private Map<Character, String> peers;

	public SplitPlayersState(char pId, Event e, Map<Character, String> chosenPeers) {
		super(pId, e);
		peers = chosenPeers;
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeChar(playerId);
		stream.writeObject(event);
		stream.writeObject(peers);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		peers = (Map<Character, String>) stream.readObject();
	}

}
