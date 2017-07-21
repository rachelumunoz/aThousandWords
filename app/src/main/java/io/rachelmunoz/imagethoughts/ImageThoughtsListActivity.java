package io.rachelmunoz.imagethoughts;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rachelmunoz on 7/21/17.
 */

public class ImageThoughtsListActivity extends SingleFragmentActivity {

	@Override
	public Fragment createFragment() {
		return new ImageThoughtsListFragment();
	}
}
