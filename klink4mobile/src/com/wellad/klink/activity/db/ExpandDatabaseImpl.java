package com.wellad.klink.activity.db;

import java.io.File;
import java.util.ArrayList;

import com.wellad.klink.activity.api.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class ExpandDatabaseImpl implements ExpandDatabase {
	public static final String TAG = "com.wellad.klink.activity.db.ExpandDatabaseImpl";

	private static final String DB_NAME = "/klink.db";
	private static final String TABLE_ITEM_FAVORITE = "url_favorite";

	private static final int DB_VERSION = 1;

	private String mPath;
	private SQLiteDatabase mDb;

	/**
	 * Default constructor
	 * 
	 * @param path
	 *            Database file
	 */
	public ExpandDatabaseImpl(Context context) {
		mPath = getCacheDir(context);
		openDB();
		if (mDb == null) {
			return;
		}
		if (mDb.getVersion() < DB_VERSION) {
			new UpdaterBuilder().getUpdater(DB_VERSION).update();
		}
	}
	
	/********************************
	 ** 获得到cache的文件夹路径
	 * 
	 * @return cache文件夹路径
	 */
	public String getCacheDir(Context context) {
		File file = context.getCacheDir();
		return file.getAbsolutePath();
	}

	public boolean isOpen() {
		return mDb.isOpen();
	}

	public void openDB() {
		mDb = getDb();
	}

	private SQLiteDatabase getDb() {
		boolean success = (new File(mPath)).mkdirs();
		if (success) {
			Log.i(TAG, "Directory: " + mPath + " created");
		}
		try {
			return SQLiteDatabase.openDatabase(mPath + DB_NAME, null,
					SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (SQLException e) {
			Log.e(TAG, "Failed creating database");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			Log.e(TAG, "Failed open database");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void finalize() {
		if (mDb != null) {
			mDb.close();
		}
	}

	/**
	 * Updater from version 0 to 1.
	 * 
	 * Updates from previous version without copying data. Creates database if
	 * it doesn't exist.
	 * 
	 * @author yiming wang
	 * 
	 */
	private class DatabaseUpdaterV1 extends DatabaseUpdater {
		// TODO Add copying and updating data from previous db version
		private static final int VERSION = 1;

		public DatabaseUpdaterV1() {

		}

		public DatabaseUpdaterV1(DatabaseUpdater updater) {
			setUpdater(updater);
		}

		@Override
		public void update() {
			if (getUpdater() != null) {
				getUpdater().update();
			}
			try {
				mDb.execSQL("DROP TABLE " + TABLE_ITEM_FAVORITE + ";");
			} catch (SQLiteException e) {
				Log.v(TAG, "Library table not existing");
			}
			createTables();
			mDb.setVersion(VERSION);
		}

		private void createTables() {
			mDb.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ITEM_FAVORITE
					+ " (key_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "url NVARCHAR, " + "text NVARCHAR);");
		}
	}

	/**
	 * DatabaseUpdater Builder. Builds an updater for updating database from its
	 * current version to version passed as parameter of getUpadater method.
	 * 
	 * Each updater uses a previous db version as input. Builder builds a
	 * decorated updater to update to the desired version.
	 * 
	 * @author yiming wang
	 * 
	 */
	private class UpdaterBuilder {

		public DatabaseUpdater getUpdater(int version) {
			DatabaseUpdater updater = null;
			switch (version) {
			case 1:
				updater = new DatabaseUpdaterV1();
				break;
			case 0:
				updater = null;
				break;
			}
			if (version > mDb.getVersion() + 1) {
				updater.setUpdater(getUpdater(version - 1));
			}
			return updater;
		}
	}

	/**
	 * update item
	 */
	public synchronized void updateItemTicket(Item item) {
		if (mDb == null) {
			return;
		}
		ContentValues values = new ContentValues();
		values.putAll(new ItemBuilder().deconstruct(item));
		String[] whereArgs = { item.getUrl() };
		long row_count = mDb.update(TABLE_ITEM_FAVORITE, values, "url=?", whereArgs);
		if (row_count == 0) {
			item.setKey_id(mDb.insert(TABLE_ITEM_FAVORITE, null, values));
		}
	}

	/**
	 * get all item
	 */
	public synchronized ArrayList<Item> getAllItem() {
		ArrayList<Item> jobs = new ArrayList<Item>();
		if (mDb == null) {
			return null;
		}
		Cursor query = null;
		query = mDb.query(TABLE_ITEM_FAVORITE, null, null, null, null, null,
				null);
		if (query.moveToFirst()) {
			ItemBuilder builder = new ItemBuilder();
			while (!query.isAfterLast()) {
				jobs.add(builder.build(query));
				query.moveToNext();
			}
		}
		query.close();
		return jobs;
	}

}

abstract class DatabaseUpdater {
	private DatabaseUpdater mUpdater;

	abstract void update();

	public void setUpdater(DatabaseUpdater mUpdater) {
		this.mUpdater = mUpdater;
	}

	public DatabaseUpdater getUpdater() {
		return mUpdater;
	}
}
