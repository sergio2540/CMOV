package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class InitState extends State{

	private char[][] gameMap;
	float coordX;
	float coordY;
	
	public InitState(int pId, Event e, char[][] map, float x, float y){
		super(pId, e);
		gameMap = map;
		coordX = x;
		coordY = y;
	}
	
	public char[][] getGameMap() {
		return gameMap;
	}


	public float getCoordX() {
		return coordX;
	}


	public float getCoordY() {
		return coordY;
	}



	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(playerId);
		stream.writeObject(event);
		stream.writeObject(gameMap);
		stream.writeObject(coordX);
		stream.writeObject(coordY);

	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playerId = stream.readInt();
		event = (Event) stream.readObject();
		gameMap = (char[][]) stream.readObject();
		coordX = (float) stream.readFloat();
		coordY = (float) stream.readFloat();

		
	}

}
