package my.game.achmed.Multiplayer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

import my.game.achmed.Level;


public class InitState extends State {

    private static final long serialVersionUID = 1L;
    
    int coordX;
    int coordY;
    Level level;
    InetAddress ipAddress;
    
    
    public InitState(char pId, Event e, int x, int y, 
	    Level level, InetAddress ipAddress){
	
	super(pId, e);
	this.coordX = x;
	this.coordY = y;
	this.level = level;
	this.ipAddress = ipAddress;
	
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

    public InetAddress getIp(){
    	return this.ipAddress;
    }

    
    private void writeObject(ObjectOutputStream stream) throws IOException{
	stream.writeChar(playerId);
	stream.writeObject(event);
	stream.writeInt(coordX);
	stream.writeInt(coordY);
	stream.writeObject(level);
	stream.writeObject(ipAddress);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
	this.playerId = stream.readChar();
	this.event = (Event) stream.readObject();
	this.coordX = stream.readInt();
	this.coordY = stream.readInt();
	this.level = (Level) stream.readObject();
	this.ipAddress = (InetAddress) stream.readObject();

    }

}
