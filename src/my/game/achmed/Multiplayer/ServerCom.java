package my.game.achmed.Multiplayer;

public class ServerCom {

	ServerThread server;
	
	public ServerCom() {
		server = new ServerThread();
		new Thread(server).start();
	}
	
}
