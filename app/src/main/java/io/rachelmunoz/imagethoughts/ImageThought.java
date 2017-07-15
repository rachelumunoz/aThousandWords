package io.rachelmunoz.imagethoughts;

import java.util.Date;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThought {
	private String mThought;
	private Date mDate;
	private boolean mThoughtComplete;
	private int mImage;

	public ImageThought(String thought){
		mDate = new Date();
		mThought = thought;
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

	public boolean isThoughtComplete() {
		return mThoughtComplete;
	}

	public void setThoughtComplete(boolean thoughtComplete) {
		mThoughtComplete = thoughtComplete;
	}

}
