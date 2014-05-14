package my.game.achmed.Score;

public class Rank {
	
	private String name; 
	private String level; 
	private float highscore;
	private float lastscore;
	
	
	public Rank(String name, String level, float highscore, float lastscore) {
		this.name = name;
		this.level = level;
		this.highscore = highscore;
		this.lastscore = lastscore;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	} 

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public float getHighscore() {
		return highscore;
	}

	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}

	public float getLastscore() {
		return lastscore;
	}

	public void setLastscore(int lastscore) {
		this.lastscore = lastscore;
	}
	
	public String toString() {
		//Teste
		return this.name + "            " + this.highscore + " pts";
		
	}
	
}
