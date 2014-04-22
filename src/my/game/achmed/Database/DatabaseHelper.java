package my.game.achmed.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Achmed.db";

	public static final String TABLE_SINGLE_RANKS = "SingleRanks";

	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LEVEL = "level";
	public static final String COLUMN_HIGHSCORE = "highscore";
	public static final String COLUMN_LASTSCORE = "lastscore";

	//TEXT = STRING, INTEGER = LONG, REAL = DOUBLE
	private static final String TABLE_CREATE =
			"CREATE TABLE " + TABLE_SINGLE_RANKS + " (" + 
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_NAME + " TEXT, " +
					COLUMN_LEVEL + " INTEGER, " +
					COLUMN_HIGHSCORE + " REAL, " +
					COLUMN_LASTSCORE + " REAL);";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINGLE_RANKS);
		db.execSQL(TABLE_CREATE);
	}
	
}
