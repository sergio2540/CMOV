package my.game.achmed.Multiplayer;

public class ClientCom {

	public ClientThread client;
	String goIp;
	
	public ClientCom(String goIp) {
		client = new ClientThread(goIp);
		new Thread(client).start();
	}

}
