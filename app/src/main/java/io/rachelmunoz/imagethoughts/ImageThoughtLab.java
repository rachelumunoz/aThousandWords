package io.rachelmunoz.imagethoughts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.rachelmunoz.imagethoughts.database.ImageThoughtBaseHelper;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtLab {
	public static ImageThoughtLab sImageThoughtLab;

	private List<ImageThought> mImageThoughts;
	private Context mContext;
	private SQLiteDatabase mDatabase;

	private ImageThoughtLab(Context context){
		mContext = context.getApplicationContext();
		mDatabase = new ImageThoughtBaseHelper(mContext).getWritableDatabase();
		mImageThoughts = new ArrayList<>();
	}

	public static ImageThoughtLab get(Context context) {
		if (sImageThoughtLab == null) {
			sImageThoughtLab = new ImageThoughtLab(context);
		}

		return sImageThoughtLab;

	}

	public void addImageThought(ImageThought imageThought){
		mImageThoughts.add(imageThought);
	}

	public List<ImageThought> getImageThoughts(){
		return mImageThoughts;
	}

	public ImageThought getImageThought(UUID id){
		for (ImageThought imageThought : mImageThoughts){
			if (imageThought.getId().equals(id)){
				return imageThought;
			}
		}

		return null;
	}
}
