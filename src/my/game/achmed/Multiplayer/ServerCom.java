package my.game.achmed.Multiplayer;

import java.util.HashMap;
import java.util.Map;

public class ServerCom {

	public volatile static Map<Character, PeerData> playingPeersnew = new HashMap<Character, PeerData>();
	ServerThread server;
	
	public ServerCom() {
		server = new ServerThread();
		new Thread(server).start();
	}
	
}
