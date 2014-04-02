package my.game.achmed;

public class GameMatrix {
	
	private int rows = 0;
	private int cols = 0;
	
	char[][] gameMatrix;
	
	//para criar um objecto quando for para loadar de um ficheiro
	public void GameMatrix() {}
	
	public GameMatrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		
		gameMatrix = new char[rows][cols];
		
	}
	
	//TODO lodar a matriz de um file
	public void loadMatrixFromFile() {
		
	}

}
