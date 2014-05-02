package my.game.achmed.Database;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<Rank> getSingleHighScoreTable(int level) {
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, DatabaseHelper.COLUMN_LEVEL + " = " + level, 
				null, null, null, 
				DatabaseHelper.COLUMN_HIGHSCORE + " DESC");
		
		//database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		
		List<Rank> ranks = new ArrayList<Rank>();
		Rank rank;

		while(cursor.moveToNext()) {
			
			String name = cursor.getString(1);
			int rankLevel = cursor.getInt(2);
			float highscore = cursor.getFloat(3);
			float lastscore = cursor.getFloat(4);
			
			rank = new Rank(name, rankLevel, highscore, lastscore);
			ranks.add(rank);
			
		}
		
		//Log.w("RANK LIST", "" + ranks.size());
		return ranks;
		
	}
	
	public boolean createPlayer(String name) {
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, DatabaseHelper.COLUMN_NAME + " = " + "\"" + name + "\"", 
				null, null, null, null);
		
		if(cursor.getCount() != 0) {
			return false;
		}
		
		ContentValues values = new ContentValues();
		
		values.put(DatabaseHelper.COLUMN_NAME, name);
		values.put(DatabaseHelper.COLUMN_LEVEL, 0);
		values.put(DatabaseHelper.COLUMN_HIGHSCORE, 0);
		values.put(DatabaseHelper.COLUMN_LASTSCORE, 0);
		
	    long insertId = database.insert(DatabaseHelper.TABLE_SINGLE_RANKS, null, values);
	    
	    if(insertId == -1)
	    	return false;
	    
	    return true;
		
		
	}
	
	public List<String> getPlayerNamesList() {
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_SINGLE_RANKS, null, null, 
				null, null, null, 
				DatabaseHelper.COLUMN_HIGHSCORE + " DESC");
		
		List<String> playersNames = new ArrayList<String>();

		while(cursor.moveToNext()) {
			
			String playerName = cursor.getString(1);
			playersNames.add(playerName);
			
		}
		
		return playersNames;
		
	} 
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
}
