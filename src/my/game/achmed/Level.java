package my.game.achmed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.JsonReader;


public class Level {

    private final static Level level = new Level();

    //Level Parameters
    private String name;
    private int gameDurationInSeconds;
    private int explosionTimeoutInSeconds;
    private int explosionDurationInSeconds;
    private int explosionRange;
    private int robotSpeedInCellPerSeconds;
    private int pointsPerRobotKilled;
    private int pointsPerOpponentKilled;
    private char[][] gridLayout;

    public String getName(){
	return name;
    }

    public int getGameDurationInSeconds(){
	return gameDurationInSeconds;
    }

    public int getExplosionTimeoutInSeconds(){
	return explosionTimeoutInSeconds;
    }

    public int getExplosionDurationInSeconds(){
	return explosionDurationInSeconds;
    }

    public int getExplosionRange(){
	return explosionRange;
    }

    public int getRobotSpeedInCellsPerSeconds(){
	return robotSpeedInCellPerSeconds;
    }

    public int getPointsPerRobotKilled(){
	return pointsPerRobotKilled;
    }

    public int getPointsPerOpponentKilled(){
	return pointsPerOpponentKilled;
    }

    public char[][] getGridLayout(){
	return gridLayout;
    }


    private Level(){
    }

    public static Level load(String path){

	JsonReader reader = null;

	try {
	    reader = new JsonReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

	    reader.beginObject();

	    while (reader.hasNext()) {
		String name = reader.nextName();
		if (name.equals("LN")) {
		    level.name = reader.nextString();
		} else if (name.equals("GD")) {
		    level.gameDurationInSeconds = reader.nextInt();
		} else if (name.equals("ED")) {
		    level.gameDurationInSeconds = reader.nextInt();
		} else if (name.equals("ER")) {
		    level.explosionRange = reader.nextInt();
		} else if (name.equals("RS")) {
		    level.robotSpeedInCellPerSeconds = reader.nextInt();
		} else if (name.equals("PR")) {
		    level.pointsPerRobotKilled = reader.nextInt();
		} else if (name.equals("PO")) {
		    level.pointsPerOpponentKilled = reader.nextInt();
		} else if (name.equals("GL")) {
		    
		    // TODO Fazer readGridLayout!!!!
		    //level.gridLayout = readGridLayout(reader);
		} else {
		    reader.skipValue();
		}
	    }


	    reader.endObject();

	} catch (IOException e) {
	    // TODO Auto-generated catch block

	    e.printStackTrace();
	} finally {
	    try {
		reader.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return level;

    }




}
