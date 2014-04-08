package com.example.moleurlshortener.DAO;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UrlDataBaseAdapter {

	private static final String DATABASE_NAME = "Urls.db";
	private static final String DATABASE_TABLE = "urls";
	private static final int DATABASE_VERSION = 1;

	// the index key column name for use in where clauses.
	public static final String KEY_ID = "_id";

	// the name and column index of each column in your database
	public static final String KEY_ORIGINAL_URl = "original_url";
	public static final int ORIGINAL_URL_COLUMN = 1;

	public static final String KEY_CREATED_URL = "created_url";
	public static final int CREATED_URL_COLUMN = 2;

	// SQL statemen to create a new dabase.
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " integer privamry key autoincremente, " + KEY_ORIGINAL_URl
			+ " text not null, " + KEY_CREATED_URL + " text not null" + ");";

	private SQLiteDatabase db;
	private final Context context;
	public myDbHelper dbHelper;

	public UrlDataBaseAdapter(Context _context) {
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public UrlDataBaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public int insertEntry(UrlObject urlobject) {
		int index = 0;

		String strFilter = KEY_CREATED_URL + "=" + "\""
				+ urlobject.getCreatedUrl() + "\"";

		ContentValues newValues = new ContentValues();
		newValues.put(KEY_ORIGINAL_URl, urlobject.getOriginalUrl());
		newValues.put(KEY_CREATED_URL, urlobject.getCreatedUrl());

		int nRowsEffected = db.update(DATABASE_TABLE, newValues, strFilter,
				null);
		if (nRowsEffected == 0) {
			db.insert(DATABASE_TABLE, null, newValues);
		}

		return index;
	}

	public boolean removeAll() {
		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public boolean isEmpty() {
		Cursor cursor = getAllEntries();
		if (cursor.moveToFirst()) {
			return false;
		} else {
			return true;
		}
	}

	public Cursor getAllEntries() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ID,
				KEY_ORIGINAL_URl, KEY_CREATED_URL }, null, null, null, null,
				null);
	}

	public ArrayList<UrlObject> getAllEntriesParsed() {
		ArrayList<UrlObject> list = new ArrayList<UrlObject>();
		Cursor cursor = getAllEntries();
		if (cursor.moveToFirst())
			do {
				UrlObject url = new UrlObject();
				url.setOriginalUrl(cursor.getString(ORIGINAL_URL_COLUMN));
				url.setCreatedUrl(cursor.getString(CREATED_URL_COLUMN));

				list.add(url);
			} while (cursor.moveToNext());
		cursor.close();
		return list;
	}

	private static class myDbHelper extends SQLiteOpenHelper {
		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		// Called when no database exists in disk and the helper class needs to
		// create a new one.
		// this means that you need to take the row of users, so we need to make
		// an insert
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		// called when there is a dabase vewrsion mismatch meaning that the
		// version of the database on disk need to be upgraded to the curren
		// version
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Upgrade the existing database to conform to the new version.
			// multiple
			// previous version can be handle by comparing _oldVersion and
			// _newVersion values.
			// the simplest case is to drop the old table and create a new one.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(_db);
		}
	}
}
