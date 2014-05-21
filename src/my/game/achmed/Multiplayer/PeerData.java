package my.game.achmed.Multiplayer;

public class PeerData {

	
	private String ipAddress;
	private String macAddress;
	
	public PeerData(){}
	
	public void setIpAddress(String ip){
		
		ipAddress = ip;
	}
	public void setMacAddress(String mac){
		macAddress = mac;
	}
	
	public String getMacAddress() {
		return macAddress;
	}
	
}
