package io.rachelmunoz.imagethoughts;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.rachelmunoz.imagethoughts.database.ImageThoughtBaseHelper;
import io.rachelmunoz.imagethoughts.database.ImageThoughtDbSchema;
import io.rachelmunoz.imagethoughts.database.ImageThoughtDbSchema.ImageThoughtTable;

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

	public List<ImageThought> getImageThoughts(){
		return new ArrayList<>();
	}

	public ImageThought getImageThought(UUID id){
		return null;
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

	private static ContentValues getContentValues(ImageThought imageThought){
		ContentValues values = new ContentValues();
		values.put(ImageThoughtTable.Cols.UUID, imageThought.getId().toString());
		values.put(ImageThoughtTable.Cols.THOUGHT, imageThought.getThought());
		values.put(ImageThoughtTable.Cols.DATE, imageThought.getDate().getTime());
		values.put(ImageThoughtTable.Cols.COMPLETE, imageThought.isThoughtComplete() ? 1 : 0);

		return values;
	}

}
