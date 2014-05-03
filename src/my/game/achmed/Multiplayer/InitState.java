package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class InitState extends State{

	private char[][] gameMap;
	int coordX;
	int coordY;
	
	
	public InitState(char pId, Event e, char[][] map, int x, int y){
		super(pId, e);
		gameMap = map;
		coordX = x;
		coordY = y;
	}
	
	public char[][] getGameMap() {
		return gameMap;
	}


	public int getCoordX() {
		return coordX;
	}


	public int getCoordY() {
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
		playerId = stream.readChar();
		event = (Event) stream.readObject();
		gameMap = (char[][]) stream.readObject();
		coordX = (int) stream.readInt();
		coordY = (int) stream.readInt();

		
	}

}
