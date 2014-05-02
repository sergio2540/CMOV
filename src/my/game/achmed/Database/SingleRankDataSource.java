package my.game.achmed.Database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.game.achmed.Rank.Rank;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SingleRankDataSource {

	private SQLiteDatabase database; 
	private DatabaseHelper dbHelper;

	private String[] allColumns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_LEVEL,
			DatabaseHelper.COLUMN_HIGHSCORE, DatabaseHelper.COLUMN_LASTSCORE};

	public SingleRankDataSource(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	public List<Rank> getSingleHighScoreTable(String level) {

		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, DatabaseHelper.COLUMN_LEVEL + " = " + level, 
				null, null, null, 
				DatabaseHelper.COLUMN_HIGHSCORE + " DESC");

		List<Rank> ranks = new ArrayList<Rank>();
		Rank rank;

		while(cursor.moveToNext()) {

			String name = cursor.getString(1);
			String rankLevel = cursor.getString(2);
			float highscore = cursor.getFloat(3);
			float lastscore = cursor.getFloat(4);
			
			rank = new Rank(name, rankLevel, highscore, lastscore);
			ranks.add(rank);

		}

		return ranks;

	}

	public boolean createPlayer(String name) {

		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, DatabaseHelper.COLUMN_NAME + " = " + "\"" + name + "\"", 
				null, null, null, null);

		if(cursor.getCount() != 0) {
			return false;
		}

		database.beginTransaction();
		try {
			long success = 0;
			for(int i = 0; i < 3; i++) {

				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.COLUMN_NAME, name);
				values.put(DatabaseHelper.COLUMN_LEVEL, i+1);
				values.put(DatabaseHelper.COLUMN_HIGHSCORE, 0);
				values.put(DatabaseHelper.COLUMN_LASTSCORE, 0);

				success = database.insert(DatabaseHelper.TABLE_SINGLE_RANKS, null, values);

			}

			if(success != -1) {
				database.setTransactionSuccessful();
			}

		} catch(Exception e){
			database.endTransaction();
			return false;
		}

		database.endTransaction();
		return true;
	}

	public List<String> getPlayerNamesList() {

		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, null, 
				null, null, null, 
				DatabaseHelper.COLUMN_HIGHSCORE + " DESC");

		Set<String> playersNames = new HashSet<String>();

		while(cursor.moveToNext()) {

			String playerName = cursor.getString(1);
			playersNames.add(playerName);

		}
		List<String> playersNameList = new ArrayList<String>(playersNames);
		return playersNameList;

	} 

	public boolean updatePlayerScore(String name, String level, float newScore) {

		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, DatabaseHelper.COLUMN_NAME + " = " + "\"" + name + "\"" + " AND " 
				+ DatabaseHelper.COLUMN_LEVEL + " = " + "\"" + level + "\"", null, null, null, null);

		int rowsAfected = 0;
		
		int dbId = 0;
		String dbName = null;
		String dbLevel = null;
		float dbHighScore = 0;
		float dbLastScore = 0;

		while(cursor.moveToNext()) {
			dbId = cursor.getInt(0);
			dbName = cursor.getString(1);
			dbLevel = cursor.getString(2);
			dbHighScore = cursor.getFloat(3);
			dbLastScore = cursor.getFloat(4);
		}
		
		if(dbHighScore < newScore) {
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.COLUMN_ID, dbId);
			values.put(DatabaseHelper.COLUMN_NAME, dbName);
			values.put(DatabaseHelper.COLUMN_LEVEL, dbLevel);
			values.put(DatabaseHelper.COLUMN_HIGHSCORE, newScore);
			values.put(DatabaseHelper.COLUMN_LASTSCORE, dbLastScore);
			rowsAfected = database.update(DatabaseHelper.TABLE_SINGLE_RANKS, values, DatabaseHelper.COLUMN_ID + "=?", new String[]{dbId + ""});
		}

		if(rowsAfected == 1) {
			return true;
		} else {
			return false;
		}

	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

}
