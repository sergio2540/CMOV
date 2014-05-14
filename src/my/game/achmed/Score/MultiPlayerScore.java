package my.game.achmed.Score;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MultiPlayerScore implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String name; 
	public float score;
	boolean highlight;
	
	public MultiPlayerScore(String name, float score, boolean highlight) {
		this.name = name;
		this.score = score;
		this.highlight = highlight;
	}
	
	public float getScore() {
		return this.score;
	}

	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeUTF(name);
		stream.writeFloat(score);
		stream.writeBoolean(highlight);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		name = stream.readUTF();
		score = stream.readFloat();
		highlight = stream.readBoolean();
	}
	
	public String toString() {
		return name + "               " + score;
	}
	
}
