package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import my.game.achmed.Level;
import my.game.achmed.Characters.Player;
import my.game.achmed.Characters.Robot;


public class InitState extends State {

    private static final long serialVersionUID = 1L;
    
    private char[][] gameMap;
    int coordX;
    int coordY;
    Level level;
    //Map<Character,Player> opponentsPlayers = new TreeMap<Character,Player>();
    //List<Robot> robots;


    public InitState(char pId, Event e, char[][] map, int x, int y, 
	    Level level){
	super(pId, e);
	this.gameMap = map;
	this.coordX = x;
	this.coordY = y;
	this.level = level;
	//this.opponentsPlayers = opponentsPlayers;
	//this.robots = robots;
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
    
    public Level getLevel() {
	return this.level;
    }

//    public Map<Character, Player> getOpponentsPlayers() {
//	return this.opponentsPlayers;
//    }
//
//    public List<Robot> getRobots() {
//	return this.robots;
//    }

    private void writeObject(ObjectOutputStream stream) throws IOException{
	stream.writeChar(playerId);
	stream.writeObject(event);
	stream.writeObject(gameMap);
	stream.writeInt(coordX);
	stream.writeInt(coordY);
	stream.writeObject(level);
//	stream.writeObject(opponentsPlayers);
//	stream.writeObject(robots);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
	this.playerId = stream.readChar();
	this.event = (Event) stream.readObject();
	this.gameMap = (char[][]) stream.readObject();
	this.coordX = stream.readInt();
	this.coordY = stream.readInt();
	this.level = (Level) stream.readObject();
//	this.opponentsPlayers = (Map<Character, Player>) stream.readObject();
//	this.robots = (List<Robot>) stream.readObject();

    }

}
