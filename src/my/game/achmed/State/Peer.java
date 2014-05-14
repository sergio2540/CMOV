package my.game.achmed.State;

public class Peer {
	
	
	private String peerName;
	private String peerAddress;
	
	
	public Peer(String name, String add){
		
		peerName = name;
		peerAddress = add;

	}

	
	public String getPeerName() {
		return peerName;
	}

	
	public String getPeerAddress() {
		return peerAddress;
	}

	public String toString(){
		return peerName;
		
	}
}
