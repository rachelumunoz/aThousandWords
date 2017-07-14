package io.rachelmunoz.imagethoughts;

import java.util.Date;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThought {
	private String mThought;
	private Date mDate;
	private boolean mThoughComplete;
	private int mImage;

	public ImageThought(String thought, int image){
		mDate = new Date();
		mThought = thought;
		mImage = image;
	}

	public String getThought() {
		return mThought;
	}

	public void setThought(String thought) {
		mThought = thought;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isThoughComplete() {
		return mThoughComplete;
	}

	public void setThoughComplete(boolean thoughComplete) {
		mThoughComplete = thoughComplete;
	}

	public int getImage() {
		return mImage;
	}

	public void setImage(int image) {
		mImage = image;
	}
}
