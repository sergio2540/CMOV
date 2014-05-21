package my.game.achmed.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import my.game.achmed.Characters.SPLIT_ACTION;


public class SplitState extends State {

	private static final long serialVersionUID = 1L;

	private SPLIT_ACTION splitAction;

	private String newOwner;

	private List<Character> leavers;

	public SplitState(char pId, Event e, SPLIT_ACTION pAction, String newOwnerAddress, List<Character> selectedPlayers){
		super(pId, e);
		playerId = pId;
		splitAction = pAction;
		newOwner = newOwnerAddress;
		leavers = selectedPlayers;
		
	}
	
	public SPLIT_ACTION getSplitAction() {
		return splitAction;
	}
	
	public List<Character> getPeers(){
		
		return leavers;
	}
	
	public String getNewOwner(){
		return newOwner;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeChar(playerId);
		stream.writeObject(event);
		stream.writeObject(splitAction);
		stream.writeUTF(newOwner);
		stream.writeObject(leavers);
		
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		splitAction = (SPLIT_ACTION) stream.readObject();
		newOwner = stream.readUTF();
		leavers = (List<Character>) stream.readObject();

	}
	
	
}
