package io.rachelmunoz.imagethoughts.database;

/**
 * Created by rachelmunoz on 7/23/17.
 */

public class ImageThoughtDbSchema {

	public static final class ImageThoughtTable {
		public static final String NAME = "imageThoughts";

		public static final class Cols {
			public static final String UUID = "uuid";
			public static final String THOUGHT = "thought";
			public static final String TITLE = "title";
			public static final String DATE = "date";
			public static final String COMPLETE = "complete";
		}
	}
}
