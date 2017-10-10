package io.rachelmunoz.aThousandWords;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rachelmunoz on 7/14/17.
 */

public class ImageThought {
	private String mThought;
	private Date mDate;
	private boolean mThoughtComplete;
	private int mImage;
	private UUID mId;
	private String mTitle;

	public ImageThought(){
		mDate = new Date();
		mThought = "";
		mId = UUID.randomUUID();
		mTitle = "";
	}

	public ImageThought(String thought){
		mDate = new Date();
		mThought = thought;
		mId = UUID.randomUUID();
//		mTitle = title;
	}

	public ImageThought(UUID id){
		mId = id;
		mDate = new Date();
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

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getFormattedDate(){
		Date date = getDate();
		String dateString = DateFormat.format("EEE, MMM d, ''yy", date).toString();

		return dateString;
	}

	public UUID getId() {
		return mId;
	}



	public String getPhotoFilename(){
		return "IMG_" + getId().toString() + ".jpg";
	}



}
