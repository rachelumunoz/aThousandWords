package io.rachelmunoz.imagethoughts.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.media.Image;

import java.util.Date;
import java.util.UUID;

import io.rachelmunoz.imagethoughts.ImageThought;

import static io.rachelmunoz.imagethoughts.database.ImageThoughtDbSchema.*;

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
		long date = getLong(getColumnIndex(ImageThoughtTable.Cols.DATE));
		int isComplete = getInt(getColumnIndex(ImageThoughtTable.Cols.COMPLETE));

		ImageThought imageThought = new ImageThought(UUID.fromString(uuidString));
		imageThought.setThought(thought);
		imageThought.setDate(new Date(date));
		imageThought.setThoughtComplete(isComplete != 0);

		return imageThought;
	}

}
