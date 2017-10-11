package com.moon_rocks_dev.aThousandWords.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import com.moon_rocks_dev.aThousandWords.ImageThought;

import static com.moon_rocks_dev.aThousandWords.database.ImageThoughtDbSchema.*;

/**
 * Created by rachelmunoz on 7/23/17.
 */

public class ImageThoughtCursorWrapper extends CursorWrapper {
	public ImageThoughtCursorWrapper(Cursor cursor){
		super(cursor);
	}

	public ImageThought getImageThought(){
		String uuidString = getString(getColumnIndex(ImageThoughtTable.Cols.UUID));
		String thought = getString(getColumnIndex(ImageThoughtTable.Cols.THOUGHT));
		String title = getString(getColumnIndex(ImageThoughtTable.Cols.TITLE));
		long date = getLong(getColumnIndex(ImageThoughtTable.Cols.DATE));
		int isComplete = getInt(getColumnIndex(ImageThoughtTable.Cols.COMPLETE));

		ImageThought imageThought = new ImageThought(UUID.fromString(uuidString));
		imageThought.setThought(thought);
		imageThought.setTitle(title);
		imageThought.setDate(new Date(date));
		imageThought.setThoughtComplete(isComplete != 0);

		return imageThought;
	}

}
