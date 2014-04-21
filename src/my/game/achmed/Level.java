package my.game.achmed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.util.JsonReader;


public class Level {

	private Level() {}

	private static String name;
	private static int gameDurationInSeconds;
	private static int explosionTimeoutInSeconds;
	private static int explosionDurationInSeconds;
	private static int explosionRange;
	private static int robotSpeedInCellPerSeconds;
	private static int pointsPerRobotKilled;
	private static int pointsPerOpponentKilled;
	private static char[][] gridLayout;
	private static boolean verified = false;

	public static String getName(){
		return name;
	}

	public int getGameDurationInSeconds(){
		return gameDurationInSeconds;
	}

	public static int getExplosionTimeoutInSeconds(){
		return explosionTimeoutInSeconds;
	}

	public static int getExplosionDurationInSeconds(){
		return explosionDurationInSeconds;
	}

	public static int getExplosionRange(){
		return explosionRange;
	}

	public static int getRobotSpeedInCellsPerSeconds(){
		return robotSpeedInCellPerSeconds;
	}

	public static int getPointsPerRobotKilled(){
		return pointsPerRobotKilled;
	}

	public static int getPointsPerOpponentKilled(){
		return pointsPerOpponentKilled;
	}

	public static char[][] getGridLayout(){
		return gridLayout;
	}

	//TODO estas informaçoes explosionRange etc, etc... deviam estar no engine?
	public static boolean load(String level){

		JsonReader reader = null;

		ArrayList<ArrayList<Character>> mat = new ArrayList<ArrayList<Character>>();

		try {

			InputStream in = ABEngine.context.getAssets().open("levels/" + level + ".json");
			reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

			reader.beginObject();

			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("LN")) {
					name = reader.nextString();
				} else if (name.equals("GD")) {
					gameDurationInSeconds = reader.nextInt();
				} else if (name.equals("ED")) {
					explosionDurationInSeconds = reader.nextInt();
				} else if (name.equals("ER")) {
					explosionRange = reader.nextInt();
				} else if (name.equals("RS")) {
					robotSpeedInCellPerSeconds = reader.nextInt();
				} else if (name.equals("PR")) {
					pointsPerRobotKilled = reader.nextInt();
				} else if (name.equals("PO")) {
					pointsPerOpponentKilled = reader.nextInt();
				} else if (name.equals("GL")) {

					int x = 0, y = 0;
					reader.beginArray();
					while(reader.hasNext()) {
						mat.add(new ArrayList<Character>());
						reader.beginArray();
						while(reader.hasNext()) {
							String tile = reader.nextString();
							mat.get(x).add(tile.charAt(0));
							y++;
						}
						x++;
						y=0;
						reader.endArray();
					}
					x=0;
					reader.endArray();

				} else {
					reader.skipValue();
				}
			}

			reader.endObject();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		verified = verifyMatrix(mat);	
		
		if(verified) {
			createMatrix(mat);
			return true;
		} else {
			return false;
		}

	}

	private static boolean verifyMatrix(ArrayList<ArrayList<Character>> mat) {

		for(int i = 0; i < mat.size(); i++) {
			int num = mat.get(i).size();
			for(int j = 0; j < mat.size(); j++) {
				if(num != mat.get(j).size()) {
					return false;
				}
			}
		}
		return true;
	}

	public static void createMatrix(ArrayList<ArrayList<Character>> mat) {

		int cols = mat.size();
		int rows = mat.get(0).size();

		gridLayout = new char[cols][rows];

		for(int i = 0; i < mat.size(); i++) {
			for(int j = 0; j < mat.get(i).size(); j++) {

				gridLayout[i][j] = mat.get(i).get(j);

			}
		}
	}

	public void printLevel() {

		BufferedWriter bufferedWriter;
		
		try {
			
			bufferedWriter = new BufferedWriter(new FileWriter(new 
					File(ABEngine.context.getFilesDir()+File.separator+"teste1.txt")));
			
			bufferedWriter.write("LN: " + name);
			bufferedWriter.newLine();
			bufferedWriter.write("GD: " + gameDurationInSeconds);
			bufferedWriter.newLine();
			bufferedWriter.write("ED: " + explosionDurationInSeconds);
			bufferedWriter.newLine();
			bufferedWriter.write("ER: " + explosionRange);
			bufferedWriter.newLine();
			bufferedWriter.write("RS: " + robotSpeedInCellPerSeconds);
			bufferedWriter.newLine();
			bufferedWriter.write("PR: " + pointsPerRobotKilled);
			bufferedWriter.newLine();
			bufferedWriter.write("PO: " + pointsPerOpponentKilled);
			bufferedWriter.newLine();

			bufferedWriter.write("GL: ");

			for(int i = 0; i < gridLayout.length; i++) {

				for(int j = 0; j < gridLayout[i].length; j++) {

					bufferedWriter.write(gridLayout[i][j] + " ");
				}

				bufferedWriter.newLine();

			}

			if(this.verified) {
				bufferedWriter.write("Verified");
			} else {
				bufferedWriter.write("Not Verified");
			}
			
			bufferedWriter.newLine();
			
			bufferedWriter.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
