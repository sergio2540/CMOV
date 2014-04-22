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

    private final String levelName;
    private final float gameDurationInSeconds;
    private final float explosionTimeoutInSeconds;
    private final float explosionDurationInSeconds;
    private final float explosionRange;
    private final float robotSpeedInCellPerSeconds;
    private final float pointsPerRobotKilled;
    private final float pointsPerOpponentKilled;
    private final char[][] gameLevelMatrix;

    private static boolean verified = false;


    private Level(String ln, float gd, float et, float ed, float er, float rs, float pr, float po, char [][] gl) {

	this.levelName = ln;
	this.gameDurationInSeconds = gd;
	this.explosionTimeoutInSeconds = et;
	this.explosionDurationInSeconds = ed;
	this.explosionRange = er;
	this.robotSpeedInCellPerSeconds = rs;
	this.pointsPerRobotKilled = pr;
	this.pointsPerOpponentKilled = po;
	this.gameLevelMatrix = gl;
    }



    public String getLevelName(){
	return levelName;
    }

    public float getGameDurationInSeconds(){
	return gameDurationInSeconds;
    }

    public float getExplosionTimeoutInSeconds(){
	return explosionTimeoutInSeconds;
    }

    public float getExplosionDurationInSeconds(){
	return explosionDurationInSeconds;
    }

    public float getExplosionRange(){
	return explosionRange;
    }

    public float getRobotSpeedInCellsPerSeconds(){
	return robotSpeedInCellPerSeconds;
    }

    public float getPointsPerRobotKilled(){
	return pointsPerRobotKilled;
    }

    public float getPointsPerOpponentKilled(){
	return pointsPerOpponentKilled;
    }

    public char[][] getGameLevelMatrix(){
	return gameLevelMatrix;
    }

    //TODO estas informaçoes explosionRange etc, etc... deviam estar no engine?
    public static Level load(String level){

	String ln = "";
	float gd = 0;
	float et = 0;
	float ed = 0;
	float er = 0;
	float rs = 0;
	float pr = 0;
	float po = 0;


	JsonReader reader = null;

	ArrayList<ArrayList<Character>> mat = new ArrayList<ArrayList<Character>>();

	try {

	    InputStream in = ABEngine.context.getAssets().open("levels/" + level + ".json");
	    reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

	    reader.beginObject();

	    while (reader.hasNext()) {
		String name = reader.nextName();
		if (name.equals("LN")) {
		    ln = reader.nextString();
		} else if (name.equals("GD")) {
		    gd = (float) reader.nextDouble();
		}else if (name.equals("ET")) {
		    et = (float) reader.nextDouble();
		} else if (name.equals("ED")) {
		    ed = (float) reader.nextDouble();
		} else if (name.equals("ER")) {
		    er = (float) reader.nextDouble();
		} else if (name.equals("RS")) {
		    rs = (float) reader.nextDouble();
		} else if (name.equals("PR")) {
		    pr = (float) reader.nextDouble();
		} else if (name.equals("PO")) {
		    po = (float) reader.nextDouble();
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
	    return null;
	} finally {
	    try {
		reader.close();
	    } catch (IOException e) {
		e.printStackTrace();
		return null;
	    }
	}

	verified = verifyMatrix(mat);	

	if(verified) {
	    
	    char [][] gl = createMatrix(mat);
	    Level l = new Level(ln, gd, et, ed, er, rs, pr, po, gl);
	    ABEngine.FIRST_MAP_DRAW = true;
	    
	    return l;
	} else {
	    return null;
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

    public static char [][] createMatrix(ArrayList<ArrayList<Character>> mat) {

	int cols = mat.size();
	int rows = mat.get(0).size();

	char [][] gl = new char[cols][rows];

	for(int i = 0; i < mat.size(); i++) {
	    for(int j = 0; j < mat.get(i).size(); j++) {

		gl[i][j] = mat.get(i).get(j);

	    }
	}

	return gl;
    }

    public void printLevel() {

	BufferedWriter bufferedWriter;

	try {

	    bufferedWriter = new BufferedWriter(new FileWriter(new 
		    File(ABEngine.context.getFilesDir()+File.separator+"teste1.txt")));

	    bufferedWriter.write("LN: " + levelName);
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

	    for(int i = 0; i < gameLevelMatrix.length; i++) {

		for(int j = 0; j < gameLevelMatrix[i].length; j++) {

		    bufferedWriter.write(gameLevelMatrix[i][j] + " ");
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
