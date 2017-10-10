package com.moon_rocks_dev.aThousandWords;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.moon_rocks_dev.aThousandWords.database.ImageThoughtBaseHelper;
import com.moon_rocks_dev.aThousandWords.database.ImageThoughtCursorWrapper;
import com.moon_rocks_dev.aThousandWords.database.ImageThoughtDbSchema.ImageThoughtTable;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtLab {
	public static ImageThoughtLab sImageThoughtLab;

	private Context mContext;
	private SQLiteDatabase mDatabase;

	private ImageThoughtLab(Context context){
		mContext = context.getApplicationContext();
		mDatabase = new ImageThoughtBaseHelper(mContext).getWritableDatabase();
	}

	public static ImageThoughtLab get(Context context) {
		if (sImageThoughtLab == null) {
			sImageThoughtLab = new ImageThoughtLab(context);
		}

		return sImageThoughtLab;

	}

	public void addImageThought(ImageThought imageThought){
		ContentValues values = getContentValues(imageThought);

		mDatabase.insert(ImageThoughtTable.NAME, null, values);
	}


	// pass in, what want, add case statement
	public List<ImageThought> getImageThoughts(String filterType){
		List<ImageThought> imageThoughts = new ArrayList<>();
		String whereClause = null;
		String whereArgs[] = null;

		switch (filterType){
			case "all":
				queryImageThoughts(null, null);
				break;
			case "completed":
				whereClause = ImageThoughtTable.Cols.COMPLETE + " = ?";
				whereArgs =  new String[]{String.valueOf(1)};
				break;
			default:
				queryImageThoughts(null, null);
		}


		ImageThoughtCursorWrapper cursor = queryImageThoughts(whereClause, whereArgs);

		try {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				imageThoughts.add(cursor.getImageThought());
				cursor.moveToNext();
			}

		} finally {
			cursor.close();
		}

		return imageThoughts;
	}


//	public List<ImageThought> getImageThoughts(){
//		List<ImageThought> imageThoughts = new ArrayList<>();
//
//		ImageThoughtCursorWrapper cursor = queryImageThoughts(null, null);
//
//		try {
//			cursor.moveToFirst();
//			while(!cursor.isAfterLast()){
//				imageThoughts.add(cursor.getImageThought());
//				cursor.moveToNext();
//			}
//
//		} finally {
//			cursor.close();
//		}
//
//		return imageThoughts;
//	}

	public ImageThought getImageThought(UUID id){
		ImageThoughtCursorWrapper cursor = queryImageThoughts(
				ImageThoughtTable.Cols.UUID + " = ?",
				new String[] {id.toString()}
		);

		try {
			if (cursor.getCount() == 0){
				return null;
			}

			cursor.moveToFirst();
			return cursor.getImageThought();
		} finally {
			cursor.close();
		}
	}

	public File getPhotoFile(ImageThought imageThought){
		File filesDir = mContext.getFilesDir();
		return new File(filesDir, imageThought.getPhotoFilename());
	}

	public void updateImageThought(ImageThought imageThought){
		String uuidString = imageThought.getId().toString();
		ContentValues values = getContentValues(imageThought);

		mDatabase.update(ImageThoughtTable.NAME,
				values,
				ImageThoughtTable.Cols.UUID + " = ?",
				new String[] { uuidString }
		);
	}

	public void deleteImageThought(ImageThought imageThought){
		String uuidString = imageThought.getId().toString();

		mDatabase.delete(ImageThoughtTable.NAME, ImageThoughtTable.Cols.UUID  + " = ?", new String[] {uuidString});
	}


	private ImageThoughtCursorWrapper queryImageThoughts(String whereClause, String[] whereArgs){
		Cursor cursor = mDatabase.query(
				ImageThoughtTable.NAME,
				null,
				whereClause,
				whereArgs,
				null,
				null,
				null
		);

		return new ImageThoughtCursorWrapper(cursor);
	}


	private static ContentValues getContentValues(ImageThought imageThought){
		ContentValues values = new ContentValues();
		values.put(ImageThoughtTable.Cols.UUID, imageThought.getId().toString());
		values.put(ImageThoughtTable.Cols.THOUGHT, imageThought.getThought());
		values.put(ImageThoughtTable.Cols.TITLE, imageThought.getTitle());
		values.put(ImageThoughtTable.Cols.DATE, imageThought.getDate().getTime());
		values.put(ImageThoughtTable.Cols.COMPLETE, imageThought.isThoughtComplete() ? Integer.toString(1) : Integer.toString(0));

		return values;
	}

}
