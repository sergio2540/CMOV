package my.game.achmed.Score;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MultiplayerRank implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public List<MultiPlayerScore> playersScore;

	public MultiplayerRank() {
		playersScore = new ArrayList<MultiPlayerScore>();
	}

	public void addPlayer(MultiPlayerScore playerScore) {
		this.playersScore.add(playerScore);
	}
	
	public List<MultiPlayerScore> getMultiplayerScoreList() {
		return this.playersScore;
	}

	public void sortPlayersScore() {
		Collections.sort(playersScore, new Comparator<MultiPlayerScore>() {
			public int compare(MultiPlayerScore o1, MultiPlayerScore o2) {
				return o1.getScore() < o2.getScore() ? -1 : o1.getScore() > o2.getScore() ? 1 : 0;
			}
		});
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(playersScore);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		playersScore = (List<MultiPlayerScore>) stream.readObject();
	}

}
