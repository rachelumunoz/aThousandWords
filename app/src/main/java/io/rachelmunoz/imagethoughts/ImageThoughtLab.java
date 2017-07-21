package io.rachelmunoz.imagethoughts;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtLab {
	public static ImageThoughtLab sImageThoughtLab;
	private List<ImageThought> mImageThoughts;

	private ImageThoughtLab(Context context){
		mImageThoughts = new ArrayList<>();

		for (int i = 0; i < 10; i++){
			ImageThought imageThought = new ImageThought("New image thought" + i);
			imageThought.setThoughtComplete(i % 2 == 0);
			mImageThoughts.add(imageThought);
		}
	}

	public ImageThoughtLab get(Context context) {
		if (sImageThoughtLab == null) {
			sImageThoughtLab = new ImageThoughtLab(context);
		}

		return sImageThoughtLab;

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
