package io.rachelmunoz.imagethoughts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import io.rachelmunoz.imagethoughts.ImageThought;
import io.rachelmunoz.imagethoughts.database.ImageThoughtDbSchema.ImageThoughtTable;

/**
 * Created by rachelmunoz on 7/23/17.
 */

public class ImageThoughtBaseHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "imageThoughtBase.db";

	public ImageThoughtBaseHelper(Context context){
		super(context, DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL("create table " + ImageThoughtTable.NAME +  "(" +
			" _id integer primary key autoincrement, " +
			ImageThoughtTable.Cols.UUID + ", " +
			ImageThoughtTable.Cols.THOUGHT + ", " +
			ImageThoughtTable.Cols.TITLE + ", " +
			ImageThoughtTable.Cols.DATE + ", " +
			ImageThoughtTable.Cols.COMPLETE +
			")"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
