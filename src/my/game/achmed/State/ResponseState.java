package my.game.achmed.State;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import my.game.achmed.Characters.CHARACTER_ACTION;

public class ResponseState extends State {
	
	private static final long serialVersionUID = 1L;
	private String macAddress;


	
	
	public ResponseState(char pId, Event e, String mac) {
		super(pId, e);
		macAddress = mac;
		
		
	}
	
	public String getMac(){
		return macAddress;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeChar(playerId);
		stream.writeObject(event);
		stream.writeUTF(macAddress);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		macAddress = stream.readUTF();
  	}
	

}
